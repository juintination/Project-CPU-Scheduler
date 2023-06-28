import java.util.*;

public class Scheduler {

    private static Scheduler scheduler = new Scheduler();
    private Map<Integer, PCB> processMap;
    private ReadyQueue readyQueue;
    private List<PCB> blockedList;
    private Random random;
    private PCB p;
    private int n, quantum, cpuBurstTime, ioBurstTime;
    private long finishingTime, totalCpuTime, totalIoTime, totalWaitingTime, totalTurnaroundTime;
    private boolean isFinished;
    private StringBuilder sb;

    private Scheduler() {}

    public static Scheduler getInstance() {
        return scheduler;
    }

    public void setScheduler(Map<Integer, PCB> processMap, int n, ReadyQueue readyQueue, int quantum) {
        this.n = n;
        this.quantum = quantum;
        this.processMap = processMap;
        this.readyQueue = readyQueue;
        readyQueue.setQueue(processMap);
        blockedList = new ArrayList<>();
    }

    public void run() {
        // finishingTime 및 Random, StringBuilder, TotalTime 초기화
        finishingTime = readyQueue.peek().getA();
        random = new Random();
        sb = new StringBuilder();
        initTotalTime();
        // readyQueue와 blockedList 중 1개라도 비어있지 않으면 반복
        while (!readyQueue.isEmpty() || !blockedList.isEmpty()) {
            // p : Running 상태로 이동
            p = readyQueue.poll();
            if (p == null) { // readyQueue가 비어있다면
                addFinishingTime(); // 1 unit time 경과
                checkAndEnqueue(); // 현재 시간에 도착한 프로세스 확인
                ioBurst(); // 1 unit time동안 io 실행
                continue;
            }
            if (!p.getIsQuantumed()) {
                setBurstTime(p.getB(), p.getIo()); // cpuBurstTime, ioBurstTime
            } else {
                cpuBurstTime = p.getRemainingCpuBurstTime();
                ioBurstTime = p.getRemainingIoBurstTime();
            }
            int remainingCpuTime = p.getRemainingCpuTime(); // p의 남은 cpu time
            cpuBurst(cpuBurstTime, remainingCpuTime); // cpuBurstTime만큼 cpu burst (RR인 경우 quantum까지)
            // System.out.println("pid: " + p.getPid() + ", cpuBirstTime: " + cpuBurstTime); // for test
            if (isFinished) { // p가 종료됐다면
                setFinished(false); // isFinished false로 초기화
                addInfo(p); // p에 대한 정보 sb에 추가
            } else { // cpuBurstTime이 지났지만 p가 종료되지 않았다면
                if (p.getIsQuantumed()) {
                    p.setIsQuantumed(false);
                    if (p.getRemainingCpuBurstTime() > 0) {
                        readyQueue.offer(p);
                        continue;
                    }
                }
                if (ioBurstTime > 0) { // ioBurstTime이 0이 아니라면
                    // System.out.println("ioBurstTime: " + ioBurstTime); // for test
                    p.setRemainingIoBurstTime(ioBurstTime); // p의 남은 io time 초기화
                    blockedList.add(p); // blockedList에 p 추가
                } else { // ioBurstTime이 0이라면
                    readyQueue.offer(p); // 다시 readyQueue에 추가
                }
            }
        }
        // 모든 프로세스가 완료되면 추가 정보 sb에 추가
        addNextSummary();
    }

    private void initTotalTime() {
        totalCpuTime = 0;
        totalIoTime = 0;
        totalWaitingTime = 0;
        totalTurnaroundTime = 0;
    }

    private void setBurstTime(int b, int io) {
        cpuBurstTime = 1 + random.nextInt(b); // 1 ~ b 사이의 난수
        if (io == 0) ioBurstTime = 0; // io가 0이라면 ioBurstTime = 0
        else ioBurstTime = 1 + random.nextInt(io); // 1 ~ io 사이의 난수
        p.setRemainingCpuBurstTime(cpuBurstTime);
        p.setRemainingIoBurstTime(ioBurstTime);
    }

    private void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    private void checkAndEnqueue() {
        if (processMap.containsKey((int) finishingTime)) {
            readyQueue.addPcbList((int) finishingTime, processMap);
        }
    }

