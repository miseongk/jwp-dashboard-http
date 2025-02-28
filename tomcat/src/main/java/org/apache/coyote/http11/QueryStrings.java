package org.apache.coyote.http11;

import java.util.HashMap;
import java.util.Map;

public class QueryStrings {

    private final Map<String, String> values = new HashMap<>();

    public void add(final String key, final String value) {
        values.put(key, value);
    }

    public Map<String, String> getValues() {
        return values;
    }
}
