<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Verificador de Avarias</title>
    
    </head>
    <body>
   <header class="text-center mt-4">
            <h1>Verificador de Avarias</h1>
        </header>

        <!-- Formulário para análise de imagem -->
        <form method="POST" action="APIGemini">
            <div style="div { margin: 50px auto 0; width: 80%; max-width: 600px; text-align: center; background: lightblue; padding: 20px; border: 1px solid #ccc; border-radius: 8px; }"
>
                <label for="img_end" class="form-label">Endereço da Imagem do Automóvel</label>
                <input type="text" class="form-control mb-3" id="img_end" name="imagePath" required>
                <br>
                <input type="submit" value="Analisar Imagem" class="btn btn-primary"/>
                <div class="form-text" id="basic-addon4" style="font-size:12px; text-align: right;">
                    <a href="${pageContext.request.contextPath}/index.jsp">Página Inicial</a>
                </div>
            </div>       
        </form>

        <!-- Resultado da análise -->
        <div style="margin: 30px auto; width: 80%; max-width: 600px; text-align: center; padding: 20px;">
            <% if (request.getAttribute("analysisResult") != null) { %>
            <h2>Resultado da Análise:</h2>
            <p><%= request.getAttribute("analysisResult") %></p>
            <% } else if (request.getAttribute("error") != null) { %>
            <p style="color: red;"><%= request.getAttribute("error") %></p>
            <% } %>

        </div><!-- Script para validação -->
        <script>
            // Validação do formulário
            function validateForm() {
                const inputField = document.getElementById("img_end");
                const imagePath = inputField.value;
                return true; // Permite o envio do formulário
            }
        </script>
    </body>
