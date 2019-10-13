package com.jam01.mule4.wiremock.internal;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.values.OfValues;

import static com.jam01.mule4.wiremock.internal.VerificationComparisonValueProvider.IS_AT_LEAST;

public class VerificationParameter {

  @Parameter
  @DisplayName("Comparison")
  @OfValues(VerificationComparisonValueProvider.class)
  @Optional(defaultValue = IS_AT_LEAST)
  String comparison;

  @Parameter
  @DisplayName("Value")
  @Optional(defaultValue = "1")
  Integer value;

  @Parameter
  @DisplayName("JSON Verification Mapping")
  Object jsonMapping;
}
