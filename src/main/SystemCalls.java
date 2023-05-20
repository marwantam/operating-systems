package main;


import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class SystemCalls {
	
	
	  public String readFile(String filename) throws FileNotFoundException {
	        File file = new File(filename);
	        StringBuilder fileData = new StringBuilder();
	        Scanner scanner = new Scanner(file);

	        while (scanner.hasNextLine()) {
	            fileData.append(scanner.nextLine()).append("\n");
	        }

	        scanner.close();
	        return fileData.toString();
	    }

	    public void writeFile(String filename, String data) throws IOException {
	        FileWriter writer = new FileWriter(filename);
	        writer.write(data);
	        writer.close();
	    }

	    public void printOutput(String data) {
	        System.out.println(data);
	    }

	    public String takeInput() {
	        Scanner scanner = new Scanner(System.in);
	        System.out.print("Please enter a value: ");
	        return scanner.nextLine();
	    }

	    public String readMemory(int processId, int address) {
	        // Read data from memory for the given processId and address
	        return null;
	    }

	    public void writeMemory(int processId, int address, String data) {
	        // Write data to memory for the given processId and address
	    }
	    public static void main(String[] args) throws IOException {
			SystemCalls hassan = new SystemCalls();
			
		}
}

