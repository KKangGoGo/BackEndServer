package com.kkanggogo.facealbum.scheduler;

import com.kkanggogo.facealbum.album.service.AlbumImageFacade;
import com.kkanggogo.facealbum.connection.dto.TeskIdListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DetectPoolingScheduler {

    @Value("${connectionbaseurl}")
    private String baseUrl;
    private final RestTemplate restTemplate;
    private final TeskIdListDto teskIdListDto;
    private final AlbumImageFacade albumImageFacade;


    @Scheduled(fixedDelay = 60000)
    public void pooling() {
        List<String> teskKeyList = teskIdListDto.getTeskKeyList();
        for(String teskKey:teskKeyList){
            String url=baseUrl+"/"+teskKey;
            ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            log.debug(exchange.getBody());

            if(!exchange.getBody().equals("PENDING")){
                JSONObject jsonObject= (JSONObject) JSONValue.parse(exchange.getBody());
                teskIdListDto.remove(teskKey);
                JSONObject result = (JSONObject)jsonObject.get("result");
                albumImageFacade.sharingImage(result);
            }
        }
    }
}
