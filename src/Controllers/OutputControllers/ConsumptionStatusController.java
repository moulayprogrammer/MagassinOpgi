package Controllers.OutputControllers;

import BddPackage.CategoryOperation;
import BddPackage.ConnectBD;
import BddPackage.DepartmentOperation;
import BddPackage.ServiceOperation;
import Models.Category;
import Models.Department;
import Models.Service;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.controlsfx.control.ListSelectionView;


import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class ConsumptionStatusController implements Initializable {

    @FXML
    HBox hBox;
    @FXML
    DatePicker dpDate;
    @FXML
    ComboBox<String> cbDep,cbServ;
    @FXML
    ListSelectionView<String> lsCategory;
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
    ArrayList<Category> categories;

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

    @FXML
    private void ActionPrint(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();

            LocalDate date = dpDate.getValue();
            ObservableList<String> lsCategorySelected = lsCategory.getTargetItems();

            if (date != null && lsCategorySelected.size() != 0){

                HashMap<String,List<List<StringProperty>>> cons = new HashMap<>();
                HashMap<String,Double> consGas = new HashMap<>();
                int indexDep = cbDep.getSelectionModel().getSelectedIndex();
                int indexServ = cbServ.getSelectionModel().getSelectedIndex();

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
                                cons.put(String.valueOf(resultSet.getInt("ID")),new ArrayList<>());
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
                                cons.put(String.valueOf(resultSet.getInt("ID")),new ArrayList<>());
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
                            cons.put(String.valueOf(resultSet.getInt("ID")),new ArrayList<>());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                // select other cat not gasoline
                if (cons.size() != 0){
                    List<String> bsVide = new ArrayList<>();
                    List<String> bsNotVide = new ArrayList<>();
                    cons.forEach((s, stringProperties) -> {
                        try {
                            List<List<StringProperty>> data = new ArrayList<>();
                            double totBs = 0.0;
                            String nbBs = "";

                            for (String cat : lsCategorySelected) {
                                if (!cat.equals("Carburants")) {
                                    int index = categoryList.indexOf(cat);
                                    int idCat = categories.get(index).getId();

                                    String query = "SELECT OUTPUT.NUMBER, SUM(STORE_CARD.PRICE * COMPONENT_OUTPUT.QTE_SERV) AS TOT FROM OUTPUT,STORE_CARD, COMPONENT_OUTPUT, ARTICLE \n" +
                                            "WHERE OUTPUT.ID = ? AND COMPONENT_OUTPUT.ID_OUTPUT = OUTPUT.ID AND ARTICLE.ID_CAT = ? AND COMPONENT_OUTPUT.ID_ART = ARTICLE.ID \n" +
                                            "AND COMPONENT_OUTPUT.ID_STORE = STORE_CARD.ID";

                                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                                    preparedStmt.setInt(1, Integer.parseInt(s));
                                    preparedStmt.setInt(2, idCat);

                                    ResultSet resultSet = preparedStmt.executeQuery();

                                    while (resultSet.next()) {
                                        List<StringProperty> d = new ArrayList<>();

                                        double tot = resultSet.getDouble("TOT");
                                        totBs += tot;
                                        nbBs = resultSet.getString("NUMBER");
                                        d.add(new SimpleStringProperty(resultSet.getString("NUMBER")));
                                        d.add(new SimpleStringProperty(categories.get(index).getName()));
                                        d.add(new SimpleStringProperty(String.valueOf(tot)));

                                        data.add(d);
                                    }
                                }
                            }
                            if (totBs != 0) {
                                cons.put(s,data);
                                bsNotVide.add(s);
                            }else bsVide.add(s);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    });
                    // clean hashMap
                    bsVide.forEach(cons::remove);
                    bsNotVide.forEach(s -> {
                        List<List<StringProperty>> data = new ArrayList<>(cons.get(s));
                        cons.remove(s);
                        String nb = data.get(0).get(0).get();

                        for (int i = 0; i < data.size(); i++) {
                            data.get(i).remove(0);
                        }
                        cons.put(nb,data);
                    });
                }

                // select gasoline
                if (lsCategorySelected.contains("Carburants")){
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
                                    cons.put(String.valueOf(resultSet.getInt("ID")),new ArrayList<>());
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
                                    cons.put(String.valueOf(resultSet.getInt("ID")),new ArrayList<>());
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

                TreeSet<String> treeSet = new TreeSet<>();
                if (cons.size() != 0) treeSet.addAll(cons.keySet());
                if (consGas.size() != 0) treeSet.addAll(consGas.keySet());

                treeSet.forEach(s -> {
                    if (cons.containsKey(s)){
                        List<List<StringProperty>> lists = cons.get(s);
                        System.out.print("output = " + s + "     ");
                        for (List<StringProperty> list : lists){
                            System.out.print("Cat = " + list.get(0).getValue());
                            System.out.print("    Tot = " + list.get(1).getValue());
                            System.out.print("    ||");
                        }
                        System.out.println();
                    }else {
                        System.out.print("gas = " + s);
                        System.out.print("     PRICE  = " + consGas.get(s));
                        System.out.println();
                    }
                });

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
