package main;

public class Scheduler {
    private PriorityQueue readyQueue;
    private PriorityQueue blockedQueue;
    private int timeSlice;

    public Scheduler(int timeSlice) {
        readyQueue = new PriorityQueue(3);
        blockedQueue = new PriorityQueue(3);
        this.timeSlice = timeSlice;
    }

    public void addToReadyQueue(Process process) {
        readyQueue.insert(process);
    }

    public Process getNextProcess() {
        if (readyQueue.isEmpty()) {
            return null;
        }

        // Rotate the ready queue
        Process nextProcess = (Process) readyQueue.remove();
        readyQueue.insert(nextProcess);

        return nextProcess;
    }

    public void addToBlockedQueue(Process process) {
        blockedQueue.insert(process);
    }


    public int getTimeSlice() {
        return timeSlice;
    }
}

