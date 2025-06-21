package org.ezcode.codetest.presentation.game.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class GameViewController {

	@GetMapping("/gaming")
	public String getChattingPage() {

		return "game-page";
	}
}
