/*
 * Copyright 2019 Jose Montoya
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jam01.mule.extension.wiremock.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.client.CountMatchingStrategy;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.common.JsonException;
import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.github.tomakehurst.wiremock.stubbing.StubMappingCollection;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;
import static com.jam01.mule.extension.wiremock.internal.VerificationComparisonValueProvider.*;

public class WireMockOperations {

  private static final String WHEN_PARAM_GROUP = "Given scenario... when request... then respond...";
  private static final String VERIFY_PARAM_GROUP = "Verify that request...";
  private static final Logger LOGGER = LoggerFactory.getLogger(WireMockOperations.class);

  public void addStub(@Config WireMockConfiguration config,
                      @Connection WireMock wireMock,
                      @ParameterGroup(name = WHEN_PARAM_GROUP) StubParameter param) {
    if (param.jsonMapping == null)
      return;

    // See:
    // https://github.com/tomakehurst/wiremock/blob/2.25.1/src/main/java/com/github/tomakehurst/wiremock/standalone/JsonFileMappingsSource.java
    // available under SPDX-License-Identifier: Apache-2.0
    try {
      StubMappingCollection stubCollection = read(param.jsonMapping, StubMappingCollection.class);
      for (StubMapping mapping : stubCollection.getMappingOrMappings()) {
        mapping.setDirty(false);
        wireMock.register(mapping);
      }
    } catch (JsonException e) {
      throw new IllegalArgumentException(String.format("Error loading json mapping:\n%s", e.getErrors().first().getDetail()));
    }
  }

  public void verifyRequest(@Config WireMockConfiguration config,
                            @Connection WireMock wireMock,
                            @ParameterGroup(name = VERIFY_PARAM_GROUP) VerificationParameter param) {
    if (param.jsonMapping == null)
      return;

    CountMatchingStrategy matchingStrategy = getCountMatchingStrategy(param.comparison, param.times);

    try {
      RequestPattern requestPattern = read(param.jsonMapping, RequestPattern.class);
      wireMock.verifyThat(matchingStrategy, RequestPatternBuilder.like(requestPattern));
    } catch (JsonException e) {
      throw new IllegalArgumentException(String.format("Error loading json mapping:\n%s", e.getErrors().first().getDetail()));
    }
  }

  // See: https://github.com/tomakehurst/wiremock/blob/2.25.1/src/main/java/com/github/tomakehurst/wiremock/common/Json.java
  // available under SPDX-License-Identifier: Apache-2.0
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

    throw new IllegalArgumentException(
                                       String.format("Unrecognized comparison value %s, valid values are %s", comparison,
                                                     VALUES.stream().map(Value::getId).collect(Collectors.joining(", "))));
  }
}
