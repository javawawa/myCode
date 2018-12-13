package com.linjiao.cmslive.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linjiao.cmslive.model.CMSLive;
import com.linjiao.cmslive.service.Crawler;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

@Service
public class Crawl36kr implements Crawler {
    private final String source = "4";

    @Override
    public Stream<CMSLive> generateStream() throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Document document = getDocument("https://36kr.com/newsflashes");
        //获取id为app的div
        Element ele = document.body().getElementById("app");
        //div的下一个元素
        String scr = ele.nextElementSibling().html();
        //截取字符串
        int first = scr.indexOf("hotPosts|hotPost");
        String substring = scr.substring(10, first-2)+"}".trim();
        //转为jsonobject
        JSONObject resultObj = JSON.parseObject(substring);
        JSONArray list = resultObj.getJSONArray("newsflashList|newsflash");
        return list.stream().map(json->{
            JSONObject obj= (JSONObject) json;
            Integer sourceId = obj.getInteger("id");
            String dateString = obj.getString("published_at");
            Date liveDate=null;
            try {
                liveDate = simpleDateFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String title = obj.getString("title");
            String content = obj.getString("description");
            return convert2CMSLive(sourceId, liveDate, title, content, source, "0");
        });
    }

    public static void main(String[] args) throws IOException {
        Crawl36kr crawl36kr = new Crawl36kr();
        Stream<CMSLive> cmsLiveStream = crawl36kr.generateStream();
        cmsLiveStream.forEach(System.out::println);
        System.out.println(cmsLiveStream);
    }
}
