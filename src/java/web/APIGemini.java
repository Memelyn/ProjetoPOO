package web;

// Importação das bibliotecas necessárias para a funcionalidade da servlet
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import javax.imageio.ImageIO;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// Define a servlet acessível pelo caminho "/APIGemini"
@WebServlet("/APIGemini")
public class APIGemini extends HttpServlet {

    // Chave da API e URL da API Gemini (substitua pela chave correta)
    private static final String API_KEY = "AIzaSyDnIj9oYTHbgy5XZATGls86SfwoqVlVedc";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + API_KEY;

    /**
     * Redimensiona uma imagem para as dimensões especificadas.
     *
     * @param inputPath  Caminho do arquivo original
     * @param outputPath Caminho para salvar a imagem redimensionada
     * @param width      Largura desejada
     * @param height     Altura desejada
     */
    public static void resizeImage(String inputPath, String outputPath, int width, int height) throws IOException {
        File inputFile = new File(inputPath);
        BufferedImage originalImage = ImageIO.read(inputFile);

        // Cria uma nova imagem redimensionada
        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        // Salva a imagem redimensionada
        File outputFile = new File(outputPath);
        ImageIO.write(resizedImage, "png", outputFile);
    }

    /**
     * Codifica uma imagem em Base64.
     *
     * @param imagePath Caminho do arquivo da imagem
     * @return String codificada em Base64
     */
    public static String encodeImage(String imagePath) throws IOException {
        File imageFile = new File(imagePath);
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    /**
     * Envia uma requisição para a API Gemini com a imagem em Base64 e um prompt.
     *
     * @param base64Image Imagem codificada em Base64
     * @param prompt      Texto descrevendo o que a API deve fazer
     * @return Resposta processada pela API
     */
    private String sendRequest(String base64Image, String prompt) throws IOException, InterruptedException {
        // Monta os dados da requisição em formato JSON
        JSONObject inlineData = new JSONObject()
                .put("mime_type", "image/png")
                .put("data", base64Image);

        JSONArray parts = new JSONArray()
                .put(new JSONObject().put("text", prompt))
                .put(new JSONObject().put("inline_data", inlineData));

        JSONObject content = new JSONObject()
                .put("parts", parts);

        JSONObject payload = new JSONObject()
                .put("contents", new JSONArray().put(content));

        // Configura o cliente HTTP e a requisição
        HttpClient client = SelfCertificatedServer.getHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        // Envia a requisição e processa a resposta
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Erro na API: " + response.statusCode() + " - " + response.body());
        }

        // Processa o JSON da resposta
        JSONObject responseJson = new JSONObject(response.body());
        JSONArray candidates = responseJson.optJSONArray("candidates");

        if (candidates != null && candidates.length() > 0) {
            JSONObject firstCandidate = candidates.getJSONObject(0);
            JSONObject contentObj = firstCandidate.optJSONObject("content");
            if (contentObj != null) {
                JSONArray partsArray = contentObj.optJSONArray("parts");
                if (partsArray != null && partsArray.length() > 0) {
                    return partsArray.getJSONObject(0).getString("text");
                }
            }
        }

        return "Nenhum resultado encontrado.";
    }

    /**
     * Processa requisições HTTP POST enviadas à servlet.
     *
     * @param req  Objeto de requisição HTTP
     * @param resp Objeto de resposta HTTP
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Obtém o caminho da imagem enviado no formulário
            String imagePath = req.getParameter("imagePath");

            // Verifica se o caminho da imagem foi fornecido
            if (imagePath == null || imagePath.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                req.setAttribute("error", "O caminho da imagem é obrigatório.");
                req.getRequestDispatcher("index.jsp").forward(req, resp);
                return;
            }

            // Codifica a imagem em Base64
            String base64Image = encodeImage(imagePath);

            // Envia a imagem para a API Gemini com um prompt
            String analysisResult = sendRequest(base64Image, 
                "Dê um relatório detalhado sobre as avarias detectadas no automóvel, categorize as avarias em diferentes níveis de severidade e dê sugestões de reparo. A resposta deve começar com 'Aqui está um relatório das avarias' e conter apenas as avarias.");

            // Define o resultado da análise na requisição
            req.setAttribute("analysisResult", analysisResult);
            req.getRequestDispatcher("index.jsp").forward(req, resp);

        } catch (Exception e) {
            // Trata erros e redireciona para a página com mensagem de erro
            req.setAttribute("error", "Erro ao processar a imagem: " + e.getMessage());
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
    }
}
