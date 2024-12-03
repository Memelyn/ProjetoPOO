package web;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import model.User;
import model.VehicleAnalysis;
import org.json.JSONArray;
import org.json.JSONObject;
import java.time.*;
import java.util.ArrayList;

@WebServlet(name = "ApiServlet", urlPatterns = {"/api/*"})
public class ApiServlet extends HttpServlet {

    // Lê o corpo da requisição e converte para um objeto JSON
    private JSONObject getJSONBody(BufferedReader reader) throws IOException {
        StringBuilder buffer = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        return new JSONObject(buffer.toString());
    }

    // Processa requisições relacionadas à sessão de usuário
    private void processSession(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getMethod().toLowerCase().equals("put")) {
            // Realiza o login do usuário
            JSONObject body = getJSONBody(request.getReader());
            String login = body.getString("login");
            String password = body.getString("password");
            User u = User.getUserByLoginAndPassword(login, password);
            if (u == null) {
                response.sendError(403, "Login or password incorrects");
                file.put("error", "Login or password incorrects");
            } else {
                // Cria uma nova sessão para o usuário
                request.getSession().setAttribute("user", u);
                file.put("id", u.getRowId());
                file.put("login", u.getLogin());
                file.put("name", u.getName());
                file.put("passwordHash", u.getPasswordHash());
                file.put("message", "Logged in");
            }
        } else if (request.getMethod().toLowerCase().equals("delete")) {
            // Realiza o logout do usuário
            request.getSession().removeAttribute("user");
            file.put("message", "Logged out");
        } else if (request.getMethod().toLowerCase().equals("get")) {
            // Retorna as informações do usuário logado
            User u = (User) request.getSession(false).getAttribute("user");
            if (u == null) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                file.put("error", "No session");
            } else {
                file.put("id", u.getRowId());
                file.put("login", u.getLogin());
                file.put("name", u.getName());
            }
        } else {
            // Método não permitido
            response.sendError(405, "Method not allowed");
            file.put("error", "Method not allowed");
        }
    }

    // Processa requisições relacionadas aos usuários
    private void processUsers(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getMethod().toLowerCase().equals("post")) {
            // Cria um novo usuário
            try {
                JSONObject body = getJSONBody(request.getReader());
                String login = body.getString("login");
                String name = body.getString("name");
                String password = body.getString("password");
                User.insertUser(login, name, password);
                System.out.println("User registered successfully");
            } catch (Exception e) {
                file.put("error", e.getMessage());
            }
        } else if (request.getMethod().toLowerCase().equals("get")) {
            // Retorna a lista de usuários
            file.put("list", new JSONArray(User.getUsers()));
        } else if (request.getMethod().toLowerCase().equals("put")) {
            // Atualiza as informações do usuário logado
            HttpSession session = request.getSession();
            if (session.getAttribute("user") == null) {
                response.sendError(401, "Unauthorized: no session");
                file.put("error", "Unauthorized: no session");
                return;
            }
            User loggedUser = (User) session.getAttribute("user");
            Long rowId = loggedUser.getRowId();
            if (rowId == null) {
                response.sendError(401, "Unauthorized: User not authenticated");
                return;
            }
            JSONObject bodyUpdate = getJSONBody(request.getReader());
            String newLogin = bodyUpdate.getString("login");
            String newName = bodyUpdate.getString("name");
            User.updateUser(rowId, newLogin, newName);
            file.put("success", "User updated successfully");
        } else if (request.getMethod().toLowerCase().equals("delete")) {
            // Deleta o usuário logado
            try {
                HttpSession deleteSession = request.getSession();
                User deleteUser = (User) deleteSession.getAttribute("user");
                if (deleteUser == null) {
                    response.sendError(401, "Unauthorized: no session");
                    file.put("error", "Unauthorized: no session");
                    return;
                }
                Long userId = deleteUser.getRowId();
                if (userId == null) {
                    response.sendError(401, "Unauthorized: User not authenticated");
                    return;
                }
                User.deleteUser(userId);
                file.put("message", "User deleted successfully");
                deleteSession.invalidate();
            } catch (Exception e) {
                response.sendError(500, "Internal Server Error: " + e.getMessage());
            }
        } else {
            // Método não permitido
            response.sendError(405, "Method not allowed");
            file.put("error", "Method not allowed");
        }
    }

    // Processa requisições relacionadas às análises de veículos
    private void processAnalysis(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getMethod().toLowerCase().equals("get")) {
            // Retorna o histórico de análises do usuário logado
            ArrayList<VehicleAnalysis> history = VehicleAnalysis.getHistory(request);
            file.put("list", new JSONArray(history));
        } else {
            // Método não permitido
            response.sendError(405, "Method not allowed");
            file.put("error", "Method not allowed");
        }
    }

    // Processa a requisição principal
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject file = new JSONObject();
        try {
            String uri = request.getRequestURI();
            if (uri.endsWith("/api/session")) {
                // Processa requisições relacionadas à sessão de usuário
                processSession(file, request, response);
            } else if (uri.endsWith("/api/users")) {
                // Processa requisições relacionadas aos usuários
                processUsers(file, request, response);
            } else if (uri.endsWith("/api/analyser")) {
                // Processa requisições relacionadas às análises de veículos
                processAnalysis(file, request, response);
            } else {
                // URL inválida
                response.sendError(400, "Invalid URL");
                file.put("error", "Invalid URL");
            }
        } catch (Exception ex) {
            // Erro interno
            response.sendError(500, "Internal error: " + ex.getLocalizedMessage());
            file.put("error", "Internal error: " + ex.getLocalizedMessage());
        }
        // Envia a resposta
        response.getWriter().print(file.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "API Servlet";
    }
}
