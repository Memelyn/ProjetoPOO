package ai;

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

@WebServlet("/APIGemini")
public class APIGemini extends HttpServlet {

    private static final String API_KEY = "TODO"; // Substitua pela chave correta
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + API_KEY;

    // Função para redimensionar a imagem
    public static void resizeImage(String inputPath, String outputPath, int width, int height) throws IOException {
        File inputFile = new File(inputPath);
        BufferedImage originalImage = ImageIO.read(inputFile);

        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        File outputFile = new File(outputPath);
        ImageIO.write(resizedImage, "png", outputFile);
    }

    // Função para codificar a imagem em Base64
    public static String encodeImage(String imagePath) throws IOException {
        File imageFile = new File(imagePath);
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    // Envia a requisição para a API Gemini
    private String sendRequest(String base64Image, String prompt) throws IOException, InterruptedException {
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

        HttpClient client = SelfCertificatedServer.getHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Erro na API: " + response.statusCode() + " - " + response.body());
        }

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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Obtém o caminho do arquivo enviado no formulário
            String imagePath = req.getParameter("imagePath");  // Recebe o caminho da imagem do front-end

            if (imagePath == null || imagePath.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                req.setAttribute("error", "O caminho da imagem é obrigatório.");
                req.getRequestDispatcher("analyze_gemini.jsp").forward(req, resp);
                return;
            }

            // Codifica a imagem para Base64
            String base64Image = encodeImage(imagePath);

            // Envia a requisição para a API
            String analysisResult = sendRequest(base64Image, "Diga quais são as avarias no veículo.");

            // Define o resultado da análise para a JSP
            req.setAttribute("analysisResult", analysisResult);
            req.getRequestDispatcher("analyze_gemini.jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("error", "Erro ao processar a imagem: " + e.getMessage());
            req.getRequestDispatcher("analyze_gemini.jsp").forward(req, resp);
        }
    }
}
