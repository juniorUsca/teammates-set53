package teammates.test.cases.ui.browsertests;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;
import teammates.common.datatransfer.DataBundle;
import teammates.common.util.Const;
import teammates.common.util.Url;
import teammates.test.driver.BackDoor;
import teammates.test.driver.TestProperties;
import teammates.test.pageobjects.AppPage;
import teammates.test.pageobjects.Browser;
import teammates.test.pageobjects.BrowserPool;
import teammates.test.pageobjects.HomePage;
import teammates.test.pageobjects.LoginPage;
import teammates.test.pageobjects.StudentHelpPage;
import teammates.test.pageobjects.StudentHomePage;
import teammates.test.pageobjects.AppPage;

/**
 * Covers Homepage and Login page for students. Some part of it is using a 
 * real Google account alice.tmms. <br> 
 * SUT: {@link StudentHelpPage} and {@link LoginPage} for students.
 */
public class StudentHomePageUiTest extends BaseUiTestCase{
    private static Browser browser;
    private static DataBundle testData;
    private StudentHomePage studentHome;
    
    
    @BeforeClass
    public static void classSetup() throws Exception {
        printTestClassHeader();
        testData = loadDataBundle("/StudentHomePageUiTest.json");
        removeAndRestoreTestDataOnServer(testData);

        browser = BrowserPool.getBrowser(true);
    }


    @Test    
    public void show_recent_only() throws Exception{
        testContentAndLogin();        
    }


    private void testContentAndLogin() throws Exception {
        
        ______TS("content: no courses, 'welcome stranger' message");
        
        AppPage.logout(browser);
        Url homeUrl = createUrl(Const.ActionURIs.STUDENT_HOME_PAGE)
                .withUserId("jatch21instructor");
        
        
    studentHome = loginAdminToPage(browser, homeUrl, StudentHomePage.class);
    browser.driver.findElement(By.id("displayArchivedData_check")).click();
    Thread.sleep(3000);
    AppPage.logout(browser);
    
    
    }

    @AfterClass
    public static void classTearDown() throws Exception {
        BrowserPool.release(browser);
    }
}
