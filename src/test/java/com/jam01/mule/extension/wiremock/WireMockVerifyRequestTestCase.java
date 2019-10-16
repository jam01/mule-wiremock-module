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
package com.jam01.mule.extension.wiremock;

import org.apache.http.client.fluent.Request;
import org.junit.Rule;
import org.junit.Test;
import org.mule.functional.api.exception.ExpectedError;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;

public class WireMockVerifyRequestTestCase extends MuleArtifactFunctionalTestCase {

  @Rule
  public ExpectedError expectedError = ExpectedError.none();

  @Override
  protected String getConfigFile() {
    return "wiremock-verify-request-config.xml";
  }

  @Test
  public void verifyRequest() throws Exception {
    String uri = "http://localhost:8080/hello";
    Request.Get(uri).execute().returnResponse();
    Request.Get(uri).execute().returnResponse();
    runFlow("verify-request-flow");
  }

  @Test
  public void verifyRequestFails() throws Exception {
    expectedError.expectCause(instanceOf(AssertionError.class))
        .expectMessage(containsString("Expected at least one request matching:"));
    runFlow("verify-request-flow");
  }

}

