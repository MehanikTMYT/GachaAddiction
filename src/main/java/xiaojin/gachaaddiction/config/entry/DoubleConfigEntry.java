package xiaojin.gachaaddiction.config.entry;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class DoubleConfigEntry extends RangedNumberConfigEntry<Double> {
    public DoubleConfigEntry(ModConfigSpec.DoubleValue rawValue, List<String> path, String translationKey,
                             List<String> comments, double defaultValue, double min, double max) {
        super(rawValue, path, translationKey, comments, defaultValue, min, max,
                value -> Double.isFinite(value) && value >= min && value <= max);
    }

    public static Builder builder(ModConfigSpec.Builder specBuilder, String modId, List<String> currentPath) {
        return new Builder(specBuilder, modId, currentPath);
    }

    public static class Builder extends RangedNumberConfigEntry.Builder<Double, DoubleConfigEntry, Builder> {

        private Builder(ModConfigSpec.Builder specBuilder, String modId, List<String> currentPath) {
            super(specBuilder, modId, currentPath);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public DoubleConfigEntry build() {
            validateRangeDefinition(Double::isFinite);
            var value = prepareDefinition().defineInRange(key, defaultValue, min, max);
            return new DoubleConfigEntry(value, path(), translationKey(), comments, defaultValue, min, max);
        }
    }
}
