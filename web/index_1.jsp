<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
         <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
        <title>Verificador de Avarias</title>
    
    </head>
    <body>
         <%@include file="WEB-INF/jspf/header.jspf" %>
         <div id="header_app">
         <div v-if="!user">
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
         </div>
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
        
</div>
        <script>
            // Validação do formulário
            function validateForm() {
                const inputField = document.getElementById("img_end");
                const imagePath = inputField.value;
                return true; // Permite o envio do formulário
            }
            
             document.addEventListener('DOMContentLoaded', function () {
        const session = Vue.createApp({
            data() {
                return {
                    user: null, // Receba o usu�rio da sess�o via API ou inicialize como `null`
                };
            },
            
             async mounted() {
                try {
                    const response = await fetch('/AutoVar/api/session'); // Checa se h� uma sess�o ativa
                    if (response.ok) {
                        this.user = await response.json();
                    }
                } catch (err) {
                    console.error('Erro ao buscar sess�o:', err);
                }
            }
        });
        session.mount('#header_app');
        }
    
            
        </script>
    </body>
