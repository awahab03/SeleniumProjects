package global;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import java.util.Properties;
//import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public abstract class Global {
	public static final String FWRoot = getFWRoot();
	
	public static final String globalConfigPath = FWRoot + "Junit_HybridFW\\FWSupport\\Config\\glblConfig.properties";
	public static final String cssPath = FWRoot + "Junit_HybridFW\\FWSupport\\ResConfig\\style.css";
	public static final String usptoLogo = FWRoot + "Junit_HybridFW\\FWSupport\\ResConfig\\IAE_Logo.jpg";
	
	public static String projName;
	
	//public static final String localPath = "C:\\Users\\awaha\\workspace\\IAE_FW\\AutFramework\\Config\\";
	public static final String profileFilePath = FWRoot + "Junit_HybridFW\\FWSupport\\Config\\profile.properties";
	public static final String localConfigPath = FWRoot + "Junit_HybridFW\\FWSupport\\Config\\lConfig.properties";
	public static final String localBatchPath = FWRoot + "Junit_HybridFW\\FWSupport\\Config\\Script_List.xls";
	
	public static String projBatchPath; // = FWRoot + projName + "\\Config\\Script_List.xls";
	public static String projConfigPath; // = FWRoot + projName + "\\Config\\projConfig.properties";
	public static String ORpath; // = FWRoot + projName + "\\OR\\OR.properties";
	
	public static String scriptListPath;	
	public static String fullScriptPath;
	
	public static String curTesName;
	public static String resultPath;
	public static String resultMatrixPath;
	public static String curResPath;
	public static String curImagePath;
	
	public static WebDriver drvr;
	public static WebElement curElement;
	public static List<WebElement> radList;
	public static String prevORkey;
	//public static WebDriverWait wait = new WebDriverWait(drvr, 60);
	
	public static String resString;
	public static String actualRes;
	public static String stepStts;
	public static Boolean ElementBuildErr = false;
	public static Boolean ORElementFindErr = false;
	public static final Boolean contChkPointFail = false;
	public static boolean continueOnTestFail;
	public static Boolean status = true;
	public static String actionDef;
	public static int totSteps = 0;
	public static int curStep = 0;
	public static int passCounter = 0;
	public static int failCounter = 0;
	public static int warningCounter = 0;
	public static int doneCounter = 0;
	
	public static String htmlHead = "";
	public static String resSummaryHtmlTable = "";
	public static String resDetailHtmlTable = "";
	public final static String htmlTail = "</Table></body></html>";
	
	public static boolean createResMatrix;
	public static String resMatrixHead = "";
	public static String resMatrixTable = "";
	public static String resMatrixFile;
	
	
	//public static String brType = "IE";
	public static String brType;
		
	public static String chrmDrvPath;
	public static String ieDrvPath;
		
	public static Map scriptDict = new HashMap();
	public static Map pathDict = new HashMap();
	public static Map runTimeDataRepository = new HashMap();
	
	public static String url = "";
	
	public static void addToDictionary(String dKey, String dItem, Map dictToWrite){
		dictToWrite.put(dKey, dItem);
	}
	
	public static String readDictionary(String dKey, Map dictToRead){
		return (String) dictToRead.get(dKey);
	}
	
	public static String getSttsAbbr(){
		String status = "";
		
		switch(stepStts){
		case "P":
			status = "Passed";
			break;
		case "F":
			status = "Failed";
			break;
		case "D":
			status = "Done";
			break;
		case "W":
			status = "Warning";
			break;
		}
		return status;
		
	}
	
	public static boolean isNumeric(String s) {  
	    return s.matches("[-+]?\\d*\\.?\\d+");  
	} 
	
	public static void buildResultPath() throws IOException {
		
		String pathArr[];
		//Global.fullScriptPath="C:\\IAE_testWorkSpace\\Junit_HybridFW\\FWSupport\\Release_05\\App_Ref_Data\\TestApps\\MercuryTours\\MercuryTours_login";
		String newPath = Global.fullScriptPath.replace("\\", "/");
		System.out.println("new replaced path: " + newPath);
		
		pathArr = newPath.split("/");
		System.out.println("pathArr "+pathArr[0]);
	//System.out.println("reslut path array: "+pathArr[0]+" "+pathArr[1]+" "+pathArr[2]+" "+pathArr[3]+" "+pathArr[4]+" "+pathArr[5]+" "+pathArr[6]);
		// build result path string
//		Global.resultPath = pathArr[0] + "\\\\" + pathArr[1] + "\\\\" + pathArr[2] + "\\\\" + pathArr[3] + "\\\\Results\\\\" + pathArr[4] + "\\\\" +
//				pathArr[5] + "\\\\";
		Global.resultPath = pathArr[0] + "\\\\" + pathArr[1] + "\\\\" + pathArr[2] + "\\\\" + pathArr[3] +"\\\\" + pathArr[4] + "\\\\Results\\\\" + pathArr[5] + "\\\\" +
				pathArr[6] + "\\\\" + "\\\\" + pathArr[7] + "\\\\";
		//System.out.println("result path: "+Global.resultPath);
		File theDir = new File(Global.resultPath);

		if (!theDir.exists()) {
		    System.out.println("creating directory: " + Global.resultPath);

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
	
	public static void buildImagePath(){
		Global.curImagePath = Global.resultPath + "\\Images\\";
		
	}
	
	public static String getStamp(String frmt) {
		Date tDate = new Date();
		SimpleDateFormat dFormat = new SimpleDateFormat(frmt);
		String newTime = dFormat.format(tDate);
		return newTime;
	}
	
	public static void FWSetup()  throws IOException{
		
		if(FileChecker.fileCheck(globalConfigPath)){
			//System.out.println("Global Config file found.");

			Global.ieDrvPath = getPropery("ie_Driver", Global.globalConfigPath);
			Global.chrmDrvPath = getPropery("Chrome_Driver", Global.globalConfigPath);
			//System.out.println(Global.chrmDrvPath);
			//System.out.println(Global.ieDrvPath);
		} else{
			status = false;
			System.out.println("Global Config file does NOT exist. Make sure the path is correct or file - glblConfig.properties exists in the path.");
		}
		
		if (status && FileChecker.fileCheck(localConfigPath)){
			Global.projName = getPropery("Project_Name", Global.localConfigPath);
			//System.out.println("Current Project name: " + projName);
			Global.ORpath = FWRoot + "OR\\OR.properties";
			Global.projBatchPath = FWRoot + "Config\\Script_List.xls";
			Global.projConfigPath = FWRoot + "Config\\IConfig.properties";
			Global.brType = getPropery("Browser_Type", Global.localConfigPath);
			Global.url = getPropery("url", Global.localConfigPath);
			String LorG = getPropery("use_local_batch", Global.localConfigPath);
			
			createResMatrix = Boolean.parseBoolean(getPropery("create_TRR", Global.localConfigPath));
			continueOnTestFail = Boolean.parseBoolean(getPropery("continue_on_test_fail", Global.localConfigPath));
			//System.out.println("URL: " + Global.url);
			//System.out.println("Browser Type: " + Global.brType);
			boolean TorF = Boolean.parseBoolean(String.valueOf(LorG));
			if(TorF){
				scriptListPath = localBatchPath;
			} else {
				scriptListPath = projBatchPath;
			}
		
		} else {
			System.out.println("Local Config file is missing in the locatin: '" + localConfigPath + "\nMakesure path is corect or config file exists.");
			status = false;
		}

	}
	
	
	public static String getPropery(String prKey, String fPath){
		File pFile = new File(fPath);
		FileInputStream fi = null;
		String pVal = "";
		//System.out.println("prKey: " + prKey);
		try{
			fi = new FileInputStream(pFile);
			
		} catch(FileNotFoundException e){
			System.out.println("Properties file \"" + fPath + "\" couldn't be found! Exception occured: " + e);
			e.printStackTrace();
		}
		
		Properties OR = new Properties();
		
		try{
			
			OR.load(fi);
			if (OR.containsKey(prKey)){
				pVal = OR.getProperty(prKey);
			} else{
				System.out.println("\nProperty key " + prKey + " does NOT exist in the file.\n");
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

	public static String getObjecClass(String cls){
		String ObjClass = "";
		
		switch(cls){
			case "edt" :
				ObjClass = "Edit";
				break;
			case "img" :
				ObjClass = "Image";
				break;
			case "btn" :
				ObjClass = "Button";
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
				break;
			case "tbl" :
				ObjClass = "Table";
				break;
			case "chk" :
				ObjClass = "Checkbox";
				break;
			case "win" :
				ObjClass = "Window";
				break;
			default:
				ObjClass = "Unknow Object Type";
		}
		
		
		return ObjClass;
	}
	private static String getFWRoot(){
		//printInfo();
	 String rootPath = System.getProperty("user.dir");
//		//System.out.println("Project root path: "  + rootPath);		
		String scriptRootPath = rootPath.replace("Junit_HybridFW", "");
		scriptRootPath = scriptRootPath.replace("\\", "\\\\");
		System.out.println("Script root path: "  + scriptRootPath);
		return scriptRootPath;
		
	}
}