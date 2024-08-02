package com.jerry.up.lala.framework.boot.exception;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.jerry.up.lala.framework.boot.properties.MailProperties;
import com.sun.mail.util.MailSSLSocketFactory;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.security.GeneralSecurityException;
import java.util.List;

/**
 * <p>Description: 异常信息
 *
 * @author FMJ
 * @date 2023/8/15 17:59
 * @since v1.0.0
 */
@Slf4j
@Component
public class ExceptionEmailComponent {

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @Async("exception-email-executor")
    public void send(List<String> receivers, ExceptionEmailBO emailExceptionBO) {
        if (mailProperties == null) {
            log.warn("邮箱账户未配置,请检查common.mail选项");
            return;
        }
        try {
            MailAccount mailAccount = loadMailAccount();
            Template tpl = freeMarkerConfig.getConfiguration().getTemplate("exception.ftl");
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(tpl, emailExceptionBO);

            MailUtil.send(mailAccount, receivers, mailProperties.getSubjectPrefix() + "· 异常", content, true);
        } catch (Exception e) {
            log.error("发送邮件失败", e);
        }
    }

    private MailAccount loadMailAccount() throws GeneralSecurityException {
        MailAccount mailAccount = new MailAccount();
        mailAccount.setHost(mailProperties.getHost());
        mailAccount.setPort(mailProperties.getPort());
        mailAccount.setFrom(mailProperties.getFrom());
        mailAccount.setUser(mailProperties.getUser());
        mailAccount.setPass(mailProperties.getPass());
        mailAccount.setAuth(true);
        mailAccount.setSslEnable(true);
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        mailAccount.setCustomProperty("mail.smtp.ssl.socketFactory", sf);
        return mailAccount;
    }
}
