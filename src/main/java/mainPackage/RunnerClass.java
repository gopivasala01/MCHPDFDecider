package mainPackage;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;

import org.openqa.selenium.Alert;

public class RunnerClass {

    // Static variables for holding information and instances
    public static String[][] pendingLeases;
    public static ChromeDriver driver;
    public static String downloadFilePath;
    public static Actions actions;
    public static JavascriptExecutor js;
    public static WebDriverWait wait;
    public static Alert alert;

    // Other static variables
    public static String ID;
    public static String Company;
    public static String buildingabbreviation;
    public static String LeaseName;
    public static String failedReason = "";
    public static String unitEntityID;
    public static String leaseEntityID;
    public static String address;
    public static String current_Resident_FirstName;
    public static String[][] completedLeasesList;
    public static String portfolioType;
    public static int updateStatus;
    public static String buildingEntityID;

    public static void main(String args[]) {
        try {
            initializeAutomation();
            processPendingLeases();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser or perform any cleanup here
        }
    }

    public static void initializeAutomation() {
        // Get all Pending Leases
        DataBase.getBuildingsList(AppConfig.pendingLeasesQuery);

        // Initialize browser
        PropertyWare.initiateBrowser();

        // Login to PropertyWare
        PropertyWare.signIn();
    }

    public static void processPendingLeases() {
        for (int i = 0; i <pendingLeases.length; i++) //pendingLeases.length
        {
            System.out.println("Lease ---- " + (i + 1));
            try {
                // Initialize lease data
                initializeLeaseData(i);

                // Process different steps for the lease
                if (!PropertyWare.selectBuilding() || //!PropertyWare.selectLease() ||
                    !PropertyWare.downloadLeaseAgreement(LeaseName, leaseEntityID) ||
                    !PDFReader.extractPDFData()) {
                    updateFailedRecord();
                    continue;
                }

                // Decide PDF format
                String pdfFormat = PDFReader.decidePDFFormat();
                if (!pdfFormat.equals("Format1") && !pdfFormat.equals("Format2")) {
                    updateFailedRecord();
                    continue;
                }
                else {
                	String query = "Update Automation.LeasePdfDecider set PdfFormat = '"+pdfFormat+"' , AutomationStatus ='Completed' Where ID= '"+ID+"'" ;
                	DataBase.updateTable(query);
                }

                // Update lease record status
                updateLeaseRecordStatus();
                
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        CreateExcelFileWithProcessedData.createExcelFileWithProcessedData(pendingLeases);
        
    }

    public static void initializeLeaseData(int i) {
        ID = pendingLeases[i][0];
        leaseEntityID = pendingLeases[i][1];
        buildingEntityID = pendingLeases[i][2]; // Assuming the index is correct
        Company = pendingLeases[i][3];
        buildingabbreviation = pendingLeases[i][4];
        LeaseName = pendingLeases[i][5];
        failedReason = "";

        System.out.println(ID + " | " + leaseEntityID + " | " + buildingEntityID + " | " + Company + " | " + LeaseName);
    }

    

    public static void updateFailedRecord() {
        String trimmedFailedReason = failedReason.trim();
        String query = "UPDATE Automation.LeasePdfDecider SET AutomationStatus='Failed', Note='" + trimmedFailedReason + "' WHERE ID = '" + ID + "'";
        DataBase.updateTable(query);
    }

    public static void updateLeaseRecordStatus() {
        String query;
        if (failedReason.isEmpty()) {
            query = "UPDATE Automation.LeasePdfDecider SET AutomationStatus='Completed' WHERE ID = '" + ID + "'";
        } else {
            if (failedReason.charAt(0) == ',') {
                failedReason = failedReason.substring(1);
            }
            query = "UPDATE Automation.LeasePdfDecider SET AutomationStatus='Review', Note='" + failedReason + "' WHERE ID = '" + ID + "'";
        }
        DataBase.updateTable(query);
    }

    public static File getLastModified() throws Exception {
        File directory = new File(AppConfig.downloadFilePath);
        File[] files = directory.listFiles(File::isFile);
        long lastModifiedTime = Long.MIN_VALUE;
        File chosenFile = null;

        if (files != null) {
            for (File file : files) {
                if (file.lastModified() > lastModifiedTime) {
                    chosenFile = file;
                    lastModifiedTime = file.lastModified();
                }
            }
        }

        return chosenFile;
    }
}
