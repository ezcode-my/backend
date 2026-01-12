package org.ezcode.codetest.presentation.usermanagement;

import org.ezcode.codetest.application.usermanagement.auth.dto.request.FindPasswordRequest;
import org.ezcode.codetest.application.usermanagement.user.dto.request.ResetPasswordRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.request.SendEmailRequest;
import org.ezcode.codetest.application.usermanagement.auth.dto.response.FindPasswordResponse;
import org.ezcode.codetest.application.usermanagement.auth.dto.response.SendEmailResponse;
import org.ezcode.codetest.application.usermanagement.auth.service.AuthService;
import org.ezcode.codetest.application.usermanagement.user.dto.response.ChangeUserPasswordResponse;
import org.ezcode.codetest.application.usermanagement.user.dto.response.VerifyFindPasswordResponse;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.domain.user.exception.UserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "이메일, 비밀번호 관련 인증 API", description = "이메일 전송을 통한 인증 회원 만들기(verified), 비밀번호 찾기 기능")
public class UserVerifyController {
    private final AuthService authService;

    @Value("${app.redirect.verify.url}")
    String verifyUrl;

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
    public void verifyEmailCode(
        @RequestParam String email,
        @RequestParam String key,
        HttpServletResponse response
    ) throws IOException {
        try {
            authService.verifyEmailCode(email, key);

            // 성공 시 프론트엔드로 리디렉션
            String redirectUrl = UriComponentsBuilder
                .fromUriString(verifyUrl)
                .queryParam("status", "success")
                .build()
                .toUriString();

            response.sendRedirect(redirectUrl);
        } catch (UserException e) {
            // 실패 시 프론트엔드로 리디렉션 (에러 메시지 포함)
            String errorMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            String redirectUrl = UriComponentsBuilder
                .fromUriString(verifyUrl)
                .queryParam("status", "failure")
                .queryParam("message", errorMessage)
                .build()
                .toUriString();

            response.sendRedirect(redirectUrl);
        }
    }


    //비밀번호 찾기 요청
    @Operation(summary = "비밀번호 찾기 요청", description = "비밀번호를 찾기 위해 이메일로 인증코드를 전송합니다.")
    @PostMapping("/auth/find-password")
    public ResponseEntity<FindPasswordResponse> findPassword(
        @RequestBody FindPasswordRequest request
    ){
        return ResponseEntity.status(HttpStatus.OK).body(authService.findPassword(request));
    }

    @Operation(summary = "비밀번호 찾기 요청 이메일 코드 인증", description = "비밀번호 찾기로 받은 이메일에서 '인증하기' 버튼을 누르면 자동으로 호출되는 api")
    @GetMapping("/auth/find-password-verify")
    public ResponseEntity<VerifyFindPasswordResponse> verifyFindPassword(
        @RequestParam String email,
        @RequestParam String key
    ){
        return ResponseEntity.status(HttpStatus.OK).body(authService.verifyFindPassword(email, key));
    }

    @Operation(summary = "비밀번호 찾기 후 비밀번호 변경", description = "비밀번호 찾기를 통해 인증한 회원의 비밀번호 변경")
    @PostMapping("/auth/reset-password")
    public ResponseEntity<ChangeUserPasswordResponse> resetPassword(
        @Valid @RequestBody ResetPasswordRequest request
    ){
        return ResponseEntity.status(HttpStatus.OK).body(authService.resetPassword(request));
    }

}
