package mainPackage;

import java.io.File;
import java.io.FileInputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFReader {

    public static boolean extractPDFData() {
        try {
            File file = RunnerClass.getLastModified();
            System.out.println(file);

            try (FileInputStream fis = new FileInputStream(file);
                 PDDocument document = PDDocument.load(fis)) {

                PDFTextStripper stripper = new PDFTextStripper();
                stripper.setStartPage(1);
                stripper.setEndPage(1);
                String firstPageText = stripper.getText(document);
                String allPagesText = stripper.getText(document);

                firstPageText = cleanText(firstPageText);
                allPagesText = cleanText(allPagesText);

               System.out.println("First page text:\n" + firstPageText);
               System.out.println("All pages text:\n" + allPagesText);
            }

            return true; // Return true if PDF data extraction was successful
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Return false if there was an error in PDF data extraction
        }
    }

    public static boolean readPDFPerMarket(String market) {
        try {
            String pdfFormatType = decidePDFFormat();
            System.out.println("PDF Format Type = " + pdfFormatType);

            if (pdfFormatType.equals("Format1") || pdfFormatType.equals("Format2")) {
                return extractPDFData();
            } else {
                RunnerClass.failedReason = RunnerClass.failedReason + ", Wrong PDF Format";
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String decidePDFFormat() {
        String format1Text = AppConfig.PDFFormatConfirmationText;
        String format2Text = AppConfig.PDFFormat2ConfirmationText;

        try {
            File file = RunnerClass.getLastModified();
            System.out.println(file);

            try (FileInputStream fis = new FileInputStream(file);
                 PDDocument document = PDDocument.load(fis)) {

                String text = new PDFTextStripper().getText(document);
                text = cleanText(text);

                if (text.contains(format1Text)) {
                    System.out.println("PDF Format Type = " + format1Text);
                    return "Format1";
                } else if (text.contains(format2Text)) {
                    System.out.println("PDF Format Type = " + format2Text);
                    return "Format2";
                } else {
                    return "Error";
                }
            }
        } catch (Exception e) {
            return "Error";
        }
    }

    private static String cleanText(String text) {
        return text.replaceAll(System.lineSeparator(), " ")
                   .replaceAll(" +", " ");
    }
}
