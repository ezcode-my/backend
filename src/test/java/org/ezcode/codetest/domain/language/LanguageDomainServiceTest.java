package org.ezcode.codetest.domain.language;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.language.exception.LanguageException;
import org.ezcode.codetest.domain.language.exception.code.LanguageExceptionCode;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.ezcode.codetest.domain.language.repository.LanguageRepository;
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
@DisplayName("언어 도메인 서비스 테스트")
public class LanguageDomainServiceTest {

    @InjectMocks
    private LanguageDomainService languageDomainService;

    @Mock
    private LanguageRepository languageRepository;

    private Long languageId;
    private String name;
    private String version;
    private Language language;

    @BeforeEach
    void setup() {
        languageId = 1L;
        name = "Java";
        version = "17";

        language = new Language("Java", "17", 62L);
        ReflectionTestUtils.setField(language, "id", 1L);
    }

    @Nested
    @DisplayName("언어 도메인 서비스 성공 테스트")
    class LanguageDomainServiceSuccessTest {

        @Test
        @DisplayName("존재할 때 예외 없이 통과")
        void validateLanguageExists() {

            // given
            given(languageRepository.existsById(languageId)).willReturn(true);

            // when & then
            assertThatCode(() -> languageDomainService.validateLanguageExists(languageId))
                .doesNotThrowAnyException();

            then(languageRepository).should(times(1)).existsById(languageId);

        }

        @Test
        @DisplayName("중복 아닐 때 예외 없이 통과")
        void validateLanguageNotDuplicated() {

            // given
            given(languageRepository.existsByNameAndVersion(name, version)).willReturn(false);

            // when & then
            assertThatCode(() -> languageDomainService.validateLanguageNotDuplicated(name, version))
                .doesNotThrowAnyException();

            then(languageRepository).should(times(1)).existsByNameAndVersion(name, version);
        }

        @Test
        @DisplayName("저장한 엔티티를 그대로 반환")
        void createLanguage() {

            // given
            given(languageRepository.saveLanguage(language)).willReturn(language);

            // when
            Language result = languageDomainService.createLanguage(language);

            // then
            assertThat(result).isSameAs(language);

            then(languageRepository).should(times(1)).saveLanguage(language);
        }

        @Test
        @DisplayName("존재하면 엔티티 반환")
        void getLanguage() {

            // given
            given(languageRepository.findLanguageById(languageId)).willReturn(Optional.of(language));

            // when
            Language result = languageDomainService.getLanguage(languageId);

            // then
            assertThat(result).isSameAs(language);

            then(languageRepository).should(times(1)).findLanguageById(languageId);
        }

        @Test
        @DisplayName("리스트 반환")
        void getLanguages() {

            // given
            given(languageRepository.findLanguages()).willReturn(List.of(language));

            // when
            List<Language> result = languageDomainService.getLanguages();

            // then
            assertThat(result).containsExactly(language);
            then(languageRepository).should(times(1)).findLanguages();
        }

        @Test
        @DisplayName("수정 메서드 호출")
        void modifyLanguage() {

            // given
            Long newJudge0Id = 100L;
            willDoNothing().given(languageRepository).updateLanguage(language, newJudge0Id);

            // when
            languageDomainService.modifyLanguage(language, newJudge0Id);

            // then
            then(languageRepository).should(times(1)).updateLanguage(language, newJudge0Id);
        }

        @Test
        @DisplayName("삭제 메서드 호출")
        void removeLanguage() {

            // given
            willDoNothing().given(languageRepository).deleteLanguage(languageId);

            // when
            languageDomainService.removeLanguage(languageId);

            // then
            then(languageRepository).should(times(1)).deleteLanguage(languageId);
        }

    }

    @Nested
    @DisplayName("언어 도메인 서비스 실패 테스트")
    class LanguageDomainServiceFailureTest {

        @Test
        @DisplayName("존재하지 않을 때 예외 발생_1")
        void validateLanguageExists_NotFound() {

            // given
            given(languageRepository.existsById(languageId)).willReturn(false);

            // when & then
            assertThatThrownBy(() -> languageDomainService.validateLanguageExists(languageId))
                .isInstanceOf(LanguageException.class)
                .hasMessage(LanguageExceptionCode.LANGUAGE_NOT_FOUND.getMessage());

            then(languageRepository).should(times(1)).existsById(languageId);
        }

        @Test
        @DisplayName("중복일 때 예외 발생")
        void validateLanguageNotDuplicated_LanguageAlreadyExists() {

            // given
            given(languageRepository.existsByNameAndVersion(name, version)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> languageDomainService.validateLanguageNotDuplicated(name, version))
                .isInstanceOf(LanguageException.class)
                .hasMessage(LanguageExceptionCode.LANGUAGE_ALREADY_EXISTS.getMessage());

            then(languageRepository).should(times(1)).existsByNameAndVersion(name, version);
        }

        @Test
        @DisplayName("존재하지 않을 때 예외 발생_2")
        void getLanguage_NotFound() {

            // given
            given(languageRepository.findLanguageById(languageId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> languageDomainService.getLanguage(languageId))
                .isInstanceOf(LanguageException.class)
                .hasMessage(LanguageExceptionCode.LANGUAGE_NOT_FOUND.getMessage());

            then(languageRepository).should(times(1)).findLanguageById(languageId);
        }
    }
}
