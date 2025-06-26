package org.ezcode.codetest.domain.problem.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String categoryCode;

	@Column(unique = true, nullable = false)
	private String categoryKorName;

	public Category (String category, String categoryKor) {
		this.categoryCode = category;
		this.categoryKorName = categoryKor;
	}

}
