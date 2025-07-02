package org.ezcode.codetest.domain.user;

import org.ezcode.codetest.common.security.util.PasswordEncoder;
import org.ezcode.codetest.domain.user.exception.AuthException;
import org.ezcode.codetest.domain.user.exception.UserException;
import org.ezcode.codetest.domain.user.exception.code.AuthExceptionCode;
import org.ezcode.codetest.domain.user.exception.code.UserExceptionCode;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.entity.UserAuthType;
import org.ezcode.codetest.domain.user.model.enums.AuthType;
import org.ezcode.codetest.domain.user.model.enums.Tier;
import org.ezcode.codetest.domain.user.model.enums.UserRole;
import org.ezcode.codetest.domain.user.repository.UserAuthTypeRepository;
import org.ezcode.codetest.domain.user.repository.UserRepository;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDomainServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserAuthTypeRepository userAuthTypeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @InjectMocks
    private UserDomainService userDomainService;

    // 테스트 유저 정보 설정
    private final User testUser = new User(
        "test@example.com",
        "hashedPassword",
        "testUser",
        "TestNick",
        30,
        Tier.NEWBIE,
        UserRole.USER,
        false,  // isDeleted
        true,   // verified
        "https://github.com/test",
        false   // gitPushStatus
    ) {
        public Long getId() { return 1L; }
        public int getReviewToken() { return 5; }
        public int getZeroReviewToken() { return 0; }
    };
    private final UserAuthType testAuthType = new UserAuthType(testUser, AuthType.EMAIL);

    // 1. 이메일 존재 여부 테스트
    @Test
    void checkEmailUnique_shouldPassWhenEmailAvailable() {
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> userDomainService.checkEmailUnique("new@example.com"));
    }

    @Test
    void checkEmailUnique_shouldThrowWhenEmailExistsWithAuthType() {
        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(testUser));
        when(userAuthTypeRepository.getUserAuthType(testUser)).thenReturn(List.of(AuthType.EMAIL));

        AuthException exception = assertThrows(AuthException.class,
            () -> userDomainService.checkEmailUnique("existing@example.com"));
        assertEquals(AuthExceptionCode.ALREADY_EXIST_USER, exception.getResponseCode());
    }

    // 2. 유저 생성 테스트
    @Test
    void createUser_shouldCallRepository() {
        userDomainService.createUser(testUser);
        verify(userRepository).createUser(testUser);
    }

    @Test
    void createUserAuthType_shouldCallRepository() {
        userDomainService.createUserAuthType(testAuthType);
        verify(userAuthTypeRepository).createUserAuthType(testAuthType);
    }

    // 3. 유저 존재 테스트
    @Test
    void getUser_shouldReturnUserWhenExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        User result = userDomainService.getUser("test@example.com");
        assertEquals(testUser, result);
    }

    @Test
    void getUser_shouldThrowWhenNotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());
        assertThrows(AuthException.class, () -> userDomainService.getUser("unknown@example.com"));
    }

    // 4. 비번 검증 테스트
    @Test
    void userPasswordCheck_shouldPassWhenValid() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("correct", "hashedPassword")).thenReturn(true);

        assertDoesNotThrow(() ->
            userDomainService.userPasswordCheck("test@example.com", "correct"));
    }

    @Test
    void userPasswordCheck_shouldThrowWhenInvalid() {
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        AuthException exception = assertThrows(AuthException.class,
            () -> userDomainService.userPasswordCheck("test@example.com", "wrong"));

        assertEquals(AuthExceptionCode.PASSWORD_NOT_MATCH, exception.getResponseCode());
    }


    // 5. 비번 인코딩 테스트
    @Test
    void encodePassword_shouldReturnEncodedValue() {
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        assertEquals("encodedPassword", userDomainService.encodePassword("rawPassword"));
    }

    // 6. 인증 타입 테스트
    @Test
    void getUserAuthTypes_shouldReturnAuthTypes() {
        List<AuthType> expectedTypes = List.of(AuthType.EMAIL, AuthType.GOOGLE);
        when(userAuthTypeRepository.getUserAuthType(testUser)).thenReturn(expectedTypes);

        assertEquals(expectedTypes, userDomainService.getUserAuthTypes(testUser));
    }

    // 7. 비번 검증 테스트
    @Test
    void passwordComparison_shouldThrowWhenSame() {
        when(passwordEncoder.matches("newPass", "oldHashed")).thenReturn(true);
        assertThrows(AuthException.class,
            () -> userDomainService.passwordComparison("newPass", "oldHashed"));
    }

    @Test
    void passwordComparison_shouldPassWhenDifferent() {
        when(passwordEncoder.matches("newPass", "oldHashed")).thenReturn(false);
        assertDoesNotThrow(() ->
            userDomainService.passwordComparison("newPass", "oldHashed"));
    }

    // 8. 탈퇴 회원 테스트
    @Test
    void isDeletedUser_shouldThrowWhenDeleted() {
        User deletedUser = new User("email@gmail.com","Aa12345**", "username",
            "full@week.com", 100, Tier.CODER, UserRole.USER, true, true, "gitUrl.com", true);
        assertThrows(AuthException.class, () -> userDomainService.isDeletedUser(deletedUser));
    }

    @Test
    void isDeletedUser_shouldPassWhenActive() {
        assertDoesNotThrow(() -> userDomainService.isDeletedUser(testUser));
    }

    // 9. 닉네임 자동 생성 테스트
    @Test
    void generateUniqueNickname_shouldReturnNonExistingNickname() {
        when(userRepository.existsByNickname(any())).thenReturn(false);
        String nickname = userDomainService.generateUniqueNickname();
        assertNotNull(nickname);
    }

    @Test
    void generateUniqueNickname_shouldThrowWhenMaxAttempts() {
        when(userRepository.existsByNickname(any())).thenReturn(true);
        assertThrows(IllegalStateException.class, () -> userDomainService.generateUniqueNickname());
    }

    // 10. 리뷰 토큰 테스트
    @Test
    void decreaseReviewToken_shouldUpdateWhenTokensAvailable() {
        testUser.setReviewToken(5);
        assertDoesNotThrow(() -> userDomainService.decreaseReviewToken(testUser));
        verify(userRepository).decreaseReviewToken(testUser);
    }

    @Test
    void decreaseReviewToken_shouldThrowWhenNoTokens() {
        User zeroTokenUser = new User(
            "zero@example.com", "pwd", "user", "nick",
            28, Tier.NEWBIE, UserRole.USER,
            false, true, "https://github.com", false
        ) {
            public int getReviewToken() {
                return 0;
            }
        };

        UserException exception = assertThrows(UserException.class,
            () -> userDomainService.decreaseReviewToken(zeroTokenUser));

        assertEquals(UserExceptionCode.NOT_ENOUGH_TOKEN, exception.getResponseCode());
    }


    // 12. 이메일로 유저 찾기
    @Test
    void getUserByEmail_shouldReturnUser() {
        when(userRepository.getUserByEmail("test@example.com")).thenReturn(testUser);
        assertEquals(testUser, userDomainService.getUserByEmail("test@example.com"));
    }
}
