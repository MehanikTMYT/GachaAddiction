package xiaojin.gachaaddiction.config.entry;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.function.Predicate;

public class StringListConfigEntry extends ListConfigEntry<String> {
    public StringListConfigEntry(ModConfigSpec.ConfigValue<List<? extends String>> rawValue, List<String> path,
                                 String translationKey, List<String> comments, List<? extends String> defaultValue,
                                 Predicate<Object> elementValidator) {
        super(rawValue, path, translationKey, comments, List.copyOf(defaultValue), elementValidator,
                value -> value, value -> value);
    }

    public static Builder builder(ModConfigSpec.Builder specBuilder, String modId, List<String> currentPath) {
        return new Builder(specBuilder, modId, currentPath);
    }

    public static class Builder extends ListConfigEntry.Builder<String, StringListConfigEntry, Builder> {

        private Builder(ModConfigSpec.Builder specBuilder, String modId, List<String> currentPath) {
            super(specBuilder, modId, currentPath);
            codec(value -> value, value -> value);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public StringListConfigEntry build() {
            validateDefinition();
            var defaultValue = encodedDefaultValue();
            var value = prepareDefinition().defineList(key, defaultValue, () -> "", elementValidator);
            return new StringListConfigEntry(value, path(), translationKey(), comments, defaultValue, elementValidator);
        }
    }
}
