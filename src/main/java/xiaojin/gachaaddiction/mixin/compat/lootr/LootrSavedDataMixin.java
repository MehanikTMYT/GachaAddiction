package xiaojin.gachaaddiction.mixin.compat.lootr;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import noobanidus.mods.lootr.common.api.data.ILootrData;
import noobanidus.mods.lootr.common.data.LootrInventory;
import noobanidus.mods.lootr.common.data.LootrInventoryStore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xiaojin.gachaaddiction.mixed.IILootrInventory;

@Mixin(LootrInventoryStore.class)
public abstract class LootrSavedDataMixin {
    // The codec constructor restores Lootr's persisted, per-player opener set.
    // Reuse it without changing Lootr's on-disk inventory codec.
    @Inject(method = "<init>(ZLnoobanidus/mods/lootr/common/api/data/ILootrData;Ljava/util/Map;Ljava/util/Set;Ljava/util/Set;JJ)V", at = @At("RETURN"))
    private void gachaaddiction$restoreOpeners(boolean hasInventories, ILootrData info,
            Map<UUID, LootrInventory> inventories, Set<UUID> openers, Set<UUID> actualOpeners,
            long decayTime, long refreshTime, CallbackInfo ci) {
        inventories.forEach((player, inventory) ->
                IILootrInventory.of(inventory).gachaaddiction$setOpen(actualOpeners.contains(player)));
    }
}
