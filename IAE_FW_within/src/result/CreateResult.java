package result;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;






import org.apache.commons.io.FileUtils;

import global.FileChecker;
import global.Global;
import global.ImageCapture;

public class CreateResult {
	
	
	public static void createResultHeader(){
		
		Global.htmlHead = "";	// Reset the header first.
		Global.htmlHead = "<html><head>";
		Global.htmlHead = Global.htmlHead + "<script src=\"Z:\\FQT\\AutFramework\\Config\\all_javaScripts.js\" language=\"JavaScript\" type=\"text/javascript\">";
		//Global.htmlHead = "<script type=\"text/javascript\">";
		//Global.htmlHead = Global.htmlHead + " function openWin(img){\n";
		//Global.htmlHead = Global.htmlHead + " window.open(img,\"mywin\",\"menubar=0,resizable=1,width=200,height=200\")}";
		Global.htmlHead = Global.htmlHead + "</script><title>" + Global.curTesName + "</title>";
		Global.htmlHead = Global.htmlHead +  "<link rel=\"stylesheet\" type=\"text/css\" href=" + Global.cssPath + ">";
		Global.htmlHead = Global.htmlHead + "</head><body><br><hr>";
	}
	
	
	public static void createResultSummaryTable (String Descr, String Depnd){

		String dForm = "MM/dd/yyyy";
		String tForm = "HH:mm:ss:a";
		String rDate = Global.getStamp(dForm);
		String rTime = Global.getStamp(tForm);
				
		Global.resSummaryHtmlTable = "";
		Global.resSummaryHtmlTable = Global.resSummaryHtmlTable + "<table class=logoTbl><tr><td><img src=" + 
				Global.usptoLogo + " height=140 width=140></td>";
		Global.resSummaryHtmlTable = Global.resSummaryHtmlTable + "<td><table class=topTbl><tr><th colspan=100%><h3>Test Summary</h3></th></tr>";
		Global.resSummaryHtmlTable = Global.resSummaryHtmlTable + "<tr><td><table class = infoTable>";
		
		Global.resSummaryHtmlTable = Global.resSummaryHtmlTable + "<tr><th colspan=100%>Test Information:</td></tr>";
		Global.resSummaryHtmlTable = Global.resSummaryHtmlTable + "<tr><td>Test Name:</td><td>" + Global.curTesName + "</td></tr>";
		Global.resSummaryHtmlTable = Global.resSummaryHtmlTable + "<tr><td>Description:</td><td>" + Descr + "</td></tr>";
		//Global.resSummaryHtmlTable = Global.resSummaryHtmlTable + "<tr><td>Dependency:</td><td>" + Depnd + "</td></tr></table></td>";
		Global.resSummaryHtmlTable = Global.resSummaryHtmlTable + "<tr><td>Browser Type:</td><td>" + Global.brType + "</td></tr></table></td>";
		Global.resSummaryHtmlTable = Global.resSummaryHtmlTable + "<td><div class=d1><center><h4>Over-all Status: <b class=$stts$>$stts$</b></h4><center></div></td>";
		Global.resSummaryHtmlTable = Global.resSummaryHtmlTable + "<td><table class = infoTable><tr><th colspan=100%>Run Information</td></tr><tr>";
		Global.resSummaryHtmlTable = Global.resSummaryHtmlTable + "<td>RunDate:</td><td>" + rDate + "</td></tr><tr><td>Start Time:</td><td>" + rTime + "</td></tr><tr>";
		Global.resSummaryHtmlTable = Global.resSummaryHtmlTable + "<td>End Time:</td><td>" + "$endTime$" + "</td></tr></table></td></tr></table></td></tr></table>";
		Global.resSummaryHtmlTable = Global.resSummaryHtmlTable + "<hr><center><table class = usTable><col width =3%><col width=10%>";
		Global.resSummaryHtmlTable = Global.resSummaryHtmlTable + "<th colspan=100%><b>User Story Information:</b></td></tr>";
		Global.resSummaryHtmlTable = Global.resSummaryHtmlTable + "<tr><td><b>US ID:</b></td><td>$usid$</td></tr>";
		Global.resSummaryHtmlTable = Global.resSummaryHtmlTable + "<tr><td><b>Name:</b></td><td>$usname$</td></tr>";
		Global.resSummaryHtmlTable = Global.resSummaryHtmlTable + "<tr><td><b>Description:</b></td><td>$usdsc$</td></tr></table></center><hr>";
		//Global.resSummaryHtmlTable = Global.resSummaryHtmlTable + "";
		Global.resSummaryHtmlTable = Global.resSummaryHtmlTable + "<br><div class=d2><center><h3>Results in Detail:</h3></center></div>";
		Global.resSummaryHtmlTable = Global.resSummaryHtmlTable + "<Table class=mainTable><tr><th>Step</th><th>Step Description</th><th>Actual Result</th><th>Status</th></tr>";
	}
	
	public static void createDetailResultTable(String stts){
		
		Global.resDetailHtmlTable = Global.resDetailHtmlTable + "<tr class=" + Global.stepStts + "><td class=stp>" + Global.curStep + "</td><td>";
		Global.resDetailHtmlTable = Global.resDetailHtmlTable + Global.resString + "</td>";
		Global.resDetailHtmlTable = Global.resDetailHtmlTable + "<td>" + Global.actualRes+ "</td><td class=res>" + stts + "</td></tr>";
		
	}
	
	public static void buildResult(){		
		// update Over All status part <b class=@@>$stts$</b>
		// Add the end time to the result by replacing $endTime$ with the time stamp.
		String oaStatus;
		File resultFile = new File(Global.curResPath);
		
		Date tDate = new Date();
		SimpleDateFormat dFormat = new SimpleDateFormat("HH:mm:ss");
		String endTime = dFormat.format(tDate);
		if (Global.status){
			oaStatus = "Passed";
		} else {
			oaStatus = "Failed";
		}
		String finalHtml;
		finalHtml = Global.htmlHead + Global.resSummaryHtmlTable;
		finalHtml = finalHtml + Global.resDetailHtmlTable;
		finalHtml = finalHtml.replace("$stts$", oaStatus);		// Adds Over All status = Passed or Failed
		finalHtml = finalHtml.replace("$endTime$", endTime);
		finalHtml = finalHtml + Global.htmlTail;
		
		// finally dump the result file to html.
		try {
			FileUtils.writeStringToFile(resultFile, finalHtml);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to create HTML Result file. -CreateResult.buildResult- \n" + e);
		}
			
		
		
	}
	
	public static void resetHtmlContent(){
		Global.htmlHead = "";
		Global.resSummaryHtmlTable = "";
		Global.resDetailHtmlTable = "";
	}

}
