package org.ezcode.codetest.domain.game.model.encounter;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class EncounterLog {

	private final List<String> messages = new ArrayList<>();

	@Setter
	private Boolean isPositive;

	public void add(String format, Object... args) {
		messages.add(String.format(format, args));
	}

}
