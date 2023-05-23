package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class SystemCalls {

	public String readFile(String file) {
		try {
			File f = new File(file);
			if (!f.exists()) {
				System.out.println("File does not exist: " + file);
				return null;
			}
			FileInputStream fis = new FileInputStream(f);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
			return null;
		}
	}

	public void writeFile(String filepath, String data) {
	    try {
	        File f = new File(filepath);
	        if (!f.exists()) {
	            f.createNewFile();
	        }
	        FileWriter fw = new FileWriter(f);
	        fw.write(data);
	        fw.close();
	    } catch (IOException e) {
	        System.out.println("Error writing file: " + e.getMessage());
	    }
	}


	public ArrayList<Object> readMem(Object[] memory, Process p) {
		ArrayList<Object> res = new ArrayList<>();
		for (int i = p.getPcb().getMemoryBoundaries()[0]; i < p.getPcb().getMemoryBoundaries()[1]; i++) {
			res.add(memory[i]);
		}
		return res;
	}

	public void writeMem(Object[] memory, PCB process, Object data, String var) {
		// WRITE TO DESIGNATED SLOT
	}

	public void printOutput(String data) {
		System.out.println(data);
	}

	public String takeInput() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Please enter a value: ");
		return scanner.nextLine();
	}

	public String readDisk(int processID) throws FileNotFoundException {
		File file = new File("src/text/disk.txt");
		StringBuilder data = new StringBuilder();

		Scanner scanner = new Scanner(file);
		boolean isTargetProcess = false;

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.startsWith("Process ID:")) {
				int id = Integer.parseInt(line.split(":")[1].trim());
				if (isTargetProcess) {
					// Next process ID encountered, exit the loop
					break;
				}
				isTargetProcess = (id == processID);
			} else if (isTargetProcess) {
				// Append the line to the data
				data.append(line).append("\n");
			}
		}

		return data.toString();
	}

	public void writeDisk(int processID, String[] data) throws IOException {
		  File file = new File("src/text/disk.txt");

		  // Check if the file exists.
		  if (!file.exists()) {
		    System.out.println("File does not exist: " + file.getAbsolutePath());
		    return;
		  }

		  // Create a FileWriter object for the File object.
		  FileWriter fw = new FileWriter(file);

		  // Write the process ID to the file.
		  fw.write("Process ID: " + processID + "\n");

		  // Write the data to the file, separated by commas.
		  for (int i = 0; i < data.length; i++) {
		    fw.write(data[i] + ",");
		  }

		  // Close the FileWriter object.
		  fw.close();
		}

	public static void main(String[] args) throws IOException {
		SystemCalls hassan = new SystemCalls();
	//	hassan.writeDisk(2, "marwan");
	//	System.out.println(hassan.readDisk(2));
		hassan.writeFile("src/text/marwan.txt", "hassan");
	}
}
