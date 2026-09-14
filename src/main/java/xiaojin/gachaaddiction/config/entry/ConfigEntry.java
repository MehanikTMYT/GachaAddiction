package xiaojin.gachaaddiction.config.entry;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConfigEntry<V> extends BasicConfigEntry<V> {
    private final Function<String, V> parse;
    private final Function<V, String> encode;

    public ConfigEntry(ModConfigSpec.ConfigValue<String> rawValue, List<String> path, String translationKey,
                       List<String> comments, V defaultValue, Function<String, V> decoding, Function<V, String> encode,
                       Predicate<V> validator) {
        super(rawValue, path, translationKey, comments, defaultValue, validator,
                () -> decoding.apply(rawValue.get()), value -> rawValue.set(encode.apply(value)));
        this.parse = Objects.requireNonNull(decoding);
        this.encode = Objects.requireNonNull(encode);
    }

    public String encode(V v) {
        V value = isValid(v) ? v : defaultValue();
        try {
            return encode.apply(value);
        } catch (RuntimeException ignored) {
            return encode.apply(defaultValue());
        }
    }

    @SuppressWarnings("unused")
    public V parseOrDefault(String string) {
        return tryParse(string).orElseGet(this::defaultValue);
    }

    public Optional<V> tryParse(String string) {
        try {
            V value = parse.apply(string);
            return isValid(value) ? Optional.of(value) : Optional.empty();
        } catch (RuntimeException ignored) {
            return Optional.empty();
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public abstract static class Builder<V, E extends ConfigEntry<V>, B extends Builder<V, E, B>>
            extends BasicConfigEntry.Builder<E, B> {
        protected V defaultValue;
        protected boolean defaultValueSet;
        protected Function<String, V> decoding;
        protected Function<V, String> encode;
        protected Predicate<V> validator = value -> true;

        protected Builder(ModConfigSpec.Builder specBuilder, String modId, List<String> currentPath) {
            super(specBuilder, modId, currentPath);
        }

        public B defaultValue(V defaultValue) {
            this.defaultValue = Objects.requireNonNull(defaultValue);
            this.defaultValueSet = true;
            return self();
        }

        public B codec(Function<String, V> decoding, Function<V, String> encode) {
            this.decoding = Objects.requireNonNull(decoding);
            this.encode = Objects.requireNonNull(encode);
            return self();
        }

        public B validator(Predicate<V> validator) {
            this.validator = Objects.requireNonNull(validator);
            return self();
        }

        protected void validateDefinition() {
            if (!defaultValueSet || decoding == null || encode == null) {
                throw new IllegalStateException("Default value and codec must be configured");
            }
            if (!validator.test(defaultValue)) {
                throw new IllegalStateException("Default value must pass validation");
            }
            String encodedDefault = Objects.requireNonNull(encode.apply(defaultValue));
            V decodedDefault = decoding.apply(encodedDefault);
            if (!Objects.equals(defaultValue, decodedDefault) || !validator.test(decodedDefault)) {
                throw new IllegalStateException("Default value must survive codec conversion");
            }
        }
    }
}
