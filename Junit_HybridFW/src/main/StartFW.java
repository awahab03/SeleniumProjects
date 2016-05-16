package main;

import java.io.File;
import java.io.IOException;

import result.CreateResult;
import result.CreateResultMatrix;
import selenium.SelMethods;
import selenium.WaitStatements;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import excel.ReadExcelFile;
import excel.WriteToExcel;
import global.FileChecker;
import global.Global;

public class StartFW {

	//@SuppressWarnings("static-access")
	public void statusCheck() throws IOException, Exception{
		Kill_Process endProc = new Kill_Process();
		endProc.kill("EXCEL.EXE");
		Boolean allFileFound;
		//Checking config file
		if(FileChecker.fileCheck(Global.FWRoot)){
			System.out.println("Framework Root path is Valid.");
			if(FileChecker.fileCheck(Global.localConfigPath)){
				// Initialize all the Global Veriables.
				//System.out.println("Local Config path is valid.");
				Global.FWSetup();
				
			} else {
				Global.status = false;
				System.out.println("Local Config path " + Global.localConfigPath + " is INVALID !");
				// Test run shall stop.
			}
			
		} else{
			Global.status = false;
			System.out.println("Framework Root path is missing or invalid.");
		}
		//checking OR file
		if (Global.status && (FileChecker.fileCheck(Global.ORpath))){
			Global.status = true;
			//System.out.println("Property file found!");
		} else {
			System.out.println("Object Repository file NOT found! Path: " + Global.ORpath);
			Global.status = false;
		} // end if-else;	********** Step 1 ends here.
		//checking global status- from Global.java
		if(Global.status){
			allFileFound = true;
		} else {
			allFileFound = false;
		}
		
		
	}
	//public static void main(String[] args) throws Exception {
		public static void createResults(String scriptName) throws Exception {
			//Result create initialization
			if(Global.createResMatrix){
				CreateResultMatrix.buildResulMatrixtPath();
				Global.resMatrixFile = Global.projName + "_Test_Result_";
				CreateResultMatrix.createResultMatrixHeader();
				String matrixInfo[] = ReadExcelFile.getRelInfoFromScriptList();			
				CreateResultMatrix.createResultMatrixTable(matrixInfo[0], matrixInfo[1]);
			}
			//CreateResult.createResultHeader();
			//creating result page using current test case name
			Global.curTesName = scriptName;
			Global.buildResultPath();				
			if (FileChecker.fileCheck(Global.resultPath)){
				//System.out.println("Result folder exist: " + Global.resultPath);
				Global.curImagePath = Global.resultPath + "Images\\\\";
				Global.curResPath = Global.resultPath + Global.curTesName + ".html";		// add time stamp later. ?????????????????????
			} else {
				System.out.println("Reult folder doesn't exist and failed to create. See erro for more detail.");
				
			}
			CreateResult.resetHtmlContent();
			
		
		}
				
		public void ReadTestScript(File filePath, String scriptPath) throws IOException, InterruptedException{
			ReadExcelFile XL = new ReadExcelFile();
			Workbook wb;
			int totRows = 0;
			int steps = XL.totalSteps(scriptPath);
			try{
			wb = Workbook.getWorkbook(filePath);
			wb.close();
			wb = Workbook.getWorkbook(filePath);
			Sheet sht = wb.getSheet("Aut_Script");
			totRows = sht.getRows();
			
			int blnkRow = (totRows - steps);
			System.out.println("Total Row in excel file: " + totRows);
			System.out.println("Blank rows: " + blnkRow);
			String pPage = null, curPage, El, key, data;
			//String ScArr[] = null;
			String stts = null;
			System.out.println("\nTest Name");
			System.out.println("==============");
			//System.out.println(Global.curTesName + "\tTotal Steps: " + (steps - 3));					
			System.out.println("------------------------------------------\n");
			
			CreateResult.createResultHeader();	// Create HTML Result header part.
			// Get Test Information: 
			String testDescr = sht.getCell(1,1).getContents();
			String testDepnd = sht.getCell(1,2).getContents();
			CreateResult.createResultSummaryTable(testDescr, testDepnd);	//	Create Result Summary part.
//			
			
			for (int j = 4; j <= steps; j ++){
				
				Cell pCell = sht.getCell(0, j);
				Cell eCell = sht.getCell(1, j);
				Cell kCell = sht.getCell(2, j);
				
				Cell dCell = sht.getCell(3, j);
				
				curPage = pCell.getContents().trim();
				//System.out.println("curr page "+curPage);
				El = eCell.getContents().trim();
				key = kCell.getContents().trim();
				key = key.toUpperCase();
				data = dCell.getContents().trim();
				
				if (curPage == "") {
					curPage = pPage;
				} else{
					pPage = curPage;
				}
				
				if (!(key.equals("IW")||key.equals("W")||key.equals("*")) && El == "") {
					System.out.println("\n-- ** -- Element name cannot be blank ! Excel Row: " + j);
					break;
				} else {
					
					//System.out.println("Page: " + curPage);
					//System.out.println("Element: " + El);
					
					//System.out.println("Key: "+ key);
					//System.out.println("Data: " + data);
					
					/// Run each test here . . . .
					Global.resString = "";
					System.out.println("Step " + (j - 3) + ":\t\t\t\t\t\t\t\t\t\tStatus:");
					System.out.println("-------\t\t\t\t\t\t\t\t\t\t--------");
					Global.curStep = j - 3;
					ExceuteScript.Run(curPage, El, key, data);
					
					stts = Global.getSttsAbbr();
					CreateResult.createDetailResultTable(stts);		// log each step to the html result log.
					//System.out.println("Step Status: " + Global.stepStts);				
			  		System.out.println(Global.resString + "\t\t" + stts);
					System.out.println("\n");
												
					if (!Global.status && Global.stepStts.equals("F")){
						wb.close();
						j = totRows + 1;
						System.out.println("\n** Current Test - " + Global.curTesName + " - Execution Stopped ! **\n");
					}
					
				}
			}	// j for loop -- Steps loop.
			
			//CreateResult.buildResult();			// Once test run complete, weather pass or fail, it will create the html result.
			if(Global.createResMatrix){
				CreateResultMatrix.addEndPartToMatrixTable();
			}
			
		} catch (BiffException e){
			System.out.println("Error form reading Scipt(Excel) file: " + e.fillInStackTrace());
		}
		// add test status to console
		if (!Global.status){
			System.out.println("\n** Test Run Failed ! **\n");
		}else{
			System.out.println("Test - " + Global.curTesName + " - completed successfyll!");
		}
		//	
		
		if(Global.status){
			Global.drvr.quit();	//	 ******** Close Browser after each test run.
		}else{
			if(!Global.continueOnTestFail){
				System.out.println("\n** Test Set Stopped due to your \"continue_on_test_fail=false\" **\n");
				//k = Global.pathDict.size() + 1;
			}else{
				System.out.println("\n** Test Set continues due to your \"continue_on_test_fail=true\" **\n");
			}
		}

	}
	public void buildResult(){
		CreateResult.buildResult();			// Once test run complete, weather pass or fail, it will create the html result.
		if(Global.createResMatrix){
			CreateResultMatrix.addEndPartToMatrixTable();
		}
		CreateResultMatrix.finalizeResMatrix();
	}

}// End Class
