package xiaojin.gachaaddiction.util;

import java.util.regex.Pattern;

/** Patterns shared by configuration validation and client resource loading. */
public final class GachaaPatterns {
    public static final Pattern TAG_PATTERN = Pattern.compile("^#[a-z0-9_.-]+:[a-z0-9_.-]+$");

    private GachaaPatterns() {}
}
