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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.controlsfx.control.ListSelectionView;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class ConsumptionStatusController implements Initializable {

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

                HashMap<Integer,List<List<StringProperty>>> cons = new HashMap<>();
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
                            System.out.println("serv = " + services.get(indexServ).getId());
                            ResultSet resultSet = preparedStmt.executeQuery();

                            while (resultSet.next()){
                                cons.put(resultSet.getInt("ID"),new ArrayList<>());
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else {
                        // select dep
                        try {
                            String query = "SELECT OUTPUT.ID FROM OUTPUT,EMPLOYEE,SERVICE WHERE OUTPUT.DATE = ? " +
                                    "AND SERVICE.ID_DEP = ? AND EMPLOYEE.ID_SERVICE = SERVICE.ID AND OUTPUT.ID_EMP = EMPLOYEE.ID ;";
                            PreparedStatement preparedStmt = conn.prepareStatement(query);
                            preparedStmt.setDate(1, Date.valueOf(date));
                            preparedStmt.setInt(2,departments.get(indexDep).getId());
                            ResultSet resultSet = preparedStmt.executeQuery();

                            while (resultSet.next()){
                                cons.put(resultSet.getInt("ID"),new ArrayList<>());
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
                            cons.put(resultSet.getInt("ID"),new ArrayList<>());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                if (cons.size() != 0){
                    cons.forEach((s, stringProperties) -> {
                        try {
                            List<List<StringProperty>> data = new ArrayList<>();
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

                                    d.add(new SimpleStringProperty(resultSet.getString("NUMBER")));
                                    d.add(new SimpleStringProperty(categories.get(index).getName()));
                                    d.add(new SimpleStringProperty(String.valueOf(resultSet.getDouble("TOT"))));

                                    data.add(d);
                                }
                            }
                            cons.put(s,data);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    });
                }

                cons.forEach((integer, lists) -> {
                    System.out.println("output = " + lists.get(0).get(0).getValue());
                    for (List<StringProperty> list : lists){
                        System.out.print("Cat = " + list.get(1).getValue());
                        System.out.print("    Tot = " + list.get(2).getValue());
                        System.out.print("    ||");
                    }
                    System.out.println();
                });



                /*for (String cat : lsCategorySelected){
                    int index = categoryList.indexOf(cat);
                    int idCat = categories.get(index).getId();

                    String query = "SELECT OUTPUT.NUMBER, \n" +
                            "(SELECT SUM(STORE_CARD.PRICE * COMPONENT_OUTPUT.QTE_SERV) FROM STORE_CARD, COMPONENT_OUTPUT, ARTICLE \n" +
                            "WHERE ARTICLE.ID_CAT = ? AND COMPONENT_OUTPUT.ID_ART = ARTICLE.ID AND COMPONENT_OUTPUT.ID_OUTPUT = OUTPUT.ID \n" +
                            "AND COMPONENT_OUTPUT.ID_STORE = STORE_CARD.ID) AS TOT_OUTPUT FROM OUTPUT WHERE OUTPUT.DATE = ?;";

                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1,idCat);
                    preparedStmt.setDate(2, Date.valueOf(date));
                    ResultSet resultSet = preparedStmt.executeQuery();
                    while (resultSet.next()){
                        List<StringProperty> d = new ArrayList<>();

                        d.add( new SimpleStringProperty(resultSet.getString("NUMBER")));
                        d.add( new SimpleStringProperty(categories.get(index).getName()));
                        d.add( new SimpleStringProperty(String.valueOf(resultSet.getDouble("TOT_OUTPUT"))));

                        data.add(d);
                    }
                }*/


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
