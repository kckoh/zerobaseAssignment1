
<%@ page import="com.example.zerobaseassignment.sqlite.SQLiteJDBCTable" %>
<%@ page import="com.example.zerobaseassignment.service.SeoulService" %><%--
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

    SeoulService seoulService = new SeoulService();
    int numSavedWifi = seoulService.saveSeoulWifiApi();


    int cnt = sqLiteJDBCTable.JsonInsert();
    out.write("<h1>" + numSavedWifi +"개의 WIFI 정보를 정상적으로 저장하였습니다. </h1>");


%>




<a href="/">홈 으로 가기</a>
</body>
</html>
