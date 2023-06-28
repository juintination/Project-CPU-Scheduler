import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Sched {
    public static void main(String[] args) throws IOException {
        // 기본 inputFilename은 "test.txt"으로, 기본 Scheduling 알고리즘은 "FCFS"으로
        String inputFilename = "test.txt", schedulingAlgorithm = "FCFS";
        // 기본 quantum은 0으로 초기화
        int quantum = 0;
        if (args.length < 2) {
            System.err.println("Usage: java Sched <input filename> <scheduling method>");
            System.exit(1);
        } else {
            inputFilename = args[0];
            schedulingAlgorithm = args[1];
            if (args.length > 2) {
                quantum = Integer.parseInt(args[2]);
            }
        }
        // inputString : 파일에 있는 내용
        String inputString = null;
        try {
            FileReader fr = new FileReader(inputFilename);
            StringBuffer sb = new StringBuffer();
            char[] buf = new char[1024];
            int size = 1024;
            do {
                size = fr.read(buf);
                sb.append(buf, 0, size);
            } while (size == 1024);
            inputString = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // '(', ' ', ')'을 분리하기 위한 StringTokenizer
        StringTokenizer st = new StringTokenizer(inputString, "( )");
        // n : 프로세스 개수
        int n = Integer.parseInt(st.nextToken());
        // processMap : 프로세스 맵, key값 : 프로세스 도착 시작, value값 : 프로세스
        Map<Integer, PCB> processMap = new HashMap<>();
        // n개의 프로세스 입력
        for (int i = 0; i < n; i++) {
            int arrivalTime = Integer.parseInt(st.nextToken()); // A
            int cpuTime = Integer.parseInt(st.nextToken()); // C
            int cpuBurstTime = Integer.parseInt(st.nextToken()); // B
            int ioBurstTime = Integer.parseInt(st.nextToken()); // IO
            int pid = i + 1; // pid는 i + 1 (1 ~ n)으로 설정
            if (!processMap.containsKey(arrivalTime)) { // p의 도착시간과 같은 도착시간을 가지는 프로세스가 이전에 없었다면 추가
                processMap.put(arrivalTime, new PCB(pid, arrivalTime, cpuTime, cpuBurstTime, ioBurstTime));
            } else { // 있었다면 맨 끝의 프로세스의 next를 p로 설정
                PCB p = processMap.get(arrivalTime);
                while (p.getNext() != null) {
                    p = p.getNext();
                }
                p.setNext(new PCB(pid, arrivalTime, cpuTime, cpuBurstTime, ioBurstTime));
            }
        }
        // Scheduler 선언
        Scheduler scheduler = Scheduler.getInstance();
        // ReadyQueue 선언
        ReadyQueue readyQueue;
        if (schedulingAlgorithm.equals("SJF")) { // SJF일 경우 ReadyQueue는 PriorityQueue로 구현함
            readyQueue = ReadyQueueForSJF.getInstance();
        } else { // FCFS, RR일 경우 ReadyQueue는 LinkedList로 구현함
            readyQueue = ReadyQueueForFCFS.getInstance();
        }
        // Scheduling Algorithm 출력
        StringBuilder sb = new StringBuilder();
        sb.append("\nScheduling Algorithm is ").append(schedulingAlgorithm);
        if (schedulingAlgorithm.equals("RR")) {
            sb.append(" ").append(quantum);
        }
        sb.append("\n");
        System.out.println(sb);
        // Scheduler 세팅
        scheduler.setScheduler(processMap, n, readyQueue, quantum);
        // Scheduling 시작
        scheduler.run();
        // 결과 출력
        System.out.println(scheduler.getResult());
    }
}
