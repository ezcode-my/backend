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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "problems", createIndex = false)
@Setting(settingPath = "/elasticsearch/my_index-settings.json")
public class ProblemSearch {

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
	public ProblemSearch(Long id, String title, String description, Boolean isDeleted) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.isDeleted = isDeleted;
	}

	public static ProblemSearch of(Long id, String title, String description, Boolean isDeleted) {
		return ProblemSearch.builder()
			.id(id)
			.title(title)
			.description(description)
			.isDeleted(isDeleted)
			.build();
	}
}
