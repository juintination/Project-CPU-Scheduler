import java.util.Map;

public interface ReadyQueue {
    void setQueue(Map<Integer, PCB> processMap);
    void addPcbList(int arrivalTime, Map<Integer, PCB> processMap);
    void offer(PCB p);
    PCB peek();
    PCB poll();
    boolean isEmpty();
}
