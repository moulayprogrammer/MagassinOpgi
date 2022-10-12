package BddPackage;



import Models.Employee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeOperation extends BDD<Employee> {

    @Override
    public boolean insert(Employee o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO EMPLOYEE (ID_SERVICE, FIRST_NAME, LAST_NAME, FUNCTION) VALUES (?,?,?,?)";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            preparedStmt.setInt(1,o.getIdService());
            preparedStmt.setString(2,o.getFirstName());
            preparedStmt.setString(3,o.getLastName());
            preparedStmt.setString(4,o.getFunction());

            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(Employee o1, Employee o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE EMPLOYEE SET ID_SERVICE = ?, FIRST_NAME = ?, LAST_NAME = ?, FUNCTION = ? WHERE ID = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getIdService());
            preparedStmt.setString(2,o1.getFirstName());
            preparedStmt.setString(3,o1.getLastName());
            preparedStmt.setString(4,o1.getFunction());
            preparedStmt.setInt(5,o2.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    @Override
    public boolean delete(Employee o) {
        return false;
    }

    @Override
    public boolean isExist(Employee o) {
        return false;
    }

    @Override
    public ArrayList<Employee> getAll() {
        connectDatabase();
        ArrayList<Employee> list = new ArrayList<>();
        String query = "SELECT * FROM  EMPLOYEE ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Employee employee = new Employee();
                employee.setId(resultSet.getInt("ID"));
                employee.setIdService(resultSet.getInt("ID_SERVICE"));
                employee.setFirstName(resultSet.getString("FIRST_NAME"));
                employee.setLastName(resultSet.getString("LAST_NAME"));
                employee.setFunction(resultSet.getString("FUNCTION"));

                list.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public Employee get(int id) {
        connectDatabase();
        Employee employee = new Employee();
        String query = "SELECT * FROM  EMPLOYEE WHERE ID = ? AND ARCHIVE = 0;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            if (resultSet.next()){

                employee.setId(resultSet.getInt("ID"));
                employee.setIdService(resultSet.getInt("ID_SERVICE"));
                employee.setFirstName(resultSet.getString("FIRST_NAME"));
                employee.setLastName(resultSet.getString("LAST_NAME"));
                employee.setFunction(resultSet.getString("FUNCTION"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return employee;
    }

    public Employee getArchive(int id) {
        connectDatabase();
        Employee employee = new Employee();
        String query = "SELECT * FROM  EMPLOYEE WHERE ID = ? AND ARCHIVE = 1;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            if (resultSet.next()){

                employee.setId(resultSet.getInt("ID"));
                employee.setIdService(resultSet.getInt("ID_SERVICE"));
                employee.setFirstName(resultSet.getString("FIRST_NAME"));
                employee.setLastName(resultSet.getString("LAST_NAME"));
                employee.setFunction(resultSet.getString("FUNCTION"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return employee;
    }

    public boolean AddToArchive(Employee o) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE EMPLOYEE SET ARCHIVE = 1 WHERE ID = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    public boolean DeleteFromArchive(Employee o) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE EMPLOYEE SET ARCHIVE = 0 WHERE ID = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }
}
