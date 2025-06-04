package org.ezcode.codetest.domain.problem.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// ELASTICSEARCH 엔티티입니다.
// DB의 PK, title, description, 삭제여부만 저장합니다.
// 검색엔진에서 PK만 빠르게 검색해온다음 PK 로 다시 DB 조회를 합니다.

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "problems", createIndex = false)
@Setting(settingPath = "/elasticsearch/my_index-settings.json")
public class ProblemSearchDocument {

	@Id
	@Field(type = FieldType.Keyword)
	private Long id;

	@Field(
		type = FieldType.Text,
		analyzer = "ngram_analyzer",
		searchAnalyzer = "standard"
	)
	private String title;

	@Field(type = FieldType.Text, analyzer = "standard")
	private String description;

	@Field(type = FieldType.Boolean)
	private Boolean isDeleted;

	@Builder
	public ProblemSearchDocument(Long id, String title, String description, Boolean isDeleted) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.isDeleted = isDeleted;
	}

	public static ProblemSearchDocument from(Problem problem) {
		return ProblemSearchDocument.builder()
			.id(problem.getId())
			.title(problem.getTitle())
			.description(problem.getDescription())
			.isDeleted(problem.getIsDeleted())
			.build();
	}
}
