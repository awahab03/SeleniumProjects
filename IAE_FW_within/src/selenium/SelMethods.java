package selenium;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;

import com.google.common.collect.Lists;

import global.Global;
import global.ImageCapture;
import global.ObjectRepsitory;

public class SelMethods {
		
	public static void action(String am, String ot, String oName, String dt) throws InterruptedException{
		
		oName = oName.substring(3);
		String objCls, imgName;
		imgName = ImageCapture.getImageName();
		IWait(3);
		objCls = ObjectRepsitory.getObjectClass(ot);
		try{
			String tValArr[];
			switch(ot){
			case "edt" :
				//objCls = ObjectRepsitory.getObjectClass(ot);
				if (am.equals("IN")){
					Global.resString = Global.resString + " enter '" + dt + "' into " + oName + " " + objCls + " field.";
					enterDataIntoEditField(Global.curElement, dt, imgName);
				} else if(am.equals("E")){
					Global.resString = Global.resString + " verif if " + oName + " " + objCls + " field exists.";
					objectExist(imgName);
				} else if(am.equals("CP")){			// incomplete for now, need to write more code...
					String tVal, pNam;
					if(dt.contains("=")){
						tValArr = dt.split("=");
						pNam = tValArr[0];
						tVal = tValArr[1];
					} else{
						tVal = dt;
						pNam = "value";
					}
					if(tVal == ""){
						Global.resString = Global.resString + " validate if " + pNam + " value of " + objCls + " field '" + oName + "' is Null";
					}else{
						Global.resString = Global.resString + " validate if " + pNam + " value of " + objCls + " field '" + oName + "' is '" + tVal + "'";
					}
					
					checkObjectProperty(pNam, tVal, imgName);
					//Global resString is created in the function above.
				}
				break;
			case "img":
				//objCls = ObjectRepsitory.getObjectClass(ot);
				if (am.equals("IN")){
					objClick(Global.curElement, objCls, oName, imgName);
				} else if(am.equals("E")){
					Global.resString = Global.resString + " verify if " + oName + " " + objCls + " exists.";
					objectExist(imgName);
				} else if(am.equals("CP")){			// incomplete for now, need to write more code...
					String tVal, pNam;
					if(dt.contains("=")){
						tValArr = dt.split("=");
						pNam = tValArr[0];
						tVal = tValArr[1];						
					} else{
						tVal = dt;
						pNam = "Text";
					}
					Global.resString = Global.resString + " validate if " + pNam + " of the " + objCls + " '" + oName + "' is '" + tVal + "'";
					checkObjectProperty(pNam, tVal, imgName);
				}				
				break;
			case "lnk" :
				//objCls = "Link";
				if (am.equals("IN")){
					objClick(Global.curElement, objCls, oName, imgName);
				} else if(am.equals("E")){
					Global.resString = Global.resString + " verify if " + oName + " " + objCls + " exists.";
					objectExist(imgName);
				} else if(am.equals("CP")){			// incomplete for now, need to write more code...
					String tVal, pNam;
					if(dt.contains("=")){
						tValArr = dt.split("=");
						pNam = tValArr[0];
						tVal = tValArr[1];						
					} else{
						tVal = dt;
						pNam = "Text";
					}
					Global.resString = Global.resString + " validate if " + pNam + " of the " + objCls + " '" + oName + "' is '" + tVal + "'";
					checkObjectProperty(pNam, tVal, imgName);
				}
				break;
			case "btn":
				//objCls = "Button";
				if (am.equals("IN")){
					objClick(Global.curElement, objCls, oName, imgName);				
				} else if(am.equals("E")){
					Global.resString = Global.resString + " verify if " + oName + " " + objCls + " exists.";
					objectExist(imgName);
				} else if(am.equals("CP")){
					String tVal, pNam;
					if(dt.contains("=")){
						tValArr = dt.split("=");
						pNam = tValArr[0];
						tVal = tValArr[1];
					} else{
						tVal = dt;
						pNam = "Text";
					}
					Global.resString = Global.resString + " validate if " + pNam + " of the " + objCls + " '" + oName + "' is '" + tVal + "'";
					checkObjectProperty(pNam, tVal, imgName);
				}		
				break;
			case "sel":
				//objCls = "Drop-Down List";
				if (am.equals("IN")){
					//System.out.println("Select from Drop-down list.");
					SelMethods.selectDropdownList(Global.curElement, dt, oName);									
				} else if(am.equals("E")){
					Global.resString = Global.resString + " verify if " + oName + " " + objCls + " exists.";
					objectExist(imgName);
				} else if(am.equals("CP")){			// incomplete for now, need to write more code...
					String tVal, pNam;
					boolean CPSuccess = false;
					if(dt.contains("=")){						
						tValArr = dt.split("=");
						pNam = tValArr[0];
						tVal = tValArr[1];
						CPSuccess = checkDropDownListProperty(Global.curElement, pNam, tVal, oName, imgName);
					} else if(dt.contains(";")){
						tValArr = dt.split(";");
						pNam = tValArr[0];
						tVal = tValArr[1];	
						CPSuccess = checkDropDownListProperty(Global.curElement, pNam, tVal, oName, imgName);
					} else{
						Global.resString = Global.resString + " Drop-down list checkpoint -- invalid data in the data column.";
						tVal = dt;
						pNam = "Text";
					}
					
					if(CPSuccess){
						Global.status = true;
						Global.stepStts = "P";
						Global.actualRes = objCls + " CheckPoint successful.";
					}else{
						Global.status = false;
						Global.stepStts = "F";
						Global.actualRes = objCls + " CheckPoint failed !";
					}
				}else if(am.equals("SP")){
					String prpName, varName, arr[];					
					if(dt.contains(";")){
						arr = dt.split(";");
						prpName = arr[0].toString();
						varName = arr[1].toString().trim();
					}else{
						varName = dt.trim();
						prpName = "selecteditem";
					}
					Global.resString = Global.resString + " Save runtime value of " + prpName + " form " + oName + " drop-down to runtime variable.";
					saveDropDowListProperty(Global.curElement, prpName, varName);				
					
				}
				break;
			case "chk":
				Global.curElement.click();
				break;
			case "rad":
				if(am.equals("IN")){
					selectRadioButton(dt, oName);
				}else if(am.equals("E")){
					//
				}
				break;
			case "tbl":
				Global.resString = Global.resString + " in the WebTable " + oName;
				if(am.equals("IN")){
					WebTable.webTableInput(dt, oName);
				} else if(am.equals("CP")){
					String tVal, pNam;
					boolean CPSuccess = false;
					if(dt.contains("=")){						
						tValArr = dt.split("=");
						pNam = tValArr[0];
						tVal = tValArr[1];
						CPSuccess = WebTable.tableDataCheck(Global.curElement, pNam, tVal, imgName);
					} else if(dt.contains(";")){
						tValArr = dt.split("=");
						pNam = tValArr[0];
						tVal = tValArr[1];	
						CPSuccess = WebTable.tableDataCheck(Global.curElement, pNam, tVal, oName);
					} else{
						Global.resString = Global.resString + " Drop-down list checkpoint -- invalid data in the data column.";
						tVal = dt;
						pNam = "Text";
					}
					
					if(CPSuccess){
						Global.status = true;
						Global.stepStts = "P";
						Global.actualRes = objCls + " CheckPoint successful.";
					}else{
						Global.status = false;
						Global.stepStts = "F";
						Global.actualRes = objCls + " CheckPoint failed !";
					}
				} else if(am.equals("E")){
					if(dt== null){
						Global.resString = Global.resString + " verify if WebTable " + oName + " exists.";
						objectExist(imgName);
					}else{
						// NEED TO ADD CODE HERE. . . . . . . later . 
					}
				} else if(am.equals("SR")){
					String rText, varName, arr[];
					arr = dt.split(";");
					rText = arr[0].toString();
					varName = arr[1].toString().trim();
					Global.resString = Global.resString + " Saving row form table " + oName + " to runtime variable '" + varName + "' = ";
					WebTable.saveTableRowToVariable(rText, varName);
					//
				}
				break;
			case "txt":
				if (am.equals("IN")){
					Global.status = false;
					Global.resString = Global.resString + " Iput Operation on " + oName + " " + objCls;
					Global.stepStts = "F";
					Global.actualRes = "Input Operation is NOT allowed on object type " + objCls + ". Please correct your script.";	
				} else if(am.equals("E")){
					Global.resString = Global.resString + " verify if " + oName + " " + objCls + " exists.";
					objectExist(imgName);
				} else if(am.equals("CP")){
					String tVal, pNam;
					if(dt.contains("=")){
						tValArr = dt.split("=");
						pNam = tValArr[0];
						tVal = tValArr[1];						
					} else{
						tVal = dt;
						pNam = "Text";
					}
					Global.resString = Global.resString + " validate if " + pNam + " of the " + objCls + " '" + oName + "' is '" + tVal + "'";
					checkObjectProperty(pNam, tVal, imgName);
				}		
				break;
			case "msg":
				if (am.equals("IN")){
					Global.status = false;
					Global.resString = Global.resString + " Iput Operation on " + oName + " " + objCls;
					Global.stepStts = "F";
					Global.actualRes = "Input Operation is NOT allowed on object type " + objCls + ". Please correct your script.";	
				} else if(am.equals("E")){
					Global.resString = Global.resString + " verify if " + oName + " " + objCls + " exists.";
					objectExist(imgName);
				} else if(am.equals("CP")){
					String tVal, pNam;
					if(dt.contains("=")){
						tValArr = dt.split("=");
						pNam = tValArr[0];
						tVal = tValArr[1];						
					} else{
						tVal = dt;
						pNam = "Text";
					}
					Global.resString = Global.resString + " validate if " + pNam + " of the " + objCls + " '" + oName + "' is '" + tVal + "'";
					checkObjectProperty(pNam, tVal, imgName);
				}		
				break;
			case "pan":
				if (am.equals("IN")){
					Global.status = false;
					Global.resString = Global.resString + " Iput Operation on " + oName + " " + objCls;
					Global.stepStts = "F";
					Global.actualRes = "Input Operation is NOT allowed on object type " + objCls + ". Please correct your script.";	
				} else if(am.equals("E")){
					Global.resString = Global.resString + " verify if " + oName + " " + objCls + " exists.";
					objectExist(imgName);
				} else if(am.equals("CP")){
					String tVal, pNam;
					if(dt.contains("=")){
						tValArr = dt.split("=");
						pNam = tValArr[0];
						tVal = tValArr[1];						
					} else{
						tVal = dt;
						pNam = "Text";
					}
					Global.resString = Global.resString + " validate if " + pNam + " of the " + objCls + " '" + oName + "' is '" + tVal + "'";
					checkObjectProperty(pNam, tVal, imgName);
				}		
				break;
			case "lbl":
				if (am.equals("IN")){
					Global.status = false;
					Global.resString = Global.resString + " Iput Operation on " + oName + " " + objCls;
					Global.stepStts = "F";
					Global.actualRes = "Input Operation is NOT allowed on object type " + objCls + ". Please correct your script.";			
				} else if(am.equals("E")){
					Global.resString = Global.resString + " verify if " + oName + " " + objCls + " exists.";
					objectExist(imgName);
				} else if(am.equals("CP")){
					String tVal, pNam;
					if(dt.contains("=")){
						tValArr = dt.split("=");
						pNam = tValArr[0];
						tVal = tValArr[1];						
					} else{
						tVal = dt;
						pNam = "Text";
					}
					Global.resString = Global.resString + " validate if " + pNam + " of the " + objCls + " '" + oName + "' is '" + tVal + "'";
					checkObjectProperty(pNam, tVal, imgName);
				}		
				break;
			case "pag":
				//code will be added as we build the functionality as required.
				Global.curElement.click();
				break;
			case "alt":
				//code will be added as we build the functionality as required.
				Global.curElement.click();
				break;
			case "win":
				//code will be added as we build the functionality as required.
				Global.curElement.click();
				break;
			case "dia":
				//code will be added as we build the functionality as required.
				Global.curElement.click();
				break;
			case "inf":
				if (am.equals("IN")){
					Global.status = false;
					Global.resString = Global.resString + " Iput Operation on " + oName + " " + objCls;
					Global.stepStts = "F";
					Global.actualRes = "Input Operation is NOT allowed on object type " + objCls + ". Please correct your script.";			
				} else if(am.equals("E")){
					Global.resString = Global.resString + " verify if " + oName + " " + objCls + " exists.";
					objectExist(imgName);
				} else if(am.equals("CP")){
					String tVal, pNam;
					if(dt.contains("=")){
						tValArr = dt.split("=");
						pNam = tValArr[0];
						tVal = tValArr[1];						
					} else{
						tVal = dt;
						pNam = "Text";
					}
					Global.resString = Global.resString + " validate if " + pNam + " of the " + objCls + " '" + oName + "' is '" + tVal + "'";
					checkObjectProperty(pNam, tVal, imgName);
				}		
				Global.curElement.click();
				break;
			case "pup":
				if (am.equals("IN")){
					Global.status = false;
					Global.resString = Global.resString + " Iput Operation on " + oName + " " + objCls;
					Global.stepStts = "F";
					Global.actualRes = "Input Operation is NOT allowed on object type " + objCls + ". Please correct your script.";				
				} else if(am.equals("E")){
					Global.resString = Global.resString + " verify if " + oName + " " + objCls + " exists.";
					objectExist(imgName);
				} else if(am.equals("CP")){
					String tVal, pNam;
					if(dt.contains("=")){
						tValArr = dt.split("=");
						pNam = tValArr[0];
						tVal = tValArr[1];						
					} else{
						tVal = dt;
						pNam = "Text";
					}
					Global.resString = Global.resString + " validate if " + pNam + " of the " + objCls + " '" + oName + "' is '" + tVal + "'";
					checkObjectProperty(pNam, tVal, imgName);
				}		
				Global.curElement.click();
				break;
			default :
				Global.resString = "Action Method is not defined for the object type(" + ot + ") or you have entered wrong object type.\n";
				Global.resString = Global.resString + "Please enter correct object type.";
				Global.actualRes = "Action Method NOT defined!\nStep Failed!";
				Global.stepStts = "F";
			}
		} catch (Exception e){
			Global.resString = Global.resString + "\t\tFailed !";
			System.out.println("Step Failed ! -- " + e);
			Global.status = false;
		}
		
		
	}
	
