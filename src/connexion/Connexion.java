package connexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Connexion {
    private static final String url="jdbc:mysql://localhost:3308/gestionreservation?useUnicode=true" +
                					"&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String user="root";
    private static final String password="";
    private static Connection cnx=null;
   static {
       try {
           Class.forName("com.mysql.cj.jdbc.Driver");
           
            cnx = DriverManager.getConnection(url, user ,password);
           

       }
       catch (ClassNotFoundException e) {
           System.out.println("classnotfound");
       }
       catch (SQLException e) {
           System.out.println("sql exception");
       }

   }
   public static Connection getCnx() {
       return cnx;
   }
}
