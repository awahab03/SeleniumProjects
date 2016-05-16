package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Kill_Process {
	
	private static final String TASKLIST = "tasklist";
	private static final String KILL = "taskkill /F /IM ";
	
	public void kill(String procName) throws IOException, Exception {
		
		//procName = "EXCEL.EXE";
		if(isProcessRunning(procName)){
			killProcess(procName);
		}
		

	}
	
	public boolean isProcessRunning(String processName) throws IOException{
		
		Process p = Runtime.getRuntime().exec(TASKLIST);
		 BufferedReader reader = new BufferedReader(new InputStreamReader(
		   p.getInputStream()));
		 String line;
		 while ((line = reader.readLine()) != null) {

		  //System.out.println(line);
		  if (line.contains(processName)) {
		   return true;
		  }
		 }

		 return false;
	}

	public  void killProcess(String serviceName) throws Exception {

		  Runtime.getRuntime().exec(KILL + serviceName);

		 }

}
