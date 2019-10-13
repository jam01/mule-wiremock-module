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
