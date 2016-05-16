package result;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import global.Global;

public class CreateResultMatrix {
	public static void createResultMatrixHeader(){
		
		Global.resMatrixHead = "";	// Reset the header first.
		Global.resMatrixHead = "<html><head>";
		Global.resMatrixHead = Global.resMatrixHead + "<script src=\"Z:\\FQT\\AutFramework\\Config\\all_javaScripts.js\" language=\"JavaScript\" type=\"text/javascript\">";
		//Global.htmlHead = "<script type=\"text/javascript\">";
		//Global.htmlHead = Global.htmlHead + " function openWin(img){\n";
		//Global.htmlHead = Global.htmlHead + " window.open(img,\"mywin\",\"menubar=0,resizable=1,width=200,height=200\")}";
		String dForm = "MM/dd/yyyy";
		//String tForm = "HH:mm:ss:a";
		String rDate = Global.getStamp(dForm);
		//String rTime = Global.getStamp(tForm);
		Global.resMatrixHead = Global.resMatrixHead + "</script><title>" + Global.projName + " Automation Result Matrix - " + rDate + "</title>";
		Global.resMatrixHead = Global.resMatrixHead +  "<link rel=\"stylesheet\" type=\"text/css\" href=" + Global.cssPath + ">";
		Global.resMatrixHead = Global.resMatrixHead + "</head><body><br><center><h1>Test Results Report (TRR)</h1></center><hr>";		
	}
	
	public static void createResultMatrixTable (String Rel, String Sprint){

		String dForm = "MM/dd/yyyy";
		String rDate = Global.getStamp(dForm);
				
		Global.resMatrixTable = "";
		Global.resMatrixTable = Global.resMatrixTable + "<table class = usTable><col width=20%><col width=50%>";		
		Global.resMatrixTable = Global.resMatrixTable + "<tr><th colspan=100%>Test Set Information:</td></tr>";
		Global.resMatrixTable = Global.resMatrixTable + "<tr><td>Project Name:</td><td>" + Global.projName + "</td></tr>";
		Global.resMatrixTable = Global.resMatrixTable + "<tr><td>Release:</td><td>" + Rel + "</td></tr>";
		Global.resMatrixTable = Global.resMatrixTable + "<tr><td>Sprint:</td><td>" + Sprint + "</td></tr></table><hr><br><br>";
		
		Global.resMatrixTable = Global.resMatrixTable + "<center><h3>Run Date: " + rDate + "</h3></center><h3>Run Information:</h3>";
		Global.resMatrixTable = Global.resMatrixTable + "<table class = mainTable>";
		Global.resMatrixTable = Global.resMatrixTable + "<tr><th>Run Order.</th><th>Test Name</th><th>Start Time:</th><th>End Time</th><th>Status</th></tr>";
		
	}
	
	public static void addInitialPartToMatrixTable (int no){

		String tForm = "HH:mm:ss:a";
		String rTime = Global.getStamp(tForm);
		String resLink = Global.curResPath.replace("\\\\", "/");
		resLink = "file:///" + resLink;
		System.out.println(resLink);
		Global.resMatrixTable = Global.resMatrixTable + "<tr><td>" + no + "</td>";
		Global.resMatrixTable = Global.resMatrixTable + "<td><a href=" + resLink + ">" + Global.curTesName + "</a></td>";
		Global.resMatrixTable = Global.resMatrixTable + "<td>" + rTime + "</td>";
		
	}
	
	
	public static void addEndPartToMatrixTable(){

		String tForm = "HH:mm:ss:a";
		String rTime = Global.getStamp(tForm);
		String tStts = "";
		if(Global.status){
			tStts = "Passed";
		}else{
			tStts = "Failed";
		}
		Global.resMatrixTable = Global.resMatrixTable + "<td>" + rTime + "</td>";
		Global.resMatrixTable = Global.resMatrixTable + "<td>" + tStts + "</td></tr>";		
	}
	
	public static void finalizeResMatrix(){
		String dForm = "MMddyyyy";
		String rDate = Global.getStamp(dForm);
		String tForm = "HH_mm_ss_a";
		String rTime = Global.getStamp(tForm);
		String rMtrxPath = Global.resultMatrixPath + Global.resMatrixFile + "_" + rDate + "_" + rTime + ".html";
		System.out.println("Full Result matrix path: \n" + rMtrxPath);
		File matrixFile = new File(rMtrxPath);
		
		String finalMatixHtml;
		finalMatixHtml = Global.resMatrixHead + Global.resMatrixTable;
		finalMatixHtml = finalMatixHtml + Global.htmlTail;
		
		try {
			FileUtils.writeStringToFile(matrixFile, finalMatixHtml);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to create HTML Result file. -CreateResultMatrix.finalizeResMatrix- \n" + e);
		}
	}
	

	public static void buildResulMatrixtPath() throws IOException {
		
		Global.resultMatrixPath = Global.FWRoot + "\\\\" +  Global.projName + "\\\\Results\\\\";
		
		File theDir = new File(Global.resultMatrixPath);

		if (!theDir.exists()) {
		    System.out.println("creating directory: " + Global.resultMatrixPath);

		    try{
		        theDir.mkdirs();
		        Global.status = true;
		    } 
		    catch(SecurityException se){
		        //handle it
		    	Global.status = false;
		    	System.out.println("Failed to create the result folders. Error occured: " + se);
		    }        
		    if(Global.status) {    
		        System.out.println("Result path was created.");  
		    }
		}		
		
		
	}
	
}
