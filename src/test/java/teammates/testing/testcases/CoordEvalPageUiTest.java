package teammates.testing.testcases;

import static org.junit.Assert.fail;

import java.io.FileNotFoundException;

import org.json.JSONException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;

import teammates.api.Common;
import teammates.datatransfer.CoordData;
import teammates.datatransfer.CourseData;
import teammates.datatransfer.EvaluationData;
import teammates.exception.NoAlertAppearException;
import teammates.testing.config.Config;
import teammates.testing.lib.BrowserInstance;
import teammates.testing.lib.BrowserInstancePool;
import teammates.testing.lib.TMAPI;
import teammates.testing.script.ImportTestData;

/**
 * Tests coordEval.jsp from UI functionality and HTML test
 * @author Aldrian Obaja
 *
 */
public class CoordEvalPageUiTest extends BaseTestCase {
	private static BrowserInstance bi;
	private static TestScenario ts;
	
	private static String appUrl = Config.inst().TEAMMATES_URL.replaceAll("/(?=$)","");
	
	@BeforeClass
	public static void classSetup() throws Exception {
		printTestClassHeader("CoordEvalAddUITest");
		ts = loadTestScenario();
		bi = BrowserInstancePool.getBrowserInstance();
		
		System.out.println("Recreating "+ts.coordinator.id);
		long start = System.currentTimeMillis();
		TMAPI.deleteCoord(ts.coordinator.id);
		TMAPI.createCoord(ts.coordinator);
		System.out.println("Finished recreating in "+(System.currentTimeMillis()-start)+" ms");
		
		bi.loginCoord(ts.coordinator.id, Config.inst().TEAMMATES_APP_PASSWD);
		bi.goToUrl(appUrl+Common.JSP_COORD_EVAL);
	}
	
	@AfterClass
	public static void classTearDown() throws Exception {
		BrowserInstancePool.release(bi);
		printTestClassFooter("CoordEvalAddUITest");
	}

	@Test
	public void testCoordEvalPage() throws Exception{
		testCoordEvalHTML();
		testCoordEvalUiPaths();
//		testCoordEvalLinks();
	}

	public void testCoordEvalHTML() throws Exception{
		bi.printCurrentPage(Common.TEST_PAGES_FOLDER+"/coordEvalEmptyNew.html");
		bi.verifyCurrentPageHTML(Common.TEST_PAGES_FOLDER+"/coordEvalEmptyNew.html");

		ImportTestData.main(new String[]{});
		bi.goToEvaluation();

		bi.printCurrentPage(Common.TEST_PAGES_FOLDER+"/coordEvalByIdNew.html");
		bi.verifyCurrentPageHTML(Common.TEST_PAGES_FOLDER+"/coordEvalByIdNew.html");

		bi.click(By.id("button_sortname"));
		bi.printCurrentPage(Common.TEST_PAGES_FOLDER+"/coordEvalByNameNew.html");
		bi.verifyCurrentPageHTML(Common.TEST_PAGES_FOLDER+"/coordEvalByNameNew.html");
		
		bi.click(By.id("button_sortcourseid"));
		bi.verifyCurrentPageHTML(Common.TEST_PAGES_FOLDER+"/coordEvalByIdNew.html");
	}

	public void testCoordEvalUiPaths() throws Exception{
		bi.printCurrentPage(Common.TEST_PAGES_FOLDER+"/coordEvalDupNameFailed.html");
		
		long start = System.currentTimeMillis();
		bi.verifyCurrentPageHTML(Common.TEST_PAGES_FOLDER+"/coordEvalDupNameFailed.html");
		System.out.println("Time to assert a page: "+(System.currentTimeMillis()-start)+" ms");
	}

	public void testCoordEvalLinks() throws Exception{

		bi.verifyCurrentPageHTMLRegex(Common.TEST_PAGES_FOLDER+"/coordCourseAddDeleteInit.html");
		
//		// Check delete link
		By deleteLinkLocator = bi.getCoordEvaluationDeleteLinkLocator(0);
		try{
			bi.clickAndCancel(deleteLinkLocator);
			bi.verifyCurrentPageHTMLRegex(Common.TEST_PAGES_FOLDER+"/coordCourseAddDeleteInit.html");
		} catch (NoAlertAppearException e){
			fail("No alert box when clicking delete button at course page.");
		}

		try{
			bi.clickAndConfirm(deleteLinkLocator);
			bi.verifyCurrentPageHTMLRegex(Common.TEST_PAGES_FOLDER+"/coordCourseAddDeleteSuccessful.html");
		} catch (NoAlertAppearException e){
			fail("No alert box when clicking delete button at course page.");
		}
	}
	
	private static TestScenario loadTestScenario() throws JSONException, FileNotFoundException {
		String testScenarioJsonFile = Common.TEST_DATA_FOLDER + "/CoordEvalUiTest.json";
		String jsonString = Common.readFile(testScenarioJsonFile);
		TestScenario scn = Common.getTeammatesGson().fromJson(jsonString, TestScenario.class);
		return scn;
	}

	private class TestScenario{
		public CoordData coordinator;
		public CourseData course;
		public EvaluationData evaluation;
		public EvaluationData evaluationInCourseWithNoTeams;
	}
}