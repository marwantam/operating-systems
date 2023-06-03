package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class SystemCalls {
	
	public static ArrayList<String> readFromFile(String file) throws IOException {
	    try (
	        FileInputStream fis = new FileInputStream(file);
	        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	    ) {
	        ArrayList<String> lines = new ArrayList<>();
	        String line;
	        while ((line = br.readLine()) != null) {
	            lines.add(line);
	        }
	        return lines;
	        
	    }
	}


	
	public static void writeToFile(String data, String filepath) throws IOException {
	  
	    File file = new File(filepath);

	    if (!file.exists()) {
	        file.createNewFile();
	    }
	    FileWriter writer = new FileWriter(file);
	    writer.write(data);
	    writer.close();
	}
	public static void print(String data) {
		System.out.println(data);
	}
	public String takeInput() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Please enter a value: ");
		return scanner.nextLine();
	}
	public static String[] readMem(int start, int end, String[] words) {
	    // Check if the start and end indices are valid.
	    if (start < 0 || start >= words.length || end < 0 || end >= words.length) {
	        return null;
	    }

	    // Create a new array to hold the results.
	    String[] result = new String[end - start + 1];

	    // Copy the words from the original array to the new array.
	    for (int i = start; i <= end; i++) {
	        result[i - start] = words[i];
	    }

	    // Return the results.
	    return result;
	}
	public static String[] writeMem(String[] data, String[] memory, int start) {
	    // Check if the start index is valid.
	    if (start < 0 || start >= memory.length) {
	    	
	        return null;
	    }

	    // Check if the data array is larger than the memory array.
	    if (data.length > memory.length - start) {
	    	
	        return null;
	    }

	    // Copy the words from the data array to the memory array, starting at the start index.
	    for (int i = 0; i < data.length; i++) {
	        memory[start + i] = data[i];
	       
	    }
	    return memory;
	}
	public static void main(String[] args) {
//		String[] data = {"1","2","3","4"};
//		String[] memory= {"","","","","","","","","",""};
//		memory = writeMem(data, memory, 3);
//		
//		for(int i = 0;i<memory.length;i++) {
//			System.out.println(i+"="+memory[i]);
//		}
	}

}
