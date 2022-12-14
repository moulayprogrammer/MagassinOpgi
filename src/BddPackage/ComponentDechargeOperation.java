package BddPackage;




import Models.ComponentDecharge;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComponentDechargeOperation extends BDD<ComponentDecharge> {

    @Override
    public boolean insert(ComponentDecharge o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO COMPONENT_DECHARGE (ID_ART, ID_DECHARGE, ID_STORE, QTE) VALUES (?,?,?,?);" ;
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdArt());
            preparedStmt.setInt(2,o.getIdDecharge());
            preparedStmt.setInt(3,o.getIdStore());
            preparedStmt.setInt(4,o.getQte());

            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    public int insertId(ComponentDecharge o) {
        connectDatabase();
        int ins = 0;
        String query = "INSERT INTO COMPONENT_DECHARGE (ID_ART, ID_DECHARGE, ID_STORE, QTE) VALUES (?,?,?,?);" ;
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdArt());
            preparedStmt.setInt(2,o.getIdDecharge());
            preparedStmt.setInt(3,o.getIdStore());
            preparedStmt.setInt(4,o.getQte());

            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = preparedStmt.getGeneratedKeys().getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(ComponentDecharge o1, ComponentDecharge o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE COMPONENT_DECHARGE SET QTE = ? WHERE ID = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getQte());
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
    public boolean delete(ComponentDecharge o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM COMPONENT_DECHARGE WHERE ID = ? ;";
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
    public boolean isExist(ComponentDecharge o) {
        return false;
    }

    @Override
    public ArrayList<ComponentDecharge> getAll() {
        return null;
    }

    public boolean deleteByDecharge(int idDecharge) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM COMPONENT_DECHARGE WHERE ID_DECHARGE = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idDecharge);

            int update = preparedStmt.executeUpdate();
            if(update != -1) del = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return del;
    }

    public ArrayList<ComponentDecharge> getAllByDecharge(int idDecharge) {
        connectDatabase();
        ArrayList<ComponentDecharge> list = new ArrayList<>();
        String query = "SELECT * FROM COMPONENT_DECHARGE WHERE  ID_DECHARGE = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idDecharge);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                ComponentDecharge componentDecharge = new ComponentDecharge();
                componentDecharge.setId(resultSet.getInt("ID"));
                componentDecharge.setIdArt(resultSet.getInt("ID_ART"));
                componentDecharge.setIdDecharge(resultSet.getInt("ID_DECHARGE"));
                componentDecharge.setIdStore(resultSet.getInt("ID_STORE"));
                componentDecharge.setQte(resultSet.getInt("QTE"));

                list.add(componentDecharge);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;

    }
}
