import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class ReadyQueueForFCFS implements ReadyQueue {

    // Singleton 패턴으로 구현
    private static ReadyQueueForFCFS readyQueue = new ReadyQueueForFCFS();

    // FCFS와 RR은 Queue 클래스 위임
    private Queue<PCB> queue = new LinkedList<>();

    private ReadyQueueForFCFS() {}

    public static ReadyQueueForFCFS getInstance() {
        return readyQueue;
    }

    @Override
    public void setQueue(Map<Integer, PCB> processMap) {
        int firstArrivalTime = 0; // 처음 프로세스가 도착한 시간
        while (!processMap.containsKey(firstArrivalTime)) {
            firstArrivalTime++;
        }
        // 처음 프로세스가 도착한 시간에 도착한 모든 프로세스 readyQueue에 추가
        this.addPcbList(firstArrivalTime, processMap);
    }

    @Override
    public void addPcbList(int arrivalTime, Map<Integer, PCB> processMap) {
        PCB p = processMap.remove(arrivalTime); // processMap의 key값 중 arrivalTime 삭제
        do {
            this.offer(p); // readyQueue에 p 추가
            p = p.getNext(); // p = p의 다음 프로세스
        } while (p != null);
    }

    @Override
    public void offer(PCB p) {
        queue.offer(p);
    }

    @Override
    public PCB peek() {
        return queue.peek();
    }

    @Override
    public PCB poll() {
        return queue.poll();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
