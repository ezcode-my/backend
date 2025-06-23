package org.ezcode.codetest.presentation.game.play;

import org.ezcode.codetest.domain.game.service.CharacterStatusDomainService;
import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/games/levels")
public class TestGamePlayController {

	private final CharacterStatusDomainService statusDomainService;

	@GetMapping("/characters")
	@Transactional
	public ResponseEntity<Void> CharacterLevelUpTest(
		@AuthenticationPrincipal AuthUser authUser
	) {

		Category randomCat = Category.values()[java.util.concurrent.ThreadLocalRandom.current().nextInt(Category.values().length)];

		statusDomainService.gameCharacterLevelUp(authUser.getId(), true, randomCat.getDescription());

		System.out.println(randomCat.getDescription() + "레벨업");

		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
