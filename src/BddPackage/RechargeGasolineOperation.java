package BddPackage;

import Models.RechargeGasoline;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class RechargeGasolineOperation extends BDD<RechargeGasoline> {

    @Override
    public boolean insert(RechargeGasoline o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO RECHARGE_GASOLINE (ID_PROVIDER,NUMBER,DATE,NUMBER_BC,DATE_BC,NUMBER_FACTUR,DATE_FACTUR,PRICE) VALUES (?,?,?,?,?,?,?,?) ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdProvider());
            preparedStmt.setString(2,o.getNumber());
            preparedStmt.setDate(3, Date.valueOf(o.getDate()));
            preparedStmt.setString(4,o.getNumberBC());
            preparedStmt.setDate(5, Date.valueOf(o.getDateBC()));
            preparedStmt.setString(6,o.getNumberFact());
            preparedStmt.setDate(7, Date.valueOf(o.getDateFact()));
            preparedStmt.setDouble(8, o.getPrice());

            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(RechargeGasoline o1, RechargeGasoline o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE RECHARGE_GASOLINE SET ID_PROVIDER = ?, NUMBER = ?, DATE = ?, NUMBER_BC = ?, DATE_BC = ?, NUMBER_FACTUR = ?, DATE_FACTUR = ?, PRICE = ?  WHERE ID = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getIdProvider());
            preparedStmt.setString(2,o1.getNumber());
            preparedStmt.setDate(3, Date.valueOf(o1.getDate()));
            preparedStmt.setString(4,o1.getNumberBC());
            preparedStmt.setDate(5, Date.valueOf(o1.getDateBC()));
            preparedStmt.setString(6,o1.getNumberFact());
            preparedStmt.setDate(7, Date.valueOf(o1.getDateFact()));
            preparedStmt.setDouble(8, o1.getPrice());
            preparedStmt.setInt(9,o2.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    @Override
    public boolean delete(RechargeGasoline o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM RECHARGE_GASOLINE WHERE ID = ? ;";
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
    public boolean isExist(RechargeGasoline o) {
        return false;
    }

    @Override
    public ArrayList<RechargeGasoline> getAll() {
        connectDatabase();
        ArrayList<RechargeGasoline> list = new ArrayList<>();
        String query = "SELECT * FROM RECHARGE_GASOLINE ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                RechargeGasoline rechargeGasoline = new RechargeGasoline();
                rechargeGasoline.setId(resultSet.getInt("ID"));
                rechargeGasoline.setIdProvider(resultSet.getInt("ID_PROVIDER"));
                rechargeGasoline.setNumber(resultSet.getString("NUMBER"));
                rechargeGasoline.setDate(resultSet.getDate("DATE").toLocalDate());
                rechargeGasoline.setNumberBC(resultSet.getString("NUMBER_BC"));
                rechargeGasoline.setDateBC(resultSet.getDate("DATE_BC").toLocalDate());
                rechargeGasoline.setNumberFact(resultSet.getString("NUMBER_FACTUR"));
                rechargeGasoline.setDateFact(resultSet.getDate("DATE_FACTUR").toLocalDate());
                rechargeGasoline.setPrice(resultSet.getDouble("PRICE"));


                list.add(rechargeGasoline);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public RechargeGasoline get(int id) {
        connectDatabase();
        RechargeGasoline gasoline = new RechargeGasoline();
        String query = "SELECT * FROM RECHARGE_GASOLINE WHERE  ID = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                gasoline.setId(resultSet.getInt("ID"));
                gasoline.setIdProvider(resultSet.getInt("ID_PROVIDER"));
                gasoline.setNumber(resultSet.getString("NUMBER"));
                gasoline.setDate(resultSet.getDate("DATE").toLocalDate());
                gasoline.setNumberBC(resultSet.getString("NUMBER_BC"));
                gasoline.setDateBC(resultSet.getDate("DATE_BC").toLocalDate());
                gasoline.setNumberFact(resultSet.getString("NUMBER_FACTUR"));
                gasoline.setDateFact(resultSet.getDate("DATE_FACTUR").toLocalDate());
                gasoline.setPrice(resultSet.getDouble("PRICE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return gasoline;
    }

    public ArrayList<RechargeGasoline> getAllByProvider(int idProvider) {
        connectDatabase();
        ArrayList<RechargeGasoline> list = new ArrayList<>();
        String query = "SELECT * FROM RECHARGE_GASOLINE WHERE  ID_PROVIDER = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idProvider);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                RechargeGasoline gasoline = new RechargeGasoline();
                gasoline.setId(resultSet.getInt("ID"));
                gasoline.setIdProvider(resultSet.getInt("ID_PROVIDER"));
                gasoline.setNumber(resultSet.getString("NUMBER"));
                gasoline.setDate(resultSet.getDate("DATE").toLocalDate());
                gasoline.setNumberBC(resultSet.getString("NUMBER_BC"));
                gasoline.setDateBC(resultSet.getDate("DATE_BC").toLocalDate());
                gasoline.setNumberFact(resultSet.getString("NUMBER_FACTUR"));
                gasoline.setDateFact(resultSet.getDate("DATE_FACTUR").toLocalDate());
                gasoline.setPrice(resultSet.getDouble("PRICE"));

                list.add(gasoline);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public ArrayList<RechargeGasoline> getAllByDate(LocalDate dateFirst, LocalDate dateSecond) {
        connectDatabase();
        ArrayList<RechargeGasoline> list = new ArrayList<>();
        String query = "SELECT * FROM RECHARGE_GASOLINE WHERE  DATE BETWEEN ? AND ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDate(1,Date.valueOf(dateFirst));
            preparedStmt.setDate(2,Date.valueOf(dateSecond));
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                RechargeGasoline gasoline = new RechargeGasoline();
                gasoline.setId(resultSet.getInt("ID"));
                gasoline.setIdProvider(resultSet.getInt("ID_PROVIDER"));
                gasoline.setNumber(resultSet.getString("NUMBER"));
                gasoline.setDate(resultSet.getDate("DATE").toLocalDate());
                gasoline.setNumberBC(resultSet.getString("NUMBER_BC"));
                gasoline.setDateBC(resultSet.getDate("DATE_BC").toLocalDate());
                gasoline.setNumberFact(resultSet.getString("NUMBER_FACTUR"));
                gasoline.setDateFact(resultSet.getDate("DATE_FACTUR").toLocalDate());
                gasoline.setPrice(resultSet.getDouble("PRICE"));

                list.add(gasoline);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public ArrayList<RechargeGasoline> getAllByDateProvider(int idProvider, LocalDate dateFirst, LocalDate dateSecond) {
        connectDatabase();
        ArrayList<RechargeGasoline> list = new ArrayList<>();
        String query = "SELECT * FROM RECHARGE_GASOLINE WHERE  ID_PROVIDER = ? AND DATE BETWEEN ? AND ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idProvider);
            preparedStmt.setDate(2,Date.valueOf(dateFirst));
            preparedStmt.setDate(3,Date.valueOf(dateSecond));
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                RechargeGasoline gasoline = new RechargeGasoline();
                gasoline.setId(resultSet.getInt("ID"));
                gasoline.setIdProvider(resultSet.getInt("ID_PROVIDER"));
                gasoline.setNumber(resultSet.getString("NUMBER"));
                gasoline.setDate(resultSet.getDate("DATE").toLocalDate());
                gasoline.setNumberBC(resultSet.getString("NUMBER_BC"));
                gasoline.setDateBC(resultSet.getDate("DATE_BC").toLocalDate());
                gasoline.setNumberFact(resultSet.getString("NUMBER_FACTUR"));
                gasoline.setDateFact(resultSet.getDate("DATE_FACTUR").toLocalDate());
                gasoline.setPrice(resultSet.getDouble("PRICE"));

                list.add(gasoline);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
}
