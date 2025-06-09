package org.ezcode.codetest.domain.problem.model.entity;

import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.problem.model.enums.Difficulty;
import org.ezcode.codetest.domain.problem.model.enums.Reference;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;
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

	@MultiField(
		mainField = @Field(
			type = FieldType.Text,
			analyzer = "lowercase_analyzer",
			searchAnalyzer = "lowercase_standard"
		),
		otherFields = {
			@InnerField(
				suffix = "keyword",
				type = FieldType.Keyword,
				normalizer = "lowercase_normalizer"
			)
		}
	)
	private String title;

	@MultiField(
		mainField = @Field(
			type = FieldType.Text,
			analyzer = "uppercase_analyzer",
			searchAnalyzer = "uppercase_standard"
		),
		otherFields = {
			@InnerField(
				suffix = "keyword",
				type = FieldType.Keyword,
				normalizer = "uppercase_normalizer"
			)
		}
	)
	private Category category;

	@MultiField(
		mainField = @Field(
			type = FieldType.Text,
			analyzer = "nori_ko_with_en",
			searchAnalyzer = "nori_ko_with_en"
		),
		otherFields = {
			@InnerField(
				suffix = "keyword",
				type = FieldType.Keyword
			)
		}
	)
	private String categoryKor;

	@MultiField(
		mainField = @Field(
			type = FieldType.Text,
			analyzer = "uppercase_analyzer",
			searchAnalyzer = "uppercase_standard"
		),
		otherFields = {
			@InnerField(
				suffix = "keyword",
				type = FieldType.Keyword,
				normalizer = "uppercase_normalizer"
			)
		}
	)
	private String difficulty;

	@MultiField(
		mainField = @Field(
			type = FieldType.Text,
			analyzer = "uppercase_analyzer",
			searchAnalyzer = "uppercase_standard"
		),
		otherFields = {
			@InnerField(
				suffix = "keyword",
				type = FieldType.Keyword,
				normalizer = "uppercase_normalizer"
			)
		}
	)
	private Difficulty difficultyEn;

	@MultiField(
		mainField = @Field(
			type = FieldType.Text,
			analyzer = "uppercase_analyzer",
			searchAnalyzer = "uppercase_standard"
		),
		otherFields = {
			@InnerField(
				suffix = "keyword",
				type = FieldType.Keyword,
				normalizer = "uppercase_normalizer"
			)
		}
	)
	private Reference reference;

	@MultiField(
		mainField = @Field(
			type = FieldType.Text,
			analyzer = "nori_ko_with_en",
			searchAnalyzer = "nori_ko_with_en"
		),
		otherFields = {
			@InnerField(
				suffix = "keyword",
				type = FieldType.Keyword
			)
		}
	)
	private String referenceKor;

	@Field(
		type = FieldType.Text,
		analyzer = "nori_ko_with_en",
		searchAnalyzer = "nori_ko_with_en"
	)
	private String description;

	@Field(type = FieldType.Integer)
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
		String categoryKor,
		Difficulty difficultyEn,
		String referenceKor,
		int score,
		Boolean isDeleted
	) {
		this.id = id;
		this.title = title;
		this.category = category;
		this.difficulty = difficulty;
		this.reference = reference;
		this.description = description;
		this.categoryKor = categoryKor;
		this.difficultyEn = difficultyEn;
		this.referenceKor = referenceKor;
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
			.categoryKor(problem.getCategory().getDescription())
			.difficultyEn(Difficulty.getDifficultyFromKor(problem.getDifficulty()))
			.referenceKor(problem.getReference().getDescription())
			.isDeleted(problem.getIsDeleted())
			.build();
	}

	public void softDelete() {
		this.isDeleted = true;
	}

	public void update(Problem problem) {
		if (problem.getId().equals(this.id)) {
			this.title = problem.getTitle();
			this.category = problem.getCategory();
			this.difficulty = problem.getDifficulty();
			this.reference = problem.getReference();
			this.description = problem.getDescription();
			this.score = problem.getScore();
		}
	}
}