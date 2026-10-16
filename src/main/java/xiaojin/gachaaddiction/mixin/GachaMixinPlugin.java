package xiaojin.gachaaddiction.mixin;

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.neoforged.fml.loading.FMLLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

/** Activates each optional integration only when its owning mod is installed. */
public final class GachaMixinPlugin implements IMixinConfigPlugin {
    private static final Map<String, String> MODS = Map.of(
            "ftb", "ftbquests", "lootr", "lootr",
            "treasurebags", "treasurebags", "lootbundles", "lootbundles");

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String prefix = "xiaojin.gachaaddiction.mixin.compat.";
        if (!mixinClassName.startsWith(prefix)) {
            return true;
        }
        String integration = mixinClassName.substring(prefix.length()).split("\\.", 2)[0];
        String modId = MODS.get(integration);
        if (modId == null) {
            throw new IllegalArgumentException("Unknown Gacha integration: " + integration);
        }
        return FMLLoader.getCurrent().getLoadingModList().getModFileById(modId) != null;
    }

    @Override public void onLoad(String mixinPackage) {}
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
