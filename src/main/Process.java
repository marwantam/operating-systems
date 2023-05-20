package main;

import java.util.ArrayList;

public class Process {
	private PCB pcb;
	private ArrayList<Instruction> arr;
	public PCB getPcb() {
		return pcb;
	}
	public void setPcb(PCB pcb) {
		this.pcb = pcb;
	}
	public ArrayList<Instruction> getArr() {
		return arr;
	}
	public void setArr(ArrayList<Instruction> arr) {
		this.arr = arr;
	}
	public ArrayList<Instruction> getInstructions() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
