package global;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.seleniumhq.jetty7.util.log.Log;

import selenium.SelMethods;

public class ObjectRepsitory {
	InputStream iStream;

	@SuppressWarnings("deprecation")
	public static void buildObject(String propStr, String oType){
		
		String findBy, val;
		//System.out.println(" ************ \t" + propStr);
		
		String propArr[] = propStr.split("<>");
		
		findBy = propArr[0].trim();
		val = propArr[1].trim();
		
		String txtFindBy = "Find [" + oType + "] By: " + findBy;
		
		
		try{
			if(oType.equals("rad")){
				switch (findBy){
				case "id":
					Global.radList = Global.drvr.findElements(By.id(val));
					break;
				case "name":
					Global.radList = Global.drvr.findElements(By.name(val));
					break;
				case "xpath":
					Global.radList = Global.drvr.findElements(By.xpath(val));
					break;
				case "css":
					Global.radList = Global.drvr.findElements(By.cssSelector(val));
					break;
				case "tag":
					String tag = "";
					Global.curElement = findByTag(tag, val);
					//Global.radList = Global.drvr.findElements(By.tagName(val));
					break;
				} 
			} // end if
			else {				
				switch (findBy){
					case "id":
						txtFindBy = txtFindBy + " | val: " + val;
						System.out.println(txtFindBy);
						SelMethods.IWait(3);	// Temporary wait 
						Global.curElement = Global.drvr.findElement(By.id(val));
						break;
					case "name":
						txtFindBy = txtFindBy + " | val: " + val;
						System.out.println(txtFindBy);
						Global.curElement = Global.drvr.findElement(By.name(val));
						break;
					case "xpath":
						txtFindBy = txtFindBy + " | val: " + val;
						System.out.println(txtFindBy);
						try{
							Global.curElement = FindByXPath(val);
						}catch(Exception e){
							Log.debug(e);
							Global.curElement = FindByXPath(val);
						}
												
						break;
					case "css":
						txtFindBy = txtFindBy + " | val: " + val;
						System.out.println(txtFindBy);
						Global.curElement = Global.drvr.findElement(By.cssSelector(val));
						break;
					case "linkText":
						txtFindBy = txtFindBy + " | val: " + val;
						System.out.println(txtFindBy);
						Global.curElement = Global.drvr.findElement(By.linkText(val));
						break;
					case "partLinkText":
						txtFindBy = txtFindBy + " | val: " + val;
						System.out.println(txtFindBy);
						Global.curElement = Global.drvr.findElement(By.partialLinkText(val));
						break;
					case "tag":
						String tag = "";						
						if (oType.equals("btn")){
							tag = "button";
						}else if(oType.equals("sel")){
							tag = "select";
						} else if(oType.equals("tbl")){
							tag = "table";
						}else if(oType.equals("")){
							tag = "";
						}else if(oType.equals("chk")){
							tag = "input";
						}else if(oType.equals("lnk")){
							tag = "a";
						}else if(oType.equals("rad")){
							tag = "";
						}else if(oType.equals("pup")){	//pop-up
							tag = "div";
						}else if(oType.equals("edt")){
							tag = "input";
						}else if(oType.equals("txt")){
							tag = "p";
						}else if(oType.equals("lbl")){
							tag = "label";
						}else if(oType.equals("inf")){
							tag = "span";
						}else if(oType.equals("msg")){
							tag = "div";
						}else if(oType.equals("pan")){
							tag = "div";
						}else{
							System.out.println("Unknow Object Type: " + oType);
							Global.status = false;
						}
						
						txtFindBy = txtFindBy + " - " + tag + " | other val: " + val;
						System.out.println(txtFindBy);
						Global.curElement = findByTag(tag, val);
						
						//Global.curElement = Global.drvr.findElement(By.tagName(val));
						break;
						
				// More case will be added later.
				}	// end Switch
			}// end else
			if(oType == "edt"){
				Global.curElement.click();
			}
			
			if(Global.curElement.isEnabled()){
				System.out.println("Build object with " + findBy + " value: " + val + " successful.");
				Global.status = true;
			} else{
				System.out.println("Build object with " + findBy + " value: " + val + " FAILED!");
				Global.status = false;
			}
		}	// end try block
		catch (Exception e){
			System.out.println(" -- > Error form 'buildObject' method in SelMethods");
			e.printStackTrace();
			System.out.println("Element couldn't be found ! " + e);
			Global.ElementBuildErr = true;
			Global.status = false;
			Global.stepStts = "F";
		}
		
	}
	
