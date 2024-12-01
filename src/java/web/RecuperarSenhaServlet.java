import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RecuperarSenhaServlet")
public class RecuperarSenhaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtém o e-mail do formulário
        String email = request.getParameter("email");

        // Simulação de verificação do e-mail no banco de dados
        // Substitua isso com uma lógica real de busca no banco de dados
        boolean emailExiste = verificarEmailNoBanco(email);

        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(
            "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
            "<meta charset='UTF-8'>" +
            "<title>Recuperar Senha</title>" +
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

        // Se o e-mail for encontrado
        if (emailExiste) {
            response.getWriter().write(
                "<h1>E-mail encontrado</h1>" +
                "<p>Se este e-mail estiver registrado, você receberá instruções para redefinir sua senha.</p>"
            );
        } else {
            // Se o e-mail não for encontrado
            response.getWriter().write(
                "<h1>E-mail não encontrado</h1>" +
                "<p>O e-mail fornecido não está registrado em nosso sistema. Por favor, tente novamente ou cadastre-se.</p>"
            );
        }

        response.getWriter().write(
            "<a href='login.jsp'>Voltar para Login</a>" +
            "</body>" +
            "</html>"
        );
    }

    // Método fictício para verificar o e-mail no banco de dados
    private boolean verificarEmailNoBanco(String email) {
        // Substitua esta lógica por uma consulta real no banco de dados
        return "exemplo@teste.com".equals(email); // Exemplo: e-mail registrado no sistema
    }
}
