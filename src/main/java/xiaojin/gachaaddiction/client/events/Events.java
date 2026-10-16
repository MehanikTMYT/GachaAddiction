package xiaojin.gachaaddiction.client.events;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.jetbrains.annotations.Nullable;
import xiaojin.gachaaddiction.GachaAddiction;
import xiaojin.gachaaddiction.api.GachaType;
import xiaojin.gachaaddiction.api.GachaaData;
import xiaojin.gachaaddiction.mixed.IAbstractContainerMenu;
import xiaojin.gachaaddiction.registry.GachaaAdictionConfigData;

@EventBusSubscriber(modid = GachaAddiction.MODID, value = Dist.CLIENT)
public class Events {
    @SubscribeEvent
    public static void onScreenOpening(ScreenEvent.Opening event) {
        Screen screen = event.getScreen();
        if (!(screen instanceof MenuAccess<?> menuAccess)) {
            return;
        }

        @Nullable GachaaData gachaaData = IAbstractContainerMenu.of(menuAccess.getMenu()).gachaaddiction$getGachaaData();
        if (gachaaData == null) {
            return;
        }
        boolean isOpenGachaaUi = gachaaData.isOpenGachaaUi();

        if (!isOpenGachaaUi) {
            return;
        }

        // 接受类型
        GachaType gachaType = gachaaData.getGachaType();

        if (gachaType.isEmpty()) {
            return;
        }

        event.setNewScreen(gachaType.getClientData().create(screen, gachaaData));
    }

    @SubscribeEvent
    public static void onRegisterClientReloadListeners(AddClientReloadListenersEvent event) {
        event.addListener(net.minecraft.resources.Identifier.fromNamespaceAndPath(GachaAddiction.MODID, "reward_sounds"), GachaaAdictionConfigData.INSTANCE);
    }
}