	public static WebElement findByTagOld(String tagName, String othrVal){
		
		String morePropArr[], prop1Arr[], prop2Arr[];
		String prop1 = "", prop1Val = "", prop2 = "", prop2Val = "";
		String othrProp = "", oPV = "", curPVal1, curPVal2;
		List<WebElement> allElFound;
		WebElement testEl = null;			
		boolean dblProp, elFound = false;
		
		if(othrVal.contains(";")){	// Checking 2 properties...
			System.out.println("Using double properties...");
			morePropArr = othrVal.split(";");
			prop1Arr = morePropArr[0].toString().split("=");
			prop1 = prop1Arr[0].toString().trim();
			prop1Val = prop1Arr[1].toString().trim();
			
			prop2Arr = morePropArr[1].toString().split("=");				
			prop2 = prop2Arr[0].toString().trim();
			prop2Val = prop2Arr[1].toString().trim();
			
			System.out.println("Property1: " + prop1 + "\t Value: " + prop1Val);
			System.out.println("Property2: " + prop2 + "\t Value: " + prop2Val);
			dblProp = true;
		}else {
			System.out.println("Using single property...");
			dblProp = false;
			morePropArr = othrVal.split("=");
			othrProp = morePropArr[0].trim();
			oPV = morePropArr[1].toString().trim();
			System.out.println("Prop Name: " + othrProp + "\tValue: " + oPV);
		}
		
		try{
			allElFound = Global.drvr.findElements(By.tagName(tagName));			
			int totElFound = allElFound.size();
			int matchFound = 0;
			
			if(totElFound > 0){
				System.out.println("Total element found by tag " + tagName + ": " + totElFound);
					
				for(int k = 0; k< totElFound; k++){
						if(dblProp){	// Double properties . . .
							curPVal1 = allElFound.get(k).getAttribute(prop1).trim();
							curPVal2 = allElFound.get(k).getAttribute(prop2).trim();
							System.out.println("Property1:\n" + prop1 + " || Test Value: " + prop1Val + "\tActual Value: " + curPVal1 + "\n");
							System.out.println("Property2:\n" + prop2 + " || Test Value: " + prop2Val + "\tActual Value: " + curPVal2 + "\n");
														
							if(curPVal1.equals(prop1Val) && curPVal2.equals(prop2Val)){
								matchFound = matchFound + 1;
								if (allElFound.get(k).isDisplayed()){
									System.out.println("Element found at: " + k);
									testEl = allElFound.get(k);
									break;
								}
								
							} else if(k == (totElFound - 1)){
								System.out.println("Couldn't find the element.");
							}
						} else{	// Single property . . .
							System.out.println("Checking element " + k + " for matching info.");
							curPVal1 = SelMethods.getObjectProperty(allElFound.get(k), othrProp);
							//ObjectRepsitory.curPVal1 = allElFound.get(k).getAttribute(othrProp).trim();
							System.out.println("Current value of property [" + k +"] " + othrProp + " is: " + curPVal1);
							System.out.println("Property: " + othrProp + " || Test Value: " + oPV + "\tActual Value: " + curPVal1 + "\n");
							if (curPVal1 != null){
								if(curPVal1.equals(oPV)){								
									matchFound = matchFound + 1;
									if (allElFound.get(k).isDisplayed()){
										System.out.println("Element found at: " + k);
										testEl = allElFound.get(k);
										break;
									}
								}
							}else if((k == totElFound - 1)&& !elFound){
								System.out.println("Coulnd't find an element wtith tag: " + tagName + " and " + othrProp + "=" + oPV);
								Global.resString = Global.resString + "\nCoulnd't find an element wtith tag: " + tagName + " and " + othrProp + "=" + oPV;
								Global.resString = Global.resString + "Make sure such object is present in the application or object property may be changed.";
								testEl = null;
								Global.status = false;			
								Global.stepStts = "F";
								Global.actualRes = "Object build failed !";
							}
							
						}
				}
				System.out.println("Matched element found: " + matchFound);
			} else{
				System.out.println("Coulnd't find an element wtith tag: " + tagName + " and " + othrProp + "=" + oPV);
				Global.resString = Global.resString + "\nCoulnd't find an element wtith tag: " + tagName + " and " + othrProp + "=" + oPV;
				Global.resString = Global.resString + "Make sure such object is present in the application or object property may be changed.";
				testEl = null;
				Global.status = false;			
				Global.stepStts = "F";
				Global.actualRes = "Object build failed !";
			}			
		}catch(Exception e){
			Global.resString = Global.resString + "\nCoulnd't find an element wtith tag: " + tagName + " and " + othrProp + "=" + oPV;
			Global.resString = Global.resString + "Make sure such object is present in the application or object property may be changed.";
			System.out.println("<findByTag> Coulnd't find an element wtith tag: " + tagName + " and " + othrProp + "=" + oPV);
			System.out.println("Object build failid! " + e);
			testEl = null;
			Global.status = false;			
			Global.stepStts = "F";
			Global.actualRes = "Object build failed !";
		}
				
		return testEl;
	}

