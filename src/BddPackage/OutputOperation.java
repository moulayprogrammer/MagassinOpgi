package BddPackage;

import Models.Output;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class OutputOperation extends BDD<Output> {

    @Override
    public boolean insert(Output o) {
        return false;
    }

    public int insertId(Output output) {
        connectDatabase();
        int ins = 0;
        String query = "INSERT INTO OUTPUT  (ID_EMP, NUMBER, DATE, INSERT_DATE) VALUES (?,?,?,?);";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,output.getIdEmp());
            preparedStmt.setString(2,output.getNumber());
            preparedStmt.setDate(3, Date.valueOf(output.getDate()));
            preparedStmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = preparedStmt.getGeneratedKeys().getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(Output o1, Output o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE OUTPUT SET ID_EMP = ?, NUMBER = ?, DATE = ? WHERE ID = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getIdEmp());
            preparedStmt.setString(2,o1.getNumber());
            preparedStmt.setDate(3,Date.valueOf(o1.getDate()));
            preparedStmt.setInt(4,o2.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    @Override
    public boolean delete(Output o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM OUTPUT WHERE ID = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getId());

            int delete = preparedStmt.executeUpdate();
            if(delete != -1) del = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return del;
    }

    @Override
    public boolean isExist(Output o) {
        return false;
    }

    @Override
    public ArrayList<Output> getAll() {
        connectDatabase();
        ArrayList<Output> list = new ArrayList<>();
        String query = "SELECT *  FROM OUTPUT ; ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Output output = new Output();
                output.setId(resultSet.getInt("ID"));
                output.setIdEmp(resultSet.getInt("ID_EMP"));
                output.setNumber(resultSet.getString("NUMBER"));
                output.setDate(resultSet.getDate("DATE").toLocalDate());

                list.add(output);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
    public Output get(int id) {
        connectDatabase();
        Output output = new Output();
        String query = "SELECT *  FROM OUTPUT WHERE ID = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){


                output.setId(resultSet.getInt("ID"));
                output.setIdEmp(resultSet.getInt("ID_EMP"));
                output.setNumber(resultSet.getString("NUMBER"));
                output.setDate(resultSet.getDate("DATE").toLocalDate());

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return output;
    }

    public ArrayList<Output> getAllByEmp(int idEmp) {
        connectDatabase();
        ArrayList<Output> list = new ArrayList<>();
        String query = "SELECT *  FROM OUTPUT WHERE  ID_EMP = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idEmp);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Output output = new Output();
                output.setId(resultSet.getInt("ID"));
                output.setIdEmp(resultSet.getInt("ID_EMP"));
                output.setNumber(resultSet.getString("NUMBER"));
                output.setDate(resultSet.getDate("DATE").toLocalDate());

                list.add(output);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public ArrayList<Output> getAllByDate(LocalDate dateFirst, LocalDate dateSecond) {
        connectDatabase();
        ArrayList<Output> list = new ArrayList<>();
        String query = "SELECT *  FROM OUTPUT WHERE DATE BETWEEN ? AND ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDate(1,Date.valueOf(dateFirst));
            preparedStmt.setDate(2,Date.valueOf(dateSecond));

            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Output output = new Output();
                output.setId(resultSet.getInt("ID"));
                output.setIdEmp(resultSet.getInt("ID_EMP"));
                output.setNumber(resultSet.getString("NUMBER"));
                output.setDate(resultSet.getDate("DATE").toLocalDate());

                list.add(output);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public ArrayList<Output> getAllByDateEmp(int idEmp, LocalDate dateFirst, LocalDate dateSecond) {
        connectDatabase();
        ArrayList<Output> list = new ArrayList<>();
        String query = "SELECT * FROM OUTPUT WHERE ID_EMP = ? AND DATE BETWEEN ? AND ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idEmp);
            preparedStmt.setDate(2,Date.valueOf(dateFirst));
            preparedStmt.setDate(3,Date.valueOf(dateSecond));
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Output output = new Output();
                output.setId(resultSet.getInt("ID"));
                output.setIdEmp(resultSet.getInt("ID_EMP"));
                output.setNumber(resultSet.getString("NUMBER"));
                output.setDate(resultSet.getDate("DATE").toLocalDate());

                list.add(output);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
}
