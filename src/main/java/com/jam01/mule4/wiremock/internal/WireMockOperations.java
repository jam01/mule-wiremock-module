package com.jam01.mule4.wiremock.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.client.CountMatchingStrategy;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.common.JsonException;
import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.github.tomakehurst.wiremock.stubbing.StubMappingCollection;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.values.OfValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;
import static com.jam01.mule4.wiremock.internal.VerificationComparisonValueProvider.IS_AT_LEAST;
import static com.jam01.mule4.wiremock.internal.VerificationComparisonValueProvider.IS_AT_MOST;
import static com.jam01.mule4.wiremock.internal.VerificationComparisonValueProvider.IS_EQUAL_TO;

public class WireMockOperations {

  private static final String WHEN_PARAM_GROUP = "When request... then return...";
  private static final String VERIFY_PARAM_GROUP = "Verify that...";
  private static final Logger LOGGER = LoggerFactory.getLogger(WireMockOperations.class);

  public void startServer(@Config WireMockConfiguration config) {
    config.startMockServer();
  }

  public void stopServer(@Config WireMockConfiguration config) {
    config.stopMockServer();
  }

  public void addStub(@Config WireMockConfiguration config,
                      @ParameterGroup(name = WHEN_PARAM_GROUP) StubParameter param) {
    if (param.jsonMapping == null)
      return;

    StubMappingCollection stubCollection = read(param.jsonMapping, StubMappingCollection.class);

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

  public void verifyRequest(@Config WireMockConfiguration config,
                            @ParameterGroup(name = VERIFY_PARAM_GROUP) VerificationParameter param) {
    if (param.jsonMapping == null)
      return;

    CountMatchingStrategy matchingStrategy = getCountMatchingStrategy(param.comparison, param.value);
    RequestPattern requestPattern = read(param.jsonMapping, RequestPattern.class);

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
