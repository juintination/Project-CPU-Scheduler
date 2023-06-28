import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;

public class ReadyQueueForSJF implements ReadyQueue {

    private static ReadyQueueForSJF readyQueueForSJF = new ReadyQueueForSJF();

    // SJF는 priorityQueue 클래스 위임
    private PriorityQueue<PCB> priorityQueue = new PriorityQueue<>(new Comparator<PCB>() {
        @Override
        public int compare(PCB o1, PCB o2) {
            return Integer.compare(o1.getC(), o2.getC());
        }
    });

    private ReadyQueueForSJF() {}

    public static ReadyQueueForSJF getInstance() {
        return readyQueueForSJF;
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
        priorityQueue.offer(p);
    }

    @Override
    public PCB peek() {
        return priorityQueue.peek();
    }

    @Override
    public PCB poll() {
        return priorityQueue.poll();
    }

    @Override
    public boolean isEmpty() {
        return priorityQueue.isEmpty();
    }
}
