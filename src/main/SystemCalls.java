package main;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class SystemCalls {
	
	
		public ArrayList<Object> readMem(Object[] memory,Process p) {
			ArrayList<Object> res = new ArrayList<>();
			for(int i = p.getPcb().getMemoryBoundaries()[0];i<p.getPcb().getMemoryBoundaries()[1];i++) {
				res.add(memory[i]);
			}
			return res;
		}
		public void writeMem(Object[] memory, int index , Object data) {
			memory[index] = data;
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
	    public void writeDisk(int processID, String data) throws IOException {
	        File file = new File("src/text/disk.txt");
	        StringBuilder diskData = new StringBuilder();
	        boolean isTargetProcess = false;

	        if (file.exists()) {
	            // Read the existing data from the file
	            Scanner scanner = new Scanner(file);

	            while (scanner.hasNextLine()) {
	                String line = scanner.nextLine();
	                if (line.startsWith("Process ID:")) {
	                    int id = Integer.parseInt(line.split(":")[1].trim());
	                    if (isTargetProcess) {
	                        // Append the new data to the existing data
	                        diskData.append(data).append("\n");
	                        isTargetProcess = false;
	                    }
	                    if (id == processID) {
	                        isTargetProcess = true;
	                    }
	                }
	                diskData.append(line).append("\n");
	            }
	            scanner.close();
	        }

	        // If the target process ID is not found in the existing data, append the new data at the end
	        if (!isTargetProcess) {
	            diskData.append("Process ID: ").append(processID).append("\n");
	            diskData.append(data).append("\n");
	        }

	        // Write the modified data back to the file
	        FileWriter writer = new FileWriter(file);
	        writer.write(diskData.toString());
	        writer.close();
	    }

	    public static void main(String[] args) throws IOException {
			SystemCalls hassan = new SystemCalls();
			hassan.writeDisk(2, "marwan");
			System.out.println(hassan.readDisk(2));
		}
}

