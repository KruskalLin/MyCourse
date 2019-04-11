package mycourse.utils;

import mycourse.dao.UserDao;
import mycourse.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/22
 * @Todo:
 */
@Component
@EnableAsync
public class SendMailUtils {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserDao userRepository;

    @Value("${spring.mail.username}")
    private String sender;

    @Async
    public void SEND_COURSE_EMAIL(User user, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(user.getUsername());
        message.setSubject(title);
        message.setText(content);
        mailSender.send(message);
    }

    @Async
    public void SEND_ACTIVATE_EMAIL(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(user.getUsername());
        message.setSubject("邮箱激活");
        int code = RandomUtils.generateRondom();
        message.setText("激活码 " + code);
        user.setActivateCode(code + "");
        userRepository.save(user);
        mailSender.send(message);
    }
}
