package Controllers.OutputControllers;

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
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class ConsumptionStatusController implements Initializable {

    @FXML
    TabPane tabPane;
    @FXML
    Tab PaneDay, PanePeriod;
    @FXML
    HBox hBox;
    @FXML
    DatePicker dpDate,dpFrom,dpTo;
    @FXML
    ComboBox<String> cbDep,cbServ,cbDepPer,cbServPer;
    @FXML
    ListSelectionView<String> lsCategory,lsCategoryPer;
    @FXML
    Button btnConfirm;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;
    private final DepartmentOperation departmentOperation = new DepartmentOperation();
    private final ServiceOperation serviceOperation = new ServiceOperation();
    private final CategoryOperation categoryOperation = new CategoryOperation();
    private final ObservableList<String> comboDepartmentData = FXCollections.observableArrayList();
    private final ObservableList<String> comboServiceData = FXCollections.observableArrayList();
    private final ObservableList<String> lsCategoryData = FXCollections.observableArrayList();
    private List<String> categoryList;

    private ArrayList<Department> departments;
    private ArrayList<Service> services;
    private final HashMap<String,List<List<StringProperty>>> cons = new HashMap<>();
    private final HashMap<String,Double> consGas = new HashMap<>();
    private final ArrayList<String> treeSet = new ArrayList<>();
    ArrayList<Category> categories;
    private boolean carb = false;
    private boolean print = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();

        Label label = new Label();
        label.setFont(new Font(16));
        label.setText("Categorie");
        lsCategory.setSourceHeader(label);

        Label label1 = new Label();
        label1.setFont(new Font(16));
        label1.setText("Categorie selectionner");
        lsCategory.setTargetHeader(label1);

        refreshListSelectionCategory();
        refreshComboDepartment();

        // init date
        dpDate.setValue(LocalDate.now());


        Label labelPer = new Label();
        labelPer.setFont(new Font(16));
        labelPer.setText("Categorie");
        lsCategoryPer.setSourceHeader(labelPer);

        Label labelPer1 = new Label();
        labelPer1.setFont(new Font(16));
        labelPer1.setText("Categorie selectionner");
        lsCategoryPer.setTargetHeader(labelPer1);

        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            switch (newTab.getId()){
                case "PaneDay":
                    refreshListSelectionCategory();
                    refreshComboDepartment();

                    // init date
                    dpDate.setValue(LocalDate.now());
                    break;
                case "PanePeriod":


                    refreshListSelectionCategoryPeriod();
                    refreshComboDepartmentPeriod();

                    // init date
                    dpFrom.setValue(LocalDate.now());
                    dpTo.setValue(LocalDate.now());
                    break;
            }
        });
    }
    public void Init(boolean print){
        this.print = print;
    }

    private void refreshListSelectionCategory() {
        lsCategoryData.clear();
        try {

            this.categories = categoryOperation.getAll();
            this.categories.add(new Category("Carburants"));
            for (Category category: categories){
                lsCategoryData.add(category.getName());
            }
            categoryList = new ArrayList<>(lsCategoryData);
            lsCategory.setSourceItems(lsCategoryData);
            lsCategoryPer.setSourceItems(lsCategoryData);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshListSelectionCategoryPeriod() {
        lsCategoryData.clear();
        try {

            this.categories = categoryOperation.getAll();
            this.categories.add(new Category("Carburants"));
            for (Category category: categories){
                lsCategoryData.add(category.getName());
            }
            categoryList = new ArrayList<>(lsCategoryData);
            lsCategoryPer.setSourceItems(lsCategoryData);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshComboDepartment() {
        comboDepartmentData.clear();
        try {

            this.departments = departmentOperation.getAll();
            for (Department department : departments){
                comboDepartmentData.add(department.getName());
            }

            cbDep.setItems(comboDepartmentData);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshComboDepartmentPeriod() {
        comboDepartmentData.clear();
        try {

            this.departments = departmentOperation.getAll();
            for (Department department : departments){
                comboDepartmentData.add(department.getName());
            }

            cbDepPer.setItems(comboDepartmentData);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionComboDepartment(){
        try {
            int index = cbDep.getSelectionModel().getSelectedIndex();
            if (index != -1){
                int id = departments.get(index).getId();
                refreshComboService(id);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionComboDepartmentPeriod(){
        try {
            int index = cbDepPer.getSelectionModel().getSelectedIndex();
            if (index != -1){
                int id = departments.get(index).getId();
                refreshComboServicePeriod(id);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshComboService(int idDep) {
        comboServiceData.clear();
        try {

            this.services = serviceOperation.getAllByDepartment(idDep);
            for (Service service : services){
                comboServiceData.add(service.getName());
            }

            cbServ.setItems(comboServiceData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshComboServicePeriod(int idDep) {
        comboServiceData.clear();
        try {

            this.services = serviceOperation.getAllByDepartment(idDep);
            for (Service service : services){
                comboServiceData.add(service.getName());
            }
            cbServPer.setItems(comboServiceData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionPrint(){
        try {
            switch (tabPane.getSelectionModel().getSelectedItem().getId()){
                case "PaneDay":
                    DaySelection();
                    break;
                case "PanePeriod":
                    PeriodSelection();
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void DaySelection(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();

            LocalDate date = dpDate.getValue();
            List<String> lsCategorySelected = new ArrayList<>(lsCategory.getTargetItems());

            if (date != null && lsCategorySelected.size() != 0){

                ArrayList<Integer> idBons = new ArrayList<>();
                int indexDep = cbDep.getSelectionModel().getSelectedIndex();
                int indexServ = cbServ.getSelectionModel().getSelectedIndex();

                carb = lsCategorySelected.contains("Carburants");
                lsCategorySelected.remove("Carburants");
                cons.clear();
                consGas.clear();


                if (indexDep != -1){
                    if (indexServ != -1){
                        // select dep && serv
                        try {
                            String query = "SELECT OUTPUT.ID FROM OUTPUT,EMPLOYEE,SERVICE WHERE OUTPUT.DATE = ? AND EMPLOYEE.ID_SERVICE = ? AND OUTPUT.ID_EMP = EMPLOYEE.ID ;";
                            PreparedStatement preparedStmt = conn.prepareStatement(query);
                            preparedStmt.setDate(1, Date.valueOf(date));
                            preparedStmt.setInt(2,services.get(indexServ).getId());
                            ResultSet resultSet = preparedStmt.executeQuery();

                            while (resultSet.next()){
                                idBons.add(resultSet.getInt("ID"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else {
                        // select dep
                        try {
                            String query = "SELECT OUTPUT.ID FROM OUTPUT,EMPLOYEE,SERVICE WHERE OUTPUT.DATE = ? " +
                                    "AND SERVICE.ID_DEP     = ? AND EMPLOYEE.ID_SERVICE = SERVICE.ID AND OUTPUT.ID_EMP = EMPLOYEE.ID ;";
                            PreparedStatement preparedStmt = conn.prepareStatement(query);
                            preparedStmt.setDate(1, Date.valueOf(date));
                            preparedStmt.setInt(2,departments.get(indexDep).getId());
                            ResultSet resultSet = preparedStmt.executeQuery();

                            while (resultSet.next()){
                                idBons.add(resultSet.getInt("ID"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    // not select
                    try {
                        String query = "SELECT OUTPUT.ID FROM OUTPUT WHERE OUTPUT.DATE = ?;";
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setDate(1, Date.valueOf(date));
                        ResultSet resultSet = preparedStmt.executeQuery();

                        while (resultSet.next()){
                            idBons.add(resultSet.getInt("ID"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                // select other cat not gasoline
                if (idBons.size() != 0){

                    for (Integer s : idBons){
                        try {
                            List<List<StringProperty>> data = new ArrayList<>();
                            double totBs = 0.0;
                            String nbs = null;

                            for (String cat : lsCategorySelected) {
                                int index = categoryList.indexOf(cat);
                                int idCat = categories.get(index).getId();


                                String query = "SELECT OUTPUT.NUMBER, SUM(STORE_CARD.PRICE * COMPONENT_OUTPUT.QTE_SERV) AS TOT FROM OUTPUT,STORE_CARD, COMPONENT_OUTPUT, ARTICLE \n" +
                                        "WHERE OUTPUT.ID = ? AND COMPONENT_OUTPUT.ID_OUTPUT = OUTPUT.ID AND ARTICLE.ID_CAT = ? AND COMPONENT_OUTPUT.ID_ART = ARTICLE.ID \n" +
                                        "AND COMPONENT_OUTPUT.ID_STORE = STORE_CARD.ID";

                                PreparedStatement preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setInt(1, s);
                                preparedStmt.setInt(2, idCat);

                                ResultSet resultSet = preparedStmt.executeQuery();

                                while (resultSet.next()) {
                                    List<StringProperty> d = new ArrayList<>();

                                    double tot = resultSet.getDouble("TOT");
                                    totBs += tot;
                                    if (resultSet.getString("NUMBER") != null ) nbs = resultSet.getString("NUMBER");

                                    d.add(new SimpleStringProperty(categories.get(index).getName()));
                                    d.add(new SimpleStringProperty(String.valueOf(tot)));

                                    data.add(d);
                                }
                            }
                            if (totBs != 0) cons.put(nbs,data);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                // select gasoline
                if (carb){
                    if (indexDep != -1){
                        if (indexServ != -1){
                            // select dep && serv
                            try {
                                String query = "SELECT NUMBER, PRICE FROM RECHARGE_GASOLINE_CARD,EMPLOYEE,SERVICE WHERE RECHARGE_GASOLINE_CARD.DATE = ? \n" +
                                        "AND RECHARGE_GASOLINE_CARD.ID_EMP = EMPLOYEE.ID AND EMPLOYEE.ID_SERVICE = ?;";
                                PreparedStatement preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setDate(1, Date.valueOf(date));
                                preparedStmt.setInt(2,services.get(indexServ).getId());
                                ResultSet resultSet = preparedStmt.executeQuery();

                                while (resultSet.next()){
                                    consGas.put(String.valueOf(resultSet.getString("NUMBER")),resultSet.getDouble("PRICE"));
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            // select dep
                            try {
                                String query = "SELECT NUMBER, PRICE FROM RECHARGE_GASOLINE_CARD,EMPLOYEE,SERVICE WHERE RECHARGE_GASOLINE_CARD.DATE = ? \n" +
                                        "AND RECHARGE_GASOLINE_CARD.ID_EMP = EMPLOYEE.ID AND EMPLOYEE.ID_SERVICE = SERVICE.ID AND SERVICE.ID_DEP = ?;";
                                PreparedStatement preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setDate(1, Date.valueOf(date));
                                preparedStmt.setInt(2,departments.get(indexDep).getId());
                                ResultSet resultSet = preparedStmt.executeQuery();

                                while (resultSet.next()){
                                    consGas.put(String.valueOf(resultSet.getString("NUMBER")),resultSet.getDouble("PRICE"));
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }else{
                        // not select
                        try {
                            String query = "SELECT NUMBER, PRICE FROM RECHARGE_GASOLINE_CARD WHERE DATE = ?;";
                            PreparedStatement preparedStmt = conn.prepareStatement(query);
                            preparedStmt.setDate(1, Date.valueOf(date));
                            ResultSet resultSet = preparedStmt.executeQuery();

                            while (resultSet.next()){
                                consGas.put(String.valueOf(resultSet.getString("NUMBER")),resultSet.getDouble("PRICE"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }


                treeSet.clear();
                if (cons.size() != 0) treeSet.addAll(cons.keySet());
                if (consGas.size() != 0) treeSet.addAll(consGas.keySet());

                ArrayList<Integer> integers = new ArrayList<>();
                treeSet.forEach(s -> {
                    integers.add(Integer.parseInt(s));
                });
                Collections.sort(integers);

                treeSet.clear();
                integers.forEach(integer -> {
                    treeSet.add("000" + integer);
                });

                Print(lsCategorySelected);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void PeriodSelection(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();

            LocalDate dateFrom = dpFrom.getValue();
            LocalDate dateTo = dpTo.getValue();
            List<String> lsCategorySelected = new ArrayList<>(lsCategoryPer.getTargetItems());

            if (dateFrom != null && dateTo != null && lsCategorySelected.size() != 0){

                ArrayList<Integer> idBons = new ArrayList<>();
                int indexDep = cbDepPer.getSelectionModel().getSelectedIndex();
                int indexServ = cbServPer.getSelectionModel().getSelectedIndex();

                carb = lsCategorySelected.contains("Carburants");
                lsCategorySelected.remove("Carburants");
                cons.clear();
                consGas.clear();


                if (indexDep != -1){
                    if (indexServ != -1){
                        // select dep && serv
                        try {
                            String query = "SELECT OUTPUT.ID FROM OUTPUT,EMPLOYEE,SERVICE WHERE OUTPUT.DATE BETWEEN ? AND ? AND EMPLOYEE.ID_SERVICE = ? AND OUTPUT.ID_EMP = EMPLOYEE.ID ;";
                            PreparedStatement preparedStmt = conn.prepareStatement(query);
                            preparedStmt.setDate(1, Date.valueOf(dateFrom));
                            preparedStmt.setDate(2, Date.valueOf(dateTo));
                            preparedStmt.setInt(3,services.get(indexServ).getId());
                            ResultSet resultSet = preparedStmt.executeQuery();

                            while (resultSet.next()){
                                idBons.add(resultSet.getInt("ID"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else {
                        // select dep
                        try {
                            String query = "SELECT OUTPUT.ID FROM OUTPUT,EMPLOYEE,SERVICE WHERE OUTPUT.DATE BETWEEN ? AND ? " +
                                    "AND SERVICE.ID_DEP     = ? AND EMPLOYEE.ID_SERVICE = SERVICE.ID AND OUTPUT.ID_EMP = EMPLOYEE.ID ;";
                            PreparedStatement preparedStmt = conn.prepareStatement(query);
                            preparedStmt.setDate(1, Date.valueOf(dateFrom));
                            preparedStmt.setDate(2, Date.valueOf(dateTo));
                            preparedStmt.setInt(3,departments.get(indexDep).getId());
                            ResultSet resultSet = preparedStmt.executeQuery();

                            while (resultSet.next()){
                                idBons.add(resultSet.getInt("ID"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    // not select
                    try {
                        String query = "SELECT OUTPUT.ID FROM OUTPUT WHERE OUTPUT.DATE BETWEEN ? AND ?;";
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setDate(1, Date.valueOf(dateFrom));
                        preparedStmt.setDate(2, Date.valueOf(dateTo));
                        ResultSet resultSet = preparedStmt.executeQuery();

                        while (resultSet.next()){
                            idBons.add(resultSet.getInt("ID"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                // select other cat not gasoline
                if (idBons.size() != 0){

                    for (Integer s : idBons){
                        try {
                            List<List<StringProperty>> data = new ArrayList<>();
                            double totBs = 0.0;
                            String nbs = null;

                            for (String cat : lsCategorySelected) {
                                int index = categoryList.indexOf(cat);
                                int idCat = categories.get(index).getId();


                                String query = "SELECT OUTPUT.NUMBER, SUM(STORE_CARD.PRICE * COMPONENT_OUTPUT.QTE_SERV) AS TOT FROM OUTPUT,STORE_CARD, COMPONENT_OUTPUT, ARTICLE \n" +
                                        "WHERE OUTPUT.ID = ? AND COMPONENT_OUTPUT.ID_OUTPUT = OUTPUT.ID AND ARTICLE.ID_CAT = ? AND COMPONENT_OUTPUT.ID_ART = ARTICLE.ID \n" +
                                        "AND COMPONENT_OUTPUT.ID_STORE = STORE_CARD.ID";

                                PreparedStatement preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setInt(1, s);
                                preparedStmt.setInt(2, idCat);

                                ResultSet resultSet = preparedStmt.executeQuery();

                                while (resultSet.next()) {
                                    List<StringProperty> d = new ArrayList<>();

                                    double tot = resultSet.getDouble("TOT");
                                    totBs += tot;
                                    if (resultSet.getString("NUMBER") != null ) nbs = resultSet.getString("NUMBER");

                                    d.add(new SimpleStringProperty(categories.get(index).getName()));
                                    d.add(new SimpleStringProperty(String.valueOf(tot)));

                                    data.add(d);
                                }
                            }
                            if (totBs != 0) cons.put(nbs,data);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                // select gasoline
                if (carb){
                    if (indexDep != -1){
                        if (indexServ != -1){
                            // select dep && serv
                            try {
                                String query = "SELECT NUMBER, PRICE FROM RECHARGE_GASOLINE_CARD,EMPLOYEE,SERVICE WHERE RECHARGE_GASOLINE_CARD.DATE BETWEEN ? AND ? \n" +
                                        "AND RECHARGE_GASOLINE_CARD.ID_EMP = EMPLOYEE.ID AND EMPLOYEE.ID_SERVICE = ?;";
                                PreparedStatement preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setDate(1, Date.valueOf(dateFrom));
                                preparedStmt.setDate(2, Date.valueOf(dateTo));
                                preparedStmt.setInt(3,services.get(indexServ).getId());
                                ResultSet resultSet = preparedStmt.executeQuery();

                                while (resultSet.next()){
                                    consGas.put(String.valueOf(resultSet.getString("NUMBER")),resultSet.getDouble("PRICE"));
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            // select dep
                            try {
                                String query = "SELECT NUMBER, PRICE FROM RECHARGE_GASOLINE_CARD,EMPLOYEE,SERVICE WHERE RECHARGE_GASOLINE_CARD.DATE BETWEEN ? AND ? \n" +
                                        "AND RECHARGE_GASOLINE_CARD.ID_EMP = EMPLOYEE.ID AND EMPLOYEE.ID_SERVICE = SERVICE.ID AND SERVICE.ID_DEP = ?;";
                                PreparedStatement preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setDate(1, Date.valueOf(dateFrom));
                                preparedStmt.setDate(2, Date.valueOf(dateTo));
                                preparedStmt.setInt(3,departments.get(indexDep).getId());
                                ResultSet resultSet = preparedStmt.executeQuery();

                                while (resultSet.next()){
                                    consGas.put(String.valueOf(resultSet.getString("NUMBER")),resultSet.getDouble("PRICE"));
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }else{
                        // not select
                        try {
                            String query = "SELECT NUMBER, PRICE FROM RECHARGE_GASOLINE_CARD WHERE DATE BETWEEN ? AND ?;";
                            PreparedStatement preparedStmt = conn.prepareStatement(query);
                            preparedStmt.setDate(1, Date.valueOf(dateFrom));
                            preparedStmt.setDate(2, Date.valueOf(dateTo));
                            ResultSet resultSet = preparedStmt.executeQuery();

                            while (resultSet.next()){
                                consGas.put(String.valueOf(resultSet.getString("NUMBER")),resultSet.getDouble("PRICE"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                treeSet.clear();
                if (cons.size() != 0) treeSet.addAll(cons.keySet());
                if (consGas.size() != 0) treeSet.addAll(consGas.keySet());

                ArrayList<Integer> integers = new ArrayList<>();
                treeSet.forEach(s -> {
                    integers.add(Integer.parseInt(s));
                });
                Collections.sort(integers);

                treeSet.clear();
                integers.forEach(integer -> {
                    treeSet.add("000" + integer);
                });

                Print(lsCategorySelected);
            }else {
                System.out.println("chemps vide !!!!!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void Print(List<String> lsCategorySelected){
        final StringBuilder HTMLFacture = new StringBuilder();
        HTMLFacture.append("<!DOCTYPE html>\n")
                .append("<html>\n" )
                .append("<head>\n" )
                .append("<style>\n" )

                .append("@page {\n" )
                .append("margin: 15mm 10mm 10mm 15mm;\n" )
                .append("size: A4;" )
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
                .append("ETAT DE CONSOMMATION DE MATIERES ET FOURNITURES\n" );

        switch (tabPane.getSelectionModel().getSelectedItem().getId()){
            case "PaneDay":
                HTMLFacture.append("JOUR DU ")
                        .append("<span>")
                        .append(dpDate.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                        .append("</span> \n" )
                        .append("</H2>\n" );
                break;
            case "PanePeriod":
                HTMLFacture.append(" DE ")
                        .append("<span>")
                        .append(dpFrom.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                        .append("</span> \n" )
                        .append(" AU ")
                        .append("<span>")
                        .append(dpTo.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                        .append("</span> \n" )
                        .append("</H2>\n" );
                break;
        }



        HTMLFacture.append("<table class=\"table-art\">\n" )
                .append("<tr>\n" )
                .append("<th class=\"th-art\">")
                .append("N°")
                .append("</th>\n");

        if (carb) {
            HTMLFacture.append("<th class=\"th-art\">")
                    .append("Carburants")
                    .append("</th>\n");
            lsCategorySelected.remove("Carburants");
        }
        for (String ss : lsCategorySelected) {
            HTMLFacture.append("<th class=\"th-art\">")
                    .append(ss)
                    .append("</th>\n");
        }
        HTMLFacture.append("<th class=\"th-art\">")
                .append("MONTANT")
                .append("</th>\n");
        HTMLFacture.append("</tr>\n" );

        HashMap<String,Double> totals  = new HashMap<>();
        for (String ss : lsCategorySelected) {
            totals.put(ss,0.0);
        }
        if (carb) totals.put("Carburants",0.0);

        treeSet.forEach(s -> {
            double total = 0.0;
            HTMLFacture.append("<tr>\n" );
            HTMLFacture.append("<td class=\"td-art\" style=\"width: 3%\">")
                    .append(s)
                    .append("</td>\n");

            if (cons.containsKey(s)){

                if (carb)
                    HTMLFacture.append("<td class=\"td-art\" >")
                            .append(0.0)
                            .append("</td>\n");

                List<List<StringProperty>> lists = cons.get(s);

                for (List<StringProperty> list : lists){
                    for (String ss : lsCategorySelected) {
                        if (list.get(0).getValue().equals(ss)){
                            HTMLFacture.append("<td class=\"td-art\" >")
                                    .append(list.get(1).getValue())
                                    .append("</td>\n");
                            double val = Double.parseDouble(list.get(1).getValue());
                            total += val;
                            double t = totals.get(ss);
                            t += val;
                            totals.put(ss,t);
                        }
                    }
                }


            }else {
                HTMLFacture.append("<td class=\"td-art\" >")
                        .append(consGas.get(s))
                        .append("</td>\n");
                total += consGas.get(s);
                double t = totals.get("Carburants");
                t += consGas.get(s);
                totals.put("Carburants",t);

                for (int i = 0; i < lsCategorySelected.size(); i++) {
                    HTMLFacture.append("<td class=\"td-art\" >")
                            .append(0.0)
                            .append("</td>\n");
                }
            }
            HTMLFacture.append("<td class=\"td-art\" >")
                    .append(total)
                    .append("</td>\n");

            HTMLFacture.append("</tr>\n" );
        });
        // tot افقي
        double totalGen = 0.0;
        HTMLFacture.append("<tr>\n" );
        HTMLFacture.append("<td class=\"td-art\" style=\"width: 3%\">")
                .append("Totat")
                .append("</td>\n");
        if (carb) {
            HTMLFacture.append("<td class=\"td-art\" >")
                    .append(totals.get("Carburants"))
                    .append("</td>\n");
            totalGen += totals.get("Carburants");
        }
        for (String s : lsCategorySelected){
            HTMLFacture.append("<td class=\"td-art\" >")
                    .append(totals.get(s))
                    .append("</td>\n");
            totalGen += totals.get(s);
        }
        HTMLFacture.append("<td class=\"td-art\" >")
                .append(totalGen)
                .append("</td>\n");
        HTMLFacture.append("</tr>\n" );

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
                                "          <td style=\"width: 30%; text-align : center; font-size: large; font-weight: bold;\">\n" +
                                "            Le D.R.H.M.G\n" +
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

            String outputDirectory = mainDirectoryPath + File.separator + "Etat de Consomation" ;
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

    @FXML
    private void ActionAnnul(){
        ((Stage) btnConfirm.getScene().getWindow()).close();
    }
}
