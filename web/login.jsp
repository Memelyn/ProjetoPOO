<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="styleLogin.css">
<title>Login</title>
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
