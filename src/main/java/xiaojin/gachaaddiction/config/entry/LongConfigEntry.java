package xiaojin.gachaaddiction.config.entry;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class LongConfigEntry extends RangedNumberConfigEntry<Long> {
    public LongConfigEntry(ModConfigSpec.LongValue rawValue, List<String> path, String translationKey,
                           List<String> comments, long defaultValue, long min, long max) {
        super(rawValue, path, translationKey, comments, defaultValue, min, max,
                value -> value >= min && value <= max);
    }

    public static Builder builder(ModConfigSpec.Builder specBuilder, String modId, List<String> currentPath) {
        return new Builder(specBuilder, modId, currentPath);
    }

    public static class Builder extends RangedNumberConfigEntry.Builder<Long, LongConfigEntry, Builder> {

        private Builder(ModConfigSpec.Builder specBuilder, String modId, List<String> currentPath) {
            super(specBuilder, modId, currentPath);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public LongConfigEntry build() {
            validateRangeDefinition(value -> true);
            var value = prepareDefinition().defineInRange(key, defaultValue, min, max);
            return new LongConfigEntry(value, path(), translationKey(), comments, defaultValue, min, max);
        }
    }
}
