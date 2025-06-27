package org.ezcode.codetest.domain.problem.model.entity;

import java.util.List;

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
	private List<String> categories;

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
	private List<String> categoriesKor;

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
		List<String> categories,
		String difficulty,
		Reference reference,
		String description,
		List<String> categoriesKor,
		Difficulty difficultyEn,
		String referenceKor,
		int score,
		Boolean isDeleted
	) {
		this.id = id;
		this.title = title;
		this.categories = categories;
		this.difficulty = difficulty;
		this.reference = reference;
		this.description = description;
		this.categoriesKor = categoriesKor;
		this.difficultyEn = difficultyEn;
		this.referenceKor = referenceKor;
		this.score = score;
		this.isDeleted = isDeleted;
	}

	public static ProblemSearchDocument from(Problem problem, List<Category> categories) {
		return ProblemSearchDocument.builder()
			.id(problem.getId())
			.title(problem.getTitle())
			.categories(categories.stream().map(Category::getCode).toList())
			.difficulty(problem.getDifficulty().getDifficulty())
			.reference(problem.getReference())
			.description(problem.getDescription())
			.score(problem.getScore())
			.categoriesKor(categories.stream().map(Category::getKorName).toList())
			.difficultyEn(problem.getDifficulty())
			.referenceKor(problem.getReference().getDescription())
			.isDeleted(problem.getIsDeleted())
			.build();
	}

	public void softDelete() {
		this.isDeleted = true;
	}

	public void update(Problem problem, List<Category> categories) {
		if (problem.getId().equals(this.id)) {
			this.title = problem.getTitle();
			this.categories = categories.stream().map(Category::getCode).toList();
			this.categoriesKor = categories.stream().map(Category::getKorName).toList();
			this.difficulty = problem.getDifficulty().getDifficulty();
			this.difficultyEn = problem.getDifficulty();
			this.reference = problem.getReference();
			this.referenceKor = problem.getReference().getDescription();
			this.description = problem.getDescription();
			this.score = problem.getScore();
		}
	}
}