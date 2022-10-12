package BddPackage;


import Models.Provider;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProviderOperation extends BDD<Provider> {

    @Override
    public boolean insert(Provider o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO PROVIDER (NAME,ACTIVITY,ADDRESS) VALUES (?,?,?) ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o.getName());
            preparedStmt.setString(2,o.getActivity());
            preparedStmt.setString(3,o.getAddress());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(Provider o1, Provider o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE PROVIDER SET NAME = ?, ACTIVITY = ?, ADDRESS = ? WHERE ID = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o1.getName());
            preparedStmt.setString(2,o1.getActivity());
            preparedStmt.setString(3,o1.getAddress());
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
    public boolean delete(Provider o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM PROVIDER WHERE ID = ? ;";
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
    public boolean isExist(Provider o) {
        return false;
    }

    @Override
    public ArrayList<Provider> getAll() {
        connectDatabase();
        ArrayList<Provider> list = new ArrayList<>();
        String query = "SELECT * FROM PROVIDER WHERE ARCHIVE = 0 ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Provider provider = new Provider();
                provider.setId(resultSet.getInt("ID"));
                provider.setName(resultSet.getString("NAME"));
                provider.setActivity(resultSet.getString("ACTIVITY"));
                provider.setAddress(resultSet.getString("ADDRESS"));

                list.add(provider);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public Provider get(int id) {
        connectDatabase();
        Provider provider = new Provider();
        String query = "SELECT * FROM PROVIDER WHERE ID = ? AND ARCHIVE = 0;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            if (resultSet.next()){

                provider.setId(resultSet.getInt("ID"));
                provider.setName(resultSet.getString("NAME"));
                provider.setActivity(resultSet.getString("ACTIVITY"));
                provider.setAddress(resultSet.getString("ADDRESS"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return provider;
    }

    public boolean AddToArchive(Provider o1){
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE PROVIDER SET ARCHIVE = 1 WHERE ID = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    public boolean DeleteFromArchive(Provider o1){
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE PROVIDER SET ARCHIVE = 0 WHERE ID = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    public ArrayList<Provider> getAllArchive() {
        connectDatabase();
        ArrayList<Provider> list = new ArrayList<>();
        String query = "SELECT * FROM PROVIDER WHERE ARCHIVE = 1 ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Provider provider = new Provider();
                provider.setId(resultSet.getInt("ID"));
                provider.setName(resultSet.getString("NAME"));
                provider.setActivity(resultSet.getString("ACTIVITY"));
                provider.setAddress(resultSet.getString("ADDRESS"));

                list.add(provider);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public Provider getArchive(int id) {
        connectDatabase();
        Provider provider = new Provider();
        String query = "SELECT * FROM PROVIDER WHERE ID = ? AND ARCHIVE = 1;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            if (resultSet.next()){

                provider.setId(resultSet.getInt("ID"));
                provider.setName(resultSet.getString("NAME"));
                provider.setActivity(resultSet.getString("ACTIVITY"));
                provider.setAddress(resultSet.getString("ADDRESS"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return provider;
    }
}
