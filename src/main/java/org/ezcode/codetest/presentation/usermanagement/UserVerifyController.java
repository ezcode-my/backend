package org.ezcode.codetest.presentation.usermanagement;

import org.ezcode.codetest.application.usermanagement.auth.dto.request.FindPasswordRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.request.SendEmailRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.response.FindPasswordResponse;
import org.ezcode.codetest.application.usermanagement.auth.dto.response.SendEmailResponse;
import org.ezcode.codetest.application.usermanagement.auth.dto.response.VerifyEmailCodeResponse;
import org.ezcode.codetest.application.usermanagement.auth.service.AuthService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "이메일, 비밀번호 관련 인증 API", description = "이메일 전송을 통한 인증 회원 만들기(verified), 비밀번호 찾기 기능")
public class UserVerifyController {
    private final AuthService authService;

    @Operation(summary = "이메일 인증 코드 전송", description = "현재 로그인된 회원의 이메일로 인증 코드를 전송합니다.")
    @PostMapping("/email/send")
    public ResponseEntity<SendEmailResponse> sendMailCode(
        @AuthenticationPrincipal AuthUser authUser,
        @RequestBody SendEmailRequest request
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.sendEmailCode(authUser.getId(), authUser.getEmail(), request.getRedirectUrl()));
    }

    //이메일에서 버튼 클릭하면 자동으로 연결
    @Operation(summary = "이메일 코드 입력 및 인증", description = "이메일로 받은 코드를 입력하여 이메일 인증된 회원으로 전환합니다")
    @GetMapping("/auth/verify")
    public ResponseEntity<VerifyEmailCodeResponse> verifyEmailCode(
        @RequestParam String email,
        @RequestParam String key
    ){
        return ResponseEntity.status(HttpStatus.OK).body(authService.verifyEmailCode(email, key));
    }

    //미완성 -> 메일 전송까지는 성공
    @Operation(summary = "비밀번호 찾기 요청", description = "비밀번호를 찾기 위해 이메일로 인증코드를 전송합니다.")
    @PostMapping("/auth/find-password")
    public ResponseEntity<FindPasswordResponse> findPassword(
        @RequestBody FindPasswordRequest request
    ){
        return ResponseEntity.status(HttpStatus.OK).body(authService.findPassword(request));
    }

    @Operation(summary = "비밀번호 찾기 요청", description = "비밀번호를 찾기 위해 이메일로 인증코드를 전송합니다.")
    @PostMapping("/auth/find-password-verify")
    public ResponseEntity<FindPasswordResponse> resetPassword(
        @RequestParam String email,
        @RequestParam String key
    ){
        return ResponseEntity.status(HttpStatus.OK).body(authService.resetPassword(email, key));
    }
}
