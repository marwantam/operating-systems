package main;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

import main.PCB.processState;

public class Mutex {
    private boolean isLocked = false;
    public int id;
    public Queue <Process> blockedQueue = new ArrayDeque<Process>();

    public int semWait(Process process) {
    	if (isLocked) {
    		blockedQueue.add(process);
    		return 0;
    	}
        isLocked = true;
        id = process.getPcb().getProcess_ID();
        return 1;
    }

    public int semSignal(Process process) {
    	if(id==process.getPcb().getProcess_ID()) {
        isLocked = false;
        return 1;
    }
    	return 0;
}
    public int getID() {
    	return id;
    	
    }
    
    
}
