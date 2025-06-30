package org.ezcode.codetest.application.language;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.ezcode.codetest.application.submission.dto.request.language.LanguageCreateRequest;
import org.ezcode.codetest.application.submission.dto.request.language.LanguageUpdateRequest;
import org.ezcode.codetest.application.submission.dto.response.language.LanguageResponse;
import org.ezcode.codetest.application.submission.service.LanguageService;
import org.ezcode.codetest.domain.language.exception.LanguageException;
import org.ezcode.codetest.domain.language.exception.code.LanguageExceptionCode;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.ezcode.codetest.domain.language.service.LanguageDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("언어 서비스 테스트")
public class LanguageServiceTest {

    @InjectMocks
    private LanguageService languageService;

    @Mock
    private LanguageDomainService languageDomainService;

    private Long languageId;
    private Language language;
    private LanguageCreateRequest createRequest;
    private LanguageUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        languageId = 1L;

        language = new Language("Java", "17", 62L);
        ReflectionTestUtils.setField(language, "id", 1L);

        createRequest = new LanguageCreateRequest("Java", "17", 62L);
        updateRequest = new LanguageUpdateRequest(100L);
    }

    @Nested
    @DisplayName("언어 서비스 성공 테스트")
    class LanguageServiceSuccessTest {

        @Test
        @DisplayName("언어 생성 성공")
        void createLanguage() {

            // given
            willDoNothing().given(languageDomainService)
                .validateLanguageNotDuplicated(createRequest.name(), createRequest.version());

            given(languageDomainService.createLanguage(any(Language.class)))
                .willReturn(language);

            // when
            LanguageResponse response = languageService.createLanguage(createRequest);

            // then
            assertAll(
                () -> assertThat(response.id()).isEqualTo(language.getId()),
                () -> assertThat(response.name()).isEqualTo(language.getName()),
                () -> assertThat(response.version()).isEqualTo(language.getVersion()),
                () -> assertThat(response.judge0Id()).isEqualTo(language.getJudge0Id())
            );

            then(languageDomainService).should(times(1))
                .validateLanguageNotDuplicated(createRequest.name(), createRequest.version());
            then(languageDomainService).should(times(1)).createLanguage(any(Language.class));
        }

        @Test
        @DisplayName("언어 조회 성공")
        void getLanguages() {

            // given
            given(languageDomainService.getLanguages()).willReturn(List.of(language));

            // when
            List<LanguageResponse> responses = languageService.getLanguages();

            // then
            LanguageResponse response = responses.get(0);

            assertAll(
                () -> assertThat(responses).hasSize(1),
                () -> assertThat(response.id()).isEqualTo(language.getId()),
                () -> assertThat(response.name()).isEqualTo(language.getName()),
                () -> assertThat(response.version()).isEqualTo(language.getVersion()),
                () -> assertThat(response.judge0Id()).isEqualTo(language.getJudge0Id())
            );

            then(languageDomainService).should(times(1)).getLanguages();
        }

        @Test
        @DisplayName("언어 수정 성공")
        void modifyLanguage() {

            // given
            Long newJudge0Id = updateRequest.judge0Id();

            given(languageDomainService.getLanguage(languageId)).willReturn(language);
            willAnswer(invocationOnMock -> {
                Language langArg = invocationOnMock.getArgument(0);
                Long idArg = invocationOnMock.getArgument(1);
                ReflectionTestUtils.setField(langArg, "judge0Id", idArg);
                return null;
            }).given(languageDomainService).modifyLanguage(language, newJudge0Id);

            // when
            LanguageResponse response = languageService.modifyLanguage(languageId, updateRequest);

            // then
            assertAll(
                () -> assertThat(response.id()).isEqualTo(language.getId()),
                () -> assertThat(response.name()).isEqualTo(language.getName()),
                () -> assertThat(response.version()).isEqualTo(language.getVersion()),
                () -> assertThat(response.judge0Id()).isEqualTo(newJudge0Id)
            );

            then(languageDomainService).should(times(1)).getLanguage(languageId);
            then(languageDomainService).should(times(1)).modifyLanguage(language, newJudge0Id);
        }

        @Test
        @DisplayName("언어 삭제 성공")
        void removeLanguage() {

            // given
            willDoNothing().given(languageDomainService).validateLanguageExists(languageId);
            willDoNothing().given(languageDomainService).removeLanguage(languageId);

            // when
            languageService.removeLanguage(languageId);

            // then
            then(languageDomainService).should(times(1)).validateLanguageExists(languageId);
            then(languageDomainService).should(times(1)).removeLanguage(languageId);
        }
    }

    @Nested
    @DisplayName("언어 서비스 실패 테스트")
    class LanguageServiceFailureTest {

        @Test
        @DisplayName("언어 등록: 중복 등록 시 예외 발생")
        void createLanguage_Duplicate() {

            // given
            String name = createRequest.name();
            String version = createRequest.version();

            willThrow(new LanguageException(LanguageExceptionCode.LANGUAGE_ALREADY_EXISTS))
                .given(languageDomainService)
                .validateLanguageNotDuplicated(name, version);

            // when & then
            assertThatThrownBy(() -> languageService.createLanguage(createRequest))
                .isInstanceOf(LanguageException.class)
                .hasMessage(LanguageExceptionCode.LANGUAGE_ALREADY_EXISTS.getMessage());

            then(languageDomainService)
                .should(times(1)).validateLanguageNotDuplicated(name, version);
            then(languageDomainService).should(never()).createLanguage(any(Language.class));
        }

        @Test
        @DisplayName("언어 수정: 존재하지 않으면 예외 발생")
        void modifyLanguage_NotFound() {

            // given

            willThrow(new LanguageException(LanguageExceptionCode.LANGUAGE_NOT_FOUND))
                .given(languageDomainService)
                .getLanguage(languageId);

            // when & then
            assertThatThrownBy(() -> languageService.modifyLanguage(languageId, updateRequest))
                .isInstanceOf(LanguageException.class)
                .hasMessage(LanguageExceptionCode.LANGUAGE_NOT_FOUND.getMessage());

            then(languageDomainService).should(times(1)).getLanguage(languageId);
            then(languageDomainService).should(never()).modifyLanguage(any(Language.class), any(Long.class));
        }

        @Test
        @DisplayName("언어 삭제: 존재하지 않으면 예외 발생")
        void removeLanguage_NotFound() {

            // given

            willThrow(new LanguageException(LanguageExceptionCode.LANGUAGE_NOT_FOUND))
                .given(languageDomainService)
                .validateLanguageExists(languageId);

            // when & then
            assertThatThrownBy(() -> languageService.removeLanguage(languageId))
                .isInstanceOf(LanguageException.class)
                .hasMessage(LanguageExceptionCode.LANGUAGE_NOT_FOUND.getMessage());

            then(languageDomainService).should(times(1)).validateLanguageExists(languageId);
            then(languageDomainService).should(never()).removeLanguage(languageId);
        }
    }
}
