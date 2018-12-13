package com.linjiao.cmslive.service.impl;

import com.linjiao.cmslive.mapper.online.CMSOnLineLiveMapper;
import com.linjiao.cmslive.model.CMSLive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Service
public class CrawlService {
    @Autowired
    CMSOnLineLiveMapper onlineCmsLiveMapper;
    @Autowired
    com.linjiao.cmslive.mapper.test.CMSLiveMapper testCmsLiveMapper;
    @Autowired
    CrawlXiaoCong crawlXiaoCong;
    @Autowired
    CrawlBiShiJie crawlBiShiJie;
    @Autowired
    Crawl36kr crawl36kr;

    private final static Logger logger = LoggerFactory.getLogger(CrawlService.class);

    @Autowired
    public CrawlService() {
    }

    @Scheduled(cron = "0/90 * * * * ?")
    public void autoCrawl() {
        logger.info("start定时任务......");
        //来源  1原创2币世界3小葱
        final Integer biggest2 = testCmsLiveMapper.selectBiggestSourceId("2");
        final Integer biggest3 = testCmsLiveMapper.selectBiggestSourceId("3");
        final Integer biggest4 = testCmsLiveMapper.selectBiggestSourceId("4");

        bishijie(biggest2 == null ? 0 : biggest2);
        xiaoCong(biggest3 == null ? 0 : biggest3);
        thirtySixkr(biggest4 == null ? 0 : biggest4);
        logger.info("end定时任务......");
    }

    /**
     * 数据持久化
     *
     * @param cmsLive
     * @return
     */
    private void insertCMSLive(CMSLive cmsLive) {
        onlineCmsLiveMapper.insert(cmsLive);
        testCmsLiveMapper.insert(cmsLive);
    }

    /**
     * 小葱爬取
     */
    private void xiaoCong(final int biggest) {
        AtomicLong count = new AtomicLong();
        try {
            Stream<CMSLive> cmsLiveStream = crawlXiaoCong.generateStream()
                    .filter(e -> e.getSourceId() > biggest);
            cmsLiveStream.forEach(e -> {
                count.getAndIncrement();
                insertCMSLive(e);
            });
        } catch (Exception e) {
            logger.error("小葱 同步失败！！！", e);
            return;
        }
        logger.info("小葱 更新数据，更新了" + count + "条");
    }

    /**
     * 币世界爬
     */
    private void bishijie(final int biggest) {
        AtomicLong count = new AtomicLong();
        try {
            Stream<CMSLive> cmsLiveStream = crawlBiShiJie.generateStream().
                    filter(e -> e.getSourceId() > biggest);
            cmsLiveStream
                    .forEach(e -> {
                        count.getAndIncrement();
                        insertCMSLive(e);
                    });
        } catch (Exception e) {
            logger.error("币世界 同步失败!!!", e);
        }
        logger.info("币世界 更新数据，更新了" + count + "条");
    }

    /**
     * 36kr爬取数据
     * @param biggest
     */
    private void thirtySixkr(final int biggest){
        AtomicLong count = new AtomicLong();
        try {
            Stream<CMSLive> cmsLiveStream = crawl36kr.generateStream().
                    filter(e -> e.getSourceId() > biggest);
            cmsLiveStream
                    .forEach(e -> {
                        count.getAndIncrement();
                        insertCMSLive(e);
                    });
        } catch (Exception e) {
            logger.error("36kr 同步失败!!!", e);
        }
        logger.info("36kr 更新数据，更新了" + count + "条");
    }

}
