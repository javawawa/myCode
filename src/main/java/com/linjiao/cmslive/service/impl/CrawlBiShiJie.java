package com.linjiao.cmslive.service.impl;

import com.linjiao.cmslive.model.CMSLive;
import com.linjiao.cmslive.service.Crawler;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.stream.Stream;

@Service
public class CrawlBiShiJie implements Crawler {
    private final String source = "2";

    @Override
    public Stream<CMSLive> generateStream() throws IOException {
        DateTimeFormatter dateTimeFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Document document = getDocument("http://www.bishijie.com/kuaixun");
        Elements elements = document.select("div.kuaixun_list");
        //只拿最新的
        Element life = elements.select(".live").get(0);
        String date = life.attr("class").substring(13);
        Stream<Element> ul = life.select("ul").stream();
        return ul.map(e -> {
            Integer sourceId = Integer.valueOf(e.attr("data-id"));
            String time = e.getElementsByTag("span").text().trim();
            LocalDateTime parse = LocalDateTime.parse(date + " " + time, dateTimeFormatter);
            //liveDate
            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime zdt = parse.atZone(zoneId);
            Date liveDate = Date.from(zdt.toInstant());
            //title  and  content
            Element contentElement = e.selectFirst("li.lh32");
            String style = contentElement.attr("style").trim();
            String important = style.isEmpty() ? "0" : "1";
            String title = contentElement.getElementsByTag("h2").text().trim();
            String content = contentElement.getElementsByTag("div").text().trim();
            return convert2CMSLive(sourceId, liveDate, title, content, source, important);
        });
    }
}
