import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Client {
    public static void main(String[] args) throws IOException {
        // 입력을 위한 BufferedReader
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // '(', ' ', ')'을 분리하기 위한 StringTokenizer
        StringTokenizer st = new StringTokenizer(br.readLine(), "( )");
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
        // Scheduler 세팅
        ReadyQueue readyQueue = ReadyQueueForFCFS.getInstance();
        int quantum = 1;
        scheduler.setScheduler(processMap, n, readyQueue, quantum);
        // Scheduling 시작
        scheduler.run();
        // 결과 출화
        System.out.println(scheduler.getResult());
    }
}
