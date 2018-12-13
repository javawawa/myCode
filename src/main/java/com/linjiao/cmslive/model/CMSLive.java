package com.linjiao.cmslive.model;


import lombok.*;

import java.util.Date;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CMSLive {
    private Integer id;

    private Integer sourceId;

    private String label;

    private String title;

    private String content;

    private String source;

    private String isImportant;

    private String isComment;

    private String commentContent;

    private String status;

    private Integer stat;

    private Date liveTime;

    private Date createAt;

    private Integer createBy;

    private Date updateAt;

    private Integer updateBy;

    private String contentHtml;
}