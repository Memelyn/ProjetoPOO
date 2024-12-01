import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/CadastroServlet")
public class CadastroServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtém os dados do formulário
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Simulação de armazenamento no banco de dados
        // Substitua esta lógica com um código real de inserção no banco de dados
        boolean cadastroRealizado = cadastrarUsuarioNoBanco(email, username, password);

        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(
            "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
            "<meta charset='UTF-8'>" +
            "<title>Cadastro</title>" +
            "<style>" +
            "body {" +
            "    background-color: #1e1e2e;" +
            "    color: #e4e4e7;" +
            "    font-family: Arial, sans-serif;" +
            "    display: flex;" +
            "    flex-direction: column;" +
            "    align-items: center;" +
            "    justify-content: center;" +
            "    height: 100vh;" +
            "    margin: 0;" +
            "}" +
            "h1 {" +
            "    color: #f3f4f6;" +
            "}" +
            "p {" +
            "    text-align: center;" +
            "}" +
            "a {" +
            "    background-color: #6366f1;" +
            "    color: white;" +
            "    text-decoration: none;" +
            "    border-radius: 5px;" +
            "    padding: 10px;" +
            "    margin-top: 20px;" +
            "    display: inline-block;" +
            "}" +
            "a:hover {" +
            "    background-color: #818cf8;" +
            "}" +
            "</style>" +
            "</head>" +
            "<body>"
        );

        // Mensagem de sucesso ou erro
        if (cadastroRealizado) {
            response.getWriter().write(
                "<h1>Cadastro realizado com sucesso!</h1>" +
                "<p>Bem-vindo! Você já pode fazer login com suas credenciais.</p>"
            );
        } else {
            response.getWriter().write(
                "<h1>Erro ao cadastrar</h1>" +
                "<p>Houve um erro ao realizar seu cadastro. Por favor, tente novamente.</p>"
            );
        }

        // Link para o login
        response.getWriter().write(
            "<a href='login.jsp'>Voltar para Login</a>" +
            "</body>" +
            "</html>"
        );
    }

    // Método fictício para simular o cadastro do usuário no banco de dados
    private boolean cadastrarUsuarioNoBanco(String email, String username, String password) {
        // Aqui você faria a lógica real para inserir os dados no banco de dados
        // Neste exemplo, estamos apenas retornando true para simular um cadastro bem-sucedido
        return true; // Simulação de sucesso no cadastro
    }
}
