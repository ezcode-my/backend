package org.ezcode.codetest.application.game.dto.mapper;
import org.ezcode.codetest.application.game.dto.response.AccessoryResponse;
import org.ezcode.codetest.application.game.dto.response.DefenceResponse;
import org.ezcode.codetest.application.game.dto.response.ItemResponse;
import org.ezcode.codetest.application.game.dto.response.WeaponResponse;
import org.ezcode.codetest.domain.game.model.entity.Accessory;
import org.ezcode.codetest.domain.game.model.entity.Defence;
import org.ezcode.codetest.domain.game.model.entity.Item;
import org.ezcode.codetest.domain.game.model.entity.Weapon;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(
	componentModel = "spring",
	subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION
)
public interface GameMapper {

	@SubclassMapping(target = WeaponResponse.class, source = Weapon.class)
	@SubclassMapping(target = DefenceResponse.class, source = Defence.class)
	@SubclassMapping(target = AccessoryResponse.class, source = Accessory.class)
	ItemResponse toItemResponse(Item item);

	WeaponResponse    toItemResponse(Weapon  weapon);
	DefenceResponse   toItemResponse(Defence defence);
	AccessoryResponse toItemResponse(Accessory accessory);
}
