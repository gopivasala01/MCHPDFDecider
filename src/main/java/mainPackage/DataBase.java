package mainPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase 
{
	
			
			

	public static boolean getBuildingsList(String pendingLeasesQuery) {
	    try {
	        Connection con = null;
	        Statement stmt = null;
	        ResultSet rs = null;
	        con = DriverManager.getConnection(AppConfig.connectionUrl);

	       // String SQL = "SELECT ID, LeaseEntityID, BuildingEntityID, Company, buildingabbreviation, LeaseName,PDFFormat,AutomationStatus, Note FROM Automation.LeasePdfDecider where PortfolioAbbreviation like '%MCH%' and (AutomationStatus = 'Failed' Or AutomationStatus is Null)";		   		        

	        String SQL = "SELECT ID, LeaseEntityID, BuildingEntityID, Company, buildingabbreviation, LeaseName,PortfolioAbbreviation,PDFFormat, AutomationStatus, Note FROM Automation.LeasePdfDecider where PortfolioAbbreviation like '%MCH%' and AutomationStatus = 'Failed' And Note ='Building Not Found'";	   	

	        stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	        rs = stmt.executeQuery(SQL);
	        
	        int rows = 0;
	        if (rs.last()) {
	            rows = rs.getRow();
	            rs.beforeFirst();
	        }
	        System.out.println("No of Rows = " + rows);
	        
	        RunnerClass.pendingLeases = new String[rows][6]; // Updated array dimensions
	        
	        int i = 0;
	        while (rs.next()) {
	            int ID = rs.getInt("ID");
	            String IDString = Integer.toString(ID);
	            
	            long leaseEntityIDLong = rs.getLong("LeaseEntityID");
	            String leaseEntityID = Long.toString(leaseEntityIDLong);
	            
	            long buildingEntityIDLong = rs.getLong("BuildingEntityID");
	            String buildingEntityID = Long.toString(buildingEntityIDLong);
	            
	            String Company = rs.getString("Company");
	            String buildingAbbreviation = rs.getString("buildingabbreviation");
	            String leaseName = rs.getString("LeaseName");
	            
	            RunnerClass.pendingLeases[i][0] = IDString;
	            RunnerClass.pendingLeases[i][1] = leaseEntityID;
	            RunnerClass.pendingLeases[i][2] = buildingEntityID;
	            RunnerClass.pendingLeases[i][3] = Company;
	            RunnerClass.pendingLeases[i][4] = buildingAbbreviation;
	            RunnerClass.pendingLeases[i][5] = leaseName;
	            
	            i++;
	        }
	        
	        rs.close();
	        stmt.close();
	        con.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	    return true;
	}

	    
	
	public static void updateTable(String query)
	 {
		    try (Connection conn = DriverManager.getConnection(AppConfig.connectionUrl);
		        Statement stmt = conn.createStatement();) 
		    {
		      stmt.executeUpdate(query);
		      System.out.println("Record Updated");
		      stmt.close();
	            conn.close();
		    } catch (SQLException e) 
		    {
		      e.printStackTrace();
		    }
	 }
}
