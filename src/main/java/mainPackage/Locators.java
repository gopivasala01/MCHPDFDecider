package mainPackage;

import org.openqa.selenium.By;

public class Locators 
{
	public static By userName = By.id("loginEmail");
	public static By password = By.name("password");
	public static By signMeIn = By.xpath("//*[@value='Sign Me In']");
	public static By loginError = By.xpath("//*[@class='toast toast-error']");
	
	public static By marketDropdown = By.id("switchAccountSelect");
	public static By buildingTitle = By.id("summaryTitleBuilding");
	public static By buildingDeactivatedMessage = By.xpath("//*[text()='This Building has been deactivated']");
	public static By leasesTab = By.xpath("//*[@class='tabbedSection']/a[4]");	
    public static By leasesTab2 = By.xpath("(//a[text()='Leases'])[2]");
    public static By tenantContact = By.xpath("//*[@id='buildingLeaseList']/tbody/tr/td[2]/a");
    public static By leaseList = By.xpath("//*[@id='buildingLeaseList']/tbody/tr/td[1]/a");
    public static By popUpAfterClickingLeaseName = By.xpath("//*[@id='viewStickyNoteForm']");
    public static By scheduledMaintanancePopUp = By.xpath("//*[text()='Scheduled Maintenance Notification']");
    public static By scheduledMaintanancePopUpOkButton = By.id("alertDoNotShow");
    public static By popupClose = By.xpath("//*[@id='editStickyBtnDiv']/input[2]");
    public static By permissionDenied = By.xpath("//*[contains(text(),'Permission Denied')]");
    public static By renewalPopup = By.id("viewStickyNoteForm");
    public static By renewalPoupCloseButton = By.xpath("//*[@id='viewStickyNoteForm']/div/div[1]/input[2]");
    public static By checkPortfolioType = By.xpath("//*[@title='Click to jump to portfolio']");
    
    public static By RCDetails = By.xpath("//*[contains(text(),'Resident Coordinator [Name/Phone/Email]')]/following::td[1]/div");
    public static By leaseStartDate_PW = By.xpath("//*[@id='infoTable']/tbody/tr[3]/td[1]");
    public static By leaseEndDate_PW = By.xpath("//*[@id='infoTable']/tbody/tr[3]/td[2]");
   
    public static By notesAndDocs = By.id("notesAndDocuments");
    public static By documentsList = By.xpath("//*[@id='documentHolderBody']/tr/td[1]/a"); 

}
