package xiaojin.gachaaddiction.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.RegistryAccess;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xiaojin.gachaaddiction.api.GachaaData;
import xiaojin.gachaaddiction.mixed.IAbstractContainerMenu;
import xiaojin.gachaaddiction.api.MenuProviderData;
import xiaojin.gachaaddiction.util.LootDisplayCache;
import xiaojin.gachaaddiction.util.ModUtil;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {

    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @WrapOperation(method = "openMenu(Lnet/minecraft/world/MenuProvider;Ljava/util/function/Consumer;)Ljava/util/OptionalInt;",
            at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/common/util/FriendlyByteBufUtil;writeCustomData(Ljava/util/function/Consumer;Lnet/minecraft/core/RegistryAccess;)[B"))
    private byte[] gachaaddiction$writeMenuData(Consumer<RegistryFriendlyByteBuf> dataWriter,
                                               RegistryAccess registries, Operation<byte[]> original,
                                               @Local AbstractContainerMenu menu) {
        // Prepend our metadata without depending on a compiler-generated lambda name.
        // The client MenuType mixin consumes this prefix before the provider's data.
        GachaaData data = IAbstractContainerMenu.of(menu).gachaaddiction$getGachaaData();
        return original.call((Consumer<RegistryFriendlyByteBuf>) buffer -> {
            GachaaData.OPTIONAL_STREAM_CODEC.encode(buffer, Optional.ofNullable(data));
            dataWriter.accept(buffer);
        }, registries);
    }

    @WrapOperation(method = "openMenu(Lnet/minecraft/world/MenuProvider;Ljava/util/function/Consumer;)Ljava/util/OptionalInt;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/MenuProvider;createMenu(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/inventory/AbstractContainerMenu;"))
    private AbstractContainerMenu gachaaddiction$openMenu$createMenu(
            MenuProvider instance,
            int i,
            Inventory inventory,
            Player player,
            Operation<AbstractContainerMenu> original
    ) {
        if (!MenuProviderData.isLootrContainer(instance)) {
            return original.call(instance, i, inventory, player);
        }
        AbstractContainerMenu menu = original.call(instance, i, inventory, player);
        if (menu == null) {
            return null;
        }
        IAbstractContainerMenu iMenu = IAbstractContainerMenu.of(menu);
        MenuProviderData data = MenuProviderData.create(instance);
        List<@Nullable ResourceKey<LootTable>> lootTableKey = data.lootTable();
        // TODO 自定义GachaType
        iMenu.gachaaddiction$setGachaaData(new GachaaData(data.isOpenGachaaUi(), Optional.empty(), Optional.of(ModUtil.of(lootTableKey)), Optional.of(LootDisplayCache.get(lootTableKey))));
        MenuProviderData.open(instance);
        return menu;
    }
}
