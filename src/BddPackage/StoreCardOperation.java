package BddPackage;


import Models.StoreCard;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StoreCardOperation extends BDD<StoreCard> {

    @Override
    public boolean insert(StoreCard o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO STORE_CARD (ID_INPUT, ID_ARTICLE, DATE_STORE , QTE_STORED , QTE_CONSUMED, PRICE ) VALUES (?,?,?,?,?,?) ; ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdInput());
            preparedStmt.setInt(2,o.getIdArticle());
            preparedStmt.setDate(3, Date.valueOf(o.getDateStore()));
            preparedStmt.setInt(4,o.getQteStored());
            preparedStmt.setInt(5,o.getQteConsumed());
            preparedStmt.setDouble(6,o.getPrice());

            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(StoreCard o1, StoreCard o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE STORE_CARD SET DATE_STORE = ?, PRICE = ? , QTE_STORED = ? , QTE_CONSUMED = ? WHERE ID = ?";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDate(1,Date.valueOf(o1.getDateStore()));
            preparedStmt.setDouble(2,o1.getPrice());
            preparedStmt.setInt(3,o1.getQteStored());
            preparedStmt.setInt(4,o1.getQteConsumed());
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
    public boolean delete(StoreCard o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM STORE_CARD WHERE ID = ? ;";
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

    public boolean deleteByInput(int idInput) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM STORE_CARD WHERE ID_INPUT = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idInput);

            int update = preparedStmt.executeUpdate();
            if(update != -1) del = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return del;
    }

    @Override
    public boolean isExist(StoreCard o) {
        return false;
    }

    public boolean addQteConsumed(StoreCard o) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE STORE_CARD SET QTE_CONSUMED = QTE_CONSUMED + ? WHERE ID = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getQteConsumed());
            preparedStmt.setInt(2,o.getId());

            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    public boolean subQteConsumed(StoreCard o) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE STORE_CARD SET QTE_CONSUMED = QTE_CONSUMED - ? WHERE ID = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getQteConsumed());
            preparedStmt.setInt(2,o.getId());

            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    @Override
    public ArrayList<StoreCard> getAll() {
        connectDatabase();
        ArrayList<StoreCard> list = new ArrayList<>();
        String query = "SELECT * FROM STORE_CARD ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                StoreCard storeCard = new StoreCard();
                storeCard.setId(resultSet.getInt("ID"));
                storeCard.setIdInput(resultSet.getInt("ID_INPUT"));
                storeCard.setIdArticle(resultSet.getInt("ID_ARTICLE"));
                storeCard.setDateStore(resultSet.getDate("DATE_STORE").toLocalDate());
                storeCard.setQteStored(resultSet.getInt("QTE_STORED"));
                storeCard.setQteConsumed(resultSet.getInt("QTE_CONSUMED"));
                storeCard.setPrice(resultSet.getDouble("PRICE"));

                list.add(storeCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public StoreCard get(int id) {
        connectDatabase();
        StoreCard storeCard = new StoreCard();
        String query = "SELECT * FROM STORE_CARD  WHERE ID = ? ; ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                storeCard.setId(resultSet.getInt("ID"));
                storeCard.setIdInput(resultSet.getInt("ID_INPUT"));
                storeCard.setIdArticle(resultSet.getInt("ID_ARTICLE"));
                storeCard.setDateStore(resultSet.getDate("DATE_STORE").toLocalDate());
                storeCard.setQteStored(resultSet.getInt("QTE_STORED"));
                storeCard.setQteConsumed(resultSet.getInt("QTE_CONSUMED"));
                storeCard.setPrice(resultSet.getDouble("PRICE"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return storeCard;
    }

    public ArrayList<StoreCard> getAllByInput(int idInput) {
        connectDatabase();
        ArrayList<StoreCard> list = new ArrayList<>();
        String query = "SELECT * FROM STORE_CARD WHERE ID_INPUT = ?";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                StoreCard storeCard = new StoreCard();
                storeCard.setId(resultSet.getInt("ID"));
                storeCard.setIdInput(resultSet.getInt("ID_INPUT"));
                storeCard.setIdArticle(resultSet.getInt("ID_ARTICLE"));
                storeCard.setDateStore(resultSet.getDate("DATE_STORE").toLocalDate());
                storeCard.setQteStored(resultSet.getInt("QTE_STORED"));
                storeCard.setQteConsumed(resultSet.getInt("QTE_CONSUMED"));
                storeCard.setPrice(resultSet.getDouble("PRICE"));

                list.add(storeCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public ArrayList<StoreCard> getAllByArticleOrderByDate(int idArt) {
        connectDatabase();
        ArrayList<StoreCard> list = new ArrayList<>();
        String query = "SELECT * FROM STORE_CARD WHERE ID_ARTICLE = ?  AND (QTE_STORED - QTE_CONSUMED) > 0  ORDER BY DATE_STORE DESC;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idArt);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                StoreCard storeCard = new StoreCard();
                storeCard.setId(resultSet.getInt("ID"));
                storeCard.setIdInput(resultSet.getInt("ID_INPUT"));
                storeCard.setIdArticle(resultSet.getInt("ID_ARTICLE"));
                storeCard.setDateStore(resultSet.getDate("DATE_STORE").toLocalDate());
                storeCard.setQteStored(resultSet.getInt("QTE_STORED"));
                storeCard.setQteConsumed(resultSet.getInt("QTE_CONSUMED"));
                storeCard.setPrice(resultSet.getDouble("PRICE"));

                list.add(storeCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
}
