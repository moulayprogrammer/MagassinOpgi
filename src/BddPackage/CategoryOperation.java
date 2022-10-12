package BddPackage;

import Models.Category;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryOperation extends BDD<Category> {

    @Override
    public boolean insert(Category o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO CATEGORY (NAME) VALUES (?) ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o.getName());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(Category o1, Category o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE  CATEGORY SET  NAME = ? WHERE ID = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o1.getName());
            preparedStmt.setInt(2,o2.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    @Override
    public boolean delete(Category o) {
        return false;
    }

    @Override
    public boolean isExist(Category o) {
        return false;
    }

    @Override
    public ArrayList<Category> getAll() {
        connectDatabase();
        ArrayList<Category> list = new ArrayList<>();
        String query = "SELECT * FROM CATEGORY WHERE ARCHIVE = 0;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Category category = new Category();
                category.setId(resultSet.getInt("ID"));
                category.setName(resultSet.getString("NAME"));

                list.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public Category get(int id) {
        connectDatabase();
        Category category = new Category();
        String query = "SELECT * FROM CATEGORY WHERE ID = ? AND ARCHIVE = 0;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                category.setId(resultSet.getInt("ID"));
                category.setName(resultSet.getString("NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return category;
    }

    public boolean AddToArchive(Category o1) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE  CATEGORY SET  ARCHIVE = 1 WHERE ID = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    public boolean DeleteFromArchive(Category o1) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE  CATEGORY SET  ARCHIVE = 0 WHERE ID = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    public ArrayList<Category> getAllArchive() {
        connectDatabase();
        ArrayList<Category> list = new ArrayList<>();
        String query = "SELECT * FROM CATEGORY WHERE ARCHIVE = 1;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Category category = new Category();
                category.setId(resultSet.getInt("ID"));
                category.setName(resultSet.getString("NAME"));

                list.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public Category getArchive(int id) {
        connectDatabase();
        Category category = new Category();
        String query = "SELECT * FROM CATEGORY WHERE ID = ? AND ARCHIVE = 1;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                category.setId(resultSet.getInt("ID"));
                category.setName(resultSet.getString("NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return category;
    }
}
