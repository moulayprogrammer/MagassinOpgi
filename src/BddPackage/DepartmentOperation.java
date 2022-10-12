package BddPackage;

import Models.Department;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DepartmentOperation extends BDD<Department> {

    @Override
    public boolean insert(Department o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO DEP (NAME) VALUES (?)";
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
    public boolean update(Department o1, Department o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE DEP SET NAME = ? WHERE ID = ?;";
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
    public boolean delete(Department o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM DEP WHERE ID = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getId());

            int update = preparedStmt.executeUpdate();
            if(update != -1) del = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return del;
    }

    @Override
    public boolean isExist(Department o) {
        return false;
    }

    @Override
    public ArrayList<Department> getAll() {
        connectDatabase();
        ArrayList<Department> list = new ArrayList<>();
        String query = "SELECT * FROM  DEP WHERE ARCHIVE = 0;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Department department = new Department();
                department.setId(resultSet.getInt("ID"));
                department.setName(resultSet.getString("NAME"));

                list.add(department);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public Department get(int id) {
        connectDatabase();
        Department department = new Department();
        String query = "SELECT * FROM  DEP WHERE ID = ? AND ARCHIVE = 0 ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                department.setId(resultSet.getInt("ID"));
                department.setName(resultSet.getString("NAME"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return department;
    }

    public boolean AddToArchive(Department department){
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE DEP SET ARCHIVE = 1 WHERE ID = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,department.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    public boolean DeleteFromArchive(Department department){
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE DEP SET ARCHIVE = 0 WHERE ID = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(2,department.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    public Department getArchive(int id) {
        connectDatabase();
        Department department = new Department();
        String query = "SELECT * FROM  DEP WHERE ID = ? AND ARCHIVE = 1 ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                department.setId(resultSet.getInt("ID"));
                department.setName(resultSet.getString("NAME"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return department;
    }

    public ArrayList<Department> getAllArchive() {
        connectDatabase();
        ArrayList<Department> list = new ArrayList<>();
        String query = "SELECT * FROM  DEP WHERE ARCHIVE = 1;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Department department = new Department();
                department.setId(resultSet.getInt("ID"));
                department.setName(resultSet.getString("NAME"));

                list.add(department);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

}
