package org.ezcode.codetest.application.usermanagement.user.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.ezcode.codetest.application.usermanagement.user.dto.response.GrantAdminRoleResponse;
import org.ezcode.codetest.application.usermanagement.user.dto.response.UserDailySolvedHistoryResponse;
import org.ezcode.codetest.application.usermanagement.user.dto.response.UserProfileImageResponse;
import org.ezcode.codetest.application.usermanagement.user.dto.response.UserReviewTokenResponse;
import org.ezcode.codetest.application.usermanagement.user.model.UsersByWeek;
import org.ezcode.codetest.application.submission.dto.response.language.LanguageResponse;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.ezcode.codetest.domain.language.service.LanguageDomainService;
import org.ezcode.codetest.domain.submission.dto.DailyCorrectCount;
import org.ezcode.codetest.domain.submission.dto.WeeklySolveCount;
import org.ezcode.codetest.application.usermanagement.user.dto.request.ChangeUserPasswordRequest;
import org.ezcode.codetest.application.usermanagement.user.dto.request.ModifyUserInfoRequest;
import org.ezcode.codetest.application.usermanagement.user.dto.response.ChangeUserPasswordResponse;
import org.ezcode.codetest.application.usermanagement.user.dto.response.UserInfoResponse;
import org.ezcode.codetest.application.usermanagement.user.dto.response.WithdrawUserResponse;
import org.ezcode.codetest.domain.submission.service.SubmissionDomainService;
import org.ezcode.codetest.domain.user.exception.AuthException;
import org.ezcode.codetest.domain.user.exception.UserException;
import org.ezcode.codetest.domain.user.exception.code.AuthExceptionCode;
import org.ezcode.codetest.domain.user.exception.code.UserExceptionCode;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.entity.UserAuthType;
import org.ezcode.codetest.domain.user.model.enums.AuthType;
import org.ezcode.codetest.domain.user.model.enums.UserRole;
import org.ezcode.codetest.domain.user.service.MailService;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.ezcode.codetest.infrastructure.s3.S3Directory;
import org.ezcode.codetest.infrastructure.s3.S3Uploader;
import org.ezcode.codetest.infrastructure.s3.exception.S3Exception;
import org.ezcode.codetest.infrastructure.s3.exception.code.S3ExceptionCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserDomainService userDomainService;
	private final LanguageDomainService languageDomainService;
	private final SubmissionDomainService submissionDomainService;
	private final RedisTemplate<String, String> redisTemplate;
	private final S3Uploader s3Uploader;

	@Transactional
	public UserInfoResponse getUserInfo(AuthUser authUser) {
		log.info("authUserEmail: {}, authUserID : {}", authUser.getEmail(), authUser.getId());
		User user = userDomainService.getUserById(authUser.getId());
		int userSubmissionCount = submissionDomainService.findSubmissionCountByUserId(user.getId());
		List<UserAuthType> userAuthTypes = userDomainService.getUserAuthTypesByUser(user);
		List<AuthType> authTypes = userAuthTypes.stream()
			.map(UserAuthType::getAuthType).toList();
		if (user.getLanguage() == null) {
			Language userLanguage = languageDomainService.getLanguage(1L);
			user.setLanguage(userLanguage);
		}

        if (user.getLanguage().isEmpty || user.getLanguage() == null){
            user.set
        }

		return UserInfoResponse.builder()
			.username(user.getUsername())
			.age(user.getAge())
			.email(user.getEmail())
			.profileImageUrl(user.getProfileImageUrl())
			.blogUrl(user.getBlogUrl())
			.introduction(user.getIntroduction())
			.nickname(user.getNickname())
			.githubUrl(user.getGithubUrl())
			.userRole(user.getRole())
			.tier(user.getTier())
			.verified(user.isVerified())
			.totalSolvedCount(userSubmissionCount)
			.language(LanguageResponse.from(user.getLanguage()))
			.userAuthTypes(authTypes)
			.build();
	}

	@Transactional
	public UserInfoResponse modifyUserInfo(AuthUser authUser, ModifyUserInfoRequest request, MultipartFile image) {
		User user = userDomainService.getUserById(authUser.getId());
		Language findLangauge = languageDomainService.getLanguage(request.languageId());
		if (request.nickname() != null && !request.nickname().equals(user.getNickname())) {
			if (userDomainService.existsByNickname(request.nickname())) {
				log.info("중복 닉네임");
				throw new UserException(UserExceptionCode.ALREADY_EXIST_NICKNAME);
			}
		}

		user.modifyUserInfo(
			request.nickname(),
			request.githubUrl(),
			request.blogUrl(),
			request.introduction(),
			request.age(),
			findLangauge);

		if (image != null && !image.isEmpty()) {
			String profileImageUrl = uploadProfileImage(image);
			String oldImageUrl = user.getProfileImageUrl();

			user.modifyProfileImage(profileImageUrl);
			if (oldImageUrl!=null) {
				s3Uploader.delete(user.getProfileImageUrl(), "profile");
			}

		}

		return UserInfoResponse.builder()
			.username(user.getUsername())
			.age(user.getAge())
			.email(user.getEmail())
			.profileImageUrl(user.getProfileImageUrl())
			.blogUrl(user.getBlogUrl())
			.introduction(user.getIntroduction())
			.nickname(user.getNickname())
			.githubUrl(user.getGithubUrl())
			.userRole(user.getRole())
			.tier(user.getTier())
			.language(LanguageResponse.from(user.getLanguage()))
			.build();
	}

	@Transactional
	public ChangeUserPasswordResponse modifyUserPassword(AuthUser authUser,
		ChangeUserPasswordRequest changeUserPasswordRequest) {
		User user = userDomainService.getUserById(authUser.getId());

		//소셜로그인 회원은 변경 불가
		if (!userDomainService.getUserAuthTypes(user).contains(AuthType.EMAIL)) {
			throw new AuthException(AuthExceptionCode.NOT_EMAIL_USER);
		}

		userDomainService.userPasswordCheck(authUser.getEmail(), changeUserPasswordRequest.oldPassword());

		//기존 비밀번호와 새로운 비밀번호가 같은지 확인 -> 기존과 같으면 변경 불가
		userDomainService.passwordComparison(changeUserPasswordRequest.newPassword(), user.getPassword());

		String newPassword = userDomainService.encodePassword(changeUserPasswordRequest.newPassword());

		user.modifyPassword(newPassword);

		return new ChangeUserPasswordResponse("비밀번호를 성공적으로 변경했습니다");
	}

	@Transactional
	public WithdrawUserResponse withdrawUser(AuthUser authUser) {
		User user = userDomainService.getUserById(authUser.getId());

		userDomainService.isDeletedUser(user);

		user.setDeleted();

		redisTemplate.delete("RefreshToken:" + authUser.getId());

		return new WithdrawUserResponse("탈퇴가 완료되었습니다");
	}

	@Transactional
	public void resetAllUsersTokensWeekly(LocalDateTime startDateTime, LocalDateTime endDateTime) {

		List<WeeklySolveCount> counts = submissionDomainService.getWeeklySolveCounts(startDateTime, endDateTime);
		long weekLength = ChronoUnit.DAYS.between(startDateTime, endDateTime);

		userDomainService.resetReviewTokensForUsers(UsersByWeek.from(counts, weekLength));
	}

	private String uploadProfileImage(MultipartFile image) {
		try {
			return s3Uploader.upload(image, S3Directory.PROFILE.getDir());
		} catch (Exception e) {
			log.error("프로필 이미지 업로드 실패 - image {}", image, e);
			throw new S3Exception(S3ExceptionCode.S3_UPLOAD_FAILED);
		}
	}

	@Transactional
	public UserProfileImageResponse deleteUserProfileImage(AuthUser authUser) {
		User user = userDomainService.getUserById(authUser.getId());

		String oldImageUrl = user.getProfileImageUrl();

		// S3에서 기존 이미지 파일 삭제
		if (oldImageUrl != null) {
			try {
				s3Uploader.delete(oldImageUrl, "profile");
				user.modifyProfileImage(null);
			} catch (Exception e) {
				log.warn("프로필 이미지 삭제 실패 - url: {}", oldImageUrl, e);
			}
		}

		return new UserProfileImageResponse(null);
	}

	@Transactional(readOnly = true)
	public UserReviewTokenResponse getReviewToken(AuthUser authUser) {
		User user = userDomainService.getUserById(authUser.getId());

		return new UserReviewTokenResponse(user.getReviewToken());
	}

	@Transactional(readOnly = true)
	public UserDailySolvedHistoryResponse getUserDailySolvedHistory(AuthUser authUser) {
		Long userId = authUser.getId();
		List<DailyCorrectCount> solvedHistory = submissionDomainService.getSolvedHistoryByDate(authUser.getId());
		return new UserDailySolvedHistoryResponse(userId, solvedHistory);
	}
}
