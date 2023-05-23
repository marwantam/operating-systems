package main;

import java.util.ArrayList;

public class Process implements Comparable {
	private PCB pcb;
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
	public int value(Process x) {
		if((((Process) x)).getPcb().getProcess_state().equals(pcb.getProcess_state().NEW)){
			return 1;
		}else if((((Process) x)).getPcb().getProcess_state().equals(pcb.getProcess_state().BLOCKED)) {
			return 0;
		}else
			return -1;
	}

}
