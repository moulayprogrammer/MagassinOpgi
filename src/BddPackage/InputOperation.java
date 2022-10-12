package BddPackage;

import Models.Input;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class InputOperation extends BDD<Input> {

    @Override
    public boolean insert(Input o) {
        return false;
    }

    public int insertId(Input input) {
        connectDatabase();
        int ins = 0;
        String query = "INSERT INTO INPUT (ID_PROVIDER,NUMBER,DATE,NUMBER_BC,DATE_BC,NUMBER_FACTUR,DATE_FACTUR) VALUES (?,?,?,?,?,?,?) ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,input.getIdProvider());
            preparedStmt.setString(2,input.getNumber());
            preparedStmt.setDate(3, Date.valueOf(input.getDate()));
            preparedStmt.setString(4,input.getNumberBC());
            preparedStmt.setDate(5, Date.valueOf(input.getDateBC()));
            preparedStmt.setString(6,input.getNumberFact());
            preparedStmt.setDate(7, Date.valueOf(input.getDateFact()));

            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = preparedStmt.getGeneratedKeys().getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(Input o1, Input o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE INPUT SET ID_PROVIDER = ?, NUMBER = ?, DATE = ?, NUMBER_BC = ?, DATE_BC = ?, NUMBER_FACTUR = ?, DATE_FACTUR = ?  WHERE ID = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getIdProvider());
            preparedStmt.setString(2,o1.getNumber());
            preparedStmt.setDate(3, Date.valueOf(o1.getDate()));
            preparedStmt.setString(4,o1.getNumberBC());
            preparedStmt.setDate(5, Date.valueOf(o1.getDateBC()));
            preparedStmt.setString(6,o1.getNumberFact());
            preparedStmt.setDate(7, Date.valueOf(o1.getDateFact()));
            preparedStmt.setInt(8,o2.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    @Override
    public boolean delete(Input o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM INPUT WHERE ID = ? ;";
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
    public boolean isExist(Input o) {
        return false;
    }


    @Override
    public ArrayList<Input> getAll() {
        connectDatabase();
        ArrayList<Input> list = new ArrayList<>();
        String query = "SELECT * FROM INPUT ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Input input = new Input();
                input.setId(resultSet.getInt("ID"));
                input.setIdProvider(resultSet.getInt("ID_PROVIDER"));
                input.setNumber(resultSet.getString("NUMBER"));
                input.setDate(resultSet.getDate("DATE").toLocalDate());
                input.setNumberBC(resultSet.getString("NUMBER_BC"));
                input.setDateBC(resultSet.getDate("DATE_BC").toLocalDate());
                input.setNumberFact(resultSet.getString("NUMBER_FACTUR"));
                input.setDateFact(resultSet.getDate("DATE_FACTUR").toLocalDate());


                list.add(input);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public Input get(int id) {
        connectDatabase();
        Input input = new Input();
        String query = "SELECT * FROM INPUT WHERE  ID = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                input.setId(resultSet.getInt("ID"));
                input.setIdProvider(resultSet.getInt("ID_PROVIDER"));
                input.setNumber(resultSet.getString("NUMBER"));
                input.setDate(resultSet.getDate("DATE").toLocalDate());
                input.setNumberBC(resultSet.getString("NUMBER_BC"));
                input.setDateBC(resultSet.getDate("DATE_BC").toLocalDate());
                input.setNumberFact(resultSet.getString("NUMBER_FACTUR"));
                input.setDateFact(resultSet.getDate("DATE_FACTUR").toLocalDate());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return input;
    }

    public ArrayList<Input> getAllByProvider(int idProvider) {
        connectDatabase();
        ArrayList<Input> list = new ArrayList<>();
        String query = "SELECT * FROM INPUT WHERE  ID_PROVIDER = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idProvider);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Input input = new Input();
                input.setId(resultSet.getInt("ID"));
                input.setIdProvider(resultSet.getInt("ID_PROVIDER"));
                input.setNumber(resultSet.getString("NUMBER"));
                input.setDate(resultSet.getDate("DATE").toLocalDate());
                input.setNumberBC(resultSet.getString("NUMBER_BC"));
                input.setDateBC(resultSet.getDate("DATE_BC").toLocalDate());
                input.setNumberFact(resultSet.getString("NUMBER_FACTUR"));
                input.setDateFact(resultSet.getDate("DATE_FACTUR").toLocalDate());


                list.add(input);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public ArrayList<Input> getAllByDate(LocalDate dateFirst, LocalDate dateSecond) {
        connectDatabase();
        ArrayList<Input> list = new ArrayList<>();
        String query = "SELECT * FROM INPUT WHERE  DATE BETWEEN ? AND ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDate(1,Date.valueOf(dateFirst));
            preparedStmt.setDate(2,Date.valueOf(dateSecond));
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Input input = new Input();
                input.setId(resultSet.getInt("ID"));
                input.setIdProvider(resultSet.getInt("ID_PROVIDER"));
                input.setNumber(resultSet.getString("NUMBER"));
                input.setDate(resultSet.getDate("DATE").toLocalDate());
                input.setNumberBC(resultSet.getString("NUMBER_BC"));
                input.setDateBC(resultSet.getDate("DATE_BC").toLocalDate());
                input.setNumberFact(resultSet.getString("NUMBER_FACTUR"));
                input.setDateFact(resultSet.getDate("DATE_FACTUR").toLocalDate());


                list.add(input);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public ArrayList<Input> getAllByDateProvider(int idProvider, LocalDate dateFirst, LocalDate dateSecond) {
        connectDatabase();
        ArrayList<Input> list = new ArrayList<>();
        String query = "SELECT * FROM INPUT WHERE  ID_PROVIDER = ? AND DATE BETWEEN ? AND ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idProvider);
            preparedStmt.setDate(2,Date.valueOf(dateFirst));
            preparedStmt.setDate(3,Date.valueOf(dateSecond));
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Input input = new Input();
                input.setId(resultSet.getInt("ID"));
                input.setIdProvider(resultSet.getInt("ID_PROVIDER"));
                input.setNumber(resultSet.getString("NUMBER"));
                input.setDate(resultSet.getDate("DATE").toLocalDate());
                input.setNumberBC(resultSet.getString("NUMBER_BC"));
                input.setDateBC(resultSet.getDate("DATE_BC").toLocalDate());
                input.setNumberFact(resultSet.getString("NUMBER_FACTUR"));
                input.setDateFact(resultSet.getDate("DATE_FACTUR").toLocalDate());

                list.add(input);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
}
