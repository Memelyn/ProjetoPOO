<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Recuperar Senha</title>
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
        max-width: 420px;
    }
    label, input {
        display: block;
        margin-bottom: 10px;
        width: 100%;
    }
    input[type="email"] {
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
        margin-top: 20px;
    }
    input[type="submit"]:hover {
        background-color: #818cf8;
    }
    .button-back {
        background-color: #6366f1; /* Botão menor */
        color: white;
        text-decoration: none;
        border-radius: 5px;
        padding: 5px 10px;
        font-size: 14px;
        position: relative;
        top: 20px; /* Posicionado mais abaixo */
        display: inline-block;
    }
    .button-back:hover {
        background-color: #818cf8;
    }
</style>
</head>
<body>
<h1>Recuperar Senha</h1>
<form action="RecuperarSenhaServlet" method="post">
    <label for="email">Digite seu e-mail cadastrado:</label>
    <input type="email" id="email" name="email" placeholder="seuemail@exemplo.com" required>
    
    <input type="submit" value="Recuperar Senha">
</form>
<!-- Botão para voltar para a página de login -->
<a href="login.jsp" class="button-back">Voltar para Login</a>
</body>
</html>
