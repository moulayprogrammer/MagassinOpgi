package Controllers.OutputControllers.DechargeControllers;

import BddPackage.ConnectBD;
import BddPackage.EmployeeOperation;
import Models.*;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import javafx.application.Platform;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import javax.print.PrintService;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class Print {

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;
    private EmployeeOperation employeeOperation = new EmployeeOperation();
    private Decharge decharge;
    private Employee preneur;
    private Employee recept;
    private boolean print = false;

    public Print(Decharge decharge, boolean print) {
        this.decharge = decharge;
        this.print = print;
        this.preneur = employeeOperation.get(decharge.getIdEmp());
        this.recept = employeeOperation.get(decharge.getIdEmpDech());
        this.conn = connectBD.connect();
    }

    public void CreatePdfFacture(){
        try {
            final StringBuilder HTMLFacture = new StringBuilder();

            HTMLFacture.append("<!DOCTYPE html>\n")
                    .append("<html>\n")
                    .append("<head>\n")
                    .append("<style>\n")

                    .append("@page {\n")
                    .append("margin: 15mm 10mm 10mm 25mm;\n")
                    .append("size: A4;")
                    .append("}")

                    .append("html {\n")
                    .append("font-family: 'Times New Roman';\n")
                    .append("background-color: white;\n")
                    .append("}\n")

                    .append(".table-art{\n")
                    .append("border: 1px solid black;\n")
                    .append("border-collapse: collapse;\n")
                    .append("min-width: 100%;\n")
                    .append("width: 100%;\n")
                    .append("text-align: center;\n")
                    .append("font-size: 10pt;\n")
                    .append("}\n")

                    .append(".th-art{\n")
                    .append("border: solid black;\n")
                    .append("border-width: 1px ;\n")
                    .append("font-size: large;\n")
                    .append("font-weight: bold;\n")
                    .append("}\n")

                    .append(".td-art{\n")
                    .append("border: solid black;\n")
                    .append("border-width: 1px ;\n")
                    .append("line-height: 20px;")
                    .append("white-space: nowrap;")
                    .append("font-size: medium;\n")
                    .append("}\n")

                    .append("</style>\n")
                    .append("</head>\n")
                    .append("<body>\n")

                    .append("<div style=\"text-align: center;\">\n")
                    .append("<h3>REPEBLIQUE  ALGERIENNE DEMOCRATIQUE ET POPULAIRE</h3>\n")
                    .append("<h3 style=\"margin-top: -15px\">MINISTRE DE L'HABITAT ET DE L'URBANISME</h3>\n")
                    .append("</div>")

                    .append("<div>\n")
                    .append("<h5 >OFFICE DE PROMOTION ET DE GESTION</h5>\n")
                    .append("<h5 style=\"margin-top: -20px;\">IMMOBILIER WILAYA DE TAMANRASSET</h5>\n")
                    .append("<h5 style=\"margin-top: -20px;\">DEPARTEMENT RESSOURCES HUMAINES</h5>\n")
                    .append("<h5 style=\"margin-top: -20px;\">ET MOYENS GÉNÉRAUX</h5>\n")
                    .append("<h5 style=\"margin-top: -20px;\">SERVICE MOYENS GÉNÉRAUX</h5>\n")
                    .append("</div>")

                    .append("<div style=\"text-align: center; margin-top: 70px;\">\n")
                    .append("<h1><u>DECHARGE</u></h1>\n")
                    .append("</div>")

                    .append("<div style=\"margin-bottom: 20px;\">Je soussigné ")
                    .append("<span><b>")
                    .append(recept.getFirstName())
                    .append(" ")
                    .append(recept.getLastName())
                    .append("</b></span>")
                    .append("  <span><b>")
                    .append(recept.getFunction())
                    .append("</b></span>")
                    .append(" atteste avoir reçu de la part : ")
                    .append(" <span><b>")
                    .append(preneur.getFirstName())
                    .append(" ")
                    .append(preneur.getLastName())
                    .append("</b></span>")
                    .append("  <span><b>")
                    .append(preneur.getFunction())
                    .append("</b></span>  en date du <span> <b>")
                    .append(decharge.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                    .append("</b></span> : </div>")



                    .append("<table class=\"table-art\">\n" )
                    .append("<tr>\n" )
                    .append("<th class=\"th-art\">N°</th>\n" )
                    .append("<th class=\"th-art\">DÉSIGNATION</th>\n" )
                    .append("<th class=\"th-art\">QUANTITÉ</th>\n" )
                    .append("<th class=\"th-art\" style=\"width: 30%\">OBSERVATION</th>\n" )
                    .append("</tr>\n" );

                    final int[] count = {1};

                    try {
                        if (conn.isClosed()) conn = connectBD.connect();

                        String query = "SELECT ARTICLE.NAME, COMPONENT_DECHARGE.QTE FROM ARTICLE,COMPONENT_DECHARGE \n" +
                                "WHERE COMPONENT_DECHARGE.ID_DECHARGE = ? AND COMPONENT_DECHARGE.ID_ART = ARTICLE.ID;";
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setInt(1,this.decharge.getId());
                        ResultSet resultSet = preparedStmt.executeQuery();

                        while (resultSet.next()){

                            HTMLFacture.append("<tr>\n" )
                                    .append("<td class=\"td-art\" style=\"width: 3%\">")
                                    .append(count[0])
                                    .append("</td>\n" )

                                    .append("<td class=\"td-art\">")
                                    .append(resultSet.getString("NAME"))
                                    .append("</td>\n" )

                                    .append("<td class=\"td-art\">")
                                    .append(resultSet.getInt("QTE"))
                                    .append("</td>\n" )

                                    .append("<td class=\"td-art\">")
                                    .append(" ")
                                    .append("</td>\n" );
                            count[0]++;
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        conn.close();
                    }




            HTMLFacture.append("</table>\n" )
                    .append("<table style=\"width: 100%; margin-left: 50px; margin-top: 100px\">\n" )
                    .append("<tr>\n" )
                    .append("<td>Récepteur</td>\n" )
                    .append("<td style=\"text-align: center;\">Emetteur</td>\n" )
                    .append("</tr>\n" )
                    .append("</table>");


            HTMLFacture.append("</body>\n")
                    .append("</html>\n");

            try {
                String pathDocument = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
                String mainDirectoryPath = pathDocument + File.separator + "Magassin Document";
                File mainFile =  new File(mainDirectoryPath);

                if (!mainFile.exists()) FileUtils.forceMkdir(mainFile);

                String outputDirectory = mainDirectoryPath + File.separator + "Decharge" ;
                File invoiceFile = new File(outputDirectory);
                if (!invoiceFile.exists()) FileUtils.forceMkdir(invoiceFile);

                String dayDirectory = outputDirectory + File.separator + "dechs_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
                File dayFile = new File(dayDirectory);
                if (!dayFile.exists()) FileUtils.forceMkdir(dayFile);


                if (dayFile.exists()) {

                    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("_HH-mm-ss");

                    String path = dayDirectory + File.separator + "dech_" + LocalDateTime.now().format(myFormatObj) + ".pdf";
                    FileOutputStream file = new FileOutputStream(path);

                    ConverterProperties converterProperties = new ConverterProperties();

                    PdfDocument pdf = new PdfDocument(new PdfWriter(file));

                    HtmlConverter.convertToPdf(HTMLFacture.toString(), pdf, converterProperties);

                    pdf.close();

                    if (print){
                        Platform.runLater(() -> {

                            try {
                                PDDocument document = Loader.loadPDF(new File(path));
                                PrinterJob job = PrinterJob.getPrinterJob();
                                job.setPageable(new PDFPageable(document));

                                if (job.printDialog()) {
                                    job.print();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        });
                    }else {
                        Desktop.getDesktop().open(new File(path));
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
