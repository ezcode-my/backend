package org.ezcode.codetest.domain.user.service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
	private final JavaMailSender javaMailSender;
	private final RedisTemplate<String, String> redisTemplate;

	@Value("${spring.mail.username}")
	private String senderEmail;

	private static final long EXPIRATION_MINUTES = 3;

	public void sendMail(Long userId, String mail) {
		MimeMessage message = CreateMail(userId, mail);
		javaMailSender.send(message);
	}

	//메일 보내기
	public MimeMessage CreateMail(Long userId, String mail) {
		String code = createNumber(userId);
		MimeMessage message = javaMailSender.createMimeMessage();

		try {
			message.setFrom(senderEmail);
			message.setRecipients(MimeMessage.RecipientType.TO, mail);
			message.setSubject("EZcode 이메일 인증");
			String body = "";
			body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
			body += "<h1>" + code + "</h1>";
			body += "<h3>" + "감사합니다." + "</h3>";
			message.setText(body,"UTF-8", "html");
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return message;
	}


	// 랜덤으로 인증 번호 생성
	public String createNumber(Long userId) {
		String verificationCode = generateRandomCode();
		String redisKey = "VERIFY_CODE:" + userId;

		redisTemplate.opsForValue().set(
			redisKey,
			verificationCode,
			EXPIRATION_MINUTES,
			TimeUnit.MINUTES
		);
		return verificationCode;
	}

	// 6자리 랜덤 인증번호 생성
	private String generateRandomCode() {
		SecureRandom secureRandom = new SecureRandom();
		int randomNum = secureRandom.nextInt(900000) + 100000;
		return String.valueOf(randomNum);
	}

	/*
	입력한 번호 인증
	 */
	public boolean verifyCode(Long userId, String inputCode) {
		String key = "VERIFY_CODE:" + userId;
		String storedCode = redisTemplate.opsForValue().get(key);

		if (storedCode == null) {
			log.warn("인증코드 발급이 안된 유저임 : {}", userId);
			return false;
		}

		boolean isMatch = inputCode != null && inputCode.trim().equals(storedCode);

		//번호 검증되면 삭제하기
		if (isMatch) {
			redisTemplate.delete(key);
		}
        return isMatch;
	}
}
