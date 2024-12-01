
package model;

import java.util.ArrayList;
import java.sql.*;
import web.AppListener;

public class User {
    private long rowId;
    private String name;
    private String login;
    private String passwordHash;
    
    public static String getCreateStatement(){
        return "CREATE TABLE IF NOT EXISTS userss ("
                + "login VARCHAR(50) UNIQUE NOT NULL,"
                + "name VARCHAR (200) NOT NULL,"
                + "password_hash VARCHAR NOT NULL"
                + ")";
    }
    
    public static ArrayList<User> getUsers() throws Exception {
        ArrayList<User> list = new ArrayList<>();
        Connection con = AppListener.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT rowid, * from userss");
        while(rs.next()){
            long rowId = rs.getLong("rowid");
            String login = rs.getString("login");
            String name = rs.getString("name");
            String passwordHash = rs.getString("password_hash");
            list.add(new User (rowId, login, name, passwordHash));
        }
        
        rs.close();
        stmt.close();
        con.close();
        return list;
    }
    
    public static User getUserByLoginAndPassword(String login, String password) throws Exception {
    User user = null; // Inicializa a variável 'user' como null
    
    // Estabelece a conexão com o banco de dados
    Connection con = AppListener.getConnection();
    
    // Cria a consulta SQL para verificar as credenciais
    String sql = "SELECT rowid, name, login, password_hash FROM userss WHERE login = ? AND password_hash = ?";
    PreparedStatement stmt = con.prepareStatement(sql); // Prepara a consulta SQL
    
    // Define os parâmetros da consulta
    stmt.setString(1, login);
    stmt.setString(2, AppListener.getMd5Hash(password));
    
    // Executa a consulta e armazena o resultado
    ResultSet rs = stmt.executeQuery();
    
    // Verifica se encontrou algum resultado
    if (rs.next()) {
        long rowId = rs.getLong("rowid");
        String name = rs.getString("name");
        String passwordHash = rs.getString("password_hash");
        user = new User(rowId, login, name, passwordHash);
    }
    
    // Fecha os recursos
    rs.close();
    stmt.close();
    con.close();
    
    return user;
}

    
   public static User getUser(String loggedUserLogin) throws Exception {
    User user = null; // Inicializa a variável 'user' como null
    
    // Estabelece a conexão com o banco de dados
    Connection con = AppListener.getConnection();
    
    // Cria a consulta SQL para obter dados do usuário logado
    String sql = "SELECT rowid, name, login, password_hash FROM userss WHERE login = ?";
    PreparedStatement stmt = con.prepareStatement(sql); // Prepara a consulta SQL
    
    // Define o parâmetro para o login do usuário logado
    stmt.setString(1, loggedUserLogin);
    
    // Executa a consulta e armazena o resultado
    ResultSet rs = stmt.executeQuery();
    
    // Verifica se encontrou algum resultado
    if (rs.next()) {
        long rowId = rs.getLong("rowid"); // Obtém o ID do usuário
        String name = rs.getString("name"); // Obtém o nome do usuário
        String login = rs.getString("login"); // Obtém o login do usuário
        String passwordHash = rs.getString("password_hash"); // Obtém o hash da senha
        
        // Cria o objeto User com as informações recuperadas do banco de dados
        user = new User(rowId, login, name, passwordHash);
    }
    
    // Fecha os recursos abertos
    rs.close();
    stmt.close();
    con.close();
    
    // Retorna o objeto 'user' (null se não encontrar)
    return user;
}

  public static void insertUser(String login, String name, String password) throws Exception{
      Connection con = AppListener.getConnection();
      String sql = "INSERT INTO userss(login, name, password_hash)"
              + "VALUES (?,?,?)";
      PreparedStatement stmt = con.prepareStatement(sql);
      stmt.setString(1, login);
      stmt.setString(2, name);
      stmt.setString(3, AppListener.getMd5Hash(password));
      stmt.execute();
      stmt.close();
      con.close();
  }
  
  public static void updateUser(long rowid, String login, String name) throws Exception {
    Connection con = AppListener.getConnection();
    String sql;
    PreparedStatement stmt = null;

    try {
    System.out.println("to aqui");
    sql = "UPDATE userss SET name = ?, login = ? WHERE rowid = ?";
    stmt = con.prepareStatement(sql);
    stmt.setString(1, name);
    stmt.setString(2, login);
    stmt.setLong(3, rowid);

        System.out.println("Query executada: " + sql);
        System.out.println("Parâmetros:");
        System.out.println("1: " + name);
       
                int rowsAffected = stmt.executeUpdate();
                System.out.println("Linhas atualizadas: " + rowsAffected);
            } finally {
                // Certifique-se de fechar os recursos no bloco `finally`
                if (stmt != null) {
                    stmt.close();
                }
                con.close();
            }
}

  
  public static void deleteUser(long rowid) throws Exception{
         Connection con = AppListener.getConnection();
       String sql = "DELETE FROM userss WHERE rowid=?";
      PreparedStatement stmt = con.prepareStatement(sql);
      stmt.setLong(1, rowid);
      // verificar esse rowId pq tava dando erro
      stmt.execute();
      stmt.close();
      con.close();
  }

    public User(long rowId, String name, String login, String passwordHash) {
        this.rowId = rowId;
        this.name = name;
        this.login = login;
        this.passwordHash = passwordHash;
    }

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
