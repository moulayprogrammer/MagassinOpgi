package BddPackage;

import Models.GasolineCard;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GasolineCardOperation extends BDD<GasolineCard> {

    @Override
    public boolean insert(GasolineCard o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO GASOLINE_CARD (NUMBER,LAST_BALANCE,LAST_RECHARGE_DATE) VALUES (?,?,?);";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o.getNumber());
            preparedStmt.setDouble(2,o.getLastBalance());
            preparedStmt.setDate(3, Date.valueOf(o.getLastRechargeDate()));
            
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeDatabase();
        }
        return ins;
    }

    public int insertId(GasolineCard o) {
        connectDatabase();
        int ins = 0;
        String query = "INSERT INTO GASOLINE_CARD (NUMBER,LAST_BALANCE,LAST_RECHARGE_DATE) VALUES (?,?,?);";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o.getNumber());
            preparedStmt.setDouble(2,o.getLastBalance());
            preparedStmt.setDate(3, Date.valueOf(o.getLastRechargeDate()));

            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = preparedStmt.getGeneratedKeys().getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeDatabase();
        }
        return ins;
    }

    @Override
    public boolean update(GasolineCard o1, GasolineCard o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE GASOLINE_CARD SET NUMBER = ? WHERE ID = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o1.getNumber());
            preparedStmt.setInt(2,o2.getId());

            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeDatabase();
        }
        return upd;
    }

    public boolean addLAST_BALANCE(GasolineCard o) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE GASOLINE_CARD SET LAST_BALANCE = LAST_BALANCE + ?, LAST_RECHARGE_DATE =  ? WHERE ID = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDouble(1,o.getLastBalance());
            preparedStmt.setDate(2,Date.valueOf(o.getLastRechargeDate()));
            preparedStmt.setInt(3,o.getId());

            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeDatabase();
        }
        return upd;
    }

    public boolean setLAST_BALANCE(GasolineCard o) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE GASOLINE_CARD SET LAST_BALANCE = ?, LAST_RECHARGE_DATE =  ?  WHERE ID = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDouble(1,o.getLastBalance());
            preparedStmt.setDate(2,Date.valueOf(o.getLastRechargeDate()));
            preparedStmt.setInt(3,o.getId());

            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeDatabase();
        }
        return upd;
    }

    @Override
    public boolean delete(GasolineCard o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM GASOLINE_CARD WHERE ID = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getId());

            int update = preparedStmt.executeUpdate();
            if(update != -1) del = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeDatabase();
        }
        return del;
    }

    @Override
    public boolean isExist(GasolineCard o) {
        return false;
    }

    @Override
    public ArrayList<GasolineCard> getAll() {
        connectDatabase();
        ArrayList<GasolineCard> list = new ArrayList<>();
        String query = "SELECT * FROM GASOLINE_CARD WHERE ARCHIVE = 0 ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                GasolineCard gasolineCard = new GasolineCard();
                gasolineCard.setId(resultSet.getInt("ID"));
                gasolineCard.setNumber(resultSet.getString("NUMBER"));
                gasolineCard.setLastBalance(resultSet.getDouble("LAST_BALANCE"));
                gasolineCard.setLastRechargeDate(resultSet.getDate("LAST_RECHARGE_DATE").toLocalDate());
                list.add(gasolineCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeDatabase();
        }
        return list;
    }

    public GasolineCard get(int id) {
        connectDatabase();
        GasolineCard gasolineCard = new GasolineCard();
        String query = "SELECT * FROM GASOLINE_CARD WHERE ID = ? AND ARCHIVE = 0";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                gasolineCard.setId(resultSet.getInt("ID"));
                gasolineCard.setId(resultSet.getInt("ID"));
                gasolineCard.setNumber(resultSet.getString("NUMBER"));
                gasolineCard.setLastBalance(resultSet.getDouble("LAST_BALANCE"));
                gasolineCard.setLastRechargeDate(resultSet.getDate("LAST_RECHARGE_DATE").toLocalDate());

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeDatabase();
        }
        return gasolineCard;
    }

    public boolean AddToArchive(GasolineCard o){
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE GASOLINE_CARD SET ARCHIVE = 1 WHERE ID = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getId());

            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeDatabase();
        }
        return upd;
    }

    public boolean DeleteFromArchive(GasolineCard o){
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE GASOLINE_CARD SET ARCHIVE = 0 WHERE ID = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getId());

            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeDatabase();
        }
        return upd;
    }

    public ArrayList<GasolineCard> getAllArchive() {
        connectDatabase();
        ArrayList<GasolineCard> list = new ArrayList<>();
        String query = "SELECT * FROM GASOLINE_CARD WHERE ARCHIVE = 1 ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                GasolineCard gasolineCard = new GasolineCard();
                gasolineCard.setId(resultSet.getInt("ID"));
                gasolineCard.setNumber(resultSet.getString("NUMBER"));
                gasolineCard.setLastBalance(resultSet.getDouble("LAST_BALANCE"));
                gasolineCard.setLastRechargeDate(resultSet.getDate("LAST_RECHARGE_DATE").toLocalDate());

                list.add(gasolineCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeDatabase();
        }
        return list;
    }

    public GasolineCard getArchive(int id) {
        connectDatabase();
        GasolineCard gasolineCard = new GasolineCard();
        String query = "SELECT * FROM GASOLINE_CARD WHERE ID = ? AND ARCHIVE = 1";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                gasolineCard.setId(resultSet.getInt("ID"));
                gasolineCard.setId(resultSet.getInt("ID"));
                gasolineCard.setNumber(resultSet.getString("NUMBER"));
                gasolineCard.setLastBalance(resultSet.getDouble("LAST_BALANCE"));
                gasolineCard.setLastRechargeDate(resultSet.getDate("LAST_RECHARGE_DATE").toLocalDate());

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeDatabase();
        }
        return gasolineCard;
    }
}
