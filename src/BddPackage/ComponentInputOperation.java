package BddPackage;

import Models.ComponentInput;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComponentInputOperation extends BDD<ComponentInput> {


    @Override
    public boolean insert(ComponentInput o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO COMPONENT_INPUT (ID_INPUT,ID_ARTICLE,QTE,PRICE) VALUES (?,?,?,?) ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdInput());
            preparedStmt.setInt(2,o.getIdArticle());
            preparedStmt.setInt(3,o.getQte());
            preparedStmt.setDouble(4,o.getPrice());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(ComponentInput o1, ComponentInput o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE COMPONENT_INPUT SET QTE = ? , PRICE = ? WHERE ID_INPUT = ? AND ID_ARTICLE = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getQte());
            preparedStmt.setDouble(2,o1.getPrice());
            preparedStmt.setInt(3,o2.getIdInput());
            preparedStmt.setInt(4,o2.getIdArticle());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    @Override
    public boolean delete(ComponentInput o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM COMPONENT_INPUT WHERE ID_INPUT = ? AND ID_ARTICLE = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdInput());
            preparedStmt.setInt(2,o.getIdArticle());

            int update = preparedStmt.executeUpdate();
            if(update != -1) del = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return del;
    }

    public boolean deleteByInput(int idInput) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM COMPONENT_INPUT WHERE ID_INPUT = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idInput);

            int update = preparedStmt.executeUpdate();
            if(update != -1) del = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return del;
    }

    @Override
    public boolean isExist(ComponentInput o) {
        return false;
    }


    @Override
    public ArrayList<ComponentInput> getAll() {
        connectDatabase();
        ArrayList<ComponentInput> list = new ArrayList<>();
        String query = "SELECT * FROM COMPONENT_INPUT ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                ComponentInput input = new ComponentInput();
                input.setIdInput(resultSet.getInt("ID_INPUT"));
                input.setIdArticle(resultSet.getInt("ID_ARTICLE"));
                input.setQte(resultSet.getInt("QTE"));
                input.setPrice(resultSet.getDouble("PRICE"));


                list.add(input);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public ArrayList<ComponentInput> getAllByInput(int idInput) {
        ArrayList<ComponentInput> list = new ArrayList<>();
        String query = "SELECT * FROM COMPONENT_INPUT WHERE  ID_INPUT = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idInput);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                ComponentInput input = new ComponentInput();
                input.setIdInput(resultSet.getInt("ID_INPUT"));
                input.setIdArticle(resultSet.getInt("ID_ARTICLE"));
                input.setQte(resultSet.getInt("QTE"));
                input.setPrice(resultSet.getDouble("PRICE"));


                list.add(input);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