	public static void objClick(WebElement obj, String oCls, String oName, String imgName){
		//
		String imgLnk;
		Global.resString = Global.resString + " click on " + oName + " " + oCls;
		if(obj.isDisplayed() && obj.isEnabled()){
			try{
				obj.click();
				Global.stepStts = "D";
				Global.actualRes = oCls + " click successfull.";
			}catch(Exception e){
				Global.resString = Global.resString + "\nCouldn't click on WebButton. Error occured." + e;
				System.out.println(oCls + " click failed ! Error occurred: " + e);
				Global.status = false;
				Global.stepStts = "F";
				Global.actualRes = oCls + " click failed !";
			}
		}else{
			imgLnk = ImageCapture.takeFullScreenshot(imgName);
			System.out.println(oCls + " click failed ! Object is either Disabled or not visible.");
			Global.status = false;
			Global.stepStts = "F";
			Global.actualRes =  oCls + " click failed !\n";
			Global.actualRes = Global.actualRes + "Object is disabled and/or not visible";
			
		}		
		
	}
	
	public static void selectRadioButton(String valToSel, String oName){
		
		String objCls = "Radio-Button";
		int totRad = Global.radList.size();
		if (valToSel.matches("[-+]?\\d*\\.?\\d+")){	// select by index (0, 1, 2, . . .)
			int rIndx = Integer.parseInt(valToSel);
			Global.curElement = Global.radList.get(rIndx);
			String rName = Global.curElement.getAttribute("value");
			Global.resString = Global.resString + "Select '" + rName + "' from " + oName + " " + objCls  + " at index " + rIndx;
			Global.curElement.click();
			Global.actualRes = objCls + " selection successfull.";
		} else {
			for (int i = 0; i < totRad; i ++){ // get the correct index of the radio by value.
				Global.curElement = Global.radList.get(i);
				String radVal = Global.curElement.getAttribute("value");
				if (radVal.equals(valToSel)){
					Global.resString = Global.resString + "Select '" + valToSel + "' from " + oName + " " + objCls  + " at index " + i;
					Global.curElement.click();
				} // end if
			} // end for loop					
		} // end of main if -else
		if (Global.curElement.isSelected()){ // make sure radio button is selected.
			Global.stepStts = "D";
		}else {
			Global.stepStts = "F";				
		} // end if else
		
	}
	
