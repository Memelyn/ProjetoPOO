<!DOCTYPE html>
<html>
<head>
    <title>Gerenciar Conta</title>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
    <link rel="stylesheet" href="styleCadastro.css">
</head>
<body>
    <%@include file="WEB-INF/jspf/header.jspf" %>
    <div id="index_app">
        <div v-if="!user">
            <header class="text-center mt-4">
                <h1></h1>
            </header>
        </div>

        <div v-else>
            <header class="text-center mt-4">
                <h1>Verificador de Avarias</h1>
            </header>

            <!-- Formulário para análise de imagem -->
            <form method="POST" action="APIGemini">
                <div style="margin: 50px auto; width: 80%; max-width: 600px; text-align: center; background: lightblue; padding: 20px; border: 1px solid #ccc; border-radius: 8px;">
                    <label for="img_end" class="form-label">Endereço da Imagem do Automóvel</label>
                    <input type="text" class="form-control mb-3" id="img_end" name="imagePath" required>
                    <br>
                    <input type="submit" value="Analisar Imagem" class="btn btn-primary"/>
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
            </div>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const app = Vue.createApp({
                data() {
                    return {
                        user: null // Dados do usuário logado
                    };
                },
                methods: {
                    async fetchUser() {
                        try {
                            const response = await fetch('/AutoVar/api/session');
                            if (response.ok) {
                                this.user = await response.json();
                            } else {
                                console.warn('Usuário não logado');
                                this.user = null;
                            }
                        } catch (err) {
                            console.error('Erro ao conectar ao servidor:', err);
                        }
                    }
                },
                async mounted() {
                    await this.fetchUser(); // Busca as informações do usuário ao carregar a página
                }
            });
            app.mount('#index_app');
        });
    </script>
</body>
</html>