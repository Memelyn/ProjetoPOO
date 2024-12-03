package web;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import model.User;

/**
 * Classe AppListener que implementa ServletContextListener.
 * - É responsável por executar ações quando o contexto da aplicação é inicializado ou destruído.
 */
@WebListener // Indica que essa classe é um listener associado ao ciclo de vida do contexto da aplicação.
public class AppListener implements ServletContextListener {

    // Constantes para conexão com o banco de dados.
    public static final String CLASS_NAME = "org.sqlite.JDBC"; // Classe JDBC do SQLite.
    public static final String URL = "jdbc:sqlite:autovar.db"; // URL do banco de dados SQLite.

    // Variáveis para registro de logs e tratamento de exceções durante a inicialização.
    public static String initializeLog = ""; 
    public static Exception exception = null;

    /**
     * Método chamado quando o contexto da aplicação é destruído.
     * - Aqui não há implementação, mas é possível liberar recursos, encerrar conexões, etc.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Nenhuma ação necessária ao destruir o contexto.
    }

    /**
     * Método chamado quando o contexto da aplicação é inicializado.
     * - Responsável por configurar o banco de dados e garantir que as tabelas necessárias existem.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // Estabelece conexão com o banco de dados.
            Connection c = AppListener.getConnection();
            Statement s = c.createStatement();

            // Executa a instrução SQL para criar a tabela de usuários, caso não exista.
            s.execute(User.getCreateStatement());
            s.close();
            c.close();
        } catch (Exception ex) {
            // Registra erros encontrados durante a inicialização.
            initializeLog += "Erro: " + ex.getMessage();
            exception = ex;
        }
    }

    /**
     * Método utilitário para gerar o hash MD5 de uma string.
     * @param text Texto a ser convertido em hash.
     * @return Hash MD5 no formato hexadecimal.
     * @throws NoSuchAlgorithmException Caso o algoritmo MD5 não esteja disponível.
     */
    public static String getMd5Hash(String text) throws NoSuchAlgorithmException {
        MessageDigest m = MessageDigest.getInstance("MD5"); // Inicializa o algoritmo MD5.
        byte[] digest = m.digest(text.getBytes()); // Converte o texto para um array de bytes em hash.
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b)); // Formata cada byte como um par hexadecimal.
        }
        return sb.toString();
    }

    /**
     * Método utilitário para obter uma conexão com o banco de dados.
     * @return Conexão JDBC para o banco de dados.
     * @throws Exception Caso ocorra um erro ao carregar a classe JDBC ou ao se conectar.
     */
    public static Connection getConnection() throws Exception {
        Class.forName(CLASS_NAME); // Carrega a classe JDBC do SQLite.
        return DriverManager.getConnection(URL); // Retorna a conexão com o banco.
    }
}
