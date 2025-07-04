package org.ezcode.codetest.presentation.notification;

import org.ezcode.codetest.application.notification.service.NotificationUseCase;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.presentation.advice.ResponseMessage;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "알림 조회 및 읽음 처리 API")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationUseCase useCase;

	@Operation(
		summary = "알림 목록 조회",
		description = """
			현재 사용자(authUser)의 알림을 페이징하여 조회 요청을 보냅니다.
			해당 API는 요청만 날릴 뿐 실제 알림 데이터는 웹소켓을 통해 전달 받습니다.
			웹소켓 구독 경로는 다음과 같습니다.
			
			- 신규 알림 : /user/queue/notification
			- 알림 목록 : /user/queue/notifications
			
			자세한 내용은 다음 [링크](https://www.notion.so/teamsparta/1-5-2002dc3ef51481e7afbec86e90d0010e?p=2232dc3ef51480638fd9eecc3b90a0fc&pm=s) 참고
			""",
		parameters = {
			@Parameter(name = "pageable", description = "페이징 정보 (page, size, sort)")
		}
	)
	@ApiResponse(responseCode = "200", description = "알림 목록 조회 요청 완료. 데이터는 웹소켓으로 전달됨")
	@GetMapping
	@ResponseMessage("알림 목록 조회 요청 완료")
	public ResponseEntity<Void> getNotificationList(
		@AuthenticationPrincipal AuthUser authUser,
		@ParameterObject @PageableDefault Pageable pageable
	) {

		useCase.getNotificationList(authUser.getEmail(), pageable);
		return ResponseEntity.ok().build();
	}

	@Operation(
		summary = "알림 읽음 처리",
		description = "해당 notificationId의 알림을 읽음 처리(mark as read) 합니다.",
		parameters = {
			@Parameter(name = "notificationId", description = "읽음 처리할 알림 ID", required = true)
		}
	)
	@ApiResponse(responseCode = "200", description = "알림 읽음 처리 완료")
	@PatchMapping("/{notificationId}")
	@ResponseMessage("알림 읽음 처리 완료")
	public ResponseEntity<Void> modifyNotificationMarksRead(
		@PathVariable String notificationId,
		@AuthenticationPrincipal AuthUser authUser
	) {

		useCase.modifyNotificationMarksRead(authUser.getEmail(), notificationId);
		return ResponseEntity.ok().build();
	}
}
