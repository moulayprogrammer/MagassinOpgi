package Controllers.OutputControllers.DechargeControllers;

import BddPackage.ConnectBD;
import BddPackage.EmployeeOperation;
import Models.*;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.apache.commons.io.FileUtils;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
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

    public Print(Decharge decharge) {
        this.decharge = decharge;
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
                    .append("<h6 >OFFICE DE PROMOTION ET DE GESTION</h6>\n")
                    .append("<h6 style=\"margin-top: -20px;\">IMMOBILIER WILAYA DE TAMANRASSET</h6>\n")
                    .append("<h6 style=\"margin-top: -20px;\">DEPARTEMENT RESSOURCES HUMAINES</h6>\n")
                    .append("<h6 style=\"margin-top: -20px;\">ET MOYENS GÉNÉRAUX</h6>\n")
                    .append("<h6 style=\"margin-top: -20px;\">SERVICE MOYENS GÉNÉRAUX</h6>\n")
                    .append("</div>")

                    .append("<div style=\"text-align: center;\">\n")
                    .append("<h1><u>DECHARGE</u></h1>\n")
                    .append("</div>")

                    .append("<div style=\"margin-bottom: 10px;\">Je soussigné ")
                    .append("<span>")
                    .append(recept.getFirstName())
                    .append(" ")
                    .append(recept.getLastName())
                    .append("</span>")
                    .append("<span>")
                    .append(recept.getFunction())
                    .append("</span>")
                    .append(" atteste avoir reçu de la part : ")
                    .append("<span>")
                    .append(preneur.getFirstName())
                    .append(" ")
                    .append(preneur.getLastName())
                    .append("</span>")
                    .append("<span>")
                    .append(preneur.getFunction())
                    .append("</span> Le <span>")
                    .append(decharge.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                    .append("</span> : </div>")



                    .append("<table class=\"table-art\">\n" )
                    .append("<tr>\n" )
                    .append("<th class=\"th-art\">N°</th>\n" )
                    .append("<th class=\"th-art\">DESIGNATION</th>\n" )
                    .append("<th class=\"th-art\">QUANTITIES</th>\n" )
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

                                    .append("<td class=\"td-art\" style=\"width: 10%\">")
                                    .append(resultSet.getInt("QTE"))
                                    .append("</td>\n" );
                            count[0]++;
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        conn.close();
                    }




            HTMLFacture.append("</table>\n" )
                    .append("<table style=\"width: 100%; margin-left: 50px; margin-top: 100px\">\n" +
                            "    <tr>\n" +
                            "        <td>Signature du receveur</td>\n" +
                            "        <td style=\"text-align: center;\">Signature du donateur</td>\n" +
                            "    </tr>\n" +
                            "</table>");


            HTMLFacture.append("</body>\n" )
                    .append("</html>\n");

            try {
                String pathDocument = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
                String mainDirectoryPath = pathDocument + File.separator + "Magassin Document";
                File mainFile =  new File(mainDirectoryPath);

                if (!mainFile.exists()) FileUtils.forceMkdir(mainFile);

                String outputDirectory = mainDirectoryPath + File.separator + "Bon Sortée" ;
                File invoiceFile = new File(outputDirectory);
                if (!invoiceFile.exists()) FileUtils.forceMkdir(invoiceFile);

                String dayDirectory = outputDirectory + File.separator + "bons_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
                File dayFile = new File(dayDirectory);
                if (!dayFile.exists()) FileUtils.forceMkdir(dayFile);


                if (dayFile.exists()) {

                    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("_HH-mm-ss");

                    String path = dayDirectory + File.separator + "bon_" + LocalDateTime.now().format(myFormatObj) + ".pdf";
                    FileOutputStream file = new FileOutputStream(path);

                    ConverterProperties converterProperties = new ConverterProperties();

                    PdfDocument pdf = new PdfDocument(new PdfWriter(file));

                    HtmlConverter.convertToPdf(HTMLFacture.toString(), pdf, converterProperties);

                    pdf.close();
                    Desktop.getDesktop().open(new File(path));

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
