package main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Scheduler {
	   private Queue<Process> readyQueue;
	    private int timeSlice;

	    public Scheduler(int timeSlice) {
	        this.readyQueue = new LinkedList<>();
	        this.timeSlice = timeSlice;
	    }

	    public void addProcess(Process process) {
	        readyQueue.add(process);
	    }

	    public void runScheduler() {
	        while (!readyQueue.isEmpty()) {
	            Process currentProcess = readyQueue.poll();
	            PCB currentPCB = currentProcess.getPcb();

	            if (currentPCB.getProcess_state() == PCB.processState.NEW) {
	                // If the process is new, initialize its memory boundaries and set the state to READY
	                int[] memoryBoundaries = new int[3]; // Set the memory boundaries as per your requirements
	                currentPCB.setMemoryBoundaries(memoryBoundaries);
	                currentPCB.setProcess_state(PCB.processState.READY);
	            }

	            for (int i = 0; i < timeSlice && currentPCB.getProcess_state() != PCB.processState.FINISHED; i++) {
	                executeNextInstruction(currentProcess);
	                currentPCB.setProgram_counter(currentPCB.getProgram_counter() + 1);
	            }

	            if (currentPCB.getProcess_state() != PCB.processState.FINISHED) {
	                readyQueue.add(currentProcess);
	            }
	        }
	    }

	    private void executeNextInstruction(Process process) {
	        PCB currentPCB = process.getPcb();
	        ArrayList<Instruction> instructions = process.getInstructions();

	        if (currentPCB.getProgram_counter() >= instructions.size()) {
	            // All instructions executed, set the state to TERMINATED
	            currentPCB.setProcess_state(PCB.processState.FINISHED);
	            return;
	        }

	        Instruction instruction = instructions.get(currentPCB.getProgram_counter());
	        // Perform the necessary execution based on the instruction
	        // You can add the logic here to handle system calls, mutexes, and other instructions
	    }
}
