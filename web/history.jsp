<!DOCTYPE html>
<html>
<head>
    <title>Gerenciar Conta</title>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN6jIeHz" crossorigin="anonymous"></script>
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
</head>
<body>
<link rel="stylesheet" href="styleUser.css">
    <%@include file="WEB-INF/jspf/header.jspf" %>
    <div id="app" class="container mt-5">
        <h1>Gerenciar Conta</h1>

        <!-- Se o usu�rio est� carregado -->
        <div v-if="user">
            
            <h2>Hist�rico de An�lises</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Imagem do Ve�culo</th>
                        <th>Relat�rio</th>
                        <th>Data de An�lise</th>
                    </tr>
                </thead>
                <tbody>
                   <tr v-for="(analysis, index) in history" :key="index">
                     <td>{{ index + 1 }}</td>
                     <td><img :src="getImageUrl(analysis.vehicleImage)" alt="Imagem do Ve�culo" width="100"></td>
                     <td>{{ analysis.vehicleReport }}</td>
                     <td>{{ formatDate(analysis.analysisDate) }}</td>
                 </tr>
                </tbody>
            </table>
        </div>

        <!-- Caso o usu�rio n�o seja encontrado -->
        <div v-else>
            <p>Erro ao carregar informa��es do usu�rio.</p>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const app = Vue.createApp({
                data() {
                    return {
                        user: null, // Dados do usu�rio logado
                        history: [], // Hist�rico de an�lises
                    };
                },
                methods: {
                    async fetchUser() {
                        try {
                            const response = await fetch(`/AutoVar/api/session`, {
                                method: 'GET',
                                headers: { 'Content-Type': 'application/json' }
                            });
                            if (response.ok) {
                                this.user = await response.json();
                                await this.fetchHistory();
                            } else {
                                alert('Erro ao carregar usu�rio');
                            }
                        } catch (err) {
                            alert('Erro ao conectar ao servidor');
                        }
                    },
                    async fetchHistory() {
                        try {
                            const response = await fetch(`/AutoVar/api/analyser`, {
                                method: 'GET',
                                headers: { 'Content-Type': 'application/json' }
                            });
                            if (response.ok) {
                                this.history = await response.json();
                            } else {
                                alert('Erro ao carregar hist�rico de an�lises');
                            }
                        } catch (err) {
                            alert('Erro ao conectar ao servidor');
                        }
                    },
                    formatDate(dateString) {
                        const date = new Date(dateString);
                        return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
                    },
                  
                async mounted() {
                    await this.fetchUser(); // Busca as informa��es do usu�rio e hist�rico ao carregar a p�gina
                }
            });
            app.mount('#app');
        });
    </script>
</body>
</html>
