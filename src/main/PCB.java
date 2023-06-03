package main;

import main.PCB.processState;

public class PCB {
	
	private int process_ID;
	private processState process_state;
	private int program_counter;
	private int minBound;
	private int maxBound;
	
	public PCB(int ID, processState state, int pc, int min, int max) {
		
		setProcess_ID(ID);
		setProcess_state(state);
		setProgram_counter(pc);
		setMaxBound(max);
		setMinBound(min);
		
	}

	public int getProcess_ID() {
		return process_ID;
	}

	public void setProcess_ID(int process_ID) {
		this.process_ID = process_ID;
	}

	public processState getProcess_state() {
		return process_state;
	}

	public void setProcess_state(processState process_state) {
		this.process_state = process_state;
	}

	public int getProgram_counter() {
		return program_counter;
	}

	public void setProgram_counter(int program_counter) {
		this.program_counter = program_counter;
	}
	public int getMinBound() {
		return minBound;
	}

	public void setMinBound(int minBound) {
		this.minBound = minBound;
	}
	public int getMaxBound() {
		return maxBound;
	}

	public void setMaxBound(int maxBound) {
		this.maxBound = maxBound;
	}
	enum processState {
	    NEW,
	    READY,
	    READYDISK,
	    RUNNING,
	    BLOCKED,
	    BLOCKEDDISK,
	    FINISHED
	}
	
	
}
