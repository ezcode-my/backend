package org.ezcode.codetest.domain.game.model.character;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.ezcode.codetest.domain.problem.model.entity.Category;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryStat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "category_id", nullable = false, unique = true)
	private Category category;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(
		name = "category_per_stat_increased_rate",
		joinColumns = @JoinColumn(name = "category_stat_id")
	)
	@MapKeyColumn(name = "stat")
	@Column(name = "value")
	@MapKeyEnumerated(EnumType.STRING)
	private Map<Stat, Double> stats = new EnumMap<>(Stat.class);

	public CategoryStat(Category category) {

		this.category = category;

		for (Stat stat : Stat.values()) {
			double randomValue = ThreadLocalRandom.current()
				.nextDouble(0.5, 2.0);
			stats.put(stat, randomValue);
		}
	}

}


