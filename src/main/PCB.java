package main;

public class PCB {
	
	private int process_ID;
	private processState process_state;
	private int program_counter;
	private int[] memoryBoundaries;
	
	public PCB(int ID, processState state, int pc, int[] mem) {
		
		setProcess_ID(ID);
		setProcess_state(state);
		setProgram_counter(pc);
		setMemoryBoundaries(mem);
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
	public int[] getMemoryBoundaries() {
		return memoryBoundaries;
	}

	public void setMemoryBoundaries(int[] memoryBoundaries) {
		this.memoryBoundaries = memoryBoundaries;
	}
	enum processState {
	    NEW,
	    READY,
	    RUNNING,
	    BLOCKED,
	    FINISHED
	}
	
}
