package com.example.zerobaseassignment.service;


import com.example.zerobaseassignment.dto.History;
import com.example.zerobaseassignment.dto.SeoulJson;
import com.example.zerobaseassignment.repository.SeoulRepository;
import lombok.*;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class SeoulService {

    private final SeoulRepository seoulRepository = new SeoulRepository();


    public int saveSeoulWifiApi() throws IOException, ParseException {
        List<SeoulJson> seoulWifiApi = seoulRepository.getSeoulWifiApi();

        return seoulRepository.saveSeoulWifiApi(seoulWifiApi);

    }


    public List<SeoulJson> findWifiByLongLat(String longdi, String lat) {
        List<SeoulJson> nearestWifi = seoulRepository.findByLongLat(longdi, lat);

        return nearestWifi;
    }

    public void saveSeoulWifiHistory(List<SeoulJson> wifiByLongLat) {
        if (wifiByLongLat.size() < 1) {
            return;
        }
        seoulRepository.saveHistory(wifiByLongLat);
    }

    public List<History> getHistory() {
        return seoulRepository.getHistory();
    }

    public void deleteHistoryById(String id) {
        seoulRepository.deleteHistoryById(id);
    }
}
