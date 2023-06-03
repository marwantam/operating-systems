
package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import main.PCB.processState;

public class OpSys {

	private static SystemCalls systemCalls;
	private static Mutex fileMutex;
	private static Mutex inputMutex;
	private static Mutex outputMutex;
	private static Memory memory;
	private static Process current;
	private static Process next = null;
	private static boolean prog1 = false;
	private static boolean prog2 = false;
	private static boolean prog3 = false;
	private static boolean currentFlag = false;
	private static boolean newFlag = false;
	private static final int timeslice = 3;
	private static int t1 = 3;
	private static int t2 = 0;
	private static int t3 = 4;
	private static int finishCount = 0;
	private static int clockNew = 0;
	private static Queue<Process> readyQueue;
	private static Queue<Process> blockedQueue;
	private static int clock;
	private static int timeLeft = timeslice;
	private static ArrayList<Process> processes = new ArrayList<>();
	private static int cycles = 0;
		
	public OpSys() {
		clock = 0;
		this.memory = new Memory();
		this.systemCalls = new SystemCalls();
		this.fileMutex = new Mutex();
		this.inputMutex = new Mutex();
		this.outputMutex = new Mutex();
		readyQueue = new ArrayDeque<Process>(3);
		blockedQueue = new ArrayDeque<Process>(3);
	}

	
	public static void printMem() {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("MEMORY AT: "+clock);
		for(int i=0;i<memory.getMemory().length;i++) {
			System.out.print(memory.getMemory()[i].toString()+"    ");
			if(i==19 || i ==39) {
				System.out.println("");
			}
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

	}
	
	public static int swap() throws IOException {
		for(int i= 0;i<processes.size();i++) {
			if(processes.get(i).getPcb().getProcess_state().equals(PCB.processState.FINISHED)) {
				processes.remove(i);
				return i+1;
			}
		}
		if(processes.get(1).getPcb().getProcess_state().equals(PCB.processState.BLOCKED)) {
			processes.get(1).getPcb().setProcess_state(PCB.processState.BLOCKEDDISK);
		}else if(processes.get(1).getPcb().getProcess_state().equals(PCB.processState.READY)) {
			processes.get(1).getPcb().setProcess_state(PCB.processState.READYDISK);
		}
		ArrayList<String> proc = new ArrayList<String>();
		for(int i = 20; i<= 39;i++) {
			proc.add(memory.getMemory()[i]);
			memory.getMemory()[i] = "";
		}
		writeToDisk(proc);
		return 2;
	}
	
	public static void swapping(Process Current) throws IOException {
		ArrayList<String> fromDisk = systemCalls.readFromFile("src/files/Disk.txt");
		ArrayList <String> toDisk = new ArrayList<String>();
		for(int i = 20; i < 40 ; i++) {
			toDisk.add(memory.getMemory()[i]);
			if(i-20 < fromDisk.size())
				memory.getMemory()[i] = fromDisk.get(i-20);
			}
		memory.getMemory()[23] = 20 +"";
		memory.getMemory()[24] = 39 + "";
		current.getPcb().setMinBound(20);
		current.getPcb().setMaxBound(39);
		toDisk.set(3, -1+"");
		toDisk.set(4, -1+"");
		writeToDisk(toDisk);
		current.getPcb().setMinBound(20);
		current.getPcb().setMaxBound(39);
		System.out.println(toDisk.get(0)+ " WRITTEN TO DISK!!!!!");
		printMem();

		
	}
	
	public static boolean inMem(Process current) {
		int id = current.getPcb().getProcess_ID();
		if(memory.getMemory()[0].equals(id+"") || memory.getMemory()[20].equals(id+"")) {
			return true;
		}
		return false;
	}
	
	public static void writeToDisk(ArrayList<String> data) throws IOException {
		  
	    File file = new File("src/files/Disk.txt");

	    FileWriter writer = new FileWriter(file);
	    for(int i = 0; i < data.size(); i++) {
	    	writer.write(data.get(i));
	    	if(i != data.size() - 1) {
	    		writer.write("\n");
	    	}
	    }
	    
	    writer.close();
	}
	public static void writeToDisk(String[] data) throws IOException {
		  
	    File file = new File("src/files/Disk.txt");

	    FileWriter writer = new FileWriter(file);
	    for(int i = 0; i < data.length; i++) {
	    	writer.write(data[i]);
	    	if(i != data.length - 1) {
	    		writer.write("\n");
	    	}
	    }
	    
	    writer.close();
	}
	public static Process create(String filepath, int bound) throws IOException {
		int lower = 0;
		int upper = 0;
		PCB pcb = null;
		if(bound == 0) {
			pcb = new PCB(processes.size()+1,PCB.processState.NEW,0,-1,-1);
//				bound = swap();
		}
		
		if(bound == 1) {
			upper = 19;
			pcb = new PCB(processes.size()+1,PCB.processState.NEW,0,lower,upper);
		}else if(bound == 2){
			lower = 20;
			upper = 39;
			pcb = new PCB(processes.size()+1,PCB.processState.NEW,0,lower,upper);
		}
		
		ArrayList<String> instructions = SystemCalls.readFromFile(filepath);
		Process process = new Process(pcb, instructions);
		processes.add(process);
		return process;
	}
	public static String[] toArr(Process p) {
		String[] arr = new String[20];
		arr[0] =p.getPcb().getProcess_ID()+"";
		arr[1] =p.getPcb().getProcess_state()+"";
		arr[2] =p.getPcb().getProgram_counter()+"";
		arr[3] = p.getPcb().getMinBound()+"";
		arr[4] = p.getPcb().getMaxBound()+"";
		arr[5] = "";
		arr[6] = "";
		arr[7] = "";
		for(int i = 8;i<20;i++) {
			if((i-8)<p.getInstructions().size()) {
			arr[i] = p.getInstructions().get(i-8);
			}else
				arr[i] = "";
		}
		return arr;
		
	}
	public static String[] toArrDisk(Process p) {
		String[] arr = new String[20];
		arr[0] =p.getPcb().getProcess_ID()+"";
		arr[1] =p.getPcb().getProcess_state()+"";
		arr[2] =p.getPcb().getProgram_counter()+"";
		arr[3] = -1 + "";
		arr[4] = -1 + "";
		arr[5] = "";
		arr[6] = "";
		arr[7] = "";
		for(int i = 8;i<20;i++) {
			if((i-8)<p.getInstructions().size()) {
			arr[i] = p.getInstructions().get(i-8);
			}else
				arr[i] = "";
		}
		return arr;
		
	}
	public static int whereInMem(Process current) {
		int id = current.getPcb().getProcess_ID();
		int where = 0;
		if(memory.getMemory()[0].equals(id+"")) {
			where = 1;
		}else if(memory.getMemory()[20].equals(id+"")) {
			where = 2;
		}
		return where;
		
		
		
	}
	public static void interpretInsInTurn(Process current) throws IOException, InterruptedException{
		int id = current.getPcb().getProcess_ID();
		int where = 0;
		int insNum = -1;
		System.out.println("PROCESS EXECUTING: " + id);
		if((!inMem(current) && current.getPcb().getProcess_state() != processState.BLOCKED)) {
			swapping(current);
			System.out.println(current.getPcb().getProcess_ID() + "SWAPPED");
			printMem();
		}
		if(memory.getMemory()[0].equals(id+"")) {
			where = 1;
		}
		
		else if(memory.getMemory()[20].equals(id+"")) {
			where = 2;
		}
		if(where == 1 && Integer.parseInt(memory.getMemory()[2]) + 8 < 20) {
			insNum = Integer.parseInt(memory.getMemory()[2]) + 8;
			System.out.println("PROCESS ID: " + id + " INSTRUCTION EXECUTING: " + memory.getMemory()[insNum]);
		}
		else if(where == 2 && Integer.parseInt(memory.getMemory()[22]) + 8 + 20 < 40) {
			insNum = Integer.parseInt(memory.getMemory()[22]) + 8 + 20;
			System.out.println("PROCESS ID: " + id + " INSTRUCTION EXECUTING: " + memory.getMemory()[insNum]);

	}
		Interpret(memory.getMemory()[insNum]);
		printQ();
		printMem();
	}
	public static void setMemory(String[] memory1) {
		for(int i = 0; i< memory.getMemory().length;i++) {
		memory.getMemory()[i] = memory1[i];
		}
	}
	public static void store(Process p, int bound) throws IOException {
		if(bound == 1) {
			setMemory(systemCalls.writeMem(toArr(p), memory.getMemory(), 0));
			memory.first = true;
		}
		else if(bound == 2) {
			setMemory(systemCalls.writeMem(toArr(p), memory.getMemory(), 20));
			memory.second = true;
		}
		else {
			writeToDisk(toArrDisk(p));
		}
	}
//	public static String getState(Process p) {
//		int id = current.getPcb().getProcess_ID();
//		int where = 0;
//		if(where == 1) {
//			
//		}
//		else if(where == 2) {
//			
//		}
//		
//	}
	
	
	public static Process checkArrivals() throws IOException {
		if(clock == t1 && !prog1) {
			System.out.println("PROGRAM 1 ARRIVAL AT: " + t1);
			int bound = reserve();
			Process p1 = create("src/files/Program_1.txt",bound);
			((ArrayDeque<Process>) readyQueue).addFirst(p1);
			clockNew = clock;
			p1.getPcb().setProcess_state(PCB.processState.READY);
			store(p1,bound);
			printMem();
			prog1 = true;
			return p1;
		}
		if(clock == t2 && !prog2) {
			System.out.println("PROGRAM 2 ARRIVAL AT: " + t2);
			int bound = reserve();
			Process p2 = create("src/files/Program_2.txt",bound);
			((ArrayDeque<Process>) readyQueue).addFirst(p2);
			clockNew = clock;
			reorderQ(p2);
			p2.getPcb().setProcess_state(PCB.processState.READY);
			store(p2,bound);
			printMem();
			prog2 = true;
			return p2;
		
		}
		if(clock == t3 && !prog3) {
			System.out.println("PROGRAM 3 ARRIVAL AT: " + t3);
			int bound = reserve();
			Process p3 = create("src/files/Program_3.txt",bound);
			((ArrayDeque<Process>) readyQueue).addFirst(p3);
			clockNew = clock;
			p3.getPcb().setProcess_state(PCB.processState.READY);
			store(p3,bound);
			printMem();
			prog3 = true;
			return p3;
		}
		return null;

	}
	public static boolean idInQ(Process p) {
		int id = p.getPcb().getProcess_ID();
		Object [] arr = readyQueue.toArray();
		for(int i = 0; i < arr.length; i++) {
			if(((Process)arr[i]).getPcb().getProcess_ID() == id) {
				return true;
			}
		}
		return false;
	}
	
	public static void run() throws IOException, InterruptedException {
		System.out.println("================================================================================================================================");
		ArrayList<Process> nextProcesses = new ArrayList<Process>();
		
		checkArrivals();
		printQ();
		if(!readyQueue.isEmpty() || (clock < t1 && clock < t2 && clock < t3)) {
			current = readyQueue.poll();
			currentFlag = true;
			System.out.println("CURRENT: " + current.getPcb().getProcess_ID());
			setState(current, processState.RUNNING);
			if(!inMem(current)) {
				swapping(current);
			}
		}
		else {
			currentFlag = false;
		}
		while(timeLeft > 0 && !current.getPcb().getProcess_state().equals(processState.BLOCKED) && !current.getPcb().getProcess_state().equals(processState.FINISHED)) {	
		checkArrivals();
		printQ();
		PsInMem();
		System.out.println("CURRENT PC: " + current.getPcb().getProgram_counter());
		if(current.getPcb().getProgram_counter()==current.getInstructions().size()) {
			System.out.println("PROCESS "+current.getPcb().getProcess_ID()+" FINISHED");
			finish(current);
			finishCount++;
			if(finishCount == 3) {
				return;
			}
			break;
		}
		
		if(!current.getPcb().getProcess_state().equals(processState.BLOCKED) && !current.getPcb().getProcess_state().equals(processState.FINISHED)) {
			System.out.println("Process executing: " + current.getPcb().getProcess_ID() + " Process State: " + current.getPcb().getProcess_state());
			interpretInsInTurn(current);
//			if(current.getPcb().getProgram_counter()==current.getInstructions().size()) {
//				System.out.println("PROCESS "+current.getPcb().getProcess_ID()+" FINISHED");
//				finish(current);
//				finishCount++;
//				if(finishCount == 3) {
//					return;
//				}
//				break;
//			}
		}
		else{
			timeLeft = 0;
		}
		
		System.out.println("+++++++++++++++++++++++++current cycle:      "+ clock+"++++++++++++++++++++++++++++++++");
		clock++;
		cycles++;
		}
		if(!current.getPcb().getProcess_state().equals(processState.BLOCKED) && !current.getPcb().getProcess_state().equals(processState.FINISHED)) {
			if(!idInQ(current))
				readyQueue.add(current);
		}
		timeLeft = timeslice;
		if(!readyQueue.isEmpty() || (current != null && !current.getPcb().getProcess_state().equals(processState.FINISHED))) {
			if(!current.getPcb().getProcess_state().equals(processState.BLOCKED) && !current.getPcb().getProcess_state().equals(processState.FINISHED)) {
			setState(current, processState.READY);
			}
			run();
		}
	}
	
	
		private static void reorderQ(Process p) {
			Queue <Process> tmp = new ArrayDeque<Process>();
			if(!idInQ(p)) {
			tmp.add(p);
			for(int i = 0; i < readyQueue.size();i++) {
				tmp.add(readyQueue.poll());
			}
			for(int i = 0; i < tmp.size();i++) {
				readyQueue.add(tmp.poll());
			}
			}
	}

		

		public static void shift2in1() {
			ArrayList<String> temp = new ArrayList<String>();
			for(int i = 20; i < 40; i++) {
				temp.add(memory.getMemory()[i]);	//store second part of mem
				memory.getMemory()[i] = "";
			}
			for(int j = 0; j < 20; j++) {
				memory.getMemory()[j] = temp.get(j); // overwrite in first part
			}
			memory.getMemory()[3] = "0";
			memory.getMemory()[4] = "19";
			if(!memory.getMemory()[0].equals("")) {
			int id = Integer.parseInt(memory.getMemory()[0]);
			
			setBoundsWithId(id);
			}
		}
		public static void setBoundsWithId(int id) {
			Queue <Process> tmp = new ArrayDeque<Process>();
			Process curr;
			for(int i = 0; i < readyQueue.size();i++) {
				curr = readyQueue.poll();
				if(curr.getPcb().getProcess_ID() == id) {
					curr.getPcb().setMinBound(0);
					curr.getPcb().setMaxBound(19);
				}
				tmp.add(curr);
			}
			for(int j = 0 ; j < tmp.size(); j++) {
				readyQueue.add(tmp.poll());
			}
		}
	public static void finish(Process p) {
		readyQueue.remove(p);
		setState(p,processState.FINISHED);
		if(whereInMem(p) == 1) {
			shift2in1();
			printMem();
		}

	}
	public static void setState(Process p, processState state) {
		p.getPcb().setProcess_state(state);
		System.out.println("CURRENT STATE SHOULD BE: " + p.getPcb().getProcess_state());
		if(whereInMem(p)==1) {
		memory.getMemory()[1] = p.getPcb().getProcess_state()+"";
		}else if(whereInMem(p)==2) {
			memory.getMemory()[21] = p.getPcb().getProcess_state()+"";
		}
	}
	
	public static void incPC(Process p) {
		p.getPcb().setProgram_counter(p.getPcb().getProgram_counter()+1);
		if(whereInMem(p) == 1)
			memory.getMemory()[2] = p.getPcb().getProgram_counter()+"";
		else
			memory.getMemory()[22] = p.getPcb().getProgram_counter()+"";
		
		timeLeft--;
	}
	
	public static int reserve() {
		if (memory.first==false) {
			memory.first = true;
			return 1;
		}
		else {
			if(memory.second==false) {
				memory.second=true;
				return 2;
			}
			return 0;
		}
			
	}
	public static void PsInMem() {
		System.out.println("PROCESSES IN MEMORY: " + memory.getMemory()[0] + " AND "+ memory.getMemory()[20]);
	}
	
	
 	public static void Interpret(String inst) throws InterruptedException, IOException {
		String[] tokens = inst.split(" ");
		switch (tokens[0]) {
		case "semWait":
			wait(tokens[1]);
			incPC(current);
			break;
		case "semSignal":
			signal(tokens[1]);
			incPC(current);
			break;
		case "assign":
			assign(tokens);
			break;
		case "printFromTo":
			printFromTo(tokens);
			incPC(current);
			break;
		case "writeFile":
			write(tokens);
			incPC(current);
			break;
		case "print":
			print(tokens);
			incPC(current);
			break;
		}
	}


	private static void print(String[] tokens) {
		systemCalls.print(getVal(tokens[1]));
		
	}


	private static void write(String[] tokens) throws IOException  {
		System.out.println("VAL 2: " + getVal(tokens[2]) + "VAL 1: " + getVal(tokens[1]));
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		systemCalls.writeToFile(getVal(tokens[2]), getVal(tokens[1]));
		
	}


	private static void printFromTo(String[] tokens) {
		int low = Integer.parseInt(getVal(tokens[1]));
		int high = Integer.parseInt(getVal(tokens[2]));
		for(int i = low;i<=high;i++) {
			systemCalls.print(i+"");
		}

	}


	private static void assign(String[] tokens) throws IOException {
		String input;
		if(tokens[2].equals("input") &&  current.temp == null) {
				input = systemCalls.takeInput();
				current.temp = input;
				timeLeft--;
				return;
			}else if(current.temp == null){
				input = systemCalls.readFromFile(getVal(tokens[3])).get(0);
				current.temp = input;
				timeLeft--;
				return;
			}
			if(getVal(tokens[1])==null) {
				String var = tokens[1]+":"+current.temp;
				current.temp  =null;
				int minBound = whereInMem(current) == 1 ? 0 : 20;
				for(int i = minBound+5;i<=minBound+7;i++) {
					if(memory.getMemory()[i].equals("")) {
						memory.getMemory()[i]=var;
						break;
					}
				}
				
			}else {
				
				for(int i = current.getPcb().getMinBound()+5;i<=current.getPcb().getMinBound()+7;i++) {
					if(((String)memory.getMemory()[i]).split(":")[0].equals(tokens[1])) {
						String var = tokens[1]+":"+current.temp;
						current.temp  =null;
						memory.getMemory()[i]=var;
						break;
					}
				}
			}
			incPC(current);
	}
	public static String getVal(String name) {
//		System.out.println("GET VALLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
		if(whereInMem(current) == 1) {
			for(int i = 0;i<=19;i++) {
				if(!memory.getMemory()[i].equals("")) {
				String var = ((String) memory.getMemory()[i].toString()).split(":")[0];
				if(var.equals(name)) {
					return ( (String) memory.getMemory()[i]).split(":")[1];
				}
		}
			}
		}else if(whereInMem(current) == 2) {
			for(int i = 20;i<=39;i++) {
				if(!memory.getMemory()[i].equals("")) {
				String var = ((String) memory.getMemory()[i].toString()).split(":")[0];
				if(var.equals(name)) {
					return ( (String) memory.getMemory()[i]).split(":")[1];
				}
		}
			}
		}
		return null;
	}



	public static void insertFromBlockedToQ(Process p) {
		if(clockNew == clock) {
			Process tmp = readyQueue.poll();
			if(!idInQ(p)) {
			((ArrayDeque<Process>) readyQueue).addFirst(p);
			}
			((ArrayDeque<Process>) readyQueue).addFirst(tmp);	
		}else {
			if(!idInQ(p)) {
				((ArrayDeque<Process>) readyQueue).addFirst(p);
				}
		}
		
	}
	
	
	private static void signal(String s) {
		int status = -1;
		if (s.equals("userInput")) {
			status = inputMutex.semSignal(current);
		}
		if (s.equals("file")) {
			status = fileMutex.semSignal(current);
		}
		if (s.equals("userOutput")) {
			status = outputMutex.semSignal(current);
		}		
		if (status == 1) {
			if (s.equals("userInput")) {
				
				if(!inputMutex.blockedQueue.isEmpty()) {
					inputMutex.id = inputMutex.blockedQueue.peek().getPcb().getProcess_ID();
					setState(inputMutex.blockedQueue.peek(), processState.READY);
					blockedQueue.remove(inputMutex.blockedQueue.peek());
					System.out.println("ID TO BE UNBLOCKED: " + inputMutex.blockedQueue.peek().getPcb().getProcess_ID());
					readyQueue.add(inputMutex.blockedQueue.poll());
			}
			}
			if (s.equals("file")) {
				
				if(!fileMutex.blockedQueue.isEmpty()) {
					fileMutex.id = fileMutex.blockedQueue.peek().getPcb().getProcess_ID();
					setState(fileMutex.blockedQueue.peek(), processState.READY);
					blockedQueue.remove(fileMutex.blockedQueue.peek());
					System.out.println("ID TO BE UNBLOCKED: " + fileMutex.blockedQueue.peek().getPcb().getProcess_ID());
					readyQueue.add(fileMutex.blockedQueue.poll());
			}
			}
			if (s.equals("userOutput")) {
				
				if(!outputMutex.blockedQueue.isEmpty()) {
					outputMutex.id = outputMutex.blockedQueue.peek().getPcb().getProcess_ID();
					setState(outputMutex.blockedQueue.peek(), processState.READY);
					blockedQueue.remove(outputMutex.blockedQueue.peek());
					System.out.println("ID TO BE UNBLOCKED: " + outputMutex.blockedQueue.peek().getPcb().getProcess_ID());
					readyQueue.add(outputMutex.blockedQueue.poll());
					
			}
			}
//			insertFromBlockedToQ(blockedQueue.poll());
			}
		System.out.println("STATUS:  "+ status);
			
		}
	

	
	public static void printQ() {
		System.out.println("______________________________________________________");
		System.out.println("");

		Object[] arr = readyQueue.toArray();
		System.out.print("READY QUEUE: ");
		for(int i = 0; i < arr.length; i++) {
			System.out.print(((Process)arr[i]).getPcb().getProcess_ID() + " ");
		}
		System.out.println("");
		Object[] arr2 = blockedQueue.toArray();
		System.out.print("BLOCKED QUEUE: ");
		for(int i = 0; i < arr2.length; i++) {
			System.out.print(((Process)arr2[i]).getPcb().getProcess_ID() + " ");
		}
		System.out.println("");
		System.out.println("______________________________________________________");
		
	}
	
	public  static void removeAll(Process p) {
		int id = p.getPcb().getProcess_ID();
		for(int i = 0; i < readyQueue.size(); i++) {
			Process current = readyQueue.poll();
			if(current.getPcb().getProcess_ID() != id) {
				readyQueue.add(current);
				printQ();
			}
		}
	}
	
	private static void wait(String s) throws InterruptedException {
		int status = -1;
		if (s.equals("userInput")) {
			status = inputMutex.semWait(current);
		}
		if (s.equals("file")) {
			status = fileMutex.semWait(current);
		}
		if (s.equals("userOutput")) {
			status = outputMutex.semWait(current);
		}
		
		if(status == 0) {
			printQ();
			blockedQueue.add(current);
			setState(current,processState.BLOCKED);
			printQ();
			printMem();

		}
		
	}
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		OpSys OpSys = new OpSys();
		run();	
//		System.out.println("Total cycles: " + clock);
//		Process process = new Process();
//		PCB pcb = new PCB(1, PCB.processState.NEW, 0, 0,19);
//		process.setPcb(pcb);
//		Program prog = new Program();
//		prog.setFilepath("src/files/Program_1.txt");
//		prog.setInstructions(SystemCalls.readFromFile(prog.getFilepath()));
//		process.setProgram(prog);
//		current = process;
//		Interpret(process.getProgram().getInstructions().get(0));
//		Interpret(process.getProgram().getInstructions().get(1));
//		System.out.println((String)memory.getMemory()[5].toString());
//		Interpret(process.getProgram().getInstructions().get(2));
//		System.out.println((String)memory.getMemory()[6].toString());
//		Interpret(process.getProgram().getInstructions().get(3));
//		Interpret(process.getProgram().getInstructions().get(4));
//		Interpret(process.getProgram().getInstructions().get(5));
//		Interpret(process.getProgram().getInstructions().get(6));


//		Process process2 = new Process();
//		PCB pcb2 = new PCB(2, PCB.processState.NEW, 0, 20,39);
//		process2.setPcb(pcb2);
//		Program prog2 = new Program();
//		prog2.setFilepath("src/files/Program_2.txt");
//		prog2.setInstructions(SystemCalls.readFromFile(prog2.getFilepath()));
//		process2.setProgram(prog2);
//		current = process2;
//		Interpret(process2.getProgram().getInstructions().get(0));
//		Interpret(process2.getProgram().getInstructions().get(1));
//		Interpret(process2.getProgram().getInstructions().get(2));
//		Interpret(process2.getProgram().getInstructions().get(3));
//		Interpret(process2.getProgram().getInstructions().get(4));
//		Interpret(process2.getProgram().getInstructions().get(5));
//		Interpret(process2.getProgram().getInstructions().get(6));
//		
//		Process process3 = new Process();
//		PCB pcb3 = new PCB(3, PCB.processState.NEW, 0, 20,39);
//		process3.setPcb(pcb3);
//		Program prog3 = new Program();
//		prog3.setFilepath("src/files/Program_3.txt");
//		prog3.setInstructions(SystemCalls.readFromFile(prog3.getFilepath()));
//		process3.setProgram(prog3);
//		current = process3;
//		Interpret(process3.getProgram().getInstructions().get(0));
//		Interpret(process3.getProgram().getInstructions().get(1));
//		Interpret(process3.getProgram().getInstructions().get(2));
//		Interpret(process3.getProgram().getInstructions().get(3));
//		Interpret(process3.getProgram().getInstructions().get(4));
//		Interpret(process3.getProgram().getInstructions().get(5));
//		Interpret(process3.getProgram().getInstructions().get(6));
//		Interpret(process3.getProgram().getInstructions().get(7));
//		Interpret(process3.getProgram().getInstructions().get(8));	
		
	}
	
}
