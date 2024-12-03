/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
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
import org.json.JSONArray;
import org.json.JSONObject;
import java.time.*;

@WebServlet(name = "ApiServlet", urlPatterns = {"/api/*"})
public class ApiServlet extends HttpServlet {

    private JSONObject getJSONBody(BufferedReader reader) throws IOException{
        StringBuilder buffer = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        return new JSONObject(buffer.toString());
    }
    
    private void processSession(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws Exception{
        if(request.getMethod().toLowerCase().equals("put")){
            JSONObject body = getJSONBody(request.getReader());
            String login = body.getString("login");
            String password = body.getString("password");
            User u = User.getUserByLoginAndPassword(login, password);
            if(u==null){
                response.sendError(403, "Login or password incorrects");
                file.put("error", "Login or password incorrects");
            }else{
                request.getSession().setAttribute("user", u);
                file.put("id", u.getRowId());
                file.put("login", u.getLogin());
                file.put("name", u.getName());
                file.put("passwordHash", u.getPasswordHash());
                file.put("message", "Logged in");
            }
        }else if(request.getMethod().toLowerCase().equals("delete")){
            request.getSession().removeAttribute("user");
            file.put("message", "Logged out");
        }else if(request.getMethod().toLowerCase().equals("get")){
             User u = (User) request.getSession(false).getAttribute("user");
            // u = User.getUserByLoginAndPassword(login, password);
            if(u == null){
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                file.put("error", "No session");
            }else{
              //  User u = (User) request.getSession().getAttribute("user");
                file.put("id", u.getRowId());
                file.put("login", u.getLogin());
                file.put("name", u.getName());
               // file.put("passwordHash", u.getPasswordHash());
            }
        }else{
            response.sendError(405, "Method not allowed");
            file.put("error", "Method not allowed");
        }
    }
    private void processUsers(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws Exception{
        
         if(request.getMethod().toLowerCase().equals("post")){
             
             try{
            JSONObject body = getJSONBody(request.getReader());
            String login = body.getString("login");
            String name = body.getString("name");
            String password = body.getString("password");
            User.insertUser(login, name, password);
             System.out.println("cadastrado");
             } catch (Exception e){
                 file.put("error", e.getMessage());
             }
//                }else if(request.getSession().getAttribute("user")==null){
//            response.sendError(401, "Unauthorized: no session");
//            file.put("error", "Unauthorized: no session");
//        }else if(!((User)request.getSession().getAttribute("user")).getRole().equals("ADMIN")){
//            response.sendError(401, "Unauthorized: only admin can manage users");
//            file.put("error", "Unauthorized: only admin can manage users");
        }else if(request.getMethod().toLowerCase().equals("get")){
            
          file.put("list", new JSONArray(User.getUsers()));
                
       } else if (request.getMethod().toLowerCase().equals("put")) {
            // Recupera a sessão
            HttpSession session = request.getSession();

            // Verifica se há um usuário na sessão
            if (session.getAttribute("user") == null) {
                response.sendError(401, "Unauthorized: no session");
                file.put("error", "Unauthorized: no session");
                return;
            }

            // Recupera o usuário logado da sessão
            User loggedUser = (User) session.getAttribute("user");

            // Recupera o ID do usuário logado
            Long rowId = loggedUser.getRowId(); // Supondo que o objeto `User` tem o método `getRowId`
            if (rowId == null) {
                response.sendError(401, "Usuário não autenticado.");
                return;
            }

            // Recupera os dados do corpo da requisição
            JSONObject body = getJSONBody(request.getReader());
            String login = body.getString("login");
            String name = body.getString("name");

            // Atualiza o usuário
            User.updateUser(rowId, login, name);

            // Confirmação da operação
            file.put("success", "Usuário atualizado com sucesso");
            
       }else if (request.getMethod().toLowerCase().equals("delete")) {
    try {
        // Verifica se o usuário está logado e se há um usuário na sessão
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("user");

        if (loggedUser == null) {
            response.sendError(401, "Unauthorized: no session");
            file.put("error", "Unauthorized: no session");
            return;
        }

        // Obtém o 'rowId' do usuário logado
        Long userId = loggedUser.getRowId(); // Acessa o rowId do usuário logado diretamente

        if (userId == null) {
            response.sendError(401, "Unauthorized: User not authenticated");
            return;
        }

        // Chama o método de exclusão com o 'rowId' do usuário logado
        User.deleteUser(userId);

        // Retorna uma resposta de sucesso
        file.put("message", "User deleted successfully");

        // Opcionalmente, invalidar a sessão após excluir o usuário
        session.invalidate();

    } catch (Exception e) {
        response.sendError(500, "Internal Server Error: " + e.getMessage());
    }
}
    }

   private void processDamage(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws Exception{
//        if(request.getSession().getAttribute("user")==null){
//            response.sendError(401, "Unauthorized: no session");
//            file.put("error", "Unauthorized: no session");
//    }
  }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        JSONObject file = new JSONObject();
        try{
            if(request.getRequestURI().endsWith("/api/session")){
                processSession(file, request, response);
            }else if(request.getRequestURI().endsWith("/api/users")){
                processUsers(file, request, response);
//            }else if(request.getRequestURI().endsWith("/api/damage")){
//                processParking(file, request, response);
            }else{
                response.sendError(400, "Invalid URL");
                file.put("error", "Invalid URL");
            }
        }catch(Exception ex){
            response.sendError(500, "Internal error: "+ex.getLocalizedMessage());
            file.put("error", "Internal error: "+ex.getLocalizedMessage());
        }
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
        return "Short description";
    }

}
