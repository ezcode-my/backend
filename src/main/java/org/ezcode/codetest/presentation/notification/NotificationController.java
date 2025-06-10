package org.ezcode.codetest.presentation.notification;

import org.ezcode.codetest.application.notification.service.NotificationUseCase;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.presentation.advice.ResponseMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationUseCase useCase;

	@GetMapping
	@ResponseMessage("알림 목록 조회 요청 완료")
	public ResponseEntity<Void> getNotificationList(
		@AuthenticationPrincipal AuthUser authUser,
		@PageableDefault Pageable pageable
	) {

		useCase.getNotificationList(authUser.getEmail(), pageable);
		return ResponseEntity.ok().build();
	}
	
	@PatchMapping("/{notificationId}")
	@ResponseMessage("알림 읽음 처리 완료")
	public ResponseEntity<Void> modifyNotificationMarksRead(
		@PathVariable String notificationId,
		@AuthenticationPrincipal AuthUser authUser) {

		useCase.modifyNotificationMarksRead(authUser.getEmail(), notificationId);
		return ResponseEntity.ok().build();
	}
}
