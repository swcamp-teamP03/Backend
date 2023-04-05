package com.example.swcamp_p03.user.service;

import com.example.swcamp_p03.common.dto.ResponseDto;
import com.example.swcamp_p03.common.exception.ErrorCode;
import com.example.swcamp_p03.common.exception.GlobalException;
import com.example.swcamp_p03.user.entity.MailConfirm;
import com.example.swcamp_p03.user.repository.MailConfirmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final MailConfirmRepository mailConfirmRepository;

    @Value("${spring.mail.username}")
    private String id;

    @Transactional
    public void sendSimpleMessage(String to) throws Exception {
        MimeMessage message = createMessage(to);
        try {
            javaMailSender.send(message); // 메일 발송
        } catch (MailException es) {
            log.warn("이메일 인증보내기 실패");
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
        String certificationNumber = createKey();
        Optional<MailConfirm> findEmail = mailConfirmRepository.findByEmail(to);
        if (findEmail.isPresent()) {
            findEmail.get().setCertificationNumber(certificationNumber);
            findEmail.get().setCreatedAt(LocalDateTime.now());
        } else if(!findEmail.isPresent()) {
            mailConfirmRepository.save(new MailConfirm(to, certificationNumber, LocalDateTime.now()));
        }

        log.info("보내는 대상 : " + to);
        log.info("인증 번호 : " + certificationNumber);
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상
        message.setSubject("회원가입 인증 코드: "); //메일 제목

        // 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
        String msg = "";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 회원가입 화면에서 입력해주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += certificationNumber;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html"); //내용, charset타입, subtype
        message.setFrom(new InternetAddress(id, "copyt_Admin")); //보내는 사람의 메일 주소, 보내는 사람 이름

        return message;
    }

    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) {
            key.append(rnd.nextInt(10));
        }
        return key.toString();
    }

    public ResponseDto<Boolean> mailConfirm(String email, String certificationNumber) {
        Optional<MailConfirm> findEmail = mailConfirmRepository.findByEmail(email);
        Boolean check = false;
        if (findEmail.isPresent()) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime createdAt = findEmail.get().getCreatedAt();
            long compareMinute = ChronoUnit.MINUTES.between(createdAt, now);
            if (compareMinute > 5) {
                log.warn("인증시간 5분이 초과되었습니다. 재인증을 해주세요");
                throw new GlobalException(ErrorCode.EMAIL_CERTIFICATION_EXPIRED);
            } else {
                if (findEmail.get().getCertificationNumber().equals(certificationNumber)) {
                    check = true;
                }
            }
        } else {
            log.warn("이메일 인증을 누르지 않았습니다. 인증을 눌러주세요");
            throw new GlobalException(ErrorCode.RETRY_EMAIL_CERTIFICATION);
        }
        return ResponseDto.success(check);
    }
}
