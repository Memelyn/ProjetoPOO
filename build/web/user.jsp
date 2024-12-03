<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%-- Diretiva JSP que define o tipo de conteúdo da página como HTML com codificação UTF-8 --%>
<%--<%@include file="WEB-INF/jspf/header.jspf" %>--%>
<%-- Inclusão comentada do arquivo "header.jspf" da pasta WEB-INF. Caso precise, remova os comentários para usar o cabeçalho. --%>

<!DOCTYPE html>
<html>
<head>
    <title>Gerenciar Conta</title>
    <!-- Incluindo o Bootstrap para estilização -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" 
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" 
        crossorigin="anonymous"></script>
    <!-- Incluindo o Vue.js para manipulação reativa -->
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
</head>
<body>
    <%@include file="WEB-INF/jspf/header.jspf" %>
    <%-- Inclui o arquivo "header.jspf" no corpo da página. Geralmente contém o cabeçalho comum do site. --%>

    <div id="app" class="container mt-5">
        <h1>Gerenciar Conta</h1>

        <!-- Renderiza esta seção se os dados do usuário foram carregados -->
        <div v-if="user">
            <!-- Formulário para atualizar informações do usuário -->
            <form @submit.prevent="updateUser">
                <div class="form-group">
                    <label for="name">Nome</label>
                    <input type="text" id="name" v-model="user.name" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="login">Login</label>
                    <input type="text" id="login" v-model="user.login" class="form-control" required>
                </div>
                <button type="submit" class="btn btn-primary mt-3">Atualizar Informações</button>
            </form>

            <hr>

            <!-- Botão para deletar conta -->
            <button @click="deleteUser" class="btn btn-danger">Deletar Conta</button>
        </div>

        <!-- Renderiza esta seção se os dados do usuário não foram carregados -->
        <div v-else>
            <p>Erro ao carregar informações do usuário.</p>
        </div>
    </div>

    <!-- Script Vue.js -->
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const app = Vue.createApp({
                data() {
                    return {
                        user: null, // Armazena os dados do usuário logado
                    };
                },
                methods: {
                    // Método para buscar as informações do usuário
                    async fetchUser() {
                        try {
                            const response = await fetch(/AutoVar/api/session, {
                                method: 'GET',
                                headers: { 'Content-Type': 'application/json' }
                            });
                            if (response.ok) {
                                this.user = await response.json();
                            } else {
                                alert('Erro ao carregar usuário');
                            }
                        } catch (err) {
                            alert('Erro ao conectar ao servidor');
                        }
                    },
                    // Método para atualizar as informações do usuário
                    async updateUser() {
                        try {
                            const payload = {
                                login: this.user.login,
                                name: this.user.name
                            };
                            const response = await fetch('/AutoVar/api/users', {
                                method: 'PUT',
                                headers: { 'Content-Type': 'application/json' },
                                body: JSON.stringify(payload),
                            });
                            if (!response.ok) {
                                const error = await response.json();
                                alert(error.error || 'Erro ao atualizar');
                            } else {
                                alert('Usuário atualizado com sucesso!');
                            }
                        } catch (err) {
                            alert('Erro ao conectar ao servidor');
                        }
                    },
                    // Método para deletar o usuário
                    async deleteUser() {
                        if (!confirm('Tem certeza que deseja deletar sua conta? Esta ação não pode ser desfeita.')) {
                            return; // Aborta a operação se o usuário cancelar
                        }
                        try {
                            const response = await fetch(/AutoVar/api/users?id=${this.user.rowId}, {
                                method: 'DELETE'
                            });
                            if (response.ok) {
                                alert('Conta deletada com sucesso!');
                                window.location.href = 'index.jsp'; // Redireciona para a página inicial
                            } else {
                                alert('Erro ao deletar conta');
                            }
                        } catch (err) {
                            alert('Erro ao conectar ao servidor');
                        }
                    }
                },
                // Chamado automaticamente ao carregar a página
                async mounted() {
                    await this.fetchUser(); // Carrega as informações do usuário
                }
            });
            app.mount('#app'); // Monta a aplicação no elemento com id "app"
        });
    </script>
</body>
</html>
