package org.ezcode.codetest.domain.user.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.ezcode.codetest.application.usermanagement.user.dto.request.UserGithubRepoSelectRequest;
import org.ezcode.codetest.application.usermanagement.user.dto.response.UserGithubRepoResponse;
import org.ezcode.codetest.application.usermanagement.user.dto.response.UserGitubAutoPushResponse;
import org.ezcode.codetest.common.security.util.AESUtil;
import org.ezcode.codetest.domain.user.exception.UserException;
import org.ezcode.codetest.domain.user.exception.code.UserExceptionCode;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.entity.UserGithubInfo;
import org.ezcode.codetest.domain.user.repository.UserGithubInfoRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserGithubService {
    private final UserGithubInfoRepository userGithubInfoRepository;
    private final WebClient githubWebClient;
    private final AESUtil aesUtil;


    public void updateUserGithubAccessToken(UserGithubInfo userGithubInfo) {
        userGithubInfoRepository.updateGithubAccessToken(userGithubInfo);
    }

    public void createUserGithubInfo(UserGithubInfo userGithubInfo) {
        userGithubInfoRepository.createUserGithubInfo(userGithubInfo);
    }

    public UserGithubInfo getUserGithubInfoById(Long id) {
        return userGithubInfoRepository.getUserGithubInfo(id);
    }


    @Transactional
    public List<UserGithubRepoResponse> getGithubRepos(AuthUser authUser) throws Exception {
        // 1. 사용자 accessToken 가져오기
        UserGithubInfo userGithub = userGithubInfoRepository.getUserGithubInfo(authUser.getId());

        if (userGithub == null) {
            throw new UserException(UserExceptionCode.NO_GITHUB_INFO);
        }

        String accessToken = aesUtil.decrypt(userGithub.getGithubAccessToken());

        // 2. WebClient로 GitHub API 호출
        return githubWebClient.get()
            .uri("/user/repos")
            .headers(headers -> {
                headers.setBearerAuth(accessToken);
                headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            })
            .retrieve()
            // .onStatus(
            //     status -> status == HttpStatus.BAD_REQUEST,  // 상태 코드 조건
            //     response -> Mono.error(new UserException(UserExceptionCode.NO_GITHUB_INFO))
            // )
            .onStatus(
                status -> status.is4xxClientError() || status.is5xxServerError(),
                response -> response.bodyToMono(String.class)
                    .flatMap(errorBody -> Mono.error(new Exception( //UserException 대신 github 문구 받아오게 수정
                        "GitHub API 오류: " + response.statusCode() + " - " + errorBody
                    )))
            )
            .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
            .block()
            .stream()
            .map(repo -> new UserGithubRepoResponse(
                repo.get("name").toString(),
                repo.get("default_branch").toString()
            ))
            .collect(Collectors.toList());
    }

    @Transactional
    public UserGithubRepoResponse selectGithubRepo(AuthUser authUser, UserGithubRepoSelectRequest request) throws
        Exception {
        UserGithubInfo userGithub = userGithubInfoRepository.getUserGithubInfo(authUser.getId());

        if (userGithub == null) {
            throw new UserException(UserExceptionCode.NO_GITHUB_INFO);
        }

        List<UserGithubRepoResponse> repos = getGithubRepos(authUser);

        UserGithubRepoResponse selectedRepo = repos.stream()
            .filter(repo -> repo.getRepoName().equals(request.repositoryName()))
            .findFirst()
            .orElseThrow(() -> new UserException(UserExceptionCode.NO_GITHUB_REPO));

        userGithub.setGithubRepo(request.repositoryName(), selectedRepo.getDefaultBranch());

        userGithubInfoRepository.updateGithubInfo(userGithub);

        User user = userGithub.getUser();
        user.setGitPushStatus(true); //레포를 선택하면 자동으로 push 설정이 true

        return selectedRepo;
    }

    @Transactional
    public UserGitubAutoPushResponse changeAutoPushSetting(AuthUser authUser) {
        UserGithubInfo userGithubInfo = userGithubInfoRepository.getUserGithubInfo(authUser.getId());
        if (userGithubInfo == null) { //유저의 깃허브 정보가 없으면 에러 반환
            throw new UserException(UserExceptionCode.NO_GITHUB_INFO);
        }
        User user = userGithubInfo.getUser();
        boolean userGitPushStatus = user.getGitPushStatus();
        user.setGitPushStatus(!userGitPushStatus);
        log.info("기존 status: {} || 변경 status : {}", userGitPushStatus, user.getGitPushStatus());

        return new UserGitubAutoPushResponse("변경되었습니다", user.getGitPushStatus(), userGithubInfo.getRepo(), userGithubInfo.getBranch());
    }

    public UserGitubAutoPushResponse getAutoPushStatus(AuthUser authUser) {
        UserGithubInfo userGithubInfo = userGithubInfoRepository.getUserGithubInfo(authUser.getId());
        if (userGithubInfo == null) { //유저의 깃허브 정보가 없으면 에러 반환
            throw new UserException(UserExceptionCode.NO_GITHUB_INFO);
        }
        User user = userGithubInfo.getUser();
        return new UserGitubAutoPushResponse("현재 githubAutoPush 설정", user.getGitPushStatus(),userGithubInfo.getRepo(), userGithubInfo.getBranch());
    }
}
