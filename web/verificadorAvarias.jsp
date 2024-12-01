<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="styleAvarias.css">
    <title>Verificador de Avarias</title>
</head>
<body>
    <main>
        <h1>Verificador de Avarias</h1>
        <!-- Formulário para enviar requisição ao servlet -->
        <form action="processar" method="post">
            <button type="submit">Enviar</button>
        </form>
        <!-- Exibição do resultado -->
        <p class="output">
            <%= request.getAttribute("resultado") != null ? request.getAttribute("resultado") : "(Resultados aparecerão aqui)" %>
        </p>
    </main>
</body>
</html>