	public static String getPropery(String prKey){	//test object from Object Repository
		File pFile = new File(Global.ORpath);
		FileInputStream fi = null;
		String pVal = "";
		//System.out.println("prKey: " + prKey);
		try{
			fi = new FileInputStream(pFile);
			
		} catch(FileNotFoundException e){
			System.out.println("OR(properties) file couldn't be found! Exception occured: " + e);
			e.printStackTrace();
		}
		
		Properties OR = new Properties();
		
		try{
			
			OR.load(fi);
			if (OR.containsKey(prKey)){
				pVal = OR.getProperty(prKey);
			} else{
				System.out.println("\nProperty key " + prKey + " does NOT exist in OR file.\n");
				System.out.println("\nFollowing are the available keys in OR:\n");
				Enumeration allKeys = OR.keys();		
				while (allKeys.hasMoreElements()){
					String ck = (String) allKeys.nextElement();
					String val = OR.getProperty(ck);
					System.out.println(ck + " ~ " + val);
				}
				pVal = "";
				Global.ORElementFindErr = true;			
			}
			
		} catch (IOException e){			
			System.out.println("OR file couldn't open. Exception: " + e);
			e.printStackTrace();
		}		
		
		return pVal;
	}
	
	public static String getObjectClass(String acrNm){
		String oType;
		
		switch(acrNm){
			case "edt" :
				oType = "WebEdit";
				break;
			case "img":
				oType = "Image";
				break;
			case "lnk" :
				oType = "Link";
				break;
			case "btn":
				oType = "WebButton";
				break;
			case "lst":
				oType = "WebList";
				break;
			case "chk":
				oType = "WebCheckBox";
				break;
			case "rad":
				oType = "WebRadioGroup";
				break;
			case "tbl":
				oType = "WebTable";
				break;
			case "txt":
				oType = "Text";
				break;
			case "pag":
				oType = "Page";
				break;
			case "alt":
				oType = "Alert Message";
				break;
			case "win":
				oType = "Window";
				break;
			case "dia":
				oType = "Dialog";
				break;
			case "inf":
				oType = "Info Block";
				break;
			case "sel":
				oType = "Drop-Down";
				break;
			case "lbl":
				oType = "Message Text";
				break;
			case "pup":
				oType = "Popup Box";
				break;
			case "msg":
				oType = "Message Text";
				break;
			case "pan":
				oType = "Panel";
				break;
			default :
				oType = "Unknown Object";
				System.out.println("Unknow Object Type: " + acrNm);
				Global.status = false;
		}
		
		return oType;
	}

	
public static WebElement findByTag(String tagName, String othrVal){
		
		String morePropArr[], prop1Arr[], prop2Arr[];
		String prop1 = "", prop1Val = "", prop2 = "", prop2Val = "";
		String othrProp = "", oPV = "", curPVal1, curPVal2;
		List<WebElement> allElFound;
		WebElement testEl = null;			
		boolean dblProp, elFound = false;
		
		if(othrVal.contains(";")){	// Checking 2 properties...
			System.out.println("Using double properties...");
			morePropArr = othrVal.split(";");
			prop1Arr = morePropArr[0].toString().split("=");
			prop1 = prop1Arr[0].toString().trim();
			prop1Val = prop1Arr[1].toString().trim();
			
			prop2Arr = morePropArr[1].toString().split("=");				
			prop2 = prop2Arr[0].toString().trim();
			prop2Val = prop2Arr[1].toString().trim();
			
			System.out.println("Property1: " + prop1 + "\t Value: " + prop1Val);
			System.out.println("Property2: " + prop2 + "\t Value: " + prop2Val);
			dblProp = true;
		}else {
			System.out.println("Using single property...");
			dblProp = false;
			morePropArr = othrVal.split("=");
			othrProp = morePropArr[0].trim();
			oPV = morePropArr[1].toString().trim();
			System.out.println("Prop Name: " + othrProp + "\tValue: " + oPV);
		}
		
		allElFound = Global.drvr.findElements(By.tagName(tagName));			
		int totElFound = allElFound.size();
		int matchFound = 0;
		
		if(totElFound > 0){
			System.out.println("Total element found by tag " + tagName + ": " + totElFound);
				
			if(dblProp){	// Double properties . . .
				for(int k = 0; k< totElFound; k++){
					try{
						curPVal1 = allElFound.get(k).getAttribute(prop1).trim();
						curPVal2 = allElFound.get(k).getAttribute(prop2).trim();
						System.out.println("Property1:\n" + prop1 + " || Test Value: " + prop1Val + "\tActual Value: " + curPVal1 + "\n");
						System.out.println("Property2:\n" + prop2 + " || Test Value: " + prop2Val + "\tActual Value: " + curPVal2 + "\n");
						if(curPVal1.equals(prop1Val) && curPVal2.equals(prop2Val)){
							matchFound = matchFound + 1;
							if (allElFound.get(k).isDisplayed()){
								System.out.println("Element found at: " + k);
								testEl = allElFound.get(k);
								break;
							}							
						} else if(k == (totElFound - 1)){
							System.out.println("Completed searching " + k + " elements, coulnd't find an element wtith tag: " + tagName + " and " + othrProp + "=" + oPV);
							Global.resString = Global.resString + "\nCoulnd't find an element wtith tag: " + tagName + " and " + othrProp + "=" + oPV;
							Global.resString = Global.resString + "Make sure such object is present in the application or object property may be changed.";
							testEl = null;
							Global.status = false;			
							Global.stepStts = "F";
							Global.actualRes = "Object build failed !";
						}
					}catch(Exception e){
						System.out.println("Exception occured. Retrive the object info again.");
						allElFound = Global.drvr.findElements(By.tagName(tagName));
						totElFound = allElFound.size();
					}					
				}
			} else{	// Single property . . .
				for(int k = 0; k< totElFound; k++){
					System.out.println("Checking element " + k + " for matching info.");
					try{
						curPVal1 = SelMethods.getObjectProperty(allElFound.get(k), othrProp);
						System.out.println("Current value of property [" + k +"] " + othrProp + " is: " + curPVal1);
						System.out.println("Property: " + othrProp + " || Test Value: " + oPV + "\tActual Value: " + curPVal1 + "\n");
						if (curPVal1 != null){
							if(curPVal1.equals(oPV)){								
								matchFound = matchFound + 1;
								if (allElFound.get(k).isDisplayed()){
									System.out.println("Element found at: " + k);
									testEl = allElFound.get(k);
									break;
								}
							}
						}else if((k == totElFound - 1)&& !elFound){
							System.out.println("Completed searching " + k + " elements, coulnd't find an element wtith tag: " + tagName + " and " + othrProp + "=" + oPV);
							Global.resString = Global.resString + "\nCoulnd't find an element wtith tag: " + tagName + " and " + othrProp + "=" + oPV;
							Global.resString = Global.resString + "Make sure such object is present in the application or object property may be changed.";
							testEl = null;
							Global.status = false;			
							Global.stepStts = "F";
							Global.actualRes = "Object build failed !";
						}
					}catch(Exception e){
						System.out.println("Exception occured. Retrive the object info again.");
						allElFound = Global.drvr.findElements(By.tagName(tagName));
						totElFound = allElFound.size();
						//matchFound = 0;
					}							
				}						
			}	//End of if .. else
//
			System.out.println("Matched element found: " + matchFound);
		} else{
			System.out.println("Coulnd't find an element wtith tag: " + tagName + " and " + othrProp + "=" + oPV);
			Global.resString = Global.resString + "\nCoulnd't find an element wtith tag: " + tagName + " and " + othrProp + "=" + oPV;
			Global.resString = Global.resString + "Make sure such object is present in the application or object property may be changed.";
			testEl = null;
			Global.status = false;			
			Global.stepStts = "F";
			Global.actualRes = "Object build failed !";
		}				
		return testEl;
	}

	
	@SuppressWarnings("deprecation")
	public static WebElement FindByXPath(String xpath) throws InterruptedException{
		WebElement curEl = null;
		try{
			SelMethods.IWait(2);
			curEl = Global.drvr.findElement(By.xpath(xpath));
		}catch(StaleElementReferenceException e){
			SelMethods.IWait(3);
			System.out.println("Element NOT found!! Make sure your xpath is correct or actual object is present in application.");
			Log.debug(e);
			curEl = Global.drvr.findElement(By.xpath(xpath));
		}
		
		return curEl;		
	}

	
}
