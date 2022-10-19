package BddPackage;

import Models.RechargeGasolineCard;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RechargeGasolineCardOperation extends BDD<RechargeGasolineCard> {

    @Override
    public boolean insert(RechargeGasolineCard o) {
        connectDatabase();
        boolean ins = false;

        String query = "INSERT INTO RECHARGE_GASOLINE_CARD (NUMBER,DATE,ID_EMP,ID_CARD_GASOLINE,NUMBER_NAFTAL,PRICE,INSERT_DATE) VALUES (?,?,?,?,?,?,?) ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o.getNumber());
            preparedStmt.setDate(2,Date.valueOf(o.getDate()));
            preparedStmt.setInt(3,o.getIdEmp());
            preparedStmt.setInt(4,o.getIdGasolineCard());
            preparedStmt.setString(5,o.getNumberNaftal());
            preparedStmt.setDouble(6, o.getPrice());
            preparedStmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));

            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(RechargeGasolineCard o1, RechargeGasolineCard o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE RECHARGE_GASOLINE_CARD SET NUMBER = ?, DATE = ?, ID_EMP = ?, ID_CARD_GASOLINE = ?, NUMBER_NAFTAL = ?, PRICE = ?  WHERE ID = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            preparedStmt.setString(1,o1.getNumber());
            preparedStmt.setDate(2,Date.valueOf(o1.getDate()));
            preparedStmt.setInt(3,o1.getIdEmp());
            preparedStmt.setInt(4,o1.getIdGasolineCard());
            preparedStmt.setString(5,o1.getNumberNaftal());
            preparedStmt.setDouble(6, o1.getPrice());
            preparedStmt.setInt(7,o2.getId());

            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    @Override
    public boolean delete(RechargeGasolineCard o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM RECHARGE_GASOLINE_CARD WHERE ID = ? ;";
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
    public boolean isExist(RechargeGasolineCard o) {
        return false;
    }

    @Override
    public ArrayList<RechargeGasolineCard> getAll() {
        connectDatabase();
        ArrayList<RechargeGasolineCard> list = new ArrayList<>();
        String query = "SELECT * FROM RECHARGE_GASOLINE_CARD ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                RechargeGasolineCard gasolineCard = new RechargeGasolineCard();
                gasolineCard.setId(resultSet.getInt("ID"));
                gasolineCard.setNumber(resultSet.getString("NUMBER"));
                gasolineCard.setDate(resultSet.getDate("DATE").toLocalDate());
                gasolineCard.setNumberNaftal(resultSet.getString("NUMBER_NAFTAL"));
                gasolineCard.setIdEmp(Integer.parseInt(resultSet.getString("ID_EMP")));
                gasolineCard.setIdGasolineCard(Integer.parseInt(resultSet.getString("ID_CARD_GASOLINE")));
                gasolineCard.setPrice(resultSet.getDouble("PRICE"));


                list.add(gasolineCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public RechargeGasolineCard get(int id) {
        connectDatabase();
        RechargeGasolineCard gasolineCard = new RechargeGasolineCard();
        String query = "SELECT * FROM RECHARGE_GASOLINE_CARD WHERE  ID = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                gasolineCard.setId(resultSet.getInt("ID"));
                gasolineCard.setNumber(resultSet.getString("NUMBER"));
                gasolineCard.setDate(resultSet.getDate("DATE").toLocalDate());
                gasolineCard.setNumberNaftal(resultSet.getString("NUMBER_NAFTAL"));
                gasolineCard.setIdEmp(Integer.parseInt(resultSet.getString("ID_EMP")));
                gasolineCard.setIdGasolineCard(Integer.parseInt(resultSet.getString("ID_CARD_GASOLINE")));
                gasolineCard.setPrice(resultSet.getDouble("PRICE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return gasolineCard;
    }

    public ArrayList<RechargeGasolineCard> getAllByDate(LocalDate dateFirst, LocalDate dateSecond) {
        connectDatabase();
        ArrayList<RechargeGasolineCard> list = new ArrayList<>();
        String query = "SELECT * FROM RECHARGE_GASOLINE_CARD WHERE  DATE BETWEEN ? AND ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDate(1,Date.valueOf(dateFirst));
            preparedStmt.setDate(2,Date.valueOf(dateSecond));
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                RechargeGasolineCard gasolineCard = new RechargeGasolineCard();
                gasolineCard.setId(resultSet.getInt("ID"));
                gasolineCard.setNumber(resultSet.getString("NUMBER"));
                gasolineCard.setNumberNaftal(resultSet.getString("NUMBER_NAFTAL"));
                gasolineCard.setDate(resultSet.getDate("DATE").toLocalDate());
                gasolineCard.setIdEmp(Integer.parseInt(resultSet.getString("ID_EMP")));
                gasolineCard.setIdGasolineCard(Integer.parseInt(resultSet.getString("ID_CARD_GASOLINE")));
                gasolineCard.setPrice(resultSet.getDouble("PRICE"));

                list.add(gasolineCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

}
