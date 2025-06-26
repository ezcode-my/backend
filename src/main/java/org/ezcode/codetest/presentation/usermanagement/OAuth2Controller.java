package org.ezcode.codetest.presentation.usermanagement;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth2")
@Tag(name = "OAuth2", description = "OAuth2 인증 관련 API")
public class OAuth2Controller {

    @Operation(summary = "GitHub OAuth2 로그인 시작",
        description = """
               프론트엔드에서 GitHub 로그인 버튼 클릭 시 이 API를 먼저 호출
               redirect_uri는 로그인 완료 후 accessToken과 refreshToken을 전달받을 프론트의 콜백 URL

               예시: GET /api/oauth2/authorize/github?redirect_uri=https://ezcode.my/oauth/callback
               """)
    @Parameters({
        @Parameter(name = "redirect_uri", description = "프론트 콜백 URI", required = true, example = "https://ezcode.my/oauth/callback (이 uri는 예시이니 편하신걸로 바꾸심 됩니당)")
    })
    @GetMapping("/authorize/{provider}")
    public void redirectToProvider(
        @PathVariable String provider,
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestParam(required = false) String redirect_uri
    ) throws IOException {
        if (redirect_uri != null) {
            request.getSession().setAttribute("redirect_uri", redirect_uri);
        }

        response.sendRedirect("/oauth2/authorization/" + provider);
    }
}
