package com.example.zerobaseassignment.sqlite;

import com.example.zerobaseassignment.dto.DistanceCalculate;
import com.example.zerobaseassignment.dto.SeoulJson;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class SQLiteJDBCTable {

    private Connection c = null;
    private Statement stmt = null;
    private static final DecimalFormat df = new DecimalFormat("0.0000");

    public void createTable() {
        try {

            Connection c = null;
            Statement stmt = null;
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\user\\DataGripProjects\\h2databse\\identifier.sqlite");

            DatabaseMetaData databaseMetaData = c.getMetaData();
            boolean tableExist = false;
            ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[] {"TABLE"});

            while (resultSet.next()) {
                String name = resultSet.getString("TABLE_NAME");
                if (name.equals("test4")) {
                    tableExist = true;
                }
            }

            if (tableExist) {
                return;
            }

            stmt = c.createStatement();
            String sql = "create table test4\n" +
                    "(\n" +
                    "\n" +
                    "    inout text not null,\n" +
                    "    instlFloor text not null,\n" +
                    "    instlMby text not null,\n" +
                    "    remars3 text not null,\n" +
                    "    instlTy text not null,\n" +
                    "    mgr text not null,\n" +
                    "    wrdofc text not null,\n" +
                    "    adres1 text not null,\n" +
                    "    adres2 text not null,\n" +
                    "    cmcwr text not null,\n" +
                    "    dttm text not null,\n" +
                    "    svc text not null,\n" +
                    "    main text not null,\n" +
                    "    lnt real not null,\n" +
                    "    cnstc text not null,\n" +
                    "    lat real not null\n" +
                    "\n" +
                    ")";

            stmt.executeUpdate(sql);
            stmt.close();
            c.close();

        } catch (Exception e) {
            System.out.println("SQLiteJDBCTable.SQLiteJDBCTable");
            System.out.println("e = " + e);
        }

    }

    public int JsonInsert() throws IOException, ParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        String file = "C:\\Users\\user\\Downloads\\zerobaseAssignment\\src\\main\\webapp\\seoul.json";
        Object obj = new JSONParser().parse(new InputStreamReader(new FileInputStream(file), "utf-8"));

        JSONObject jo = (JSONObject) obj;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String data = jo.get("DATA").toString();



        List<SeoulJson> seouls = objectMapper.readValue(data, new TypeReference<List<SeoulJson>>() {
        });

        int cnt = 0;

        try {

            Connection c = null;
            PreparedStatement stmt = null;
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\user\\DataGripProjects\\h2databse\\identifier.sqlite");
            System.out.println("Opened database successfully");


            String sql = "insert into test4(inout ,\n" +
                    "    instlFloor ,\n" +
                    "    instlMby ,\n" +
                    "    remars3 ,\n" +
                    "    instlTy ,\n" +
                    "    mgr ,\n" +
                    "    wrdofc ,\n" +
                    "    adres1 ,\n" +
                    "    adres2 ,\n" +
                    "    cmcwr ,\n" +
                    "    dttm ,\n" +
                    "    svc ,\n" +
                    "    main ,\n" +
                    "    lnt ,\n" +
                    "    cnstc ,\n" +
                    "    lat ) values (\n" +
                    "    ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            stmt = c.prepareStatement(sql);
            for (int i = 1; i < 2000; i++) {
                cnt += 1;
                stmt.setString(1, seouls.get(i).getX_swifi_inout_door());
                stmt.setString(2, seouls.get(i).getX_swifi_instl_floor());
                stmt.setString(3, seouls.get(i).getX_swifi_instl_mby());
                stmt.setString(4, seouls.get(i).getX_swifi_remars3());
                stmt.setString(5, seouls.get(i).getX_swifi_instl_ty());
                stmt.setString(6, seouls.get(i).getX_swifi_mgr_no());
                stmt.setString(7, seouls.get(i).getX_swifi_wrdofc());
                stmt.setString(8, seouls.get(i).getX_swifi_adres1());
                stmt.setString(9, seouls.get(i).getX_swifi_adres2());
                stmt.setString(10, seouls.get(i).getX_swifi_cmcwr());
                stmt.setString(11, seouls.get(i).getWork_dttm());
                stmt.setString(12, seouls.get(i).getX_swifi_svc_se());
                stmt.setString(13, seouls.get(i).getX_swifi_main_nm());
                stmt.setString(14, String.valueOf(seouls.get(i).getLnt()));
                stmt.setString(15, seouls.get(i).getX_swifi_cnstc_year());
                stmt.setString(16, String.valueOf(seouls.get(i).getLat()));
                stmt.executeUpdate();
            }



            stmt.close();
            c.close();
            System.out.println("insertion completed");
            return cnt;
        } catch (Exception e) {
            System.out.println("SQLiteJDBCTable.JsonInsert");
            System.out.println("e = " + e);
        }

        return cnt;


        }

    public List<SeoulJson> findLongLat(String lon, String lat) {
        double longditude = Double.parseDouble(lon);
        double latitude = Double.parseDouble(lat);
        List<SeoulJson> seouls = new ArrayList<>();
        List<Double> distances = new ArrayList<>();

        try {

            Connection c = null;
            Statement stmt = null;
            ResultSet rs = null;

            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\user\\DataGripProjects\\h2databse\\identifier.sqlite");
            System.out.println("Opened database successfully");
            stmt = c.createStatement();

            String sql = "select * from test4 where lnt between " + (longditude - 0.1) + " and " + (longditude +0.1) +
                    " and lat between " + (latitude -0.1) + " and " + (latitude +0.1) + ";";

            rs = stmt.executeQuery(sql);

            int cnt = 0;


            while (rs.next()) {
                if (cnt == 20) {
                    break;
                }
                cnt += 1;

                SeoulJson seoulJson = new SeoulJson();
                seoulJson.setX_swifi_inout_door(rs.getString("inout"));
                seoulJson.setX_swifi_instl_floor(rs.getString("instlFloor"));
                seoulJson.setX_swifi_instl_mby(rs.getString("instlMby"));
                seoulJson.setX_swifi_remars3(rs.getString("remars3"));
                seoulJson.setX_swifi_instl_ty(rs.getString("instlTy"));
                seoulJson.setX_swifi_mgr_no(rs.getString("mgr"));
                seoulJson.setX_swifi_wrdofc(rs.getString("wrdofc"));
                seoulJson.setX_swifi_adres1(rs.getString("adres1"));
                seoulJson.setX_swifi_adres2(rs.getString("adres2"));
                seoulJson.setX_swifi_cmcwr(rs.getString("cmcwr"));
                seoulJson.setWork_dttm(rs.getString("dttm"));
                seoulJson.setX_swifi_svc_se(rs.getString("svc"));
                seoulJson.setX_swifi_main_nm(rs.getString("main"));
                seoulJson.setLnt(Double.valueOf(rs.getString("lnt")));
                seoulJson.setX_swifi_cnstc_year(rs.getString("cnstc"));
                seoulJson.setLat(Double.valueOf(rs.getString("lat")));

                double distance = DistanceCalculate.distance(latitude, Double.valueOf(rs.getString("lat")), longditude, Double.valueOf(rs.getString("lnt")));
                seoulJson.setKm(Double.valueOf(df.format(distance)));
                seouls.add(seoulJson);
                distances.add(seoulJson.getKm());

            }
            stmt.close();
            c.close();

            seouls.sort(Comparator.comparing(SeoulJson::getKm));


            return seouls;
        } catch (Exception e) {
            System.out.println("SQLiteJDBCTable.findLongLat");
            System.out.println("e = " + e);
        }

        return seouls;

    }



//    public List<SeoulJson> history() {
//
//    }



}
