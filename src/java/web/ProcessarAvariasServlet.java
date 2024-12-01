import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/processar")
public class ProcessarAvariasServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {            
            // Definir o prompt e os caminhos das imagens
            String prompt = "Diga-me as avarias presentes no automóvel.";
            String inputImagePath = getServletContext().getRealPath("C:/Users/Netun/Documents/NetBeansProjects/AutoVar2/src/main/java/images/carro.jpg");
            String resizedImagePath = getServletContext().getRealPath("C:/Users/Netun/Documents/NetBeansProjects/AutoVar2/src/main/java/images/carro_resized.jpg");

            // Redimensionar a imagem
            TesteGemini.resizeImage(inputImagePath, resizedImagePath, 500, 500);

            // Codificar a imagem em Base64
            String base64Image = TesteGemini.encodeImage(resizedImagePath);

            // Enviar a requisição para a API Gemini
            String resultado = TesteGemini.sendRequest(base64Image, prompt);

            // Atribuir o resultado à requisição
            request.setAttribute("resultado", resultado);
            
            // Obtém o context path
        String contextPath = request.getContextPath();
        
        // Encaminhar para o JSP para exibir o resultado
        request.getRequestDispatcher(contextPath + "/verificadorAvarias.jsp").forward(request, response);
        } catch (Exception e) {
            // Configurar mensagem de erro em caso de falha
            request.setAttribute("resultado", "Erro ao processar: " + e.getMessage());
        }
      
    }
}
