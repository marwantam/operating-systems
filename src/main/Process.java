package main;

import java.util.ArrayList;

public class Process {
	private PCB pcb;
	private ArrayList<String> instructions;
	String temp = null;
	
	public Process(PCB pcb, ArrayList<String> inst) {
		this.pcb = pcb;
		instructions = inst;
	}
	
	public PCB getPcb() {
		return pcb;
	}
	public void setPcb(PCB pcb) {
		this.pcb = pcb;
	}
	public int compareTo(Object o) {
		if(value(this)>value((Process)o)) {
			return 1;
		}else
			return -1;
		
	}
	@SuppressWarnings("static-access")
	public int value(Process x) {
		if((((Process) x)).getPcb().getProcess_state().equals(pcb.getProcess_state().NEW)){
			return 1;
		}else if((((Process) x)).getPcb().getProcess_state().equals(pcb.getProcess_state().BLOCKED)) {
			return 0;
		}else
			return -1;
	}
	public ArrayList<String> getInstructions() {
		return instructions;
	}
	public void setProgram(ArrayList<String> inst) {
		this.instructions = inst;
	}

	
}
