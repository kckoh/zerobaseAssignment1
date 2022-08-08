package com.example.zerobaseassignment.repository;

import com.example.zerobaseassignment.dto.DistanceCalculate;
import com.example.zerobaseassignment.dto.History;
import com.example.zerobaseassignment.dto.SeoulJson;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.example.zerobaseassignment.type.StaticFiles.*;

@Getter
@Setter
@NoArgsConstructor
public class SeoulRepository {
    private static final DecimalFormat df = new DecimalFormat("0.0000");


    public List<SeoulJson> getSeoulWifiApi() throws IOException, ParseException {

        ObjectMapper objectMapper = new ObjectMapper();

        String file = SEOULJSONFILE;
        Object obj = new JSONParser().parse(new InputStreamReader(new FileInputStream(file), "utf-8"));

        JSONObject jo = (JSONObject) obj;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String data = jo.get("DATA").toString();

        List<SeoulJson> seoulJsons = objectMapper.readValue(data, new TypeReference<List<SeoulJson>>() {
        });

        return seoulJsons;
    }

    public int saveSeoulWifiApi(List<SeoulJson> seoulWifiApi) {
        int cnt = 0;
        try {

            Connection c = null;
            PreparedStatement stmt = null;
            Class.forName(JDBCSQLITE);
            c = DriverManager.getConnection(SQLITEFILEPATH);

            String sql = "insert into wifiInfo(inout ,\n" +
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


            for (SeoulJson seoulJson :
                    seoulWifiApi) {
                cnt += 1;
                stmt.setString(1, seoulJson.getX_swifi_inout_door());
                stmt.setString(2, seoulJson.getX_swifi_instl_floor());
                stmt.setString(3, seoulJson.getX_swifi_instl_mby());
                stmt.setString(4, seoulJson.getX_swifi_remars3());
                stmt.setString(5, seoulJson.getX_swifi_instl_ty());
                stmt.setString(6, seoulJson.getX_swifi_mgr_no());
                stmt.setString(7, seoulJson.getX_swifi_wrdofc());
                stmt.setString(8, seoulJson.getX_swifi_adres1());
                stmt.setString(9, seoulJson.getX_swifi_adres2());
                stmt.setString(10, seoulJson.getX_swifi_cmcwr());
                stmt.setString(11, seoulJson.getWork_dttm());
                stmt.setString(12, seoulJson.getX_swifi_svc_se());
                stmt.setString(13, seoulJson.getX_swifi_main_nm());
                stmt.setString(14, String.valueOf(seoulJson.getLnt()));
                stmt.setString(15, seoulJson.getX_swifi_cnstc_year());
                stmt.setString(16, String.valueOf(seoulJson.getLat()));
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


    public List<SeoulJson> findByLongLat(String longdi, String lat) {

        double longditude = Double.parseDouble(longdi);
        double latitude = Double.parseDouble(lat);
        List<SeoulJson> seouls = new ArrayList<>();
        List<Double> distances = new ArrayList<>();

        try {

            Connection c = null;
            Statement stmt = null;
            ResultSet rs = null;

            Class.forName(JDBCSQLITE);
            c = DriverManager.getConnection(SQLITEFILEPATH);

            stmt = c.createStatement();

            String sql = "select * from wifiInfo where lnt between " + (longditude - 0.1) + " and " + (longditude + 0.1) +
                    " and lat between " + (latitude - 0.1) + " and " + (latitude + 0.1) + ";";

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

    public void saveHistory(List<SeoulJson> wifiByLongLat) {
        try {

            Connection c = null;
            PreparedStatement stmt = null;
            Class.forName(JDBCSQLITE);
            c = DriverManager.getConnection(SQLITEFILEPATH);

            String sql = "insert into history (x,y,date) values( ?,?,?);";

            stmt = c.prepareStatement(sql);

            for (SeoulJson seoulJson :
                    wifiByLongLat) {
                stmt.setString(1, seoulJson.getLnt().toString());
                stmt.setString(2, seoulJson.getLat().toString());
                stmt.setString(3, LocalDateTime.now().toString());
                stmt.executeUpdate();
            }


            stmt.close();
            c.close();


        } catch (Exception e) {
            System.out.println("e = " + e);
        }
    }

    public List<History> getHistory() {
        List<History> historyList = new ArrayList<>();
        try {

            Connection c = null;
            Statement stmt = null;
            ResultSet rs = null;

            Class.forName(JDBCSQLITE);
            c = DriverManager.getConnection(SQLITEFILEPATH);

            stmt = c.createStatement();

            String sql = "select * from history;";

            rs = stmt.executeQuery(sql);


            while (rs.next()) {

                History history = History.builder()
                        .id(rs.getString("id"))
                        .x(rs.getString("x"))
                        .y(rs.getString("y"))
                        .date(LocalDateTime.now().toString())
                        .build();


                historyList.add(history);

            }
            stmt.close();
            c.close();
            System.out.println("historyList = " + historyList);
            return historyList;


        } catch (Exception e) {
            System.out.println("SQLiteJDBCTable.findLongLat");
            System.out.println("e = " + e);
        }
        return historyList;
    }

    public void deleteHistoryById(String id) {
        try {

            Connection c = null;
            PreparedStatement stmt = null;
            Class.forName(JDBCSQLITE);
            c = DriverManager.getConnection(SQLITEFILEPATH);
            System.out.println("id = " + id);

            String sql = "delete from history where id = ?;";

            stmt = c.prepareStatement(sql);


            stmt.setString(1, id);
            stmt.executeUpdate();


            stmt.close();
            c.close();


        } catch (Exception e) {
            System.out.println("e = " + e);
        }

    }
}
