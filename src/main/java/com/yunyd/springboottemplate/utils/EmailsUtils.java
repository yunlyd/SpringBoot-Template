package com.yunyd.springboottemplate.utils;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.yunyd.springboottemplate.common.ErrorCode;
import com.yunyd.springboottemplate.exception.BusinessException;
import com.yunyd.springboottemplate.model.entity.EmailEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 邮件 工具类
 *
 * @lyd
 * @date 2024/7/3
 */
public class EmailsUtils {

    /**
     * 阿里邮箱 发送邮件
     * @param emailEntity
     * @throws AddressException
     * @throws Exception
     */
    public static void sendEMailByAliyun(EmailEntity emailEntity) throws AddressException, Exception {

        //设置SSL连接和邮件属性
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.qiye.aliyun.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.debug", "true");//启用调试
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.smtp.auth", "true");
        //建立邮件会话
        Session session = Session.getDefaultInstance(props, new Authenticator() {   //身份认证
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailEntity.getSendAddress(), emailEntity.getSendPassword());//发件人账号、密码
            }
        });
        //建立邮件对象
        MimeMessage message = new MimeMessage(session);
        //设置邮件的发件人、收件人、主题
        //附带发件人名字
        message.setFrom(new InternetAddress(emailEntity.getSendAddress(), emailEntity.getSenderName()));
        message.setRecipients(Message.RecipientType.TO, emailEntity.getInternetAddress());//收件人账号

        //邮件标题
        message.setSubject(emailEntity.getSubject());

        //内容
        Multipart multipart = new MimeMultipart();
        BodyPart contentPart = new MimeBodyPart();
        contentPart.setContent(emailEntity.getContent(), "text/html;charset=utf-8");//邮件内容
        multipart.addBodyPart(contentPart);

        //附件部分
        if (StringUtils.isNotBlank(emailEntity.getFilePath())) {
            BodyPart attachPart = new MimeBodyPart();
            FileDataSource fileDataSource = new FileDataSource(emailEntity.getFilePath());//附件地址  D:\模板v1.xlsx
            attachPart.setDataHandler(new DataHandler(fileDataSource));
            attachPart.setFileName(MimeUtility.encodeText(fileDataSource.getName()));
            multipart.addBodyPart(attachPart);
        }

        message.setContent(multipart);

        //抄送地址
        if (StringUtils.isNotBlank(emailEntity.getCcPeople())) {
            InternetAddress[] internetAddressCC = new InternetAddress().parse(emailEntity.getCcPeople());
            message.setRecipients(Message.RecipientType.CC, internetAddressCC);
        }

        //发送邮件
        Transport.send(message);
    }

    /**
     * 校验附件大小是否合法
     * @param multipartFile
     */
    public static void validFile(MultipartFile multipartFile){
        // 附件大小 不超过10M
        final long Ten_M = 10 * 1024 * 1024; // 10 MB
        if (multipartFile.getSize() > Ten_M){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR.getCode(),"附件大小不能超过10MB");
        }
    }

}
