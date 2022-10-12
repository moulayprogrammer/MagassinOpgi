package BddPackage;



import Models.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceOperation extends BDD<Service> {

    @Override
    public boolean insert(Service o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO SERVICE (ID_DEP,NAME) VALUES (?,?)";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdDep());
            preparedStmt.setString(2,o.getName());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(Service o1, Service o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE SERVICE SET ID_DEP = ?, NAME = ? WHERE ID = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getIdDep());
            preparedStmt.setString(2,o1.getName());
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
    public boolean delete(Service o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM SERVICE WHERE ID = ? ;";
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
    public boolean isExist(Service o) {
        return false;
    }



    @Override
    public ArrayList<Service> getAll() {
        connectDatabase();
        ArrayList<Service> list = new ArrayList<>();
        String query = "SELECT SERVICE.ID, SERVICE.NAME, SERVICE.ID_DEP, DEP.NAME AS NAME_DEP FROM SERVICE,DEP WHERE SERVICE.ID_DEP = DEP.ID AND SERVICE.ARCHIVE = 0;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){
                
                Service service = new Service();
                service.setId(resultSet.getInt("ID"));
                service.setIdDep(resultSet.getInt("ID_DEP"));
                service.setName(resultSet.getString("NAME"));
                service.setNameDep(resultSet.getString("NAME_DEP"));

                list.add(service);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public Service get(int id) {
        connectDatabase();
        Service service = new Service();
        String query = "SELECT SERVICE.ID, SERVICE.NAME, SERVICE.ID_DEP, DEP.NAME AS NAME_DEP FROM SERVICE,DEP WHERE SERVICE.ID = ? AND SERVICE.ID_DEP = DEP.ID AND SERVICE.ARCHIVE = 0;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                service.setId(resultSet.getInt("ID"));
                service.setIdDep(resultSet.getInt("ID_DEP"));
                service.setName(resultSet.getString("NAME"));
                service.setNameDep(resultSet.getString("NAME_DEP"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return service;
    }

    public ArrayList<Service> getAllByDepartment(int idDep) {
        connectDatabase();
        ArrayList<Service> list = new ArrayList<>();
        String query = "SELECT SERVICE.ID, SERVICE.NAME, SERVICE.ID_DEP, DEP.NAME AS NAME_DEP FROM SERVICE,DEP WHERE ID_DEP = ? AND SERVICE.ID_DEP = DEP.ID AND SERVICE.ARCHIVE = 0;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idDep);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Service Service = new Service();
                Service.setId(resultSet.getInt("ID"));
                Service.setIdDep(resultSet.getInt("ID_DEP"));
                Service.setName(resultSet.getString("NAME"));

                list.add(Service);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public boolean AddToArchive(Service o) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE SERVICE SET ARCHIVE = 1 WHERE ID = ?;";
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

    public boolean DeleteFromArchive(Service o) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE SERVICE SET ARCHIVE = 0 WHERE ID = ?;";
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

    public ArrayList<Service> getAllArchive() {
        connectDatabase();
        ArrayList<Service> list = new ArrayList<>();
        String query = "SELECT SERVICE.ID, SERVICE.NAME, SERVICE.ID_DEP, DEP.NAME AS NAME_DEP FROM SERVICE,DEP WHERE SERVICE.ID_DEP = DEP.ID AND SERVICE.ARCHIVE = 1;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Service service = new Service();
                service.setId(resultSet.getInt("ID"));
                service.setIdDep(resultSet.getInt("ID_DEP"));
                service.setName(resultSet.getString("NAME"));
                service.setNameDep(resultSet.getString("NAME_DEP"));

                list.add(service);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public Service getArchive(int id) {
        connectDatabase();
        Service service = new Service();
        String query = "SELECT SERVICE.ID, SERVICE.NAME, SERVICE.ID_DEP, DEP.NAME AS NAME_DEP FROM SERVICE,DEP WHERE ID = ? AND SERVICE.ID_DEP = DEP.ID AND SERVICE.ARCHIVE = 1;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                service.setId(resultSet.getInt("ID"));
                service.setIdDep(resultSet.getInt("ID_DEP"));
                service.setName(resultSet.getString("NAME"));
                service.setNameDep(resultSet.getString("NAME_DEP"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return service;
    }
}
