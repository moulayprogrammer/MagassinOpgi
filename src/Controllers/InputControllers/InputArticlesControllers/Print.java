package Controllers.InputControllers.InputArticlesControllers;

import BddPackage.ConnectBD;
import Models.ComponentOutput;
import Models.Input;
import Models.Output;
import Models.StoreCard;
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
    private Input input;

    public Print(Input input) {
        this.input = input;
        this.conn = connectBD.connect();
    }


    public void CreatePdfFacture(){
        try {
            final StringBuilder HTMLFacture = new StringBuilder();

            HTMLFacture.append("<!DOCTYPE html>\n")
                    .append("<html>\n" )
                    .append("<head>\n" )
                    .append("<style>\n" )

                    .append("@page {\n" )
                    .append("margin: 15mm 10mm 10mm 25mm;\n" )
                    .append("size: A4 landscape;" )
                    .append("}" )

                    .append("html {\n" )
                    .append("font-family: 'Times New Roman';\n" )
                    .append("background-color: white;\n" )
                    .append("}\n")

                    .append(".table-art{\n" )
                    .append("border: 1px solid black;\n" )
                    .append("border-collapse: collapse;\n" )
                    .append("min-width: 100%;\n" )
                    .append("width: 100%;\n" )
                    .append("text-align: center;\n" )
                    .append("font-size: 10pt;\n" )
                    .append("}\n")

                    .append(".th-art{\n" )
                    .append("border: solid black;\n" )
                    .append("border-width: 1px ;\n" )
                    .append("font-size: medium;\n" )
                    .append("font-weight: bold;\n" )
                    .append("}\n" )

                    .append(".td-art{\n" )
                    .append("border: solid black;\n" )
                    .append("border-width: 1px ;\n" )
                    .append("line-height: 20px;")
                    .append("font-size: medium;\n" )
                    .append("}\n" )

                    .append("</style>\n" )
                    .append("</head>\n" )
                    .append("<body>\n" )

                    .append("<table style=\"width : 100%\">" )
                    .append("<tr>\n" )
                    .append("<td style=\"width: 36%; font-size: large;\">" )
                    .append("Office de Promotion et de Gestion" )
                    .append("</td>\n" )
                    .append("<td style=\"width: 45%; font-size: large;\">" )
                    .append("République Algérienne Démocratique et Populaire" )
                    .append("</td>\n" )
                    .append("<td style=\"width: 6%; font-size: large;\">" )
                    .append("DATE :\n" )
                    .append("</td>\n" )
                    .append("<td style=\"width: 10%; font-size: large;\">\n" )
                    .append(input.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                    .append("</td>\n" )
                    .append("</tr>\n" )
                    .append("</table>\n" )

                    .append("<table style=\"width : 100%\">\n" )
                    .append("<tr>\n" )
                    .append("<td style=\"width: 35%; font-size: large;\">\n" )
                    .append("Immobiliére Wilaya de TAMANRASSET" )
                    .append("</td>\n" )
                    .append("<td style=\"width: 44%; font-size: large;\">\n" )
                    .append("Ministère de l'habitat de l'urbanisme et de la ville" )
                    .append("</td>\n" )
                    .append("<td style=\"width: 4%; font-size: large;\">\n" )
                    .append("N° :\n" )
                    .append("</td>\n" )
                    .append("<td style=\"width: 10%; font-size: large;\">\n" )
                    .append(input.getNumber())
                    .append("</td>\n" )
                    .append("</tr>\n" )
                    .append("</table>\n" )

                    .append("<div style=\"text-align: center; margin-top: 40px; margin-bottom: 20px; margin-left: 30px\">" )
                    .append("<h2><u> Bon De Réception </u> </h2>\n" )
                    .append("</div>\n" )

                    .append("<table class=\"table-art\">\n" )
                    .append("<tr>\n" )
                    .append("<th rowspan=\"2\" class=\"th-art\" style=\"height: 23px;\">DESIGNATION</th>\n" )
                    .append("<th rowspan=\"2\" class=\"th-art\">Unité de Mesure</th>\n" )

                    .append("<th rowspan=\"2\" class=\"th-art\">Quantité</th>\n" )
                    .append("<th rowspan=\"2\" class=\"th-art\">P.U.H %</th>\n" )
                    .append("<th rowspan=\"2\" class=\"th-art\">Total</th>\n" )
                    .append("<th colspan=\"3\" class=\"th-art\">Provenance</th>\n" )
                    .append("<th colspan=\"2\" class=\"th-art\">Bon de Commande</th>\n" )
                    .append("</tr>\n" )
                    .append("<tr>\n" )
                    .append("<th class=\"th-art\">Nom de Fournisseur</th>\n" )
                    .append("<th class=\"th-art\">N° de Facture</th>\n" )
                    .append("<th class=\"th-art\">Date</th>\n" )
                    .append("<th class=\"th-art\">N°</th>\n" )
                    .append("<th class=\"th-art\">Date</th>\n" )
                    .append("</tr>\n" );

                    final int[] count = {1};

                    try {
                        if (conn.isClosed()) conn = connectBD.connect();

                        String query = "SELECT ARTICLE.NAME, UNIT.NAME AS UNIT_NAME, COMPONENT_INPUT.QTE, COMPONENT_INPUT.PRICE, \n" +
                                "PROVIDER.NAME AS PROVIDER_NAME, INPUT.NUMBER_FACTUR, INPUT.DATE_FACTUR, INPUT.NUMBER_BC, \n" +
                                "INPUT.DATE_BC FROM ARTICLE, UNIT, COMPONENT_INPUT, PROVIDER, INPUT WHERE INPUT.ID = ? \n" +
                                "AND INPUT.ID_PROVIDER = PROVIDER.ID AND COMPONENT_INPUT.ID_INPUT = INPUT.ID AND \n" +
                                "COMPONENT_INPUT.ID_ARTICLE = ARTICLE.ID AND ARTICLE.ID_UNIT = UNIT.ID; ";
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setInt(1,this.input.getId());
                        ResultSet resultSet = preparedStmt.executeQuery();

                        while (resultSet.next()){

                            double pr =  resultSet.getDouble("PRICE");
                            int qte =  resultSet.getInt("QTE");

                            HTMLFacture.append("<tr>\n" )
                                    .append("<td class=\"td-art\" style=\"height: 23px;\">")
                                    .append(resultSet.getString("NAME"))
                                    .append("</td>\n" )

                                    .append("<td class=\"td-art\">")
                                    .append(resultSet.getString("UNIT_NAME"))
                                    .append("</td>\n" )

                                    .append("<td class=\"td-art\">")
                                    .append(qte)
                                    .append("</td>\n" )

                                    .append("<td class=\"td-art\">")
                                    .append(String.format(Locale.FRANCE, "%,.2f", pr))
                                    .append("</td>\n" )

                                    .append("<td class=\"td-art\">")
                                    .append(String.format(Locale.FRANCE, "%,.2f", (pr* qte)))
                                    .append("</td>\n" )

                                    .append("<td class=\"td-art\">")
                                    .append(resultSet.getString("PROVIDER_NAME"))
                                    .append("</td>\n" )

                                    .append("<td class=\"td-art\">")
                                    .append(resultSet.getString("NUMBER_FACTUR"))
                                    .append("</td>\n" )

                                    .append("<td class=\"td-art\">")
                                    .append(resultSet.getDate("DATE_FACTUR").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                                    .append("</td>\n" )

                                    .append("<td class=\"td-art\">")
                                    .append(resultSet.getString("NUMBER_BC"))
                                    .append("</td>\n" )

                                    .append("<td class=\"td-art\">")
                                    .append(resultSet.getDate("DATE_BC").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                                    .append("</td>\n" )
                            ;
                            count[0]++;
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        conn.close();
                    }

                    int rest = 15 - count[0];

                    for (int i = 0; i < rest + 1; i++) {
                        HTMLFacture.append("<tr>\n" )
                                .append("<td class=\"td-art\" style=\"height: 23px;\">")
                                .append("   ")
                                .append("</td>\n" )

                                .append("<td class=\"td-art\">")
                                .append(" ")
                                .append("</td>\n" )

                                .append("<td class=\"td-art\">")
                                .append(" ")
                                .append("</td>\n" )

                                .append("<td class=\"td-art\">")
                                .append(" ")
                                .append("</td>\n" )

                                .append("<td class=\"td-art\">")
                                .append(" ")
                                .append("</td>\n" )

                                .append("<td class=\"td-art\">")
                                .append(" ")
                                .append("</td>\n" )

                                .append("<td class=\"td-art\">")
                                .append(" ")
                                .append("</td>\n" )

                                .append("<td class=\"td-art\">")
                                .append(" ")
                                .append("</td>\n" )

                                .append("<td class=\"td-art\">")
                                .append(" ")
                                .append("</td>\n" )

                                .append("<td class=\"td-art\">")
                                .append(" ")
                                .append("</td>\n" );

                    }

                    HTMLFacture.append("<div style=\"position: relative; position: absolute; bottom: 100px; width: 100%;\">\n" +
                            "      <table style=\"width : 100%; margin-left: 30px\">\n" +
                            "        <tr>\n" +
                            "          <td style=\"width: 40%; font-size: large; font-weight: bold;\">\n" +
                            "            Le Magasinier\n" +
                            "          </td>\n" +
                            "          <td style=\"width: 30%; font-size: large; font-weight: bold;\">\n" +
                            "            Le C.S.M.G\n" +
                            "          </td>\n" +
                            "          <td style=\"width: 30%; text-align : center; font-size: large; font-weight: bold;\">\n" +
                            "            Le D.R.H.M.G\n" +
                            "          </td>\n" +
                            "        </tr>\n" +
                            "      </table>\n" +
                            "\n" +
                            "    </div>");

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
