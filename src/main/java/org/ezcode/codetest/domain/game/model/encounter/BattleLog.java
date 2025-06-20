package org.ezcode.codetest.domain.game.model.encounter;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class BattleLog {

	private final List<String> messages = new ArrayList<>();

	@Setter
	private Boolean playerWin;

	public void add(String format, Object... args) {
		messages.add(String.format(format, args));
	}

	public String asText() {
		return String.join("\n", messages);
	}
}
