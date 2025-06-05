package org.ezcode.codetest.domain.problem.model.entity;

import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.problem.model.enums.Reference;
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
public class ProblemSearchDocument {

	@Id
	@Field(type = FieldType.Keyword)
	private Long id;

	@Field(
		type = FieldType.Text,
		analyzer = "ngram_analyzer",
		searchAnalyzer = "ngram_analyzer"
	)
	private String title;

	@Field(
		type = FieldType.Text,
		analyzer = "ngram_analyzer",
		searchAnalyzer = "ngram_analyzer"
	)
	private Category category;

	@Field(
		type = FieldType.Text,
		analyzer = "ngram_analyzer",
		searchAnalyzer = "ngram_analyzer"
	)
	private String difficulty;

	@Field(
		type = FieldType.Text,
		analyzer = "ngram_analyzer",
		searchAnalyzer = "ngram_analyzer"
	)
	private Reference reference;

	@Field(type = FieldType.Text, analyzer = "standard")
	private String description;

	@Field(type = FieldType.Keyword)
	private int score;

	@Field(type = FieldType.Boolean)
	private Boolean isDeleted;

	@Builder
	public ProblemSearchDocument(
		Long id,
		String title,
		Category category,
		String difficulty,
		Reference reference,
		String description,
		int score,
		Boolean isDeleted
	) {
		this.id = id;
		this.title = title;
		this.category = category;
		this.difficulty = difficulty;
		this.reference = reference;
		this.description = description;
		this.score = score;
		this.isDeleted = isDeleted;
	}

	public static ProblemSearchDocument from(Problem problem) {
		return ProblemSearchDocument.builder()
			.id(problem.getId())
			.title(problem.getTitle())
			.category(problem.getCategory())
			.difficulty(problem.getDifficulty())
			.reference(problem.getReference())
			.description(problem.getDescription())
			.score(problem.getScore())
			.isDeleted(problem.getIsDeleted())
			.build();
	}

	public void softDelete() {
		this.isDeleted = true;
	}

	public void update(Problem problem) {

		if(problem.getId().equals(this.id)) {
			this.title = problem.getTitle();
			this.category = problem.getCategory();
			this.difficulty = problem.getDifficulty();
			this.reference = problem.getReference();
			this.description = problem.getDescription();
			this.score = problem.getScore();
		}
	}
}
