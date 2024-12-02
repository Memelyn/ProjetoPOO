<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
 <link rel="stylesheet" href="styleCadastro.css">
<title>Cadastro</title>
</head>
<body>
<h1>Cadastro</h1>
<!-- Formulário de cadastro -->
<form action="CadastroServlet" method="post">
    <label for="email">E-mail:</label>
    <input type="email" id="email" name="email" placeholder="seuemail@exemplo.com" required>

    <label for="username">Usuário:</label>
    <input type="text" id="username" name="username" required>
    
    <label for="password">Senha:</label>
    <input type="password" id="password" name="password" required>
    
    <input type="submit" value="Cadastrar">
</form>
<!-- Link para a página de login -->
<form action="login.jsp" method="get">
    <input type="submit" value="Já possui uma conta? Faça login">
</form>
</body>
</html>
