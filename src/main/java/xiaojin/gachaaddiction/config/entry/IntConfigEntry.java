package xiaojin.gachaaddiction.config.entry;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class IntConfigEntry extends RangedNumberConfigEntry<Integer> {
    public IntConfigEntry(ModConfigSpec.IntValue rawValue, List<String> path, String translationKey,
                          List<String> comments, int defaultValue, int min, int max) {
        super(rawValue, path, translationKey, comments, defaultValue, min, max,
                value -> value >= min && value <= max);
    }

    public static Builder builder(ModConfigSpec.Builder specBuilder, String modId, List<String> currentPath) {
        return new Builder(specBuilder, modId, currentPath);
    }

    public static class Builder extends RangedNumberConfigEntry.Builder<Integer, IntConfigEntry, Builder> {

        private Builder(ModConfigSpec.Builder specBuilder, String modId, List<String> currentPath) {
            super(specBuilder, modId, currentPath);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public IntConfigEntry build() {
            validateRangeDefinition(value -> true);
            var value = prepareDefinition().defineInRange(key, defaultValue, min, max);
            return new IntConfigEntry(value, path(), translationKey(), comments, defaultValue, min, max);
        }
    }
}
