package com.jam01.mule4.wiremock.internal;

import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import static org.mule.runtime.extension.api.values.ValueBuilder.getValuesFor;

public class VerificationComparisonValueProvider implements ValueProvider {

  public static final String IS_AT_LEAST = "AT_LEAST";
  public static final String IS_AT_MOST = "AT_MOST";
  public static final String IS_EQUAL_TO = "EQUAL";
  private static final Set<Value> VALUES;

  static {
    VALUES = getValuesFor(Collections.unmodifiableMap(new HashMap<String, String>(3) {

      {
        put(IS_AT_LEAST, "Is at least...");
        put(IS_AT_MOST, "Is at most...");
        put(IS_EQUAL_TO, "Is equal to...");
      }
    }));
  }

  @Override
  public Set<Value> resolve() throws ValueResolvingException {
    return VALUES;
  }
}
