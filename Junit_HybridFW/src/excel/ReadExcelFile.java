package excel;

import java.io.*;

import global.FileChecker;
import global.Global;
import jxl.*;
import jxl.read.biff.BiffException;


public class ReadExcelFile {
	
	//public Global G = new Global();
	
	private static final Object[] String = null;

	public int populateScriptList() throws IOException{
		int totScripts = 0;
		File fileToRead = new File(Global.scriptListPath);
		Workbook wb;
		try{
			wb = Workbook.getWorkbook(fileToRead);
			
			Sheet sht = wb.getSheet("List");
			totScripts = sht.getRows();
			//System.out.println("Total excel rows in the List file: " + totScripts);
			
			for(int r = 4; r < sht.getRows(); r++){ // Skipping Row( 0 to 5) as the Header and other info.
				Cell curCell = sht.getCell(0, r);
				String ord = curCell.getContents();
				
				if (ord != ""){
					//System.out.println("Item " + ord + " added to Dict.");
					curCell = sht.getCell(1, r);
					String scriptName = curCell.getContents(); 
					Global.addToDictionary(ord, scriptName, Global.scriptDict);
					
					curCell = sht.getCell(2, r);
					String sPath = curCell.getContents();
					//sPath = sPath + scriptName + ".xls";
					Global.addToDictionary(ord, sPath, Global.pathDict);
				}
							
			}	// 1st for loop end
					
		} catch(BiffException e){
			System.out.println(e.fillInStackTrace());
		}
		
		for (int key = 1; key<= Global.pathDict.size(); key++){		// Make sure the scripts numbers are in a complete sequence.
			
			if (Global.pathDict.containsKey(Integer.toString(key))){
				Global.status = true;						
			} else{
				System.out.println("Script Order Sequence is INCORRECT ! Please correct the Script Order in Script_List.xls file.");
				Global.status = false;
			}
		}
		
		return totScripts;
		
	}
	
	public static String[] getRelInfoFromScriptList() throws IOException{
		
		File fileToRead = new File(Global.scriptListPath);
		Workbook wb;
		String[] relInfo = new String[2];
		
		try{
			wb = Workbook.getWorkbook(fileToRead);
			
			Sheet sht = wb.getSheet("List");
			
			//System.out.println("Total excel rows in the List file: " + totScripts);
			
			Cell relCell = sht.getCell(1, 1);
			Cell sprntCell = sht.getCell(1, 2);
			//relInfo[0] = projCell.getContents();
			relInfo[0] = relCell.getContents().toString();
			relInfo[1] = sprntCell.getContents().toString();
		} catch(BiffException e){
			System.out.println(e.fillInStackTrace());
		}
		return relInfo;
	}
	

	@SuppressWarnings("null")
	public String[] readScriptData(Workbook wb, int rw) throws IOException{  // code change required
			String scArr[] = null;
		try{
			
			Sheet sht = wb.getSheet("Aut_Script");
			//System.out.println("Total excel rows in the List file: " + totScripts);
			
			Cell pCell = sht.getCell(0, rw);
			Cell eCell = sht.getCell(1, rw);
			Cell kCell = sht.getCell(2, rw);
			Cell dCell = sht.getCell(3, rw);
			
			scArr[0] = pCell.getContents();
			scArr[1] = eCell.getContents();
			scArr[2] = kCell.getContents();
			scArr[3] = dCell.getContents();
			
		} finally{
		wb.close();
		}
		return null;
	}
	
	public int totalSteps(String fPath) throws IOException{
		//System.out.println("-- From totalSteps func - " + fPath);
		int totSteps = 0, totRow;
		File xlFile = new File(fPath);
		Workbook wb = null;
		String pg = null, el, ky, dt;
		try{
			
			wb = Workbook.getWorkbook(xlFile);
			Sheet st = wb.getSheet("Aut_Script");			
			totRow = st.getRows();
			for (int r = 4; r < totRow; r++){
				Cell pCell = st.getCell(0, r);
				Cell eCell = st.getCell(1, r);
				Cell kCell = st.getCell(2, r);
				Cell dCell = st.getCell(3, r);
				//System.out.println("** -" + r + "- **");
				pg = pCell.getContents();
				el = eCell.getContents();
				ky = kCell.getContents();
				dt = dCell.getContents();
				
				if (pg == "" && el == "" && ky == "" && dt == ""){
					totSteps = r - 1;
					break;
				} else {
					totSteps = r;
				}
			}
		} catch(BiffException e){
			System.out.println("Exception occured: " + e.fillInStackTrace());
		}finally{
			wb.close();
		}
		
		return totSteps;

	}
	
	public boolean readAndPouplateUSInfo(Workbook wb){
		String shtName = "User_Story";		
		String usID, usName, usDsc;
		
		try{
			Sheet sht = wb.getSheet(shtName);
			
			Cell idCell = sht.getCell(1, 0);
			Cell nameCell = sht.getCell(1, 1);
			Cell dscCell = sht.getCell(1, 5);
			
			usID = idCell.getContents();
			usName = nameCell.getContents();
			usDsc = dscCell.getContents();
			System.out.println("User Story ID: " + usID);
			System.out.println("User Story Name: " + usName);
			System.out.println("User Story Desc: " + usDsc);
			
			if(usID != "" && usName != "" && usDsc != ""){
				Global.resSummaryHtmlTable = Global.resSummaryHtmlTable.replace("$usid$", usID);
				Global.resSummaryHtmlTable = Global.resSummaryHtmlTable.replace("$usname$", usName);
				Global.resSummaryHtmlTable = Global.resSummaryHtmlTable.replace("$usdsc$", usDsc);
				return true;
			}else{
				System.out.println("User Sotry Name or ID or Descripttion is blank! Please enter valid information in the 'User_Story' sheet of the script.");
				return false;
			}
			
		}catch (Exception e){
			System.out.println("Error Occured reading User Story Info: " + e);
			return false;
		}
		
	}

}
