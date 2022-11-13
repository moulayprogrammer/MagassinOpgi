package Controllers.ArticlesControllers;

import BddPackage.CategoryOperation;
import BddPackage.ConnectBD;
import BddPackage.DepartmentOperation;
import BddPackage.ServiceOperation;
import Models.Category;
import Models.Department;
import Models.Service;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.controlsfx.control.ListSelectionView;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

public class MagasinStatusController implements Initializable {

    @FXML
    Button btnConfirm;
    @FXML
    ComboBox<String> cbCategory;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;

    private final CategoryOperation categoryOperation = new CategoryOperation();


    private final ObservableList<String> comboCategoryData = FXCollections.observableArrayList();
    ArrayList<Category> categories;
    private boolean print = false;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();

        refreshComboCategory();

    }
    public void Init(boolean print){
        this.print = print;
    }

    private void refreshComboCategory() {
        comboCategoryData.clear();
        try {

            this.categories = categoryOperation.getAll();
            for (Category category: categories){
                comboCategoryData.add(category.getName());
            }

            cbCategory.setItems(comboCategoryData);
            cbCategory.getSelectionModel().select(0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionPrint(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();

            int index = cbCategory.getSelectionModel().getSelectedIndex();


            if (index != -1){
                final StringBuilder HTMLFacture = new StringBuilder();
                int idCat = categories.get(index).getId();

                HTMLFacture.append("<!DOCTYPE html>\n")
                        .append("<html>\n" )
                        .append("<head>\n" )
                        .append("<style>\n" )

                        .append("@page {" )
                        .append("margin: 15mm 10mm 10mm 15mm;" )
                        .append("size: A4;")
                        .append("}" )

                        .append("html {")
                        .append("font-family: 'Times New Roman';")
                        .append("background-color: white;")
                        .append("}")

                        .append(".table-art{")
                        .append("border: 1px solid black;")
                        .append("border-collapse: collapse;")
                        .append("min-width: 100%;")
                        .append("width: 100%;")
                        .append("text-align: center;")
                        .append("font-size: 10pt;")
                        .append("}")

                        .append(".th-art{")
                        .append("border: solid black;")
                        .append("border-width: 1px ;" )
                        .append("font-size: medium;\n" )
                        .append("font-weight: bold;\n" )
                        .append("}\n" )

                        .append(".td-art{\n" )
                        .append("border: solid black;\n" )
                        .append("border-width: 1px ;\n" )
                        .append("line-height: 20px;")
                        .append("white-space: nowrap;")
                        .append("font-size: small;\n" )
                        .append("}\n" )

                        .append("</style>\n" )
                        .append("</head>\n" );

                HTMLFacture.append("<body>\n" )
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

                        .append("<H2 style=\"text-align: center;\">\n" )
                        .append("ETAT DE MAGASIN DU ")
                        .append("<span> ")
                        .append(categories.get(index).getName())
                        .append( "</span>\n" )
                        .append(" JOUR DE " )
                        .append("<span> ")
                        .append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                        .append( "</span>\n" )
                        .append("</H2>" );

                HTMLFacture.append("<table class=\"table-art\">\n" )
                        .append("<tr>\n" )
                        .append("<th class=\"th-art\">")
                        .append("N°")
                        .append("</th>")
                        .append("<th class=\"th-art\">")
                        .append("DESIGNATION")
                        .append("</th>")
                        .append("<th class=\"th-art\">")
                        .append("QTE")
                        .append("</th>")
                        .append("<th class=\"th-art\">")
                        .append("PRIX")
                        .append("</th>")
                        .append("<th class=\"th-art\">")
                        .append("MONTANT")
                        .append("</th>");

                String query = "SELECT ARTICLE.NAME, STORE_CARD.PRICE, (STORE_CARD.QTE_STORED - STORE_CARD.QTE_CONSUMED) AS QTE FROM ARTICLE,STORE_CARD\n" +
                        "WHERE ARTICLE.ID_CAT = ? AND STORE_CARD.ID_ARTICLE = ARTICLE.ID AND (STORE_CARD.QTE_STORED - STORE_CARD.QTE_CONSUMED) > 0\n" +
                        "ORDER BY ARTICLE.ID ASC ;";
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt(1,idCat);
                ResultSet resultSet = preparedStmt.executeQuery();

                double tot = 0.0;
                final int[] count = {1};

                while (resultSet.next()){
                    int qte = resultSet.getInt("QTE");
                    double price = resultSet.getInt("PRICE");

                    tot += (qte*price);

                    HTMLFacture.append("<tr>\n" )
                            .append("<td class=\"td-art\" style=\"width: 3%\">")
                            .append(count[0])
                            .append("</td>\n" )

                            .append("<td class=\"td-art\">")
                            .append(resultSet.getString("NAME"))
                            .append("</td>\n" )

                            .append("<td class=\"td-art\">")
                            .append(qte)
                            .append("</td>\n" )

                            .append("<td class=\"td-art\">")
                            .append(String.format(Locale.FRANCE, "%,.2f", price) )
                            .append("</td>\n" )

                            .append("<td class=\"td-art\">")
                            .append(String.format(Locale.FRANCE, "%,.2f", (qte*price)) )
                            .append("</td>\n" )
                            .append("</tr>\n");
                    count[0]++;
                }

                if (tot > 0){
                    HTMLFacture.append("<tr>\n" )
                            .append("<td class=\"td-art\" colspan=\"4\">")
                            .append("Total")
                            .append("</td>\n" )

                            .append("<td class=\"td-art\">")
                            .append(String.format(Locale.FRANCE, "%,.2f", tot))
                            .append("</td>\n" )
                            .append("</tr>\n");
                }

                HTMLFacture.append("</table>\n" )
                        .append("<div style=\"margin-top: 10px; width: 100%;\">\n" +
                                "      <table style=\"width : 100%; margin-left: 30px\">\n" +
                                "        <tr>\n" +
                                "          <td style=\"width: 30%; font-size: large; font-weight: bold;\">\n" +
                                "            Le Magasinier\n" +
                                "          </td>\n" +
                                "          <td style=\"width: 30%; text-align : center; font-size: large; font-weight: bold;\">\n" +
                                "            Le C.S.M.G\n" +
                                "          </td>\n" +
                                "        </tr>\n" +
                                "      </table>\n" +
                                "    </div>")
                        .append("</body>")
                        .append("</html>");

                try {
                    String pathDocument = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
                    String mainDirectoryPath = pathDocument + File.separator + "Magassin Document";
                    File mainFile =  new File(mainDirectoryPath);

                    if (!mainFile.exists()) FileUtils.forceMkdir(mainFile);

                    String outputDirectory = mainDirectoryPath + File.separator + "Etat de Magasin" ;
                    File invoiceFile = new File(outputDirectory);
                    if (!invoiceFile.exists()) FileUtils.forceMkdir(invoiceFile);

                    String dayDirectory = outputDirectory + File.separator + "etats_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
                    File dayFile = new File(dayDirectory);
                    if (!dayFile.exists()) FileUtils.forceMkdir(dayFile);


                    if (dayFile.exists()) {

                        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("_HH-mm-ss");

                        String path = dayDirectory + File.separator + "etat_" + LocalDateTime.now().format(myFormatObj) + ".pdf";
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
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionAnnul(){
        ((Stage) btnConfirm.getScene().getWindow()).close();
    }
}
