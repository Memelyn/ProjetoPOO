<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="styleSenha.css">
<title>Recuperar Senha</title>
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
