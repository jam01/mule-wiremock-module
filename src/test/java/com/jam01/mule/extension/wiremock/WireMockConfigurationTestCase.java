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

import com.jam01.mule.extension.wiremock.internal.WireMockProvider;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.junit.Test;
import org.mule.runtime.core.api.util.IOUtils;
import org.mule.runtime.extension.api.runtime.config.ConfigurationInstance;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mule.runtime.core.api.connection.util.ConnectionProviderUtils.unwrapProviderWrapper;
import static org.mule.runtime.http.api.HttpConstants.HttpStatus.OK;
import static org.mule.test.module.extension.internal.util.ExtensionsTestUtils.getConfigurationInstanceFromRegistry;

public class WireMockConfigurationTestCase extends AbstractWireMockExtensionFunctionalTestCase {

  @Override
  protected String getConfigFile() {
    return "wiremock-configuration-config.xml";
  }

  @Test
  public void defaultLocationMappings() throws Exception {
    ConfigurationInstance config = getConfigurationInstanceFromRegistry("wiremockConfig", testEvent(), muleContext);
    WireMockProvider provider = (WireMockProvider) unwrapProviderWrapper(config.getConnectionProvider().get());

    assertThat(provider.connect().allStubMappings().getMappings().size(), is(1));
    assertThat(provider.connect().allStubMappings().getMappings().get(0).getRequest().getUrl(), is("/hello"));
  }

  @Test
  public void customLocationMappings() throws Exception {
    ConfigurationInstance config = getConfigurationInstanceFromRegistry("wiremockConfigCustomRoot", testEvent(), muleContext);
    WireMockProvider provider = (WireMockProvider) unwrapProviderWrapper(config.getConnectionProvider().get());

    assertThat(provider.connect().allStubMappings().getMappings().size(), is(1));
    assertThat(provider.connect().allStubMappings().getMappings().get(0).getRequest().getUrl(), is("/custom-hello"));
  }

  @Test
  public void helloWorldResponse() throws Exception {
    HttpResponse response = Request.Get("http://localhost:8080/hello").execute().returnResponse();

    assertThat(response.getStatusLine().getStatusCode(), is(OK.getStatusCode()));
    assertThat(IOUtils.toString(response.getEntity().getContent()), is("Hello world!"));
  }
}
