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
  public static final String IS_EQUAL_TO = "EQUAL_TO";
  public static final Set<Value> VALUES;

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
