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
package com.jam01.mule4.wiremock.internal;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@Operations(WireMockOperations.class)
public class WireMockConfiguration {

  private WireMockServer mockServer;
  private WireMock mockClient;
  private boolean isStarted;

  @Parameter
  @Optional(defaultValue = "8080")
  private Integer port;


  public void startMockServer() {
    if (isStarted)
      return;

    mockServer = new WireMockServer(options().port(port));
    mockServer.start();

    isStarted = true;
  }

  public void stopMockServer() {
    if (isStarted)
      mockServer.stop();
  }

  public WireMockServer getMockServer() {
    return mockServer;
  }

  public WireMock getMockClient() {
    if (mockClient == null)
      mockClient = new WireMock(getMockServer());

    return mockClient;
  }
}
