package com.jam01.mule4.wiremock.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.client.CountMatchingStrategy;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.common.JsonException;
import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.github.tomakehurst.wiremock.stubbing.StubMappingCollection;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.values.OfValues;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;
import static com.jam01.mule4.wiremock.internal.VerificationComparisonValueProvider.*;
import static org.mule.runtime.extension.api.values.ValueBuilder.getValuesFor;


public class WireMockOperations {

  public void start(@Config WireMockConfiguration config) {
    config.startMockServer();
  }

  public void stop(@Config WireMockConfiguration config) {
    config.stopMockServer();
  }

  public void stub(@Config WireMockConfiguration config,
                   @DisplayName("JSON Stub Mapping") Object jsonMapping) {
    if (jsonMapping == null)
      return;

    StubMappingCollection stubCollection = read(jsonMapping, StubMappingCollection.class);

    // See: com.github.tomakehurst.wiremock.standalone.JsonFileMappingsSource.loadMappingsInto
    try {
      for (StubMapping mapping : stubCollection.getMappingOrMappings()) {
        mapping.setDirty(false);
        config.getMockServer().addStubMapping(mapping);
      }
    } catch (JsonException e) {
      throw new IllegalArgumentException(String.format("Error loading json mapping:\n%s", e.getErrors().first().getDetail()));
    }
  }

  public void verify(@Config WireMockConfiguration config,
                     @DisplayName("Comparison") @OfValues(VerificationComparisonValueProvider.class) @Optional(
                         defaultValue = IS_AT_LEAST) String comparison,
                     @DisplayName("Value") @Optional(
                         defaultValue = "1") Integer value,
                     @DisplayName("JSON Verification Mapping") Object jsonMapping) {
    if (jsonMapping == null)
      return;

    CountMatchingStrategy matchingStrategy = getCountMatchingStrategy(comparison, value);
    RequestPattern requestPattern = read(jsonMapping, RequestPattern.class);

    config.getMockClient().verifyThat(matchingStrategy, RequestPatternBuilder.like(requestPattern));
  }

  // See: com.github.tomakehurst.wiremock.common.Json.read(java.lang.String, java.lang.Class<T>)
  private static <T> T read(@Nonnull Object object, Class<T> clazz) {
    try {
      if (object instanceof InputStream) {
        return Json.getObjectMapper().readValue((InputStream) object, clazz);
      } else if (object instanceof byte[]) {
        return Json.getObjectMapper().readValue((byte[]) object, clazz);
      } else if (object instanceof CharSequence) {
        String trimmed = ((CharSequence) object).toString().trim();
        return Json.getObjectMapper().readValue(trimmed, clazz);
      } else {
        throw new IllegalArgumentException(String.format("Unable to parse %s from %s object", clazz, object.getClass()));
      }
    } catch (JsonProcessingException processingException) {
      throw JsonException.fromJackson(processingException);
    } catch (IOException ioe) {
      return throwUnchecked(ioe, clazz);
    }
  }

  private static CountMatchingStrategy getCountMatchingStrategy(String comparison, Integer value) {
    switch (comparison) {
      case IS_AT_LEAST:
        return new CountMatchingStrategy(CountMatchingStrategy.GREATER_THAN_OR_EQUAL, value);
      case IS_AT_MOST:
        return new CountMatchingStrategy(CountMatchingStrategy.LESS_THAN_OR_EQUAL, value);
      case IS_EQUAL_TO:
        return new CountMatchingStrategy(CountMatchingStrategy.EQUAL_TO, value);
    }

    throw new IllegalStateException("This should never happen!");
  }
}
