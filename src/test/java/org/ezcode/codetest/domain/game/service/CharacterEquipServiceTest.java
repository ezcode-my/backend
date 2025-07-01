package org.ezcode.codetest.domain.game.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.game.exception.GameException;
import org.ezcode.codetest.domain.game.exception.GameExceptionCode;
import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.ezcode.codetest.domain.game.model.character.Inventory;
import org.ezcode.codetest.domain.game.model.item.Accessory;
import org.ezcode.codetest.domain.game.model.item.AccessoryType;
import org.ezcode.codetest.domain.game.model.item.Defence;
import org.ezcode.codetest.domain.game.model.item.DefenceType;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.Item;
import org.ezcode.codetest.domain.game.model.item.ItemType;
import org.ezcode.codetest.domain.game.model.item.Weapon;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.GameCharacterSkill;
import org.ezcode.codetest.domain.game.model.skill.Skill;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.model.skill.SkillSlotType;
import org.ezcode.codetest.domain.game.repository.GameCharacterSkillRepository;
import org.ezcode.codetest.domain.game.repository.InventoryRepository;
import org.ezcode.codetest.domain.game.repository.ItemRepository;
import org.ezcode.codetest.domain.game.repository.SkillRepository;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.model.enums.Tier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("캐릭터 장착 도메인 서비스 테스트")
class CharacterEquipServiceTest {

	@InjectMocks
	private CharacterEquipService equipService;

	@Mock
	private InventoryRepository inventoryRepository;

	@Mock
	private ItemRepository itemRepository;

	@Mock
	private GameCharacterSkillRepository characterSkillRepository;

	@Mock
	private SkillRepository skillRepository;

	@Spy
	private List<Item> fullSlotEquippedItems = new ArrayList<>();

	@Spy
	private List<Item> notFullSlotEquippedItems = new ArrayList<>();

	@Spy
	private List<GameCharacterSkill> characterSkills = new ArrayList<>();

	@Spy
	private GameCharacter character;

	@Spy
	private Inventory inventory;

	@Spy
	private GameCharacterSkill characterSkill3;

	private User user;
	private Skill skill;
	private GameCharacterSkill characterSkill1, characterSkill2;
	private Item weapon, defence, accessory;

	private static final String ITEM_NAME = "테스트 아이템";
	private static final String SKILL_NAME = "테스트 스킬";
	private static final Integer SLOT_NUMBER = 1;

	@BeforeEach
	void setUp() {

		user = User.builder()
			.email("test@unknow.com")
			.username("익명")
			.password("P@ssw0rd1225")
			.nickname("익명 닉네임")
			.tier(Tier.NEWBIE)
			.age(22)
			.build();

		ReflectionTestUtils.setField(user, "id", 1L);

		character = spy(new GameCharacter(user));

		skill = Skill.builder()
			.name("테스트 스킬 이름")
			.skillDetails("테스트 스킬 설명")
			.skillEffect(SkillEffect.BLOODY_MESS)
			.grade(Grade.TRASH)
			.build();

		ReflectionTestUtils.setField(skill, "id", 1L);

		inventory = spy(new Inventory(character));

		ReflectionTestUtils.setField(inventory, "id", 1L);

		characterSkill1 = GameCharacterSkill.builder()
			.character(character)
			.skill(skill)
			.slotType(SkillSlotType.SLOT_1)
			.build();

		characterSkill2 = GameCharacterSkill.builder()
			.character(character)
			.skill(skill)
			.slotType(SkillSlotType.SLOT_2)
			.build();

		characterSkill3 = spy(GameCharacterSkill.builder()
			.character(character)
			.skill(skill)
			.slotType(SkillSlotType.BACKPACK)
			.build());

		weapon = Weapon.builder()
			.id("1")
			.atk(1)
			.grade(Grade.TRASH)
			.speed(1)
			.stun(1)
			.accuracy(1)
			.crit(1)
			.description("테스트 무기 설명")
			.name("테스트 무기")
			.type(WeaponType.LONG_SWORD)
			.build();

		defence = Defence.builder()
			.id("2")
			.grade(Grade.TRASH)
			.speed(1)
			.def(1)
			.description("테스트 방어구 설명")
			.evasion(1)
			.name("테스트 방어구")
			.type(DefenceType.ARMOR)
			.build();

		accessory = Accessory.builder()
			.id("3")
			.grade(Grade.TRASH)
			.speed(1)
			.stun(1)
			.accuracy(1)
			.crit(1)
			.evasion(1)
			.description("테스트 악세서리 설명")
			.name("테스트 악세서리")
			.type(AccessoryType.CHARM)
			.build();

		fullSlotEquippedItems.addAll(List.of(weapon, defence, accessory));
		notFullSlotEquippedItems.addAll(List.of(weapon, defence));
		characterSkills.addAll(List.of(characterSkill1, characterSkill2, characterSkill3));
	}

