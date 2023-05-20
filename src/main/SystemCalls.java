package main;


import java.util.ArrayList;
import java.util.List;
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

	    public String readMemory(int processID, int address) throws FileNotFoundException {
	    	File file = new File("src/text/disk.txt");
	        StringBuilder data = new StringBuilder();

	        Scanner scanner = new Scanner(file);
	        boolean isTargetProcess = false;

	        while (scanner.hasNextLine()) {
	            String line = scanner.nextLine();
	            if (line.startsWith("Process ID:")) {
	                int id = Integer.parseInt(line.split(":")[1].trim());
	                isTargetProcess = (id == processID);
	            } else if (isTargetProcess && line.startsWith(address + ":")) {
	                data.append(line.substring(line.indexOf(":") + 1).trim());
	                break; // Found the desired address, so exit the loop
	            }
	        }

	        scanner.close();
	        return data.toString();
	    }
	    

	    public void writeMemory(int processId, String data) {
            try {
                File file = new File("src/text/disk.txt");
                FileWriter writer = new FileWriter(file, true);

                // Find the line that corresponds to the specified process ID
                Scanner scanner = new Scanner(file);
                boolean isTargetProcess = false;
                int address = 1;

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("Process ID:")) {
                        int id = Integer.parseInt(line.split(":")[1].trim());
                        isTargetProcess = (id == processId);
                    } else if (isTargetProcess) {
                         address++;
                        // Write the specified data to the line
                        writer.write("\n");
                        line = address + ": "+ line.substring(0, line.indexOf(":")) + data;
                        writer.write(line);

                        break; // Found the desired address, so exit the loop
                    }
                }

                scanner.close();
                writer.close();
            } catch (IOException e) {
                System.out.println("writemem");
                e.printStackTrace();
            }
        }


	    public static void main(String[] args) throws IOException {
			SystemCalls hassan = new SystemCalls();
			hassan.writeMemory(1, "hiuhiuhi");
			
		}
}

