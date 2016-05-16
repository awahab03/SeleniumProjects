package tests;
import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import global.Global;
import main.StartFW;
import selenium.SelMethods;
public class MercuryToursTest extends StartFW {
	@Before
	public void setUp() throws Exception{
		statusCheck();
		SelMethods.setBrowser();
		
	}
	@After
	public void tearDown(){
		Global.drvr.quit();
	}
//	@Rule
//	public ErrorCollector collector = new ErrorCollector();
	
	@Test
	public void testMercuryLogin() throws Exception{
		String scriptPath="C:\\IAE_testWorkSpace\\Junit_HybridFW\\FWSupport\\Release_05\\App_Ref_Data\\TestApps\\MercuryTours\\MercuryTours_login.xls";
		Global.fullScriptPath=scriptPath;
		createResults("MercuryTours_login2");
		
		File scriptFile=new File(scriptPath);
		ReadTestScript(scriptFile, scriptPath);
		buildResult();
	}
	@Test
	public void testMercuryBook_aFlight() throws Exception{
		String scriptPath="C:\\IAE_testWorkSpace\\Junit_HybridFW\\FWSupport\\Release_05\\App_Ref_Data\\TestApps\\MercuryTours\\Book_aFlight.xls";
		Global.fullScriptPath=scriptPath;
		createResults("MercuryTours_Book_aFlight");
		
		File scriptFile=new File(scriptPath);
		ReadTestScript(scriptFile, scriptPath);
		buildResult();
	}

}
