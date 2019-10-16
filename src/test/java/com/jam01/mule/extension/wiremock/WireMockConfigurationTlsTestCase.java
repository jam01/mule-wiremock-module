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

import org.junit.Rule;
import org.junit.Test;
import org.mule.functional.api.exception.ExpectedError;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.mule.runtime.core.api.event.CoreEvent;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.ThrowableMessageMatcher.hasMessage;

public class WireMockConfigurationTlsTestCase extends MuleArtifactFunctionalTestCase {

  @Rule
  public ExpectedError expectedError = ExpectedError.none();

  @Override
  protected String getConfigFile() {
    return "wiremock-configuration-tls-config.xml";
  }

  @Test
  public void defaultServerCertInsecureRequest() throws Exception {
    CoreEvent event = runFlow("test-insecure-request-default-cert-flow");

    assertThat(event.getMessage().getPayload().getValue(), is("Hello world!"));
  }

  @Test
  public void defaultServerCertSecureRequestFails() throws Exception {
    expectedError.expectCause(instanceOf(IOException.class));
    expectedError
        .expectCause(anyOf(hasMessage(containsString("General SSLEngine problem")),
                           hasMessage(containsString("PKIX path building failed"))));
    runFlow("test-secure-request-default-cert-flow");
  }

  @Test
  public void localhostServerCertSecureRequest() throws Exception {
    CoreEvent event = runFlow("test-secure-request-localhost-cert-flow");

    assertThat(event.getMessage().getPayload().getValue(), is("Hello world!"));
  }


  @Test
  public void authClientTrustedCertRequest() throws Exception {
    CoreEvent event = runFlow("test-auth-request-trusted-cert-flow");

    assertThat(event.getMessage().getPayload().getValue(), is("Hello world!"));
  }

  @Test
  public void authClientNoCertRequestFails() throws Exception {
    expectedError.expectCause(instanceOf(IOException.class));
    expectedError
        .expectCause(anyOf(hasMessage(containsString("General SSLEngine problem")),
                           hasMessage(containsString("PKIX path building failed"))));
    runFlow("test-secure-request-default-cert-flow");
  }
}