    private void cpuBurst(int cpuBurstTime, int remainingCpuTime) {
        int burstTime = 0;
        while (cpuBurstTime-- > 0) {
            addFinishingTime(); // 1 unit time 증가
            checkAndEnqueue(); // 현재 시간에 도착한 프로세스 확인
            ioBurst(); // io 실행
            burstTime++; // burstTime 1 unit time 증가
            addTotalCpuTime(); // totalCpuTime 1 unit time 증가
            if (burstTime == remainingCpuTime) { // 남은 cpu time만큼 burst time이 지났다면
                // System.out.println(p.getPid() + " is finished"); // for test
                setFinished(true); // 프로세스 p 종료
                p.setRemainingCpuTime(0); // 남은 cpu time 0으로 초기화
                break; // cpuBurst 종료
            }
            if (burstTime == quantum) { // RR인 경우 burst time이 quantum만큼 지났다면
                p.setIsQuantumed(true);
                p.setRemainingCpuBurstTime(p.getRemainingCpuBurstTime() - burstTime);
                break; // cpuBurst 종료
            }
        }
        // p의 cpuTime 관련 필드 초기화
        p.setCpuTime(p.getCpuTime() + burstTime);
        p.setRemainingCpuTime(remainingCpuTime - burstTime);
    }

    private void ioBurst() {
        List<PCB> copyBlockedList = new ArrayList<>(blockedList);
        if (!blockedList.isEmpty()) {
            addTotalIoTime();
        }
        for (PCB ioP : copyBlockedList) { // ioP : blockedList에 있는 프로세스
            ioP.setIoTime(ioP.getIoTime() + 1); // ioP의 ioTime 1 unit time 증가
            int remainingIoTime = ioP.getRemainingIoBurstTime();
            remainingIoTime -= 1; // ioP의 남은 io time 1 감소
            if (remainingIoTime == 0) { // 할당받은 io time만큼 실행됐다면
                readyQueue.offer(ioP); // readyQueue에 ioP 추가
                blockedList.remove(ioP); // blockedList에서 ioP 제거
            } else {
                ioP.setRemainingIoBurstTime(remainingIoTime); // ioP의 남은 io time 초기화
            }
        }
    }

    private void addFinishingTime() {
        finishingTime++;
    }

    private void addTotalWaitingTime(long waitingTime) {
        totalWaitingTime += waitingTime;
    }

    private void addTotalTurnaroundTime(long turnaroundTime) {
        totalTurnaroundTime += turnaroundTime;
    }

    private void addTotalIoTime() {
        totalIoTime++;
    }

    private void addTotalCpuTime() {
        totalCpuTime++;
    }

    private void addInfo(PCB p) {
        sb.append("-----").append("Process ").append(p.getPid()).append("-----\n");
        sb.append("A: ").append(p.getA()).append(", C: ").append(p.getC()).append(", B: ")
                .append(p.getB()).append(", IO: ").append(p.getIo()).append("\n")
                .append("Finishing Time: ").append(finishingTime).append("\n");
        // turnaroundTime : finishing time - p의 도착 시작
        long turnaroundTime = finishingTime - p.getA();
        sb.append("Turnaound Time: ").append(turnaroundTime).append("\n");
        addTotalTurnaroundTime(turnaroundTime); // totalTurnaroundTime 증가
        sb.append("CPU Time: ").append(p.getCpuTime()).append("\n");
        sb.append("I/O Time: ").append(p.getIoTime()).append("\n");
        // waitingTime : turnaroundTime - p의 cpu time - p의 io time
        long waitingTime = turnaroundTime - p.getCpuTime() - p.getIoTime();
        sb.append("Waiting Time: ").append(waitingTime).append("\n");
        addTotalWaitingTime(waitingTime); // totalWaitingTime 증가
    }

    private void addNextSummary() {
        sb.append("-----Next Summary Data-----\n");
        sb.append("Finishing Time: ").append(finishingTime).append("\n");
        double cpuUtilization = ((double) totalCpuTime / finishingTime) * 100;
        sb.append("CPU Utilization: ").append(String.format("%.2f", cpuUtilization)).append("%\n");
        double ioUtilization = ((double) totalIoTime / finishingTime) * 100;
        sb.append("IO Utilization: ").append(String.format("%.2f", ioUtilization)).append("%\n");
        double throughputInProcessCompletedPerHundredTimeUnits = ((double) n / finishingTime) * 100;
        sb.append("Throughput in processes completed per hundred time units: ")
                .append(String.format("%.2f", throughputInProcessCompletedPerHundredTimeUnits)).append("\n");
        double averageTrunaroundTime = (double) totalTurnaroundTime / n;
        sb.append("Average Turnaround Time: ").append(String.format("%.2f", averageTrunaroundTime)).append("\n");
        double averageWaitingTime = (double) totalWaitingTime / n;
        sb.append("Average Waiting Time: ").append(String.format("%.2f", averageWaitingTime)).append("\n");
    }

    public String getResult() {
        return sb.toString();
    }
}