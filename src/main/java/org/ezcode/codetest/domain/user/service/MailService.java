package org.ezcode.codetest.domain.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
	private final JavaMailSender javaMailSender;
	@Value("${spring.mail.username}")
	private String senderEmail;
	private static int number;

	// 랜덤으로 숫자 생성
	public static void createNumber() {
		number = (int)(Math.random() * (90000)) + 100000; //(int) Math.random() * (최댓값-최소값+1) + 최소값
	}
	public MimeMessage CreateMail(String mail) {
		createNumber();
		MimeMessage message = javaMailSender.createMimeMessage();

		try {
			message.setFrom(senderEmail);
			message.setRecipients(MimeMessage.RecipientType.TO, mail);
			message.setSubject("이메일 인증");
			String body = "";
			body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
			body += "<h1>" + number + "</h1>";
			body += "<h3>" + "감사합니다." + "</h3>";
			message.setText(body,"UTF-8", "html");
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return message;
	}

	public int sendMail(String mail) {
		MimeMessage message = CreateMail(mail);
		javaMailSender.send(message);

		return number;
	}
}
