package main;

import java.util.LinkedList;
import java.util.Queue;

import main.PCB.processState;

public class Mutex {
    private boolean isLocked = false;
    private Queue<PCB> blockedQueue = new LinkedList<>();

    public synchronized void acquire(PCB process) throws InterruptedException {
        while (isLocked) {
            blockedQueue.add(process);
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("mutex err");
            }
        }
        isLocked = true;
    }

    public synchronized void release() {
        isLocked = false;
        notify();
        if (!blockedQueue.isEmpty()) {
            PCB nextProcess = blockedQueue.poll();
            nextProcess.setProcess_state(processState.READY);
        }
    }
}