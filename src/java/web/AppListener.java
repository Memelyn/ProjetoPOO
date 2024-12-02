
package web;


import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Date;
import model.User;
import model.VehicleStay;

@WebListener
public class AppListener implements ServletContextListener {
    public static final String CLASS_NAME = "org.sqlite.JDBC";
    public static final String URL = "jdbc:sqlite:autovar.db";
    public static String initializeLog = "";
    public static Exception exception = null;
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Connection c = AppListener.getConnection();
            Statement s = c.createStatement();
            
            s.execute(User.getCreateStatement());
         
            
            initializeLog += "done; ";
            initializeLog += " Creating vehicleStay table if not exists...";
            s.execute(VehicleStay.getCreateStatement());
             initializeLog += "done; ";
            s.close();
            c.close();
        } catch (Exception ex){
            initializeLog += "Erro: " + ex.getMessage();
            exception = ex;
        }
    }
    
   public static String getMd5Hash(String text) throws NoSuchAlgorithmException {
    MessageDigest m = MessageDigest.getInstance("MD5");
    byte[] digest = m.digest(text.getBytes());
    StringBuilder sb = new StringBuilder();
    for (byte b : digest) {
        sb.append(String.format("%02x", b)); // Formata cada byte em hexadecimal
    }
    return sb.toString();
}

    
    public static Connection getConnection() throws Exception {
           Class.forName(CLASS_NAME);
           return DriverManager.getConnection(URL);
    }
}
