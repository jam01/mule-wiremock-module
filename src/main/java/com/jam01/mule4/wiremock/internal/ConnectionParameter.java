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

import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.http.api.HttpConstants;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;

public class ConnectionParameter {

  /**
   * Protocol to use for communication. Valid values are HTTP and HTTPS. Default value is HTTP. When using HTTPS the HTTP
   * communication is going to be secured using TLS / SSL. If HTTPS was configured as protocol then the user needs to configure at
   * least the keystore in the tls:context child element of this listener-config.
   */
  @Parameter
  @Optional(defaultValue = "HTTP")
  @Expression(NOT_SUPPORTED)
  @Placement(order = 1)
  private HttpConstants.Protocol protocol;

  /**
   * Host where the requests will be sent.
   */
  @Parameter
  @Example("0.0.0.0")
  @Expression(NOT_SUPPORTED)
  @Placement(order = 2)
  private String host;

  /**
   * Port where the requests will be received.
   */
  @Parameter
  @Example("8080")
  @Expression(NOT_SUPPORTED)
  @Placement(order = 3)
  private Integer port;

  @Parameter
  @Optional(defaultValue = "src/test/resources")
  @Expression(NOT_SUPPORTED)
  @DisplayName("Resources Root Directory")
  @Summary("The relative path to the WireMock resources root directory.")
  private String resources;

  public HttpConstants.Protocol getProtocol() {
    return protocol;
  }

  public String getHost() {
    return host;
  }

  public Integer getPort() {
    return port;
  }

  public String getResources() {
    return resources;
  }
}