	public static void selectDropdownList(WebElement E, String item, String oName){
			String oCls = "Drop-Down List";
			Select drpDwnLst = new Select(E);
			List<WebElement> listOpts = drpDwnLst.getOptions();
			
			int totList = listOpts.size();
			boolean itemFound = false;
			String curLstVal;
			WebElement optEl;
			int curItem, itemIndx = -1, lItem = 0;
			//System.out.println("Total Item in the list: " + totList);
			
			if (item.matches("[-+]?\\d*\\.?\\d+")){
				lItem = Integer.parseInt(item);
				//System.out.println("Item to select is numeric.");
				for (int k = 0; k<totList; k++){
					optEl = listOpts.get(k);
					curLstVal = optEl.getText().trim();
					//System.out.println("Item at index " + k + " = |" + curLstVal + "|");
					if(curLstVal.matches("[-+]?\\d*\\.?\\d+")){	// numeric
						//System.out.println("Both item to select and current list item at index " + k + " is numeric.");
						curItem = Integer.parseInt(curLstVal);
						if(lItem == curItem){
							System.out.println("Item found at index " + k);
							Global.resString = Global.resString + " select " + item + " form " + oName + " " + oCls;
							itemFound = true;
							itemIndx = k;
							break;
						}
					}else if(k == totList - 1){ // not numeric	- select the item at the given index.
						Global.resString = Global.resString + " select item at index " + item + " form " + oName + " " + oCls;
						itemIndx = Integer.parseInt(item);
						itemFound = true;
					}								
				}// end of for loop.
				System.out.println("Item found: " + itemFound + " index: " + itemIndx);

			} else{
				Global.resString = Global.resString + " select " + item + " form " + oName + " " + oCls;
				for(int j=0; j<=totList; j++){
					optEl = listOpts.get(j);
					curLstVal = optEl.getText();
					if(item.equals(curLstVal.trim())){						
						itemFound = true;
						//itemIndx = k;
						System.out.println("Item '" + curLstVal + "' found at:" + j);
						itemIndx = j;
						itemFound = true;
						break;
					} else if(j>= totList){
						itemIndx = Integer.parseInt(item);
						itemFound = false;
						System.out.println("Item you've entered: " + item + " does NOT exist in the list.");
					}
					
				}
				//drpDwnLst.selectByIndex(itemIndx);	
			}
			
			
			//Global.resString = Global.resString + " select " + item + " form " + oName + " " + oCls;
			
			if (itemFound && itemIndx >= 0){
				drpDwnLst.selectByIndex(itemIndx);				
				Global.stepStts = "D";
				Global.actualRes = oCls + " selection successful.";
			} else{
				Global.stepStts = "F";
				Global.resString = Global.resString + "\nBut the given item or indes '" + item + "' doesn't exist in the list.";
				Global.actualRes = oCls + " selection failed!";
			}			
		}
	
