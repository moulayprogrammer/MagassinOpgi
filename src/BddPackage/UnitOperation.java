package BddPackage;

import Models.Category;
import Models.Unit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UnitOperation extends BDD<Unit> {

    @Override
    public boolean insert(Unit o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO UNIT (NAME) VALUES (?) ;";
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
    public boolean update(Unit o1, Unit o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE  UNIT SET  NAME = ? WHERE ID = ? ;";
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
    public boolean delete(Unit o) {
        return false;
    }

    @Override
    public boolean isExist(Unit o) {
        return false;
    }


    @Override
    public ArrayList<Unit> getAll() {
        connectDatabase();
        ArrayList<Unit> list = new ArrayList<>();
        String query = "SELECT * FROM UNIT WHERE ARCHIVE = 0 ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Unit unit = new Unit();
                unit.setId(resultSet.getInt("ID"));
                unit.setName(resultSet.getString("NAME"));

                list.add(unit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public Unit get(int id) {
        connectDatabase();
        Unit unit = new Unit();
        String query = "SELECT * FROM UNIT WHERE ID = ? AND ARCHIVE = 0;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                unit.setId(resultSet.getInt("ID"));
                unit.setName(resultSet.getString("NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return unit;
    }

    public boolean AddToArchive(Unit o1) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE  UNIT SET  ARCHIVE = 1 WHERE ID = ? ;";
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

    public boolean DeleteFromArchive(Unit o1) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE  UNIT SET  ARCHIVE = 0 WHERE ID = ? ;";
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

    public ArrayList<Unit> getAllArchive() {
        connectDatabase();
        ArrayList<Unit> list = new ArrayList<>();
        String query = "SELECT * FROM UNIT WHERE ARCHIVE = 1 ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Unit unit = new Unit();
                unit.setId(resultSet.getInt("ID"));
                unit.setName(resultSet.getString("NAME"));

                list.add(unit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public Unit getArchive(int id) {
        connectDatabase();
        Unit unit = new Unit();
        String query = "SELECT * FROM UNIT WHERE ID = ? AND ARCHIVE = 1;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                unit.setId(resultSet.getInt("ID"));
                unit.setName(resultSet.getString("NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return unit;
    }
}
