package main;

public class Memory {
	private String[] memory;
	boolean first;
	boolean second;
	
	
public Memory() {
	setMemory(new String[40]);
	for(int i = 0; i<40;i++) {
		memory[i] = "";
	}
	
}

public String[] getMemory() {
	return memory;
}

public void setMemory(String[] memory) {
	this.memory = memory;
}

public boolean isFirst() {
	return first;
}

public void setFirst(boolean first) {
	this.first = first;
}

public boolean isSecond() {
	return second;
}

public void setSecond(boolean second) {
	this.second = second;
}



}
