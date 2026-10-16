package xiaojin.gachaaddiction.util;

import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import xiaojin.gachaaddiction.GachaAddiction;

public class RarityUtil {
    public static int getRarityColor(ItemStack itemStack) {
        if (GachaAddiction.CONFLUENCE_MAGIC_LIB_LOADED) {
            var rarity = ConfluenceMagicLibUtil.getRarity(itemStack);
            if (rarity != null) {
                return rarity.color();
            }
        }

        if (GachaAddiction.RARITYCORE_LOADED) {
            return RarityCoreUtil.getColor(itemStack);
        }
        
        Rarity rarity = itemStack.getRarity();
        TextColor color = TextColor.fromLegacyFormat(rarity.color());
        return color != null ? color.getValue() : 0xAAAAAA;
    }

    public static int getRarityLevel(ItemStack itemStack) {
        if (GachaAddiction.CONFLUENCE_MAGIC_LIB_LOADED) {
            var rarity = ConfluenceMagicLibUtil.getRarity(itemStack);
            if (rarity != null) {
                return ConfluenceMagicLibUtil.getRarityLevel(rarity);
            }
        }

        if (GachaAddiction.RARITYCORE_LOADED) {
            return RarityCoreUtil.getRarity(itemStack);
        }

        return itemStack.getRarity().ordinal();
    }
}
