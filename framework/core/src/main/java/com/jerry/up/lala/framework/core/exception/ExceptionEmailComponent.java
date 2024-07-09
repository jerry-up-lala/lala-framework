package com.jerry.up.lala.framework.core.exception;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.jerry.up.lala.framework.core.common.CommonProperties;
import com.sun.mail.util.MailSSLSocketFactory;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

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
    private CommonProperties commonProperties;

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @Async("exception-email-executor")
    public void send(List<String> receivers, ExceptionEmailBO emailExceptionBO) {
        CommonProperties.Mail mail = commonProperties.getMail();
        if (mail == null) {
            log.warn("邮箱账户未配置,请检查common.mail选项");
            return;
        }
        try {
            MailAccount mailAccount = new MailAccount();
            mailAccount.setHost(mail.getHost());
            mailAccount.setPort(mail.getPort());
            mailAccount.setFrom(mail.getFrom());
            mailAccount.setUser(mail.getUser());
            mailAccount.setPass(mail.getPass());
            mailAccount.setAuth(true);
            mailAccount.setSslEnable(true);
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            mailAccount.setCustomProperty("mail.smtp.ssl.socketFactory", sf);

            Template tpl = freeMarkerConfig.getConfiguration().getTemplate("exception.ftl");
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(tpl, emailExceptionBO);

            MailUtil.send(mailAccount, receivers, mail.getSubjectPrefix() + "· 异常", content, true);
        } catch (Exception e) {
            log.error("发送邮件失败", e);
        }
    }
}
