package mainPackage;

public class AppConfig 
{
	
		 public static boolean saveButtonOnAndOff= true;
			
		   public static String URL ="https://app.propertyware.com/pw/login.jsp";
		   public static String username ="mds0418@gmail.com";
		   public static String password ="KRm#V39fecMDGg#";
		   
		   public static String excelFileLocation = "C:\\SantoshMurthyP\\MCH Pdf\\ExcelFile";
		   public static String downloadFilePath = "C:\\SantoshMurthyP\\MCH Pdf";
		   //Mail credentials
		   public static String fromEmail = "bireports@beetlerim.com";
		   public static String fromEmailPassword = "Welcome@123";
		   
		   public static  String PDFFormatConfirmationText = "The parties to this lease are:";
			public static  String PDFFormat2ConfirmationText = "THIS RESIDENTIAL LEASE AGREEMENT";
		   
		   public static String toEmail = "santosh.p@beetlerim.com";
		   public static String CCEmail = "santosh.p@beetlerim.com";
		   
		   public static String mailSubject = "Lease PDF Decider";
		   

		  // public static String pendingLeasesQuery =" SELECT ID, LeaseEntityID, BuildingEntityID, Company, buildingabbreviation, LeaseName,PortfolioAbbreviation FROM Automation.LeasePdfDecider where PortfolioAbbreviation like '%MCH%' and (Status is null or Status ='Failed')";		   	

			   public static String pendingLeasesQuery =" SELECT ID, LeaseEntityID, BuildingEntityID, Company, buildingabbreviation, LeaseName,PortfolioAbbreviation FROM Automation.LeasePdfDecider where PortfolioAbbreviation like '%MCH%' and (AutomationStatus is null )";	   	

		   	

		   public static String formatQuery =" SELECT ID, LeaseEntityID, BuildingEntityID, Company, buildingabbreviation, LeaseName,PortfolioAbbreviation,AutomationStatus,PdfFormat FROM Automation.LeasePdfDecider where PortfolioAbbreviation like '%MCH%'";
		   

		   public static String failedLeasesQuery =" SELECT ID, LeaseEntityID,  Company, buildingabbreviation, LeaseName,PortfolioAbbreviation,AutomationStatus,PdfFormat FROM Automation.LeasePdfDecider where PortfolioAbbreviation like '%MCH%'\r\n"
		   		+ "";
		   public static String[] LeaseAgreementFileNames = {"REVISED_Lease_","Lease_","Leases_"};
		   
		   public static String connectionUrl = "jdbc:sqlserver://azrsrv001.database.windows.net;databaseName=HomeRiverDB;user=service_sql02;password=xzqcoK7T;encrypt=true;trustServerCertificate=true;";
		   
		   public static String[] companyNames = {"Alabama","Arizona","Arkansas","Austin","Boise","California","California pfw","Chattanooga","Chicago","Chicago pfw","Colorado Springs","Dallas/Fort Worth","Delaware","Florida","Hawaii","Georgia","Houston","Idaho Falls","Indiana","Institutional Accounts","Kansas City","Lake Havasu","Little Rock","Maine","Maryland","Montana","New Jersey","New Mexico","North Carolina","OKC","Ohio","Pennsylvania","Saint Louis","San Antonio","Savannah","South Carolina","Spokane","Tennessee","Tulsa","Utah","Virginia","Washington DC"};

		   public static String buildingPageURL = "https://app.propertyware.com/pw/properties/building_detail.do?entityID=";
		  // public static String leaseFetchQuery  = "Select Company, Building,leaseName from Automation.InitialRentsUpdate where Status ='Pending' and Company ='Georgia'";
		  
		   public static String[] IAGClientList = {"510","AVE","BTH","CAP","FOR","HRG","HS","MAN","MCH","OFF","PIN","RF","SFR3","TH","HH","Lofty.Ai"};


}
