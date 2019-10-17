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

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.lifecycle.InitialisationException;
import org.mule.runtime.api.lifecycle.Lifecycle;
import org.mule.runtime.api.tls.TlsContextFactory;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.RefName;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.mule.runtime.api.i18n.I18nMessageFactory.createStaticMessage;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.core.api.lifecycle.LifecycleUtils.initialiseIfNeeded;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.SECURITY_TAB;
import static org.mule.runtime.http.api.HttpConstants.Protocol.HTTP;
import static org.mule.runtime.http.api.HttpConstants.Protocol.HTTPS;

// See: https://github.com/mulesoft/mule-http-connector/blob/1.5.9/src/main/java/org/mule/extension/http/internal/listener/HttpListenerProvider.java
// available under SPDX-License-Identifier: CPAL-1.0
public class WireMockProvider implements CachedConnectionProvider<WireMock>, Lifecycle {

  private static final Logger LOGGER = LoggerFactory.getLogger(WireMockProvider.class);
  private static final String TLS_CONFIGURATION = "TLS Configuration";
  private WireMockServer mockServer;
  private WireMock mock;

  @ParameterGroup(name = ParameterGroup.CONNECTION)
  private ConnectionParameter connectionParams;

  /**
   * Reference to a TLS config element. This will enable HTTPS for this config.
   */
  @Parameter
  @Optional
  @Expression(NOT_SUPPORTED)
  @DisplayName(TLS_CONFIGURATION)
  @Placement(tab = SECURITY_TAB)
  private TlsContextFactory tlsContext;

  @RefName
  private String configName;

  @Override
  public ConnectionValidationResult validate(WireMock connection) {
    if (mockServer.isRunning())
      return ConnectionValidationResult.success();

    return ConnectionValidationResult.failure(String.format("Server on host %s and port %s is stopped.",
                                                            mockServer.getOptions().bindAddress(),
                                                            mockServer.getOptions().portNumber()),
                                              new ConnectionException("Server stopped."));
  }

  @Override
  public void initialise() throws InitialisationException {
    if (connectionParams.getProtocol().equals(HTTP) && tlsContext != null) {
      throw new InitialisationException(createStaticMessage("TlsContext cannot be configured with protocol HTTP. "
          + "If you defined a tls:context element in your wiremock:config then you must set protocol=\"HTTPS\""), this);
    }

    if (tlsContext != null) {
      initialiseIfNeeded(tlsContext);
    }

    WireMockConfiguration options = options()
        .usingFilesUnderDirectory(connectionParams.getResources())
        .bindAddress(connectionParams.getHost());

    if (connectionParams.getProtocol() == HTTPS) {
      // forcing regular HTTP to a random port as WireMock will always start one regardless
      // See: https://github.com/tomakehurst/wiremock/blob/2.25.1/src/main/java/com/github/tomakehurst/wiremock/jetty9/JettyHttpServer.java
      options.dynamicPort();
      options.httpsPort(connectionParams.getPort());
      if (tlsContext != null) {
        if (tlsContext.isKeyStoreConfigured())
          options.keystoreType(tlsContext.getKeyStoreConfiguration().getType())
              .keystorePath(tlsContext.getKeyStoreConfiguration().getPath())
              .keystorePassword(tlsContext.getKeyStoreConfiguration().getPassword());

        if (tlsContext.isTrustStoreConfigured())
          options.needClientAuth(true)
              .trustStoreType(tlsContext.getTrustStoreConfiguration().getType())
              .trustStorePath(tlsContext.getTrustStoreConfiguration().getPath())
              .trustStorePassword(tlsContext.getTrustStoreConfiguration().getPassword());
      }
    } else {
      options.port(connectionParams.getPort());
    }

    mockServer = new WireMockServer(options);
    mock = new WireMock(mockServer);
  }

  @Override
  public void start() throws MuleException {
    mockServer.start();
  }

  @Override
  public WireMock connect() throws ConnectionException {
    return mock;
  }

  @Override
  public void disconnect(WireMock connection) {
    // Should we do something?
  }

  @Override
  public void dispose() {
    mockServer.shutdownServer();
  }

  @Override
  public void stop() throws MuleException {
    mockServer.stop();
  }
}
