package org.ezcode.codetest.presentation.usermanagement;

import java.util.HashMap;

import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.domain.user.service.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MailCheckController {
	private final MailService mailService;
	private int number; // 이메일 인증 숫자를 저장하는 변수

	// 인증 이메일 전송
	@PostMapping("/mailSend")
	public HashMap<String, Object> mailSend(
		@AuthenticationPrincipal AuthUser authUser) {
		HashMap<String, Object> map = new HashMap<>();

		try {
			number = mailService.sendMail(authUser.getEmail());

			map.put("success", Boolean.TRUE);
		} catch (Exception e) {
			map.put("success", Boolean.FALSE);
			map.put("error", e.getMessage());
		}

		return map;
	}

	// 인증번호 일치여부 확인
	@GetMapping("/mailCheck")
	public ResponseEntity<?> mailCheck(@RequestParam String userNumber) {

		boolean isMatch = userNumber.equals(String.valueOf(number));

		return ResponseEntity.ok(isMatch);
	}
}
