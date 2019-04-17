<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Title</title>
</head>
<body>
    hello,${key.goodName}
    <#if (age>18) >
        成年
    </#if>
    <hr/>
    <#list goodList as goods>
        ${goods.goodName} - ${goods.id}
    </#list>


</body>
</html>