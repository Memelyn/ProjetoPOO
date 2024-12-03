<!-- header.jspf -->
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Login e Cadastro</title>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
        <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
    </head>
    <body>
       <div id="header_app" class="container mt-5">
    <!-- Se não houver usuário na sessão -->
    <div v-if="!user">
        <form @submit.prevent="register">
            <div class="form-group">
                <label for="newLogin">Login</label>
                <input type="text" id="newLogin" v-model="newLogin" class="form-control" required>
            </div>
            <div class="form-group">
                <label for="newName">Nome</label>
                <input type="text" id="newName" v-model="newName" class="form-control" required>
            </div>
            <div class="form-group">
                <label for="newPassword">Senha</label>
                <input type="password" id="newPassword" v-model="newPassword" class="form-control" required>
            </div>
            <button type="submit" class="btn btn-success">Cadastrar</button>
        </form>
    </div>

    <!-- Se houver usuário na sessão -->
    <div v-else>
        <!-- Menu para usuários logados -->
        <nav class="navbar navbar-expand-lg navbar-light bg-light mb-3">
            <div class="container-fluid">
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav">
                        <li class="nav-item">
                            <a class="nav-link" href="index.jsp">Verificador</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="history.jsp">Histórico</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="user.jsp">Usuário</a>
                        </li>
                        <li class="nav-item">
                            <button class="nav-link text-danger" href="#" @click.prevent="logout"/>Sair</a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
        
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const session = Vue.createApp({
            data() {
                return {
                    user: null, // Receba o usuário da sessão via API ou inicialize como `null`
                    login: '',
                    password: '',
                    newLogin: '',
                    newName: '',
                    newPassword: ''
                };
            },
            methods: {
               
                async register() {
                    try {
                        const response = await fetch('/AutoVar/api/users', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({
                                login: this.newLogin,
                                name: this.newName,
                                password: this.newPassword
                            })
                        });
                        if (!response.ok) {
                            const error = await response.json();
                            alert(error.error || 'Erro ao cadastrar');
                        } else {
                            alert('Usuário cadastrado com sucesso!');
                            this.newLogin = '';
                            this.newName = '';
                            this.newPassword = '';
                        }
                    } catch (err) {
                        alert('Erro ao conectar ao servidor');
                    }
                
            },
            async mounted() {
                try {
                    const response = await fetch('/AutoVar/api/session'); // Checa se há uma sessão ativa
                    if (response.ok) {
                        this.user = await response.json();
                    }
                } catch (err) {
                    console.error('Erro ao buscar sessão:', err);
                }
            }
        });
        session.mount('#header_app');
    });
</script>


      