<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--<%@include file="WEB-INF/jspf/header.jspf" %>--%>
<!DOCTYPE html>
<html>
<head>
    <title>Gerenciar Conta</title>
   <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
</head>
<body>
    <%@include file="WEB-INF/jspf/header.jspf" %>
    <div id="app" class="container mt-5">
        <h1>Gerenciar Conta</h1>

        <!-- Se o usuário está carregado -->
        <div v-if="user">
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
                        name:'',
                        login:''
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
        } else {
            alert('Erro ao carregar usuário');
        }
    } catch (err) {
        alert('Erro ao conectar ao servidor');
    }
                    },
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
                            this.user.login = this.user.login;
                            this.user.name = this.user.name;
                        }
                    } catch (err) {
                        alert('Erro ao conectar ao servidor');
                    }
                    },
                    async deleteUser() {
                        if (!confirm('Tem certeza que deseja deletar sua conta? Esta ação não pode ser desfeita.')) {
                            return;
                        }

                        try {
                            const response = await fetch(`/AutoVar/api/users?id=${this.user.rowId}`, {
                                method: 'DELETE'
                            });

                            if (response.ok) {
                                alert('Conta deletada com sucesso!');
                                window.location.href = '/login.jsp'; // Redireciona para a página de login
                            } else {
                                alert('Erro ao deletar conta');
                            }
                        } catch (err) {
                            alert('Erro ao conectar ao servidor');
                        }
                    }
                },
                async mounted() {
                    await this.fetchUser(); // Busca as informações do usuário ao carregar a página
                }
            });
            app.mount('#app');
        });
    </script>
</body>
</html>

