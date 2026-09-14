package xiaojin.gachaaddiction.config.entry;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.function.Predicate;

public class StringConfigEntry extends ConfigEntry<String> {
    public StringConfigEntry(ModConfigSpec.ConfigValue<String> rawValue, List<String> path, String translationKey,
                             List<String> comments, String defaultValue, Predicate<String> validator) {
        super(rawValue, path, translationKey, comments, defaultValue, value -> value, value -> value, validator);
    }

    public static Builder builder(ModConfigSpec.Builder specBuilder, String modId, List<String> currentPath) {
        return new Builder(specBuilder, modId, currentPath);
    }

    public static class Builder extends ConfigEntry.Builder<String, StringConfigEntry, Builder> {

        private Builder(ModConfigSpec.Builder specBuilder, String modId, List<String> currentPath) {
            super(specBuilder, modId, currentPath);
            codec(value -> value, value -> value);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public StringConfigEntry build() {
            validateDefinition();
            var value = prepareDefinition().define(key, defaultValue,
                    input -> input instanceof String string && validator.test(string));
            return new StringConfigEntry(value, path(), translationKey(), comments, defaultValue, validator);
        }
    }
}
