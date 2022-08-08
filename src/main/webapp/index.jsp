<%@ page import="com.example.zerobaseassignment.sqlite.SQLiteJDBCTable" %>
<%@ page import="com.example.zerobaseassignment.dto.SeoulJson" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="com.example.zerobaseassignment.service.SeoulService" %>
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
<a href="/">홈</a> <a href="/history.jsp">위치 히스토리 목록</a>
<a href="/load-wifi.jsp">Open API 와이파이 정보 가져오기</a>
<form>
    <label>LAT:</label>
    <input type="text" id="lat" name="lat" value="0.0"/>
    <label>LNT:</label>
    <input type="text" id="long" name="long" value="0.0"/>
    <button id="get-location" type="button" onclick="getLocation()">
        내위치 가져오기
    </button>
    <input type="submit" value="근처 WIFI 정보 보기"/>
</form>
<br/>


<%


    SeoulService seoulService = new SeoulService();


    List<SeoulJson> wifiByLongLat = null;

    if (request.getParameter("lat") == null) {
        System.out.println("Nothing is provided");
    } else if (request.getParameter("lat").equals("0.0")) {
        System.out.println("Please provide lat and long");
    } else {

        String longdi = request.getParameter("long");
        String lat = request.getParameter("lat");

         wifiByLongLat = seoulService.findWifiByLongLat(longdi, lat);


    }


%>

<table id="customers">
    <tr>
        <th>거리</th>
        <th>관리번호</th>
        <th>자치구</th>
        <th>와이파이명</th>
        <th>도로명주소</th>
        <th>상세주소</th>
        <th>설치위치 (층)</th>
        <th>설치기관</th>
        <th>서비스구분</th>
        <th>망종류</th>
        <th>설치년도</th>
        <th>설내외구분</th>
        <th>WIFI접속환경</th>
        <th>X좌표</th>
        <th>Y좌표</th>
        <th>작업일자</th>
    </tr>
    <%


        if (wifiByLongLat == null) {

        } else {

            /*히스토리 테이블로 저장*/
            seoulService.saveSeoulWifiHistory(wifiByLongLat);
            for (SeoulJson seoulJson : wifiByLongLat) {


                out.write("<tr>");

                out.write("<td>" + seoulJson.getKm() + " km</td>");
                out.write("<td>" + seoulJson.getX_swifi_mgr_no() + " </td>");
                out.write("<td>" + seoulJson.getX_swifi_wrdofc() + " </td>");
                out.write("<td>" + seoulJson.getX_swifi_main_nm() + " </td>");
                out.write("<td>" + seoulJson.getX_swifi_adres1() + " </td>");
                out.write("<td>" + seoulJson.getX_swifi_adres2() + " </td>");
                out.write("<td>" + seoulJson.getX_swifi_instl_floor() + " </td>");
                out.write("<td>" + seoulJson.getX_swifi_instl_ty() + " </td>");
                out.write("<td>" + seoulJson.getX_swifi_instl_mby() + " </td>");
                out.write("<td>" + seoulJson.getX_swifi_svc_se() + " </td>");
                out.write("<td>" + seoulJson.getX_swifi_cmcwr() + " </td>");
                out.write("<td>" + seoulJson.getX_swifi_cnstc_year() + " </td>");
                out.write("<td>" + seoulJson.getX_swifi_inout_door() + " </td>");
                out.write("<td>" + seoulJson.getLat() + " </td>");
                out.write("<td>" + seoulJson.getLnt() + " </td>");
                out.write("<td>" + seoulJson.getWork_dttm() + " </td>");
                out.write("</tr>");


            }
        }


    %>


</table>


<script>
    var x = document.getElementById("lat");
    var y = document.getElementById("long");

    function getLocation() {
        console.log("working");
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(showPosition);
        } else {
            x.innerHTML = "Geolocation is not supported by this browser.";
        }
    }

    function showPosition(position) {
        console.log(position);
        x.value = position.coords.latitude;
        y.value = position.coords.longitude;
    }
</script>
</body>
</html>
