package com.jam01.mule4.wiremock.internal;

import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.mule.runtime.extension.api.values.ValueBuilder.getValuesFor;

public class VerificationComparisonValueProvider implements ValueProvider {

  public static final String IS_AT_LEAST = "AT_LEAST";
  public static final String IS_AT_MOST = "AT_MOST";
  public static final String IS_EQUAL_TO = "EQUAL";
  private static final Map<String, String> comparisons =
      Collections.unmodifiableMap(new HashMap<String, String>() {

        {
          put(IS_AT_LEAST, "Is at least...");
          put(IS_AT_MOST, "Is at most...");
          put(IS_EQUAL_TO, "Is equal to...");
        }
      });
  private static final Set<Value> VALUES = getValuesFor(comparisons);

  @Override
  public Set<Value> resolve() throws ValueResolvingException {
    return VALUES;
  }
}
