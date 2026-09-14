package xiaojin.gachaaddiction.config.entry;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.function.Predicate;

public abstract class RangedNumberConfigEntry<N extends Number & Comparable<N>> extends BasicConfigEntry<N> {
    private final N min;
    private final N max;

    protected RangedNumberConfigEntry(ModConfigSpec.ConfigValue<N> rawValue, List<String> path, String translationKey,
                                      List<String> comments, N defaultValue, N min, N max,
                                      Predicate<N> validator) {
        super(rawValue, path, translationKey, comments, defaultValue, validator, rawValue, rawValue::set);
        this.min = min;
        this.max = max;
    }

    public N min() {
        return min;
    }

    public N max() {
        return max;
    }

    public abstract static class Builder<N extends Number & Comparable<N>,
            E extends RangedNumberConfigEntry<N>, B extends Builder<N, E, B>>
            extends BasicConfigEntry.Builder<E, B> {
        protected N defaultValue;
        protected N min;
        protected N max;
        private boolean defaultValueSet;
        private boolean rangeSet;

        protected Builder(ModConfigSpec.Builder specBuilder, String modId, List<String> currentPath) {
            super(specBuilder, modId, currentPath);
        }

        public B defaultValue(N defaultValue) {
            this.defaultValue = defaultValue;
            this.defaultValueSet = true;
            return self();
        }

        public B range(N min, N max) {
            this.min = min;
            this.max = max;
            this.rangeSet = true;
            return self();
        }

        protected void validateRangeDefinition(Predicate<N> finiteValidator) {
            if (!defaultValueSet || !rangeSet) {
                throw new IllegalStateException("Default value and range must be configured");
            }
            if (!finiteValidator.test(min) || !finiteValidator.test(max) || min.compareTo(max) > 0) {
                throw new IllegalStateException("Config range must be valid and ordered");
            }
            if (!finiteValidator.test(defaultValue)
                    || defaultValue.compareTo(min) < 0 || defaultValue.compareTo(max) > 0) {
                throw new IllegalStateException("Default value must be within the configured range");
            }
        }
    }
}
