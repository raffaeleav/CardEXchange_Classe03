<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/WEB-INF/css/recensione.css"/>
    <title>Title</title>
</head>
<body>

<div class="recensioneContainer">
    <form method="post" action="addRecensione">
        <div class="rate">
            <input type="radio" id="star5" name="rate" value="5" />
            <label for="star5" title="text"></label>
            <input type="radio" id="star4" name="rate" value="4" />
            <label for="star4" title="text"></label>
            <input type="radio" id="star3" name="rate" value="3" />
            <label for="star3" title="text"></label>
            <input type="radio" id="star2" name="rate" value="2" />
            <label for="star2" title="text"></label>
            <input type="radio" id="star1" name="rate" value="1" />
            <label for="star1" title="text"></label>
        </div><br>
        <textarea class="text-form" id="testo" name="text" rows="3" placeholder="Inserisci testo"></textarea><br>
        <input class="submit" type="submit" value="Invia">
    </form>
</div>

</body>
</html>
