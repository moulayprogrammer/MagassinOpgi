package BddPackage;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectBD {

    public ConnectBD() {

    }

    public Connection connect() {
        Connection conn = null;
        // db parameters
        String url = "jdbc:sqlite:"+System.getProperty("user.dir")+"\\src\\Database\\db.db";

        String user = "root";
        String password = "";
        String unicode= "?useUnicode=yes&characterEncoding=UTF-8";

        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                System.out.println("Connected to the database");
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred. Maybe user/password is invalid");
            ex.printStackTrace();
        }
        return conn;
    }
}