	public static boolean checkDropDownListProperty(WebElement el, String prpName, String prpVal, String oName, String imgName){
		
		boolean success = false;
		String imgLnk;
		Select drpDwnLst = new Select(el);
		List<WebElement> listOpts = drpDwnLst.getOptions();
		
		int totList = listOpts.size();
		boolean itemFound = false;
		
		switch(prpName.toUpperCase()){
			case "COUNT":
				Global.resString = Global.resString + " validate if toal items in the Drop-down list is " + prpVal;
				if(totList == Integer.parseInt(prpVal)){
					success = true;
					Global.resString = Global.resString + "\nItem count matched! Given count: " + prpVal + " Actual count: " + totList;
					Global.stepStts = "P";
					Global.status = true;
					imgLnk = ImageCapture.takeFullScreenshot(imgName);
					Global.actualRes = "Checkpoint Passed!<br>Test Value:- Count = " + prpVal;
					Global.actualRes = Global.actualRes + "<br>Actual Value:- Count = "  + totList;
					Global.actualRes = Global.actualRes + imgLnk;
				}else{
					success = false;
					imgLnk = ImageCapture.takeElementImage(imgName);
					Global.resString = Global.resString + "\nItem count doesn't match! Given count: " + prpVal + " Actual count: " + totList;
					Global.stepStts = "F";
					Global.status = false;
					imgLnk = ImageCapture.takeFullScreenshot(imgName);
					Global.actualRes = "Checkpoint FAILED!<br>Test Value:- Count = " + prpVal;
					Global.actualRes = Global.actualRes + "<br>Actual Value:- Count = "  + totList;
					Global.actualRes = Global.actualRes + imgLnk;
				}
				break;
			case "ISITEM":
				Global.resString = Global.resString + " validate if item " + prpVal + " is present in the " + oName + " Drop-down list";
				String itemVal;
				WebElement curItem;
				int itemIndx;
				for(int j=0; j<=totList; j++){
					curItem = listOpts.get(j);
					itemVal = curItem.getText();
					if(prpVal.equals(itemVal.trim())){
						itemFound = true;
						itemIndx = j;
						System.out.println("Item '" + prpVal + "' found at:" + itemIndx);
						itemFound = true;
						Global.stepStts = "P";
						drpDwnLst.selectByIndex(itemIndx);	// Selecting the item just to take screenshot
						imgLnk = ImageCapture.takeElementImage(imgName);						
						Global.actualRes = "Item exists, Checkpoint Passed!";
						Global.actualRes = Global.actualRes + imgLnk;
						break;
					} else if(j>= totList){
						itemFound = false;
						Global.stepStts = "F";
						Global.resString = Global.resString + "The item '" + prpVal + "' does NOT exist in the list.";
						imgLnk = ImageCapture.takeElementImage(imgName);						
						Global.actualRes = "Item does NOT exist, Checkpoint Failed!";
						Global.actualRes = Global.actualRes + imgLnk;
						break;
					}
				}
				if(itemFound){
					success = true;
				}else{
					success = false;
				}
				break;
			default:
				success = false;
				Global.stepStts = "F";
				Global.resString = Global.resString + " Drop-Down CheckPoint - Drop-down property check option " + prpName + " code NOT defined.";
				Global.resString = Global.resString + "The item '" + prpVal + "' does NOT exist in the list.";
				imgLnk = ImageCapture.takeElementImage(imgName);						
				Global.actualRes = "Checkpoint Failed!\nCheckPoint option " + prpName + " code NOT defiened!";
				Global.actualRes = Global.actualRes + imgLnk;
				System.out.println("Drop-down property check option " + prpName + " code NOT defined.");
		}
		
		return success;
	}
	
