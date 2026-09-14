package xiaojin.gachaaddiction.config.entry;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class BooleanConfigEntry extends BasicConfigEntry<Boolean> {
    public BooleanConfigEntry(ModConfigSpec.BooleanValue rawValue, List<String> path, String translationKey,
                              List<String> comments, boolean defaultValue) {
        super(rawValue, path, translationKey, comments, defaultValue, value -> true, rawValue, rawValue::set);
    }

    public static Builder builder(ModConfigSpec.Builder specBuilder, String modId, List<String> currentPath) {
        return new Builder(specBuilder, modId, currentPath);
    }

    public static class Builder extends BasicConfigEntry.Builder<BooleanConfigEntry, Builder> {
        private boolean defaultValue;
        private boolean defaultValueSet;

        private Builder(ModConfigSpec.Builder specBuilder, String modId, List<String> currentPath) {
            super(specBuilder, modId, currentPath);
        }

        @Override
        protected Builder self() {
            return this;
        }

        public Builder defaultValue(boolean defaultValue) {
            this.defaultValue = defaultValue;
            this.defaultValueSet = true;
            return this;
        }

        @Override
        public BooleanConfigEntry build() {
            if (!defaultValueSet) {
                throw new IllegalStateException("Default value must be configured");
            }
            var value = prepareDefinition().define(key, defaultValue);
            return new BooleanConfigEntry(value, path(), translationKey(), comments, defaultValue);
        }
    }
}
