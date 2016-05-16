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

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		
		Boolean allFileFound;
		Kill_Process endProc = new Kill_Process();
		
		endProc.kill("EXCEL.EXE");
		
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
			
		// **** Step 1: Check if OR file exists.
		if (Global.status && (FileChecker.fileCheck(Global.ORpath))){
			Global.status = true;
			//System.out.println("Property file found!");
		} else {
			System.out.println("Object Repository file NOT found! Path: " + Global.ORpath);
			Global.status = false;
		} // end if-else;	********** Step 1 ends here.
		
		// ***** Step 2: Check if Script_List file exists.
		if( Global.status && FileChecker.fileCheck(Global.scriptListPath) && Global.status){
			//System.out.println("Script_List file Found!");
			Global.status = true;
			int totalScripts = 0;
			ReadExcelFile XL = new ReadExcelFile();
			try{
				totalScripts = XL.populateScriptList() - 1;
			} catch(Exception e){
				System.out.println(e.fillInStackTrace());
			} //End Try-catch
		} else {
			System.out.println("Script_List file NOT found !!");
			Global.status = false;			
		} // ****** Step 2 ends here.
		
		//***** Step 3: Check if the profile file exist. Retrieve the url if the file exist. Change the global status variable to false otherwise.
		if (Global.status && FileChecker.fileCheck(Global.profileFilePath)){
			//FileChecker.readProfilefile()		**************************** add code here to read profile info.
			//System.out.println("Profile file found !");
			Global.status = true;
		} else {
			System.out.println("Profile file NOT found !!");
			Global.status = false;
		}// end of if-else  ****** Step 3 ends here.
		// Need to add file check code for USPTO log, Java script file and css file. *******************
		//System.out.println("\n");
		if(Global.status){
			allFileFound = true;
		} else {
			allFileFound = false;
		}
		//Boolean allFileFound = true;
		
		//******* Step 4:  make sure all the preconditions are met then begin running the scripts
		if (Global.status){
			for (int m = 1; m <= Global.pathDict.size(); m++){
				String tempFilePath = Global.readDictionary(Integer.toString(m), Global.pathDict) + 
						Global.readDictionary(Integer.toString(m), Global.scriptDict) + ".xls";
				if (FileChecker.fileCheck(tempFilePath)){
					allFileFound = true;
					//System.out.println("Script file at run order (" + m + ") Path: \"" + tempFilePath + "\" found !!");
				} else{
					allFileFound = false;
					System.out.println("Script file at run order (" + m + ") Path: \"" + tempFilePath + "\" couldn't be found !!");
					
				}							
			}// end of "m" for loop
			if(Global.createResMatrix){
				CreateResultMatrix.buildResulMatrixtPath();
				Global.resMatrixFile = Global.projName + "_Test_Result_";
				CreateResultMatrix.createResultMatrixHeader();
				String matrixInfo[] = ReadExcelFile.getRelInfoFromScriptList();			
				CreateResultMatrix.createResultMatrixTable(matrixInfo[0], matrixInfo[1]);
			}
						
			
		}//	******** End of Step 4
			
		if(allFileFound){		// FW will ONLY begin to run scripts if all the scripts file are found.
			//System.out.println("\n");
			
			for (int k = 1; k<= Global.pathDict.size(); k++){ 
				//This is main loop that will cause the the FW to run for all selected scripts in the list.
				Global.fullScriptPath = Global.readDictionary(Integer.toString(k), Global.pathDict) + 
							Global.readDictionary(Integer.toString(k), Global.scriptDict) + ".xls";	
				System.out.println("Current Test path(" + k + ")-> " + Global.fullScriptPath);
				
				//System.out.println("\n");
				Global.curTesName = Global.readDictionary(Integer.toString(k), Global.scriptDict);
				System.out.println("Cur test name:"+Global.curTesName);
				Global.buildResultPath();				
				if (FileChecker.fileCheck(Global.resultPath)){
					//System.out.println("Result folder exist: " + Global.resultPath);
					Global.curImagePath = Global.resultPath + "Images\\\\";
					Global.curResPath = Global.resultPath + Global.curTesName + ".html";		// add time stamp later. ?????????????????????
				} else {
					System.out.println("Reult folder doesn't exist and failed to create. See erro for more detail.");
					break;
				}
				if(Global.createResMatrix){
					CreateResultMatrix.addInitialPartToMatrixTable(k);
				}
				
				CreateResult.resetHtmlContent();
				int totRows = 0;
				ReadExcelFile XL = new ReadExcelFile();
				Workbook wb;
				File scriptFile = new File(Global.fullScriptPath);
				System.out.println("Script Path: " + Global.fullScriptPath);				
				int steps = XL.totalSteps(Global.fullScriptPath);		/////////////////				
				//System.out.println("Total Steps: " + steps);
				//SelMethods.browserClose();
				//WaitStatements.Wait(5);
//				endProc.kill("chromedriver.exe");
//				endProc.kill("chrome.exe");
				
				SelMethods.setBrowser();
				
				try{
					wb = Workbook.getWorkbook(scriptFile);
					wb.close();
					wb = Workbook.getWorkbook(scriptFile);
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
					System.out.println(Global.curTesName + "\tTotal Steps: " + (steps - 3));					
					System.out.println("------------------------------------------\n");
					
					CreateResult.createResultHeader();	// Create HTML Result header part.
					// Get Test Information: 
					String testDescr = sht.getCell(1,1).getContents();
					String testDepnd = sht.getCell(1,2).getContents();
					CreateResult.createResultSummaryTable(testDescr, testDepnd);	//	Create Result Summary part.
					if(!XL.readAndPouplateUSInfo(wb)){						
						System.out.println("Failed to pupulate User Story info for the script " + Global.curTesName);
						System.out.println("Tes Exceution STOPPED!");
						k = Global.pathDict.size() + 1;
						break;
					}
					
					for (int j = 4; j <= steps; j ++){
						
						Cell pCell = sht.getCell(0, j);
						Cell eCell = sht.getCell(1, j);
						Cell kCell = sht.getCell(2, j);
						
						Cell dCell = sht.getCell(3, j);
						
						curPage = pCell.getContents().trim();
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
					
					CreateResult.buildResult();			// Once test run complete, weather pass or fail, it will create the html result.
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
						k = Global.pathDict.size() + 1;
					}else{
						System.out.println("\n** Test Set continues due to your \"continue_on_test_fail=true\" **\n");
					}
				}
		
			}// End of "k" for loop -- Script List loop
			CreateResultMatrix.finalizeResMatrix();
		} else {
			System.out.println("FW didn't run any test because one or more script files are missing.");
			//k = Global.pathDict.size() + 1;	// this will cause the main for loop to exit.
		}		

	}// End main()

}// End Class