	public static void saveDropDowListProperty(WebElement el, String prpName, String varName) throws InterruptedException{
		
		Select drpDwnLst = new Select(el);
		drpDwnLst.getFirstSelectedOption();
		String runTimeProp = "";
		switch(prpName){
		case "selecteitem":
			runTimeProp = drpDwnLst.getFirstSelectedOption().toString();
			break;
		case "itemcount":
			runTimeProp = Integer.toString(drpDwnLst.getAllSelectedOptions().size());
			break;
		default:	
			runTimeProp = drpDwnLst.getAllSelectedOptions().toString();			
			break;
		}
		Global.addToDictionary(varName, runTimeProp, Global.runTimeDataRepository);
		IWait(2);
		if(Global.readDictionary(varName, Global.runTimeDataRepository) != ""){
			Global.actualRes = "Saving " + runTimeProp +" of drop-down list to variable " + varName + " successfull.";
			Global.stepStts = "D";
			Global.status = true;
		}else{
			Global.actualRes = "Saving " + runTimeProp +" of drop-down list to variable " + varName + " Failed!";
			Global.stepStts = "F";
			Global.status = false;
		}
		
		
	}
	public static void checkUncheckChkBox(WebElement obj, String oName){
		if(obj.isSelected()){
			Global.resString = Global.resString + " Uncheck " + oName + " Checkbox.";
			Global.actualRes = "Checking Checkbox successfull.";
		} else{
			Global.resString = Global.resString + " Check " + oName + " Checkbox.";
		}
		//Global.curElement.click();
		obj.click();
		Global.stepStts = "D";
		
	}
	
