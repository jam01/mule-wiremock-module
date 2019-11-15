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
import org.junit.Test;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.mule.runtime.extension.api.runtime.config.ConfigurationInstance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mule.runtime.core.api.connection.util.ConnectionProviderUtils.unwrapProviderWrapper;
import static org.mule.test.module.extension.internal.util.ExtensionsTestUtils.getConfigurationInstanceFromRegistry;

public class WireMockResetRequestsTestCase extends MuleArtifactFunctionalTestCase {

  @Override
  protected String getConfigFile() {
    return "wiremock-reset-requests.xml";
  }

  @Test
  public void resetRequests() throws Exception {
    Request.Get("http://localhost:8080/hello").execute().returnResponse();

    ConfigurationInstance config = getConfigurationInstanceFromRegistry("wiremockConfig", testEvent(), muleContext);
    Object wireMock = unwrapProviderWrapper(config.getConnectionProvider().get()).connect();

    assertThat(getServerEventsSize(wireMock), is(1));

    runFlow("reset-requests-flow");
    assertThat(getServerEventsSize(wireMock), is(0));
  }

  // TODO: 11/14/19 find if there's a better way to do this
  private int getServerEventsSize(Object wireMock)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method getServeEventsMethod = wireMock.getClass().getDeclaredMethod(
                                                                        "getServeEvents");
    List eventsList = (List) getServeEventsMethod.invoke(wireMock);

    return eventsList.size();
  }
}
