package Controllers.OutputControllers.OutputArticlesControllers;

import BddPackage.*;
import Models.*;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class Print {

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;
    private Output output;

/*    private final HashMap<Integer, List<ComponentOutput>> outputs;
    private final HashMap<Integer,List<StoreCard>> stores;*/

/*    private final OutputOperation outputOperation = new OutputOperation();
    private final ComponentOutputOperation componentOutputOperation = new ComponentOutputOperation();
    private final StoreCardOperation storeCardOperation = new StoreCardOperation();
    private final EmployeeOperation employeeOperation = new EmployeeOperation();

    private ArrayList<ComponentOutput> componentOutputs = new ArrayList<>();
    private OutputArticlesControllers output;
    private Employee employee;
    private double debt ;
    private double pay ;
    private boolean fastPrint ;
    private boolean debtPrint ;
    private final String IMAGE_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator + "Images" + File.separator +  "logoN.jpeg";

    public Print(OutputArticlesControllers output, ArrayList<ComponentOutput> componentOutputs, double pay, double debt, boolean fastPrint, boolean debtPrint) {
        this.output = output;
        this.employee = employeeOperation.get(output.getIdEmp());
        this.componentOutputs = componentOutputs;
        this.pay = pay;
        this.debt = debt;
        this.fastPrint = fastPrint;
        this.debtPrint = debtPrint;
    }*/

    public Print(Output output) {
        this.output = output;
        this.conn = connectBD.connect();
    }

    public Print(Output output, HashMap<Integer,List<ComponentOutput>> outputs, HashMap<Integer,List<StoreCard>> stores) {
        this.output = output;
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
                    .append("font-size: large;\n" )
                    .append("font-weight: bold;\n" )
                    .append("}\n" )

                    .append(".td-art{\n" )
                    .append("border: solid black;\n" )
                    .append("border-width: 1px ;\n" )
                    .append("line-height: 20px;")
                    .append("white-space: nowrap;")
                    .append("font-size: medium;\n" )
                    .append("}\n" )

                    .append("</style>\n" )
                    .append("</head>\n" )
                    .append("<body>\n" )

                    .append("<table style=\"width : 100%\">\n" )
                    .append("<tr>\n" )
                    .append("<td style=\"width: 60%; font-size: large;\">\n" )
                    .append("Office de Promotion et de Gestion\n" )
                    .append("</td>\n" )
                    .append("<td style=\"width: 5%; font-size: large;\">\n" )
                    .append("N° :\n" )
                    .append("</td>\n" )
                    .append("<td style=\"width: 15%; font-size: large;\">\n" )
                    .append(output.getNumber())
                    .append("</td>\n" )
                    .append("</tr>\n" )
                    .append("</table>\n" )

                    .append("<table style=\"width : 100%\">\n" )
                    .append("<tr>\n" )
                    .append("<td style=\"width: 60%; font-size: large;\">\n" )
                    .append("Immobiliére Wilaya de TAMANRASSET\n" )
                    .append("</td>\n" )
                    .append("<td style=\"width: 5%; font-size: large;\">\n" )
                    .append("DATE :\n" )
                    .append("</td>\n" )
                    .append("<td style=\"width: 15%; font-size: large;\">\n" )
                    .append(output.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                    .append("</td>\n" )
                    .append("</tr>\n" )
                    .append("</table>\n" )

                    .append("<div style=\"text-align: center; margin-bottom: 20px;\">\n" )
                    .append("<h1><u> BON DE SORTIE </u> </h1>\n" )
                    .append("</div>\n" )

                    .append("<table class=\"table-art\">\n" )
                    .append("<tr>\n" )
                    .append("<th rowspan=\"2\" class=\"th-art\">N°</th>\n" )
                    .append("<th rowspan=\"2\" class=\"th-art\">DESIGNATION</th>\n" )
                    .append("<th colspan=\"2\" class=\"th-art\">QUANTITIES</th>\n" )
                    .append("<th rowspan=\"2\" class=\"th-art\">PRIX UNITAIRE</th>\n" )
                    .append("<th rowspan=\"2\" class=\"th-art\">MONTANT</th>\n" )
                    .append("<th rowspan=\"2\" class=\"th-art\">OBSERVATION</th>\n" )
                    .append("</tr>\n" )
                    .append("<tr>\n" )
                    .append("<th class=\"th-art\">DEM</th>\n" )
                    .append("<th class=\"th-art\">SERV</th>\n" )
                    .append("</tr>\n" );

                    final int[] count = {1};
                    AtomicReference<Double> total = new AtomicReference<>(0.0);

                    try {
                        if (conn.isClosed()) conn = connectBD.connect();

                        String query = "SELECT ARTICLE.NAME, COMPONENT_OUTPUT.QTE_DEM, COMPONENT_OUTPUT.QTE_SERV, STORE_CARD.PRICE FROM COMPONENT_OUTPUT,STORE_CARD,ARTICLE \n" +
                                "WHERE COMPONENT_OUTPUT.ID_OUTPUT = ? AND COMPONENT_OUTPUT.ID_STORE = STORE_CARD.ID AND COMPONENT_OUTPUT.ID_ART = ARTICLE.ID;";
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setInt(1,this.output.getId());
                        ResultSet resultSet = preparedStmt.executeQuery();

                        while (resultSet.next()){

                            double pr =  resultSet.getDouble("PRICE");
                            int qte =  resultSet.getInt("QTE_SERV");

                            HTMLFacture.append("<tr>\n" )
                                    .append("<td class=\"td-art\" style=\"width: 3%\">")
                                    .append(count[0])
                                    .append("</td>\n" )

                                    .append("<td class=\"td-art\">")
                                    .append(resultSet.getString("NAME"))
                                    .append("</td>\n" )

                                    .append("<td class=\"td-art\" style=\"width: 10%\">")
                                    .append(resultSet.getInt("QTE_DEM"))
                                    .append("</td>\n" )

                                    .append("<td class=\"td-art\" style=\"width: 10%\">")
                                    .append(resultSet.getInt("QTE_SERV"))
                                    .append("</td>\n" )

                                    .append("<td class=\"td-art\" style=\"width: 10%\">")
                                    .append(String.format(Locale.FRANCE, "%,.2f", pr))
                                    .append("</td>\n" )

                                    .append("<td class=\"td-art\" style=\"width: 15%\">")
                                    .append(String.format(Locale.FRANCE, "%,.2f", ( qte * pr )))
                                    .append("</td>\n" )

                                    .append("<td class=\"td-art\" style=\"width: 17%\">")
                                    .append("</td>\n" );

                            total.updateAndGet(v -> (v + ( qte * pr ) ));
                            count[0]++;
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        conn.close();
                    }

                    int rest = 10 - count[0];

                    for (int i = 0; i < rest + 1; i++) {
                        HTMLFacture.append("<tr>\n" )
                                .append("<td class=\"td-art\" style=\"width: 3%; padding: 15px 0;\">")
                                .append("    ")
                                .append("</td>\n" )

                                .append("<td class=\"td-art\">")
                                .append("    ")
                                .append("</td>\n" )

                                .append("<td class=\"td-art\" style=\"width: 10%\">")
                                .append("    ")
                                .append("</td>\n" )

                                .append("<td class=\"td-art\" style=\"width: 10%\">")
                                .append("    ")
                                .append("</td>\n" )

                                .append("<td class=\"td-art\" style=\"width: 10%\">")
                                .append("    ")
                                .append("</td>\n" )

                                .append("<td class=\"td-art\" style=\"width: 15%\">")
                                .append("    ")
                                .append("</td>\n" )

                                .append("<td class=\"td-art\" style=\"width: 17%\">")
                                .append("    ")
                                .append("</td>\n" );
                    }


            HTMLFacture.append("</table>\n" )
                    .append("<table style=\"width: 52%; border: 1px solid black; border-collapse: collapse; text-align: center; margin-top: 10px; margin-right:0px; margin-left:auto;\">\n" )
                    .append("<tr>\n" )
                    .append("<td class=\"td-art\" style=\"width: 38%; font-weight: bold; font-size: large;\">\n" )
                    .append("Montant Total \n" )
                    .append("</td>\n" )
                    .append("<td class=\"td-art\" style=\"font-size: large;\">\n" )
                    .append(String.format(Locale.FRANCE, "%,.2f", total.get()) )
                    .append("</td>\n" )
                    .append("</tr>\n" )
                    .append("</table>\n" );

            try {
                if (conn.isClosed()) conn = connectBD.connect();

                String query = "SELECT EMPLOYEE.ID, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, DEP.NAME AS DEP_NAME, SERVICE.NAME AS SERV_NAME, EMPLOYEE.FUNCTION \n" +
                        "FROM EMPLOYEE,SERVICE,DEP WHERE EMPLOYEE.ARCHIVE = 0 AND EMPLOYEE.ID = ? AND EMPLOYEE.ID_SERVICE = SERVICE.ID AND SERVICE.ID_DEP = DEP.ID;";
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt(1,this.output.getIdEmp());
                ResultSet resultSet = preparedStmt.executeQuery();

                if (resultSet.next()){

                    HTMLFacture
                            .append("</table>\n" )
                            .append("<div style=\"position: relative; position: absolute; bottom: 20px; width: 100%;\">\n" )
                            .append("<table style=\"width : 100%\">\n" )
                            .append("<tr>\n" )
                            .append("<td style=\"width: 63%; font-size: large;\">\n" )
                            .append("Le preneur\n" )
                            .append("</td>\n" )
                            .append("<td style=\"width: 30%; text-align : center; font-size: large; font-weight: bold;\">\n" )
                            .append("Le C.S.P.M.G\n" )
                            .append("</td>\n" )
                            .append("</tr>\n" )
                            .append("</table>\n" )
                            .append("<table style=\"width : 100%\">\n" )
                            .append("<tr>\n" )
                            .append("<td style=\"width: 13%; font-size: large;\">\n" )
                            .append("Nom :\n" )
                            .append("</td>\n" )
                            .append("<td style=\"width: 17%; font-size: large;\">\n" )
                            .append(resultSet.getString("FIRST_NAME"))
                            .append("</td>\n" )
                            .append("<td style=\"width: 40%; text-align : center; font-size: large; font-weight: bold;\">\n" )
                            .append("Signature\n" )
                            .append("</td>\n" )
                            .append("<td style=\"width: 30%; text-align : center; font-size: large; font-weight: bold;\">\n" )
                            .append("Ou Le Magasiner\n" )
                            .append("</td>\n" )
                            .append("</tr>\n" )
                            .append("</table>\n" )
                            .append("<table style=\"width : 100%\">\n" )
                            .append("<tr>\n" )
                            .append("<td style=\"width: 13%; font-size: large;\">\n" )
                            .append("Prénom :\n" )
                            .append("</td>\n" )
                            .append("<td style=\" font-size: large;\">\n" )
                            .append(resultSet.getString("LAST_NAME"))
                            .append("</td>\n" )
                            .append("</tr>\n" )
                            .append("<tr>\n" )
                            .append("<td style=\"width: 13%; font-size: large;\">\n" )
                            .append("Département :\n" )
                            .append("</td>\n" )
                            .append("<td style=\"font-size: large;\">\n" )
                            .append(resultSet.getString("DEP_NAME"))
                            .append("</td>\n" )
                            .append("</tr>\n" )
                            .append("<tr>\n" )
                            .append("<td style=\"width: 13%; font-size: large;\">\n" )
                            .append("Service :\n" )
                            .append("</td>\n" )
                            .append("<td style=\"font-size: large;\">\n" )
                            .append(resultSet.getString("SERV_NAME"))
                            .append("</td>\n" )
                            .append("</tr>\n" )
                            .append("<tr>\n" )
                            .append("<td style=\"width: 13%; font-size: large;\">\n" )
                            .append("Fonction :\n" )
                            .append("</td>\n" )
                            .append("<td style=\" font-size: large;\">\n" )
                            .append(resultSet.getString("FUNCTION") )
                            .append("</td>\n" )
                            .append("</tr>\n" )
                            .append("</table>\n" )
                            .append("</div>\n" );
                }

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                conn.close();
            }

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
                    Desktop.getDesktop().print(new File(path));

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