	public static void setBrowser(){
		
		if(Global.brType.toUpperCase().equals("CHROME")){
			System.setProperty("webdriver.chrome.driver", Global.chrmDrvPath);
			Global.drvr = new ChromeDriver();
		} else if (Global.brType.toUpperCase().equals("IE")){
			System.setProperty("webdriver.ie.driver", Global.ieDrvPath);
			Global.drvr = new InternetExplorerDriver();
			// Open IE
		} else if(Global.brType.toUpperCase().equals("FX")){
			// Open FireFox.
			Global.drvr = new FirefoxDriver();
		}
		Global.drvr.get(Global.url);
		Global.drvr.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Global.drvr.manage().deleteAllCookies();
	}
	
	public static void browserClose(){
		
		Global.drvr.close();
	}
	
	public static void IWait(long tm) throws InterruptedException{
		//drv.manage().timeouts().implicitlyWait(tm, TimeUnit.SECONDS);
		TimeUnit.SECONDS.sleep(tm);
	}

	public static void enterDataIntoEditField(WebElement obj, String dt, String imgName){
		//Global.curElement.sendKeys(dt);
		try{
			obj.clear();
			obj.sendKeys(dt);
			String txt = Global.curElement.getAttribute("value");
			String imgLnk;
			if (txt.equals(dt)){		// to ensure if the data actually entered and displayed in the edit box.
				Global.stepStts = "D";
				Global.status = true;						
				//Screen capture goes here..
				imgLnk = ImageCapture.takeElementImage(imgName);
				Global.actualRes = "Data Input Successfull.";
				Global.actualRes = Global.actualRes + imgLnk;
				
			} else {
				System.out.println("Val to enter: " + dt + "\tVal entered: " + txt);
				Global.stepStts = "F";
				Global.status = false;
				imgLnk = ImageCapture.takeFullScreenshot(imgName);
				Global.actualRes = "Data Input FAILED!";
				Global.actualRes = Global.actualRes + imgLnk;
				Global.resString = Global.resString + "\nFailed to enter data into edit field.";
			} // end if - else
		}catch(Exception ex){
			System.out.println("Error occured while entering data into edit field: " + ex);
			Global.status = false;
			Global.stepStts = "F";
			Global.resString = Global.resString + "\nError occured while entering data into edit field: " + ex;
			Global.actualRes = "Data Input FAILED!";
		}
		
	}
	
	
	public static void objectExist(String imgName){
		String imgLnk = "";
		if(Global.curElement.isDisplayed()){	
			Global.stepStts = "P";
			Global.status = true;
			imgLnk = ImageCapture.takeFullScreenshot(imgName);
			Global.actualRes = "Element Exists.";
			Global.actualRes = Global.actualRes + imgLnk;
		} else{
			Global.stepStts = "F";
			Global.status = false;
			imgLnk = ImageCapture.takeFullScreenshot(imgName);
			Global.actualRes = "Element does NOT exist!";
			Global.actualRes = Global.actualRes + imgLnk;
		} // end if- else
	}		//*[@id="buildingTable"]/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div[1]/div/div[2]/div/div[2]/label[2]

