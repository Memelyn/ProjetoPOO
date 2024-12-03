<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="styleCadastro.css">
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
    <title>Cadastro</title>
</head>
<body>
    <h1>Cadastro</h1>
    <!-- Formulário de cadastro -->
    <div id="cadastro_app">
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

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const cadastroApp = Vue.createApp({
                data() {
                    return {
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
                                window.location.href = 'index.jsp'; // Redireciona após sucesso
                            }
                        } catch (err) {
                            alert('Erro ao conectar ao servidor');
                        }
                    }
                }
            });
            cadastroApp.mount('#cadastro_app');
        });
    </script>

    <!-- Link para a página de login -->
    <form action="index.jsp" method="get">
        <input type="submit" value="Já possui uma conta? Faça login">
    </form>
</body>
</html>
