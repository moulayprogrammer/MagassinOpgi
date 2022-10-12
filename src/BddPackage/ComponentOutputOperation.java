package BddPackage;




import Models.ComponentOutput;
import Models.Output;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComponentOutputOperation extends BDD<ComponentOutput> {

    @Override
    public boolean insert(ComponentOutput o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO COMPONENT_OUTPUT (ID_ART, ID_OUTPUT, ID_STORE, QTE_DEM, QTE_SERV) VALUES (?,?,?,?,?);" ;
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdArt());
            preparedStmt.setInt(2,o.getIdOutput());
            preparedStmt.setInt(3,o.getIdStore());
            preparedStmt.setInt(4,o.getQteDem());
            preparedStmt.setInt(5,o.getQteServ());
            System.out.println("insert = " + o.getIdArt());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    public int insertId(ComponentOutput o) {
        connectDatabase();
        int ins = 0;
        String query = "INSERT INTO COMPONENT_OUTPUT (ID_ART, ID_OUTPUT, ID_STORE, QTE_DEM, QTE_SERV) VALUES (?,?,?,?,?);" ;
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdArt());
            preparedStmt.setInt(2,o.getIdOutput());
            preparedStmt.setInt(3,o.getIdStore());
            preparedStmt.setInt(4,o.getQteDem());
            preparedStmt.setInt(5,o.getQteServ());

            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = preparedStmt.getGeneratedKeys().getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(ComponentOutput o1, ComponentOutput o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE COMPONENT_OUTPUT SET QTE_DEM = ?, QTE_SERV = ? WHERE ID = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getQteDem());
            preparedStmt.setDouble(2,o1.getQteServ());
            preparedStmt.setInt(3,o2.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    @Override
    public boolean delete(ComponentOutput o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM COMPONENT_OUTPUT WHERE ID = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getId());
            System.out.println("delete = " + o.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) del = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return del;
    }

    @Override
    public boolean isExist(ComponentOutput o) {
        return false;
    }

    @Override
    public ArrayList<ComponentOutput> getAll() {
        return null;
    }

    public boolean deleteByOutput(int idOutput) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM COMPONENT_OUTPUT WHERE ID_OUTPUT = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idOutput);

            int update = preparedStmt.executeUpdate();
            if(update != -1) del = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return del;
    }

    public ArrayList<ComponentOutput> getAllByOutput(int idOutput) {
        connectDatabase();
        ArrayList<ComponentOutput> list = new ArrayList<>();
        String query = "SELECT * FROM COMPONENT_OUTPUT WHERE  ID_OUTPUT = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idOutput);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                ComponentOutput componentOutput = new ComponentOutput();
                componentOutput.setId(resultSet.getInt("ID"));
                componentOutput.setIdOutput(resultSet.getInt("ID_OUTPUT"));
                componentOutput.setIdArt(resultSet.getInt("ID_ART"));
                componentOutput.setIdStore(resultSet.getInt("ID_STORE"));
                componentOutput.setQteDem(resultSet.getInt("QTE_DEM"));
                componentOutput.setQteServ(resultSet.getInt("QTE_SERV"));

                list.add(componentOutput);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;

    }
}
