package org.ezcode.codetest.domain.language.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String version;

    @Column(name = "judge0_id", nullable = false)
    private Long judge0Id;

    @Builder
    public Language(String name, String version, Long judge0Id) {
        this.name = name;
        this.version = version;
        this.judge0Id = judge0Id;
    }

    public void updateJudge0Id(Long judge0Id) {
        this.judge0Id = judge0Id;
    }
}
