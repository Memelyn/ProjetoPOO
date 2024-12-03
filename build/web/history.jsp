<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--<%@include file="WEB-INF/jspf/header.jspf" %>--%>
<!DOCTYPE html>
<html>
<head>
    <title>Gerenciar Conta</title>
    <link rel="stylesheet" href="css/styleCadastro.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
</head>
<body>
<link rel="stylesheet" href="css/styleUser.css">
<%@include file="WEB-INF/jspf/header.jspf" %>
<div id="app" class="container mt-5">

    <!-- Se o usuário está carregado -->
    <div v-if="user">
        <h1>Histórico de Análises</h1>
        
        <!-- Lista de histórico de análises -->
        <div v-if="analysisHistory.length">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Data</th>
                        <th>Imagem do Veículo</th>
                        <th>Relatório</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="analysis in analysisHistory" :key="analysis.rowId_user">
                        <td>{{ formatDate(analysis.analysis_date) }}</td>
                        <td><img :src="analysis.vehicle_image" alt="Imagem do Veículo" class="img-thumbnail" style="max-width: 100px;"></td>
                        <td>{{ analysis.vehicle_report }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div v-else>
            <p>Não há histórico de análises disponível.</p>
        </div>
        
        <hr>
        
        <button @click="deleteUser" class="btn btn-danger">Deletar Conta</button>
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
                    analysisHistory: [] // Histórico de análises
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
                            this.fetchAnalysisHistory(); // Após carregar o usuário, carrega o histórico
                        } else {
                            alert('Erro ao carregar usuário');
                        }
                    } catch (err) {
                        alert('Erro ao conectar ao servidor');
                    }
                },
                      async fetchAnalysisHistory() {
    try {
        const response = await fetch(`/AutoVar/api/analyser`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
        if (response.ok) {
            const data = await response.json();
            console.log("Dados recebidos do backend:", data); // Verifique isso no console
            this.analysisHistory = data.list || []; // Inicializa com lista vazia caso `list` não exista
        } else {
            console.error("Erro ao carregar histórico: ", await response.text());
            alert('Erro ao carregar histórico de análises');
        }
    } catch (err) {
        console.error("Erro de conexão:", err);
        alert('Erro ao conectar ao servidor');
    }
},


                formatDate(dateString) {
                    const date = new Date(dateString);
                    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
                },
//                async deleteUser() {
//                    if (!confirm('Tem certeza que deseja deletar sua conta? Esta ação não pode ser desfeita.')) {
//                        return;
//                    }
//                    try {
//                        const response = await fetch(`/AutoVar/api/users?id=${this.user.rowId}`, {
//                            method: 'DELETE'
//                        });
//                        if (response.ok) {
//                            alert('Conta deletada com sucesso!');
//                            window.location.href = '/login.jsp'; // Redireciona para a página de login
//                        } else {
//                            alert('Erro ao deletar conta');
//                        }
//                    } catch (err) {
//                        alert('Erro ao conectar ao servidor');
//                    }
//                }
            //},
            async mounted() {
                this.fetchUser(); // Busca as informações do usuário ao carregar a página
            }
             app.mount('#app');
        });
       
    });
</script>
</body>
</html>
