package BddPackage;

import Models.Decharge;
import Models.Output;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class DechargeOperation extends BDD<Decharge> {

    @Override
    public boolean insert(Decharge o) {
        return false;
    }

    public int insertId(Decharge o) {
        connectDatabase();
        int ins = 0;
        String query = "INSERT INTO DECHARGE  (ID_EMP, ID_EMP_DECH, DATE) VALUES (?,?,?);";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdEmp());
            preparedStmt.setInt(2,o.getIdEmpDech());
            preparedStmt.setDate(3, Date.valueOf(o.getDate()));

            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = preparedStmt.getGeneratedKeys().getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(Decharge o1, Decharge o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE DECHARGE SET ID_EMP = ?, ID_EMP_DECH = ?, DATE = ? WHERE ID = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getIdEmp());
            preparedStmt.setInt(2,o1.getIdEmpDech());
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
    public boolean delete(Decharge o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM DECHARGE WHERE ID = ? ;";
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
    public boolean isExist(Decharge o) {
        return false;
    }

    @Override
    public ArrayList<Decharge> getAll() {
        connectDatabase();
        ArrayList<Decharge> list = new ArrayList<>();
        String query = "SELECT *  FROM DECHARGE ; ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Decharge decharge = new Decharge();
                decharge.setId(resultSet.getInt("ID"));
                decharge.setIdEmp(resultSet.getInt("ID_EMP"));
                decharge.setIdEmpDech(resultSet.getInt("ID_EMP_DECH"));
                decharge.setDate(resultSet.getDate("DATE").toLocalDate());

                list.add(decharge);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
    public Decharge get(int id) {
        connectDatabase();
        Decharge decharge = new Decharge();
        String query = "SELECT *  FROM DECHARGE WHERE ID = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                decharge.setId(resultSet.getInt("ID"));
                decharge.setIdEmp(resultSet.getInt("ID_EMP"));
                decharge.setIdEmpDech(resultSet.getInt("ID_EMP_DECH"));
                decharge.setDate(resultSet.getDate("DATE").toLocalDate());

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return decharge;
    }

    public ArrayList<Decharge> getAllByEmp(int idEmp) {
        connectDatabase();
        ArrayList<Decharge> list = new ArrayList<>();
        String query = "SELECT *  FROM OUTPUT WHERE  ID_EMP = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idEmp);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Decharge decharge = new Decharge();
                decharge.setId(resultSet.getInt("ID"));
                decharge.setIdEmp(resultSet.getInt("ID_EMP"));
                decharge.setIdEmpDech(resultSet.getInt("ID_EMP_DECH"));
                decharge.setDate(resultSet.getDate("DATE").toLocalDate());

                list.add(decharge);
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
