package com.yunyd.springboottemplate.model.entity;

import lombok.Data;

/**
 * @lyd
 * @date 2024/7/3
 */
@Data
public class EmailEntity {
    /**
     * 发件人地址
     */
    private String sendAddress;
    /**
     * 发件人密码
     */
    private String sendPassword;
    /**
     * 标题
     */
    private String subject;
    /**
     * 内容
     */
    private String content;
    /**
     * 收件人地址
     */
    private String internetAddress;
    /**
     * 附件地址
     */
    private String filePath;
    /**
     * 抄送人
     */
    private String ccPeople;
    /**
     * 发件人名字
     */
    private String senderName;
}
