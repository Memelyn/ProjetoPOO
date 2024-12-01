import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.Base64;
import javax.imageio.ImageIO;
import org.json.JSONArray;
import org.json.JSONObject;

public class TesteGemini {

    private static final String API_KEY = "API-KEY"; // Substitua pela sua chave de API Gemini
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

    // Função para codificar a imagem redimensionada em Base64
    public static String encodeImage(String imagePath) throws IOException {
        File imageFile = new File(imagePath);
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    // Função para enviar a requisição à API Gemini
    public static String sendRequest(String base64Image, String prompt) throws IOException, InterruptedException {
    // Construção do payload para a API Gemini
    JSONObject inlineData = new JSONObject()
            .put("mime_type", "image/png")
            .put("data", base64Image);

    JSONArray parts = new JSONArray()
            .put(new JSONObject().put("text", prompt))
            .put(new JSONObject().put("inline_data", inlineData));

    JSONObject payloadContent = new JSONObject()
            .put("parts", parts);

    JSONObject payload = new JSONObject()
            .put("contents", new JSONArray().put(payloadContent));

    // Configuração da requisição HTTP
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
            .build();

    // Envio da requisição e obtenção da resposta
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    // Log da resposta completa
     System.out.println("Status Code: " + response.statusCode());
     System.out.println("Resposta Bruta: " + response.body());

    // Verifica se a resposta foi bem-sucedida
    if (response.statusCode() != 200) {
        throw new RuntimeException("Erro na requisição: " + response.statusCode() + " - " + response.body());
    }

    // Processamento do JSON retornado
    try {
        JSONObject responseJson = new JSONObject(response.body());
        JSONArray candidates = responseJson.optJSONArray("candidates");

        if (candidates != null && candidates.length() > 0) {
            JSONObject candidate = candidates.getJSONObject(0);
            JSONObject candidateContent = candidate.optJSONObject("content");
            JSONArray contentParts = candidateContent.optJSONArray("parts");

            if (contentParts != null && contentParts.length() > 0) {
                return contentParts.getJSONObject(0).getString("text");
            }
        }
        return "Nenhum texto encontrado na resposta.";
    } catch (Exception e) {
        return "Erro ao processar a resposta: " + e.getMessage();
    }
}




    public static void main(String[] args) {
        try {
            String prompt = "Quais são as avarias presentes no automóvel?";
            String inputImagePath = "C:/Users/Netun/Documents/NetBeansProjects/AutoVar2/src/main/java/images/carro.jpg";
            String resizedImagePath = "C:/Users/Netun/Documents/NetBeansProjects/AutoVar2/src/main/java/images/carro_resized.jpg";

            // Redimensionar a imagem para reduzir o tamanho (exemplo: 500x500 pixels)
            resizeImage(inputImagePath, resizedImagePath, 500, 500);

            // Codificar a imagem redimensionada para Base64
            String base64Image = encodeImage(resizedImagePath);

            // Enviar a requisição para a API Gemini
            String response = sendRequest(base64Image, prompt);

            // Exibir a resposta
            System.out.println("Resposta da API Gemini: " + response);
        } catch (Exception ex) {
            System.err.println("ERRO: " + ex.getMessage());
        }
    }
}
