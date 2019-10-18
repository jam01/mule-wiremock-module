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

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.annotation.values.OfValues;

import static com.jam01.mule.extension.wiremock.internal.VerificationComparisonValueProvider.IS_EQUAL_TO;

public class VerificationParameter {

  @Parameter
  @DisplayName("Comparison")
  @OfValues(VerificationComparisonValueProvider.class)
  @Optional(defaultValue = IS_EQUAL_TO)
  @Summary("The type of comparison to make between the expected and actual number of matched requests.")
  String comparison;

  @Parameter
  @DisplayName("Times")
  @Optional(defaultValue = "1")
  @Summary("The expected count of matched requests.")
  Integer times;

  @Parameter
  @DisplayName("JSON Verification Mapping")
  @Summary("A JSON representation of a WireMock RequestPattern as defined in http://wiremock.org/docs/verifying/")
  Object jsonMapping;
}
