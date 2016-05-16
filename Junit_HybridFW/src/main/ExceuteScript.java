package main;

import global.Global;
import global.ObjectRepsitory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import result.CreateResult;
import selenium.SelMethods;


public class ExceuteScript {
	
	public static String actWord;
	//how do u get these parameters passed?
	public static void Run(String pg, String el, String ky, String data) throws IOException, InterruptedException {
		
		String oProperty, propKey;
		ExceuteScript ex = new ExceuteScript();
		String objType, objClass;
		String actMethod, stts = "";
		
		if(ky.equals("*")){
			// Skip to the next row.
			Global.resString = "This row is skipped !";
			Global.status = true;
			Global.stepStts = "W";
			Global.actualRes = "Row Skip";
		}else if(ky.equals("IW")|| ky.equals("W")){		// need to handle error for wrong data type.
			long tm = Integer.parseInt(data);
			Global.resString = "Implicitly wait for " + tm + " seconds.";
			Global.status = true;
			Global.stepStts = "D";
			Global.actualRes = "Wait for " + tm + " seconds.";
			SelMethods.IWait(tm);
		} else if(ky.equals("WP") || ky.equals("EW")){
			// need to add code for wait for specific property of an element.
			long tm = Integer.parseInt(data);
			Global.resString = "Implicitly wait for " + tm + " seconds.";
			Global.status = true;
			Global.stepStts = "D";
			Global.actualRes = "Wait";
			SelMethods.IWait(tm);
		} else{	// Continue to test run.
			propKey = pg + "<>" + el;
			objType = el.substring(0, 3);
			if(propKey.equals(Global.prevORkey)){
				//do not need to get object property or build the object again.
				Global.resString = "On the '" + pg + "' page, ";
				SelMethods.action(ky, objType, el, data);
				System.out.println("Last action object and the current objects are same, so FW doesn't need to rebuild the object.");
			}else{
				Global.prevORkey = propKey;
				oProperty = ObjectRepsitory.getPropery(propKey);
				
				if (oProperty !="" && !Global.ORElementFindErr){
					//System.out.println("**Property: " + oProperty);
					objClass = ObjectRepsitory.getObjectClass(objType);
					if(objClass.equals("Unknown Object")){
						Global.resString = "Object type may not defined in ObjectRepository Class or you have entered wrong object type.\n";
						Global.resString = Global.resString + "Please enter correct object type.";
						Global.actualRes = "Object Undefined!\nStep Failed!";
						Global.stepStts = "F";					
					}else{
						//actMethod = ex.getActionMethod(ky, objType);
						ObjectRepsitory.buildObject(oProperty, objType);
						if(Global.status){
							Global.resString = "On the '" + pg + "' page, ";
							SelMethods.action(ky, objType, el, data);					
						}else{
							Global.resString = "Failed to create runtime object: Page - " + pg + " Element - " + el;
							Global.resString = Global.resString + "\nPlease make sure that the object is present in application.\n";
							Global.resString = Global.resString + "Or you have added the correct object information in your Object Repository file.";
							Global.actualRes = "Step Failed!";
							Global.stepStts = "F";
						}
					}
									
					//System.out.println("Object property from OR: " + oProperty);
					//System.out.println("Object Class is: " + objClass);
					//System.out.println("Method to apply: " + actMethod);
					
				} else {
					String errMsg = "Page '" + pg + "' and Element '" + el + "' combination do NOT exist in OR file";
					System.out.println(errMsg);
					Global.resString = errMsg;
					Global.status = false;
					Global.actualRes = "Step Failed!";
					Global.stepStts = "F";
				}
			}
						
			//System.out.println("\nObject type: " + objType);
			
		}
			
	}
	
	public String getActionMethod(String actKey, String oType){
		String method = "";
		oType = oType.trim();
		
		switch (actKey){
			case "IN" :				
				if(oType.equals("edt")){
					//System.out.println("SendKeys");
					actWord = "Enter Text ";
					method = "SendKeys";
				} else if(oType.equals("btn") || oType.equals("img")){
					method = "Click";
					actWord = "Click";
				} else if(oType.equals("lst") || oType.equals("rad") || oType.equals("chk")|| oType.equals("sel")){
					method = "Select";
					actWord = "Select";
				} else if(oType.equals("alt")){
					method = "";
					actWord = "";					
				}else if(oType == ""){
					actWord = "blank";
					method = "blank";
				}
				break;
			case "CP":
				if(oType == ""){
					actWord = "";
				} else if(oType == ""){
					actWord = "";
				}
				break;
			case "*":
				actWord = "Skip Step";
				break;
			case "IW" :
				actWord = "Implicit Wait";
				break;
			case "E":
				actWord = "Verify";
			case "SR":
				actWord = "Save Table Row";
			case "EW":
				actWord = "Explicit Wait";
			case "SP":
				actWord = "Save property to variable";
			case "":
				actWord = "";
			default :
				actWord = "Undefined Action Key";
				
		}
		
		return method;
	}

}