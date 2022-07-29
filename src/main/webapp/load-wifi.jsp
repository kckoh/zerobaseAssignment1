
<%@ page import="com.example.zerobaseassignment.sqlite.SQLiteJDBCTable" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2022-07-23
  Time: 오후 9:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    SQLiteJDBCTable sqLiteJDBCTable = new SQLiteJDBCTable();
    sqLiteJDBCTable.createTable();
    sqLiteJDBCTable.JsonInsert();

%>
<h1>Hello world!</h1>
</body>
</html>
