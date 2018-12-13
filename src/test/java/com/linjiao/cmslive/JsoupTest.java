package com.linjiao.cmslive;

import com.alibaba.fastjson.JSONObject;
import com.linjiao.cmslive.mapper.test.CMSLiveMapper;
import com.linjiao.cmslive.model.CMSLive;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JsoupTest {
    @Autowired
    CMSLiveMapper cmsLiveMapper;
    @Resource
    private RestTemplate restTemplate;

    int insert(CMSLive cmsLive){
        return cmsLiveMapper.insert(cmsLive);
    }
    Document getDocument(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        return document;
    }

    @Test
    public void testGetBiggest() {
        Integer i = cmsLiveMapper.selectBiggestSourceId("2");
        System.out.println(i);
    }

    @Test
    public void XiaoCong() {
        final String source="3";
        String url = "http://api-prod.wallstreetcn.com/apiv1/content/lives?" +
                "channel=xiaocong-channel&client=pc&cursor=" +
                "&limit=10" +
                "&first_page=false" +
                "&accept_symbols=coin";
        JSONObject jsonObject = restTemplate.getForObject(
                url,JSONObject.class);
        LinkedHashMap data = (LinkedHashMap) jsonObject.get("data");
        List<LinkedHashMap> items = (List) data.get("items");
        items.stream().map(json->{
            String title = (String) json.get("title");
            String content = (String) json.get("content_text");
            Integer score = (Integer) json.get("score");
            Integer sourceId = (Integer) json.get("id");
            long display_time = (long)(int) json.get("display_time");
            Date liveDate = new Date(display_time*1000);
            return CMSLive.builder()
                    .sourceId(sourceId)
                    .liveTime(liveDate)
                    .title(title)
                    .content(content)
                    .source(source)
                    .isImportant(score>1?"1":"0")
                    .isComment("0")
                    .status("a")
                    .stat(0)
                    .createBy(0)
                    .build();
        }).forEach(this::insert);
    }
    @Test
    public void BiShiJie() throws IOException {
        //来源  1原创2币世界3小葱
        final String source="2";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Document document = getDocument("http://www.bishijie.com/kuaixun");
        Elements elements = document.select("div.kuaixun_list");
        Elements lives = elements.select(".live");
        lives.forEach(life->{
            String date = life.attr("class").substring(13);
            Stream<Element> ul = life.select("ul").stream();
            ul.map(e -> {
                Integer sourceId = Integer.valueOf(e.attr("data-id"));
                String time = e.getElementsByTag("span").text().trim();
                LocalDateTime parse = LocalDateTime.parse(date + " " + time,dateTimeFormatter);
                //liveDate
                ZoneId zoneId = ZoneId.systemDefault();
                ZonedDateTime zdt = parse.atZone(zoneId);
                Date liveDate = Date.from(zdt.toInstant());

                //title  and  content
                Element contentElement = e.selectFirst("li.lh32");
                String style = contentElement.attr("style").trim();
                String important=style.isEmpty()?"0":"1";
                String title = contentElement.getElementsByTag("h2").text().trim();
                String content=contentElement.getElementsByTag("div").text().trim();
                //
                return CMSLive.builder()
                        .sourceId(sourceId)
                        .liveTime(liveDate)
                        .title(title)
                        .content(content)
                        .source(source)
                        .isImportant(important)
                        .isComment("0")
                        .status("a")
                        .stat(0)
                        .createBy(0)
                        .build();
            }).forEach(this::insert);
        });
    }
}
