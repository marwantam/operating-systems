package main;

import java.io.IOException;

public class Interpreter {
	private SystemCalls systemCalls;
	private Mutex fileMutex;
	private Mutex inputMutex;
	private Mutex outputMutex;
	private Object[] memory = new Object[40];

	public Interpreter() {
		systemCalls = new SystemCalls();
		fileMutex = new Mutex();
		inputMutex = new Mutex();
		outputMutex = new Mutex();
	}

	public void interpret(String inst, Process p) throws InterruptedException {
		String[] tokens = inst.split(" ");
		switch (tokens[0]) {
		case "semWait":
			wait(tokens[1], p);
			break;
		case "semSignal":
			signal(tokens[1], p);
			break;
		case "assign":
			assign(tokens, p);
			break;
		case "printFromTo":
			printFromTo(tokens, p);
			break;
		case "writeFile":
			write(tokens, p);
			break;
		case "print":
			;
			break;
		}
	}

	public void wait(String s, Process p) throws InterruptedException {
		if (s.equals("userInput")) {
			inputMutex.semWait(p.getPcb());
		}
		if (s.equals("file")) {
			fileMutex.semWait(p.getPcb());
		}
		if (s.equals("userOutput")) {
			outputMutex.semWait(p.getPcb());
		}

	}

	public void signal(String s, Process p) {
		if (s.equals("userInput")) {
			inputMutex.semSignal(p.getPcb());
		}
		if (s.equals("file")) {
			fileMutex.semSignal(p.getPcb());
		}
		if (s.equals("userOutput")) {
			outputMutex.semSignal(p.getPcb());
		}
	}

	public void assign(String var[], Process p) {
		String varname = var[1];
		if (var[2].equals("input")) {
			String input = systemCalls.takeInput();
			systemCalls.writeMem(memory, p.getPcb(), input, varname);
		}
		if (var[2].equals("readFile")) {
			String input = systemCalls.readFile(var[3]);
			systemCalls.writeMem(memory, p.getPcb(), input, varname);
		}

	}

	public void printFromTo(String[] tokens, Process p) throws InterruptedException {
		int a = Integer.parseInt(tokens[1]);
		int b = Integer.parseInt(tokens[2]);

		for (a = a + 1; a < b; a++) {
			systemCalls.printOutput(Integer.toString(a));
		}
	}

	public void write(String[] tokens, Process p) {
		systemCalls.writeFile(tokens[1], tokens[2]);
	}

	public void print(String data, Process p) {
		systemCalls.printOutput(data);
	}
	
	public static void main(String[] args) {
		Interpreter interpreter = new Interpreter();
		// interpreter.interpret();
	}

}
