package main;

import java.util.LinkedList;
import java.util.Queue;

public class Mutex {
    private boolean isLocked = false;
    private Queue<PCB> blockedQueue = new LinkedList<>();
    private int id;

    public synchronized void semWait(PCB process) throws InterruptedException {
        while (isLocked) {
            blockedQueue.add(process);
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("mutex err");
                // Handle the InterruptedException appropriately
                throw e;
            }
        }
        isLocked = true;
        id = process.getProcess_ID();
    }

    public synchronized void semSignal(PCB process) {
    	if(id==process.getProcess_ID()) {
        isLocked = false;
        notify();
        if (!blockedQueue.isEmpty()) {
            PCB nextProcess = blockedQueue.poll();
            nextProcess.setProcess_state(PCB.processState.READY);
        }
    }
}
}
