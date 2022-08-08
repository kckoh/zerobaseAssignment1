<%@ page import="com.example.zerobaseassignment.sqlite.SQLiteJDBCTable" %>
<%@ page import="com.example.zerobaseassignment.dto.SeoulJson" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.example.zerobaseassignment.service.SeoulService" %>
<%@ page import="com.example.zerobaseassignment.dto.History" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>JSP - Hello World</title>
  <meta charset="UTF-8">
  <style>
    #customers {
      font-family: Arial, Helvetica, sans-serif;
      border-collapse: collapse;
      width: 100%;
    }

    #customers td,
    #customers th {
      border: 1px solid #ddd;
      padding: 8px;
    }

    #customers tr:nth-child(even) {
      background-color: #f2f2f2;
    }

    #customers tr:hover {
      background-color: #ddd;
    }

    #customers th {
      padding-top: 12px;
      padding-bottom: 12px;
      text-align: left;
      background-color: #04aa6d;
      color: white;
    }
  </style>
</head>
<body>

<h1>와이파이 정보 구하기</h1>
<a href="/">홈</a> <a href="/">위치 히스토리 목록</a>
<a href="/load-wifi.jsp">Open API 와이파이 정보 가져오기</a>

<br/>



<table id="customers">
  <tr>
   <th>ID</th>
    <th>x좌표</th>
    <th>y좌표</th>
    <th>조회일자</th>
    <th>비고</th>
  </tr>
    <%
        SeoulService seoulService = new SeoulService();

        List<History> historyList = seoulService.getHistory();

        if (historyList.size() <1) {

        } else {

            for (History history : historyList) {


                out.write("<tr>");

                out.write("<td>" + history.getId() + " </td>");
                out.write("<td>" + history.getX() + " </td>");
                out.write("<td>" + history.getY() + " </td>");
                out.write("<td>" + history.getDate() + " </td>");
                out.write("<td>" +
                        "<form action=\"/history\" method=\"POST\">\n" +
                        "    <input type=\"hidden\" name=\"id\" value=" + history.getId() + ">\n" +
                        "    <input type=\"submit\" value=\"삭제\">\n" +
                        "</form>" +
                        " </td>");
                out.write("</tr>");


            }
        }




    %>


</table>



</body>
</html>
