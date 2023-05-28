class Process implements Comparable<Process>{
    private int processNum;
    private int burstTime;
    private int initialBurstTime;
    private int arrivalTime;
    private int priority;
    private int completionTime;
    private int turnAroundTime;
    private int waitingTime;
    private boolean isComplete;
    private boolean inQueue;

    public Process(int processNum, int burstTime, int arrivalTime) {
        this.processNum = processNum;
        this.burstTime = burstTime;
        initialBurstTime = burstTime;
        this.arrivalTime = arrivalTime;
    }
    public Process(int processNum, int burstTime, int arrivalTime, int priority) {
        this.processNum = processNum;
        this.burstTime = burstTime;
        initialBurstTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.priority = priority;
    }
    public int getProcessNum() {
        return processNum;
    }
    public int getBurstTime() {
        return burstTime;
    }
    public int getInitialBurstTime() {
        return initialBurstTime;
    }
    public int getPriority() {
        return priority;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public int getCompletionTime() {
        return completionTime;
    }
    public int getTurnAroundTime() {
        return turnAroundTime;
    }
    public int getWaitingTime() {
        return waitingTime;
    }
    public boolean isComplete() {
        return isComplete;
    }
    public boolean inQueue() {
        return inQueue;
    }
    public void updateProcessTimes(int completionTime) {
        isComplete = true;
        turnAroundTime = completionTime - arrivalTime;
        waitingTime = turnAroundTime - initialBurstTime;
        this.completionTime = completionTime;
    }
    public void setInQueue(boolean inQueue) {
        this.inQueue = inQueue;
    }
    public void decrementBurstTime() {
        burstTime--;
    }
    public int compareTo(Process p) {
        if (this.getArrivalTime() - p.getArrivalTime() > 0)
            return 1;
        else if (this.getArrivalTime() == p.getArrivalTime() && this.getBurstTime() > p.getBurstTime())
            return 0;
        else 
            return -1;
    }
    public String toString() {
        return "(" + getArrivalTime() + ", " + getBurstTime() + ")";
    }
}