package mainPackage;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CreateExcelFileWithProcessedData {
	public static void createExcelFileWithProcessedData(String[][] pendingLeases) {
	    try {
	        // Get Today's date in MMddyyyy format
	        LocalDate dateObj = LocalDate.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
	        String date = dateObj.format(formatter);
	        System.out.println(date);
	        String filename = AppConfig.excelFileLocation + "\\PDFFormatDecider" + date + ".xlsx";

	        // Create Excel workbook
	        Workbook wb1 = new XSSFWorkbook();
	        Sheet sheet1 = wb1.createSheet("Sheet 1");

	        // Create headers
	        Row header = sheet1.createRow(0);
	        String[] headerTitles = {"ID", "LeaseEntityID", "BuildingEntityID", "Company", "Building Abbreviation", "LeaseName", "FormatType","AutomationStatus"};
	        for (int i = 0; i < headerTitles.length; i++) {
	            header.createCell(i).setCellValue(headerTitles[i]);
	        }

	        Connection con = null;
	        Statement stmt = null;
	        ResultSet rs = null;
	        con = DriverManager.getConnection(AppConfig.connectionUrl);
	        String SQL = "SELECT ID, LeaseEntityID, BuildingEntityID, Company, buildingabbreviation, LeaseName, PortfolioAbbreviation, AutomationStatus, PdfFormat FROM Automation.LeasePdfDecider WHERE PortfolioAbbreviation LIKE '%MCH%'";
	        stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	        rs = stmt.executeQuery(SQL);

	        // Loop through query results and add data to rows
	        int rowIndex = 1; // Start from the second row (index 1) since the first row is for headers
	        while (rs.next()) {
	            // Extract data from the ResultSet
	            String ID = rs.getString("ID");
	            String leaseEntityID = rs.getString("LeaseEntityID");
	            String buildingEntityID = rs.getString("BuildingEntityID");
	            String Company = rs.getString("Company");
	            String buildingAbbreviation = rs.getString("buildingabbreviation");
	            String LeaseName = rs.getString("LeaseName");
	            String AutomationStatus = rs.getString("AutomationStatus");
	            String PdfFormat = rs.getString("PdfFormat");

	            // Create a new row in the Excel sheet
	            Row row = sheet1.createRow(rowIndex);
	            String[] rowData = {ID, leaseEntityID, buildingEntityID, Company, buildingAbbreviation, LeaseName, PdfFormat,AutomationStatus};
	            for (int j = 0; j < rowData.length; j++) {
	                row.createCell(j).setCellValue(rowData[j]);
	            }

	            rowIndex++;
	        }

	        // Save Excel file
	        try (FileOutputStream fileOut = new FileOutputStream(filename)) {
	            wb1.write(fileOut);
	        }
	        System.out.println("Excel file has been generated successfully.");
	        sendFileToMail(filename);
	        // Send Excel file via email
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


    private static void sendFileToMail(String filename) {
        // Assuming you are sending email through relay.jangosmtp.net
        String host = "smtpout.asia.secureserver.net";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "80");

        // Get the Session object.
        Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(AppConfig.fromEmail, AppConfig.fromEmailPassword);
                }
            });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(AppConfig.fromEmail));

            // Set To: header field of the header.
            InternetAddress[] toAddresses = InternetAddress.parse(AppConfig.toEmail);
            message.setRecipients(Message.RecipientType.TO, toAddresses);

            // Set CC: header field of the header.
            InternetAddress[] CCAddresses = InternetAddress.parse(AppConfig.CCEmail);
            message.setRecipients(Message.RecipientType.CC, CCAddresses);

            // Set Subject: header field
            String subject = "FormatDecider - Automation Process stopped due to site down";
            message.setSubject(subject);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Set the actual message content
            String messageInBody = "Hi All,\n PropertyWare is down, please review and start the process once it is up\n\n Regards,\n HomeRiver Group.";
            messageBodyPart.setText(messageInBody);

            // Create a multipart message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Add the parts to the message
            message.setContent(multipart);

            // Send the message
            Transport.send(message);

           // System.out.println("Sent message successfully....");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    
   
}
