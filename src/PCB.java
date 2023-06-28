public class PCB {
    private int pid, a, c, b, io;
    private PCB next = null;
    private int remainingCpuBurstTime, remainingCpuTime, remainingIoBurstTime, ioTime;
    private long cpuTime;
    private boolean isQuantumed;

    public PCB(int pid, int a, int c, int b, int io) {
        this.pid = pid;
        this.a = a;
        this.c = c;
        this.remainingCpuTime = c;
        this.b = b;
        this.io = io;
        this.cpuTime = 0;
        this.ioTime = 0;
        this.isQuantumed = false;
    }

    public void setNext(PCB next) {
        this.next = next;
    }

    public PCB getNext() {
        return this.next;
    }

    public int getPid() {
        return this.pid;
    }

    public int getA() {
        return this.a;
    }

    public int getB() {
        return this.b;
    }

    public int getC() {
        return this.c;
    }

    public int getIo() {
        return this.io;
    }

    public long getCpuTime() {
        return this.cpuTime;
    }

    public int getIoTime() {
        return this.ioTime;
    }

    public void setCpuTime(long cpuTime) {
        this.cpuTime = cpuTime;
    }

    public void setIoTime(int ioTime) {
        this.ioTime = ioTime;
    }

    public int getRemainingCpuTime() {
        return this.remainingCpuTime;
    }

    public void setRemainingCpuTime(int remainingCpuTime) {
        this.remainingCpuTime = remainingCpuTime;
    }

    public int getRemainingIoBurstTime() {
        return this.remainingIoBurstTime;
    }

    public void setRemainingIoBurstTime(int remainingIoBurstTime) {
        this.remainingIoBurstTime = remainingIoBurstTime;
    }

    public boolean getIsQuantumed() {
        return this.isQuantumed;
    }

    public void setIsQuantumed(boolean isQuantumed) {
        this.isQuantumed = isQuantumed;
    }

    public int getRemainingCpuBurstTime() {
        return this.remainingCpuBurstTime;
    }

    public void setRemainingCpuBurstTime(int remainingCpuBurstTime) {
        this.remainingCpuBurstTime = remainingCpuBurstTime;
    }
}