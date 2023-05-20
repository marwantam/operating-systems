package main;

public class Interpreter {
	SystemCalls s = new SystemCalls();
	
	
	public void interpreted(String filename ,String soso) {
		switch(soso){
		case "print": s.printOutput(soso);
		}
	}

}
