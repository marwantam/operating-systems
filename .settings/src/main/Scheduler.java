package main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Scheduler {
    private Queue<Process> readyQueue;
    private Queue<Process> blockedQueue;
    private int timeSlice;

    public Scheduler(int timeSlice) {
        readyQueue = new LinkedList<>();
        blockedQueue = new LinkedList<>();
        this.timeSlice = timeSlice;
    }

    public void addToReadyQueue(Process process) {
        readyQueue.add(process);
    }

    public Process getNextProcess() {
        if (readyQueue.isEmpty()) {
            return null;
        }

        // Rotate the ready queue
        Process nextProcess = readyQueue.poll();
        readyQueue.add(nextProcess);

        return nextProcess;
    }

    public void addToBlockedQueue(Process process) {
        blockedQueue.add(process);
    }

    public void removeBlockedProcess(Process process) {
        blockedQueue.remove(process);
    }

    public boolean isReadyQueueEmpty() {
        return readyQueue.isEmpty();
    }

    public boolean isBlockedQueueEmpty() {
        return blockedQueue.isEmpty();
    }

    public Queue<Process> getReadyQueue() {
        return readyQueue;
    }

    public Queue<Process> getBlockedQueue() {
        return blockedQueue;
    }

    public int getTimeSlice() {
        return timeSlice;
    }
}

