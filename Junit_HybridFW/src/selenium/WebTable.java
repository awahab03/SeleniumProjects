package selenium;

import global.Global;
import global.ImageCapture;
import global.ObjectRepsitory;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class WebTable {
	
	
	public static int rowCount(WebElement tbl){
		
		List<WebElement> rows = tbl.findElements(By.tagName("tr"));
		int totRows = rows.size();
		return totRows;
	}
	
	public static int columnCount(WebElement tbl, int row){
		List<WebElement> rows = tbl.findElements(By.tagName("tr"));
		int totCol = 0;
		int totRows = rows.size();
		
		if(row > totRows){
			System.out.println("The WebTable has only " + totRows + " rows. Row number you enter '" + row + "' is invalid.");
		} else {
			List<WebElement> columns=rows.get(row).findElements(By.tagName("th"));
			totCol = columns.size();
			if(totCol<1){
				columns=rows.get(row).findElements(By.tagName("th"));
				totCol = columns.size();
			}
		}
		return totCol;
	}
	
	public static int getRowByCellText(String txt){
		int row = 0, totRow = 0;
		String curCellText;
		txt = txt.trim();
		List<WebElement> rows = null;
		List<WebElement> columns = null;
		
		if(Global.curElement.isDisplayed()){
			rows =Global.curElement.findElements(By.tagName("tr"));
			totRow = rows.size();
			System.out.println("Total number of rows: " +  totRow);
			if(totRow > 1){
				for(int rnum=1;rnum<totRow;rnum++){
					columns=rows.get(rnum).findElements(By.tagName("td"));
					System.out.println("At row - " + rnum + ", number of columns are:" + columns.size());
					
					for(int cNum=0;cNum<columns.size();cNum++){
						System.out.println("Cell: (" + rnum + "," + cNum + ") text = " + columns.get(cNum).getText());
						curCellText = columns.get(cNum).getText().trim();
						if(curCellText.equals(txt)){
							System.out.println("Our row: " + rnum);
							row = rnum;
							break;
						}
					}
					if(row > 0){
						break;		// break out of the main for loop.
					}
				}
			}else{
				System.out.println("WebTable has no record.");
			}
			
		} else{
			System.out.println("WebTable is NOT available in the application.");
		}
		
		if(row <1){
			System.out.println("Could'nt find the text '" + txt + "' in the webtable.");
			Global.status = false;
			Global.stepStts = "F";
			return row;
		}else{
			return row;
		}

	}

	public static int recordCount(WebElement tbl){
		int totRecord = 0, tempRow;
		WebElement lnkNext = Global.drvr.findElement(By.partialLinkText("Next"));
		
		List<WebElement> rows = tbl.findElements(By.tagName("tr"));
		tempRow = rows.size();
		totRecord = totRecord + tempRow;
		
		while(lnkNext.isDisplayed() && lnkNext.isEnabled()){
			lnkNext.click();
			rows = tbl.findElements(By.tagName("tr"));
			tempRow = rows.size();
			totRecord = totRecord + tempRow;
		}
		
		return totRecord;
	}

	public static WebElement tableChildObject(int row, int col, String objType, int indx){
		WebElement childObj = null;
		WebElement ourRow, ourCell;
		List<WebElement> rows, cols, elCol;
		int totRow, totCol;
		
		try{
			rows=Global.curElement.findElements(By.tagName("tr"));
			totRow = rows.size();
			if(totRow<row){
				Global.resString = Global.resString + "\nTable row " + row + " does NOT exist! Total rows : " + totRow;
				Global.resString = Global.resString + "\nGiven row exceeds total number of row!";
				Global.stepStts = "F";
				Global.status = false;
				Global.actualRes = "Table Childobject Failed!";
			}else{
				ourRow = rows.get(row);
				cols = ourRow.findElements(By.tagName("td"));
				ourCell = cols.get(col-1);
				
				if(objType.equals("chk")){
					elCol = ourCell.findElements(By.tagName("input"));
					if (elCol.get(indx).getAttribute("type").equals("checkbox")){
						childObj = elCol.get(indx);
					} else {
						childObj = null;
						System.out.println("No Checkbox found in the table cell(" + row + ", " + col + ")");
					}
				} else if(objType.equals("btn")){
					elCol = ourCell.findElements(By.tagName("button"));
					System.out.println("Total button found = " + elCol.size());
					childObj = elCol.get(indx);
					System.out.println("Button Found! Title = " + childObj.getAttribute("title"));
				} else if(objType.equals("edt")){
					elCol = ourCell.findElements(By.tagName("input"));
					childObj = elCol.get(indx);
				} else if(objType.equals("rad")){
					elCol = ourCell.findElements(By.tagName("input"));
					childObj = elCol.get(indx);
					//
				} else if(objType.equals("img")){
					//
				} else if(objType.equals("sel")){
					//
				} else if(objType.equals("")){
					//				
				}
			}
		}catch(Exception e){
			System.out.println("Error occured while getting table row." + e);			
		}				
		return childObj;
	}
	
	public static void saveTableRowToVariable(String RowText, String varName){
		
		int row = getRowByCellText(RowText);
		if (row>1){
			String strRow = Integer.toString(row);
			Global.addToDictionary(varName, strRow, Global.runTimeDataRepository);
			if (Global.readDictionary(varName, Global.runTimeDataRepository) != null){
				Global.stepStts = "D";
				Global.resString = Global.resString + strRow;
				Global.actualRes = "Saving Table row successfull.";
			}else{
				Global.stepStts = "F";
				Global.actualRes = "Saving Table row Failed!";
				Global.status = false;
			}
		} else{
			Global.stepStts = "F";
			Global.actualRes = "Saving Table row Failed!";
			Global.status = false;
		}
		
		
	}
	
	public static void webTableInput(String info, String tblName){
		String infoArr [];
		infoArr = info.split(";");
		String rowInfo = "", objClass, objName, data = "";
		int indx = 0, row, col, arrSize = infoArr.length;
		WebElement childObj;
		
		if (arrSize < 3){
			System.out.println("Wrong data string provided for a table input in excel row: " + (Global.curStep + 4));
			Global.status = false;
			return;
		}else {
			rowInfo = infoArr[0];
			col = Integer.parseInt(infoArr[1]);
			objClass = infoArr[2];
			if (arrSize == 3){
				data = "";
			} else if(arrSize == 4){
				if(objClass.equals("edt") || objClass.equals("sel")){
					data = infoArr[3];
				} else{
					data = "";
					indx = Integer.parseInt(infoArr[3]);
				}
			} else if(arrSize == 5){
				if(objClass.equals("edt") || objClass.equals("sel")){
					data = infoArr[3];
					indx = Integer.parseInt(infoArr[4]);
				}else{
					indx = Integer.parseInt(infoArr[3]);
				}
			}
		}
		
		if (Global.isNumeric(rowInfo)){
			row = Integer.parseInt(rowInfo);
		} else{
			if(rowInfo.contains("_")){
				rowInfo = rowInfo.substring(1);
				System.out.println("Getting row number form dict with key = " + rowInfo);
			}
			row = Integer.parseInt(Global.readDictionary(rowInfo, Global.runTimeDataRepository));
		}
		
		Global.resString = Global.resString + " from " + tblName + " WebTable cell (" + row + ", " + col + ") ";
		
		childObj = tableChildObject(row, col, objClass, indx);
		if(childObj != null){
			System.out.println("ChildObject is NOT NULL!");
			objName = childObj.getAttribute("title");
			String ObjClass;
			String imgName = ImageCapture.getImageName();
			switch(objClass){
				case "edt" :					
					ObjClass = "Edit";
					SelMethods.enterDataIntoEditField(childObj, data, imgName);
					break;
				case "img" :
					ObjClass = "Image";
					SelMethods.objClick(childObj, ObjClass, objName, imgName);
					break;
				case "btn" :
					//Global.resString = Global.resString + " click button in the cell ("+ row + ", " + col + ")";
					ObjClass = "Button";
					if(childObj.isDisplayed()){
						SelMethods.objClick(childObj, ObjClass, objName, imgName);
					}else{
						Global.resString = Global.resString + " click on " + ObjClass;
						Global.status = false;
						System.out.println(objClass + " is NOT visible!");
						Global.stepStts = "F";
						Global.actualRes = ObjClass + " click failed !\nObject is NOT visible!";
					}
					
					break;
				case "txt" :
					ObjClass = "Text Field";
					break;
				case "rad" :
					ObjClass = "Radio button";
					break;
				case "sel" :
					ObjClass = "Drop-down list";
					break;
				case "lnk" :
					ObjClass = "Link";
					SelMethods.objClick(childObj, ObjClass, objName, imgName);
					break;
				case "tbl" :
					ObjClass = "Table";
					break;
				case "chk" :
					ObjClass = "Checkbox";
					SelMethods.checkUncheckChkBox(childObj, objName);
					break;
				case "win" :
					ObjClass = "Window";
					break;
				default:
					ObjClass = "Unknow Object Type";
			}
			
		}else{
			Global.resString = Global.resString + "WebTable Failed! Couldn't retrive an object of type " + ObjectRepsitory.getObjectClass(objClass) + " from cell.";
			Global.resString = Global.resString + "Please make sure you have entered correct row and column number.";
			System.out.println("WebTable Failed! ChildObject is NULL!");
			Global.stepStts = "F";
			Global.status = false;
			Global.actualRes = "Table Childobject Failed!";
		}		
	}
	
	public static boolean tableDataCheck(WebElement tbl, String prpName, String prpVal, String imgName){
		boolean success = false;
		int tblRow, recRow, recCol;
		String imgLink;
		tblRow = WebTable.rowCount(tbl);
		switch(prpName.toUpperCase()){
			case "RECORDFOUND":
				Global.resString = Global.resString + " Check if " + prpVal + " exists.";
				if(tblRow<2){
					Global.resString = Global.resString + "\nTable has no record!";
					success = false;
					imgLink = ImageCapture.takeElementImage(imgName);
					Global.stepStts = "F";
					Global.status = false;
					Global.actualRes = "WebTable CheckPoint Failed!";
					Global.actualRes = Global.actualRes + imgLink;
				}else{
					recRow = WebTable.getRowByCellText(prpVal);
					if(recRow > 0){
						Global.resString = Global.resString + "\nRecord found at row: " + recRow;
						success = true;
						imgLink = ImageCapture.takeElementImage(imgName);
						Global.status = true;
						Global.stepStts = "P";
						Global.actualRes = "WebTable CheckPoint Passed!";
						Global.actualRes = Global.actualRes + imgLink;
					}else{
						Global.resString = Global.resString + "\nNo such record found in the WebTable!";
						success = false;
						imgLink = ImageCapture.takeFullScreenshot(imgName);
						Global.stepStts = "F";
						Global.status = false;
						Global.actualRes = "WebTable CheckPoint Failed!";
						Global.actualRes = Global.actualRes + imgLink;
					}
				}
		}		
		return success;
	}
}