	public static void checkObjectProperty(String pNam, String tVal, String imgName){
		String rnVal, imgLnk;
		
		if(Global.curElement.isDisplayed()){
			if(pNam.equals("Text")){
				rnVal = Global.curElement.getText();				
			} else{
				rnVal = Global.curElement.getAttribute(pNam);				
			}
			
			if(rnVal != ""){
				System.out.println("Element displayed. Text = " + rnVal);
				if(tVal.toUpperCase().equals(rnVal.toUpperCase())){
					Global.stepStts = "P";
					Global.status = true;
					imgLnk = ImageCapture.takeElementImage(imgName);
					Global.actualRes = "Checkpoint Passed!<br>Test Value:- " + pNam +" = " + tVal;
					Global.actualRes = Global.actualRes + "<br>Actual Value:- " + pNam +" = " + rnVal + "<br>";
					Global.actualRes = Global.actualRes + imgLnk;
				}else if(rnVal.toUpperCase().contains(tVal.toUpperCase()) && pNam.equals("Text")){
					Global.stepStts = "W";
					Global.status = true;
					imgLnk = ImageCapture.takeElementImage(imgName);
					Global.actualRes = "Checkpoint passed with Warning!<br>Actual text contains the test data:- " + pNam +" = " + tVal;
					Global.actualRes = Global.actualRes + "<br>Actual Value:- " + pNam +" = " + rnVal + "<br>";
					Global.actualRes = Global.actualRes + imgLnk;
				}else{
					// checkpoint failed . . .
					Global.stepStts = "F";
					Global.status = false;
					imgLnk = ImageCapture.takeFullScreenshot(imgName);
					Global.actualRes = "Checkpoint FAILED!<br>Test Value:- " + pNam +" = " + tVal;
					Global.actualRes = Global.actualRes + "<br>Actual Value:- " + pNam +" = "  + rnVal;
					Global.actualRes = Global.actualRes + imgLnk;
				}
			}else{
				if(tVal == ""){
					Global.stepStts = "P";
					Global.status = true;
					imgLnk = ImageCapture.takeFullScreenshot(imgName);
					Global.actualRes = "Checkpoint Passed!\nTest value is blank, actrul value is also blank. See the picture:";
					Global.actualRes = Global.actualRes + imgLnk;
				}else{
					System.out.println("Object displays no text. Make sure correct object is added to the Object Repository.");
					Global.stepStts = "F";
					Global.status = false;
					imgLnk = ImageCapture.takeFullScreenshot(imgName);
					Global.actualRes = "Checkpoint FAILED!\nObject displays no text. Make sure your step is correct or using the object.";
					Global.actualRes = Global.actualRes + imgLnk;
				}
				
			}
			
		}else{
			Global.stepStts = "F";
			Global.status = false;
			imgLnk = ImageCapture.takeFullScreenshot(imgName);
			Global.actualRes = "Checkpoint FAILED! Object does NOT exist!";
			Global.actualRes = Global.actualRes + imgLnk;
		}
		
	}
	
	public static String getObjectProperty(WebElement el, String prpName) throws InterruptedException{
		String val = null;
		IWait(1);
		try{
			if(prpName.equals("Text")){
				val = el.getText();
			}else{ 
				val = el.getAttribute(prpName);
			}
			if(val == ""){
				val = prpName + " ** no such property avaialble.";
			}
		}catch(Exception e){
			System.out.println("Couldn't get the value of the property " + prpName + "\t--SelMethods.getObjectProperty()\n" + e);
			e.wait(2);
		}
		
		return val;
	}

}