	@Nested
	@DisplayName("캐릭터 장착 도메인 서비스 테스트 성공")
	class CharacterEquipServiceSuccessTest {

		@Test
		@DisplayName("캐릭터 장착 아이템 불러오기 성공(아이템 삭제가 일어나지 않았을 경우)")
		void loadEquippedItemsWhenItemIsNotDeleted() {

			// given
			String weaponId = character.getWeaponId();
			String defenceId = character.getDefenceId();
			String accessoryId = character.getAccessoryId();
			List<String> ids = List.of(weaponId, defenceId, accessoryId);
			given(itemRepository.findByIdIn(ids)).willReturn(fullSlotEquippedItems);

			// when
			List<Item> equippedItems = equipService.loadEquippedItems(character);

			// then
			verify(itemRepository).findByIdIn(ids);
			verify(fullSlotEquippedItems).size();

			assertAll(
				() -> assertEquals(3, equippedItems.size()),
				() -> assertEquals(fullSlotEquippedItems, equippedItems)
			);

		}

		@Test
		@DisplayName("캐릭터 장착 아이템 불러오기 성공(장착한 아이템이 삭제가 되었을 경우)")
		void loadEquippedItemsWhenItemIsDeleted() {

			// given
			String weaponId = character.getWeaponId();
			String defenceId = character.getDefenceId();
			String accessoryId = character.getAccessoryId();
			List<String> ids = List.of(weaponId, defenceId, accessoryId);
			given(itemRepository.findByIdIn(ids)).willReturn(notFullSlotEquippedItems);

			// when
			List<Item> equippedItems = equipService.loadEquippedItems(character);

			// then
			verify(itemRepository).findByIdIn(ids);
			verify(notFullSlotEquippedItems).size();
			verify(character).unEquipAllItems();
			verify(character, times(notFullSlotEquippedItems.size())).equipItem(any(), any());

			assertAll(
				() -> assertEquals(2, equippedItems.size()),
				() -> assertEquals(notFullSlotEquippedItems, equippedItems)
			);

		}

		@Test
		@DisplayName("캐릭터 장착 스킬 불러오기")
		void loadEquippedSkills() {

			// given
			given(characterSkillRepository.findByCharacterIdAndEquipped(character.getId())).willReturn(characterSkills);

			// when
			equipService.loadEquippedSkills(character);

			//then
			verify(characterSkills).sort(any());

		}

		@Test
		@DisplayName("캐릭터 미장착 스킬 불러오기")
		void loadUnEquippedSkills() {

			//given
			given(characterSkillRepository.findByCharacterIdAndUnEquipped(character.getId())).willReturn(
				characterSkills);

			//when
			equipService.loadUnEquippedSkills(character);

			//then
			verify(characterSkillRepository).findByCharacterIdAndUnEquipped(character.getId());
		}

		@Test
		@DisplayName("캐릭터 생성시 기본 아이템 장착하기")
		void equipDefaultItem() {

			//given
			given(itemRepository.findByName(ITEM_NAME)).willReturn(Optional.of(weapon));

			//when
			equipService.equipDefaultItem(character, inventory, ITEM_NAME);

			//then
			verify(inventory).addItem(any(ItemType.class), anyString());
			verify(character).equipItem(any(ItemType.class), anyString());
		}

		@Test
		@DisplayName("캐릭터 스킬 장착 성공시")
		void equipSkillWhenSkillNotEquipped() {

			// given
			given(skillRepository.findByName(SKILL_NAME))
				.willReturn(Optional.of(skill));

			GameCharacterSkill target = mock(GameCharacterSkill.class);
			given(target.getSkill()).willReturn(skill);
			given(target.getSlotType()).willReturn(SkillSlotType.BACKPACK);

			given(characterSkillRepository.findByCharacterId(character.getId()))
				.willReturn(List.of(target));

			// when
			equipService.equipSkill(character, SKILL_NAME, SLOT_NUMBER);

			// then
			verify(target).equipSkill(SkillSlotType.SLOT_1);

		}

		@Test
		@DisplayName("캐릭터 스킬 장착 해제시")
		void unEquipSkillWhenSkillEquipped() {

			// given
			given(skillRepository.findByName(SKILL_NAME))
				.willReturn(Optional.of(skill));

			GameCharacterSkill target = mock(GameCharacterSkill.class);
			given(target.getSkill()).willReturn(skill);
			given(target.getSlotType()).willReturn(SkillSlotType.SLOT_1);

			given(characterSkillRepository.findByCharacterId(character.getId()))
				.willReturn(List.of(target));

			// when
			equipService.unEquipSkill(character, SKILL_NAME);

			// then
			verify(target).unEquipSkill();

		}



	}

}