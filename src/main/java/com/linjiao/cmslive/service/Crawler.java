package com.linjiao.cmslive.service;

import com.linjiao.cmslive.model.CMSLive;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Stream;

public interface Crawler {
    Stream<CMSLive> generateStream() throws IOException;

    default CMSLive convert2CMSLive(int sourceId, Date liveDate, String title, String content, String source, String important) {
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
    }
    /**
     * url获取dom文档
     * @param url
     * @return
     * @throws IOException
     */
    default Document getDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }}
