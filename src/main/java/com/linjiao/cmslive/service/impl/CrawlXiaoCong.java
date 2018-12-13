package com.linjiao.cmslive.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.linjiao.cmslive.model.CMSLive;
import com.linjiao.cmslive.service.Crawler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Service
public class CrawlXiaoCong implements Crawler {
    @Resource
    private RestTemplate restTemplate;
    private final String source="3";
    @Override
    public Stream<CMSLive> generateStream() {
        final String url = "http://api-prod.wallstreetcn.com/apiv1/content/lives?" +
                "channel=xiaocong-channel&client=pc&cursor=" +
                "&limit=10" +
                "&first_page=false" +
                "&accept_symbols=coin";
        JSONObject jsonObject = restTemplate.getForObject(
                url, JSONObject.class);
        LinkedHashMap data = (LinkedHashMap) jsonObject.get("data");
        List<LinkedHashMap> items = (List) data.get("items");
        return items.stream().map(json -> {
            String title = (String) json.get("title");
            String content = (String) json.get("content_text");
            Integer score = (Integer) json.get("score");
            Integer sourceId = (Integer) json.get("id");
            long display_time = (long) (int) json.get("display_time");
            Date liveDate = new Date(display_time * 1000);
            String important = score > 1 ? "1" : "0";
            return convert2CMSLive(sourceId, liveDate, title, content, source, important);
        });
    }
}
