<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<style>
    body {
        background-color: #1e1e2e; /* Fundo escuro */
        color: #e4e4e7; /* Texto em cor clara */
        font-family: Arial, sans-serif;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 100vh;
        margin: 0;
    }
    h1 {
        color: #f3f4f6; /* Título mais claro */
    }
    form {
        background-color: #2e2e3d; /* Fundo do formulário */
        padding: 30px; /* Ajustado para mais espaço interno */
        border-radius: 10px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.3);
        width: 100%;
        max-width: 420px; /* Ajustado para levemente maior */
    }
    label, input {
        display: block;
        margin-bottom: 10px;
        width: 100%;
    }
    input[type="text"], input[type="password"] {
        padding: 10px;
        border: 1px solid #3e3e4d;
        border-radius: 5px;
        background-color: #3e3e4d;
        color: #e4e4e7;
    }
    input[type="submit"] {
        background-color: #6366f1; /* Botão com cor vibrante */
        color: white;
        border: none;
        border-radius: 5px;
        padding: 10px;
        cursor: pointer;
        font-weight: bold;
        text-align: center;
        display: block;
        width: 100%;
        margin-bottom: 10px;
    }
    input[type="submit"]:hover {
        background-color: #818cf8;
    }
    .button-back {
        background-color: #6366f1; /* Botão menor */
        color: white;
        text-decoration: none;
        border-radius: 5px;
        padding: 5px 10px; /* Tamanho reduzido */
        font-size: 14px; /* Fonte menor */
        position: relative;
        top: 20px; /* Posicionado mais abaixo */
        display: inline-block;
    }
    .button-back:hover {
        background-color: #818cf8;
    }
    .forgot-password {
        margin-top: 10px;
        font-size: 14px;
    }
    .forgot-password a {
        color: #818cf8;
        text-decoration: none;
    }
    .forgot-password a:hover {
        text-decoration: underline;
    }
</style>
</head>
<body>
    
<h1>Login</h1>
<form action="LoginServlet" method="post">
    <label for="username">Usuário:</label>
    <input type="text" id="username" name="username" required>
    
    <label for="password">Senha:</label>
    <input type="password" id="password" name="password" required>
    
    <input type="submit" value="Entrar">
</form>
<!-- Link para "Esqueceu a senha?" -->
<div class="forgot-password">
    <a href="recuperarSenha.jsp">Esqueceu a senha?</a>
</div>
<!-- Botão para voltar para a página de cadastro -->
<a href="cadastro.jsp" class="button-back">Voltar para Cadastro</a>
</body>
</html>
