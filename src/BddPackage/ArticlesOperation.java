package BddPackage;




import Models.Article;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ArticlesOperation extends BDD<Article> {

    @Override
    public boolean insert(Article o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO ARTICLE (ID_CAT,ID_UNIT,NAME,QTE_ALERT) VALUES (?,?,?,?) ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdCategory());
            preparedStmt.setInt(2,o.getIdUnit());
            preparedStmt.setString(3,o.getName());
            preparedStmt.setInt(4,o.getQteAlert());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    public int insertId(Article o) {
        connectDatabase();
        int ins = 0;
        String query = "INSERT INTO ARTICLE (ID_CAT,ID_UNIT,NAME,QTE_ALERT) VALUES (?,?,?,?) ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdCategory());
            preparedStmt.setInt(2,o.getIdUnit());
            preparedStmt.setString(3,o.getName());
            preparedStmt.setInt(4,o.getQteAlert());

            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = preparedStmt.getGeneratedKeys().getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(Article o1, Article o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE ARTICLE SET ID_CAT = ?, ID_UNIT = ?, NAME = ?, QTE_ALERT = ? WHERE ID = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getIdCategory());
            preparedStmt.setInt(2,o1.getIdUnit());
            preparedStmt.setString(3,o1.getName());
            preparedStmt.setInt(4,o1.getQteAlert());
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
    public boolean delete(Article o) {
        return false;
    }

    @Override
    public boolean isExist(Article o) {
        return false;
    }

    @Override
    public ArrayList<Article> getAll() {
        connectDatabase();
        ArrayList<Article> list = new ArrayList<>();
        String query = "SELECT * FROM ARTICLE WHERE ARCHIVE = 0;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Article article = new Article();
                article.setId(resultSet.getInt("ID"));
                article.setIdCategory(resultSet.getInt("ID_CAT"));
                article.setIdUnit(resultSet.getInt("ID_UNIT"));
                article.setName(resultSet.getString("NAME"));
                article.setQteAlert(resultSet.getInt("QTE_ALERT"));

                list.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
    public Article get(int id) {
        connectDatabase();
        Article article = new Article();
        String query = "SELECT ARTICLE.ID, ARTICLE.ID_CAT, ARTICLE.ID_UNIT, ARTICLE.NAME, ARTICLE.QTE_ALERT,\n" +
                "(SELECT SUM(STORE_CARD.QTE_STORED - STORE_CARD.QTE_CONSUMED) FROM STORE_CARD WHERE STORE_CARD.ID_ARTICLE = ARTICLE.ID) AS QTE\n" +
                "FROM ARTICLE WHERE ID = ? AND ARTICLE.ARCHIVE = 0 ; ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            if (resultSet.next()){

                article.setId(resultSet.getInt("ID"));
                article.setIdCategory(resultSet.getInt("ID_CAT"));
                article.setIdUnit(resultSet.getInt("ID_UNIT"));
                article.setName(resultSet.getString("NAME"));
                article.setQte(resultSet.getInt("QTE"));
                article.setQteAlert(resultSet.getInt("QTE_ALERT"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return  article;
    }

    public boolean AddToArchive(Article o) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE ARTICLE SET ARCHIVE = 1 WHERE ID = ?;";
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

    public boolean DeleteFromArchive(Article o) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE ARTICLE SET ARCHIVE = 0 WHERE ID = ?;";
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

    public Article getArchive(int id) {
        connectDatabase();
        Article article = new Article();
        String query = "SELECT * FROM ARTICLE WHERE ID = ? AND ARCHIVE = 1 ; ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            if (resultSet.next()){

                article.setId(resultSet.getInt("ID"));
                article.setIdCategory(resultSet.getInt("ID_CAT"));
                article.setIdUnit(resultSet.getInt("ID_UNIT"));
                article.setName(resultSet.getString("NAME"));
                article.setQteAlert(resultSet.getInt("QTE_ALERT"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return  article;
    }


    public ArrayList<Article> getAllWithCatUnitAndQteNotNull() {
        connectDatabase();
        ArrayList<Article> list = new ArrayList<>();
        String query = "SELECT ARTICLE.ID, ARTICLE.NAME, CATEGORY.NAME AS NAME_CAT, UNIT.NAME AS NAME_UNIT,\n" +
                "(SELECT SUM(STORE_CARD.QTE_STORED - STORE_CARD.QTE_CONSUMED) FROM STORE_CARD WHERE STORE_CARD.ID_ARTICLE = ARTICLE.ID) AS QTE\n" +
                "FROM ARTICLE, CATEGORY, UNIT WHERE QTE != 0 AND ARTICLE.ID_CAT = CATEGORY.ID AND ARTICLE.ID_UNIT = UNIT.ID  AND ARTICLE.ARCHIVE = 0;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Article article = new Article();
                article.setId(resultSet.getInt("ID"));
                article.setName(resultSet.getString("NAME"));
                article.setQte(resultSet.getInt("QTE"));
                article.setCat(resultSet.getString("NAME_CAT"));
                article.setUnit(resultSet.getString("NAME_UNIT"));

                list.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public ArrayList<Article> getAllWithCat() {
        connectDatabase();
        ArrayList<Article> list = new ArrayList<>();
        String query = "SELECT ARTICLE.ID, ARTICLE.NAME, CATEGORY.NAME AS NAME_CAT, UNIT.NAME AS NAME_UNIT,\n" +
                "(SELECT SUM(STORE_CARD.QTE_STORED - STORE_CARD.QTE_CONSUMED) FROM STORE_CARD WHERE STORE_CARD.ID_ARTICLE = ARTICLE.ID) AS QTE\n" +
                "FROM ARTICLE, CATEGORY, UNIT WHERE ARTICLE.ID_CAT = CATEGORY.ID AND ARTICLE.ID_UNIT = UNIT.ID  AND ARTICLE.ARCHIVE = 0;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Article article = new Article();
                article.setId(resultSet.getInt("ID"));
                article.setName(resultSet.getString("NAME"));
                article.setQte(resultSet.getInt("QTE"));
                article.setCat(resultSet.getString("NAME_CAT"));

                list.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public Article getWithCatUnit(int id) {
        connectDatabase();
        Article article = new Article();
        String query = "SELECT ARTICLE.ID, ARTICLE.NAME, CATEGORY.NAME AS NAME_CAT, UNIT.NAME AS NAME_UNIT FROM ARTICLE, CATEGORY, UNIT \n" +
                "WHERE ARTICLE.ID = ? AND ARTICLE.ID_CAT = CATEGORY.ID AND ARTICLE.ID_UNIT = UNIT.ID  AND ARTICLE.ARCHIVE = 0;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            if (resultSet.next()){

                article.setId(resultSet.getInt("ID"));
                article.setName(resultSet.getString("NAME"));
                article.setCat(resultSet.getString("NAME_CAT"));
                article.setUnit(resultSet.getString("NAME_UNIT"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return  article;
    }

}
