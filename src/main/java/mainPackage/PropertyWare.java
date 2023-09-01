package mainPackage;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class PropertyWare {
    public static boolean initiateBrowser() {
        try {
            RunnerClass.downloadFilePath = AppConfig.downloadFilePath;
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("download.default_directory", RunnerClass.downloadFilePath);
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("prefs", prefs);
            options.addArguments("--remote-allow-origins=*");
            WebDriverManager.chromedriver().setup();
            RunnerClass.driver = new ChromeDriver(options);
            RunnerClass.driver.manage().window().maximize();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean signIn() {
        try {
            RunnerClass.driver.get(AppConfig.URL);
            RunnerClass.driver.findElement(Locators.userName).sendKeys(AppConfig.username); 
            RunnerClass.driver.findElement(Locators.password).sendKeys(AppConfig.password);
            Thread.sleep(2000);
            RunnerClass.driver.findElement(Locators.signMeIn).click();
            Thread.sleep(3000);
            RunnerClass.actions = new Actions(RunnerClass.driver);
            RunnerClass.js = (JavascriptExecutor) RunnerClass.driver;
            RunnerClass.driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(2));
            
            try {
                if (RunnerClass.driver.findElement(Locators.loginError).isDisplayed()) {
                    System.out.println("Login failed");
                    return false;
                }
            } catch (Exception e) {}
            
            RunnerClass.driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
            RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(100));
            return true;
        } catch (Exception e) {
            System.out.println("Login failed");
            return false;
        }
    }
    
    public static boolean selectBuilding()
	{
		//Check company name contains HomeRiver Group Prefix
		if(RunnerClass.Company.toLowerCase().contains("home"))
		{
			for(int i=0;i<AppConfig.companyNames.length;i++)
			{
				if(RunnerClass.Company.contains(AppConfig.companyNames[i]))
				{
					RunnerClass.Company = AppConfig.companyNames[i].trim();
					break;
				}
			}
		}
        
        try {
            RunnerClass.driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
            RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(100));
            RunnerClass.driver.navigate().refresh();
            intermittentPopUp();
            //if (checkIfBuildingIsDeactivated() == true)
                //return false;
            RunnerClass.driver.findElement(Locators.marketDropdown).click();
            String marketName = "HomeRiver Group - " + RunnerClass.Company;
            Select marketDropdownList = new Select(RunnerClass.driver.findElement(Locators.marketDropdown));
            marketDropdownList.selectByVisibleText(marketName);
            Thread.sleep(3000);
            String buildingPageURL = AppConfig.buildingPageURL + RunnerClass.buildingEntityID;
            RunnerClass.driver.navigate().to(buildingPageURL);
            
            if (permissionDeniedPage() == true) {
                System.out.println("Wrong Unit Entity ID");
                RunnerClass.failedReason = "Wrong Unit Entity ID";
                RunnerClass.updateStatus = 1;
                return false;
            }
            
            intermittentPopUp();
            
            if (checkIfBuildingIsDeactivated() == true)
                return false;
            
            return true;
            
        } catch (Exception e) {
            RunnerClass.failedReason = "Building not found";
            System.out.println("Building not found");
            RunnerClass.updateStatus = 1;
            return false;
        }
    }
    
    public static boolean selectLease() {
        try {
            RunnerClass.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(5));
            RunnerClass.js.executeScript("window.scrollBy(0,document.body.scrollHeight)");

            if (RunnerClass.driver.findElement(Locators.leasesTab).getText().equals("Leases"))
                RunnerClass.driver.findElement(Locators.leasesTab).click();
            else
                RunnerClass.driver.findElement(Locators.leasesTab2).click();

            boolean leaseAvailibilityCheck = false;
            List<WebElement> leaseListElements = RunnerClass.driver.findElements(Locators.leaseList);

            for (int i = 0; i < leaseListElements.size(); i++) {
                WebElement leaseListItem = leaseListElements.get(i);
                String leaseListItemText = leaseListItem.getText();

                if (leaseListItemText.trim().contains(RunnerClass.LeaseName.trim())) {
                    leaseListItem.click();
                    intermittentPopUp();
                    leaseAvailibilityCheck = true;
                    break;
                }
            }

            if (!leaseAvailibilityCheck) {
                RunnerClass.failedReason = "Lease not Found";
                RunnerClass.updateStatus = 1;
                return false;
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }
 
    
    public static void intermittentPopUp() {
        try {
            RunnerClass.driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(1));
            
            try {
                if (RunnerClass.driver.findElement(Locators.popUpAfterClickingLeaseName).isDisplayed()) {
                    RunnerClass.driver.findElement(Locators.popupClose).click();
                }
            } catch (Exception e) {}
            
            try {
                if (RunnerClass.driver.findElement(Locators.scheduledMaintanancePopUp).isDisplayed()) {
                    RunnerClass.driver.findElement(Locators.scheduledMaintanancePopUpOkButton).click();
                }
            } catch (Exception e) {}
            
            try {
                if (RunnerClass.driver.findElement(Locators.scheduledMaintanancePopUpOkButton).isDisplayed()) {
                    RunnerClass.driver.findElement(Locators.scheduledMaintanancePopUpOkButton).click();
                }
            } catch (Exception e) {}
            
            RunnerClass.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(5));
        } catch (Exception e) {}
    }
    
    public static boolean checkIfBuildingIsDeactivated() {
        try {
            RunnerClass.driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(1));
            
            try {
                if (RunnerClass.driver.findElement(Locators.buildingDeactivatedMessage).isDisplayed()) {
                    System.out.println("Building is Deactivated");
                    RunnerClass.failedReason = "Building is Deactivated";
                    RunnerClass.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                    RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(5));
                    return true;
                }
            } catch (Exception e) {}
            
            RunnerClass.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(5));
            return false;
        } catch (Exception e) {}
        return false;
    }
    
    public static boolean permissionDeniedPage() {
        try {
            RunnerClass.driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(1));
            
            if (RunnerClass.driver.findElement(Locators.permissionDenied).isDisplayed()) {
                RunnerClass.driver.navigate().back();
                return true;
            }
            
            RunnerClass.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(5));
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    
    public static boolean downloadLeaseAgreement(String LeaseName, String leaseEntityID) throws Exception {
        try {
            RunnerClass.portfolioType = RunnerClass.driver.findElement(Locators.checkPortfolioType).getText();
            System.out.println("Portfolio Type = " + RunnerClass.portfolioType);
            
            int portfolioFlag = 0;
            
            for (int i = 0; i < AppConfig.IAGClientList.length; i++) {
                if (RunnerClass.portfolioType.contains(mainPackage.AppConfig.IAGClientList[i])) {
                    portfolioFlag = 1;
                    break;
                }
            }
            
            if (portfolioFlag == 1)
                RunnerClass.portfolioType = "MCH";
            else
                RunnerClass.portfolioType = "Others";
            
            System.out.println("Portfolio Type = " + RunnerClass.portfolioType);
            
        } catch (Exception e) {}
        
        try {
            RunnerClass.js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
            Thread.sleep(2000);
            RunnerClass.driver.findElement(Locators.leasesTab).click();
            RunnerClass.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(5));
            
            try {
                RunnerClass.driver.findElement(By.partialLinkText(LeaseName.trim())).click();
            } catch (Exception e) {
                System.out.println("Unable to Click Lease Owner Name");
                
                RunnerClass.failedReason = "Unable to Click Lease Onwer Name";
                RunnerClass.updateStatus = 1;
                return false;
            }
            
            try {
                if (RunnerClass.driver.findElement(Locators.renewalPopup).isDisplayed()) {
                    RunnerClass.driver.findElement(Locators.renewalPoupCloseButton).click();
                }
            } catch (Exception e) {}
            
            RunnerClass.driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
            RunnerClass.wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(15));
            RunnerClass.js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
            RunnerClass.driver.findElement(Locators.notesAndDocs).click();
            
            List<WebElement> documents = RunnerClass.driver.findElements(Locators.documentsList);
            boolean checkLeaseAgreementAvailable = false;
            
            for (int i = 0; i < documents.size(); i++) {
                if (documents.get(i).getText().contains("REVISED_Lease_")) {
                    documents.get(i).click();
                    checkLeaseAgreementAvailable = true;
                    break;
                }
            }
            
            if (checkLeaseAgreementAvailable == false) {
                for (int i = 0; i < documents.size(); i++) {
                    if (documents.get(i).getText().startsWith("Lease_") && !documents.get(i).getText().contains("Lease_MOD")) {
                        documents.get(i).click();
                        checkLeaseAgreementAvailable = true;
                        break;
                    }
                }
            }
            
            if (checkLeaseAgreementAvailable == false) {
                for (int i = 0; i < documents.size(); i++) {
                    if (documents.get(i).getText().contains("Lease_") && !documents.get(i).getText().contains("Lease_MOD")) {
                        documents.get(i).click();
                        checkLeaseAgreementAvailable = true;
                        break;
                    }
                }
            }
            
            if (checkLeaseAgreementAvailable == false) {
                for (int i = 0; i < documents.size(); i++) {
                    if (documents.get(i).getText().contains("Lease Agreement")) {
                        documents.get(i).click();
                        checkLeaseAgreementAvailable = true;
                        break;
                    }
                }
            }
            
            if (checkLeaseAgreementAvailable == false) {
                for (int i = 0; i < documents.size(); i++) {
                    if (documents.get(i).getText().startsWith("Full Lease")) {
                        documents.get(i).click();
                        checkLeaseAgreementAvailable = true;
                        break;
                    }
                }
            }
            
            if (checkLeaseAgreementAvailable == false) {
                System.out.println("Unable to download Lease Agreement");
                
                RunnerClass.failedReason = "Unable to download Lease Agreement";
                RunnerClass.updateStatus = 1;
                return false;
            }
            
            Thread.sleep(20000);
            File file = RunnerClass.getLastModified();
            
            FluentWait<WebDriver> wait = new FluentWait<WebDriver>(RunnerClass.driver)
                .withTimeout(Duration.ofSeconds(25))
                .pollingEvery(Duration.ofMillis(100));
            
            wait.until(x -> file.exists());
            Thread.sleep(10000);
            return true;
        } catch (Exception e) {
            System.out.println("Unable to download Lease Agreement");
            
            RunnerClass.failedReason = "Unable to download Lease Agreement";
            RunnerClass.updateStatus = 1;
            return false;
        }
    }
}

