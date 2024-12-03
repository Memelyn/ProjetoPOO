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

        <!-- Se o usuário está carregado -->
        <div v-if="user">
            
            <h2>Histórico de Análises</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Imagem do Veículo</th>
                        <th>Relatório</th>
                        <th>Data de Análise</th>
                    </tr>
                </thead>
                <tbody>
                   <tr v-for="(analysis, index) in history" :key="index">
                     <td>{{ index + 1 }}</td>
                     <td><img :src="getImageUrl(analysis.vehicleImage)" alt="Imagem do Veículo" width="100"></td>
                     <td>{{ analysis.vehicleReport }}</td>
                     <td>{{ formatDate(analysis.analysisDate) }}</td>
                 </tr>
                </tbody>
            </table>
        </div>

        <!-- Caso o usuário não seja encontrado -->
        <div v-else>
            <p>Erro ao carregar informações do usuário.</p>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const app = Vue.createApp({
                data() {
                    return {
                        user: null, // Dados do usuário logado
                        history: [], // Histórico de análises
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
                                alert('Erro ao carregar usuário');
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
                                alert('Erro ao carregar histórico de análises');
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
                    await this.fetchUser(); // Busca as informações do usuário e histórico ao carregar a página
                }
            });
            app.mount('#app');
        });
    </script>
</body>
</html>
