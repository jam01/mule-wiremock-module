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

import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;

//See:
//https://github.com/mulesoft/mule-http-connector/blob/1.5.9/src/main/java/org/mule/extension/http/internal/listener/HttpListener.java
//https://github.com/mulesoft/mule-http-connector/blob/1.5.9/src/main/java/org/mule/extension/http/api/listener/server/HttpListenerConfig.java
//https://github.com/mulesoft/mule-http-connector/blob/1.5.9/src/main/java/org/mule/extension/http/internal/listener/HttpListenerProvider.java
//https://github.com/mulesoft/mule-http-connector/blob/1.5.9/src/main/java/org/mule/extension/http/internal/request/HttpRequestOperations.java
//https://github.com/mulesoft/mule-http-connector/blob/1.5.9/src/main/java/org/mule/extension/http/internal/request/HttpRequesterConfig.java
//https://github.com/mulesoft/mule-http-connector/blob/1.5.9/src/main/java/org/mule/extension/http/internal/request/HttpRequesterProvider.java
@Xml(prefix = "wiremock")
@Extension(name = "wiremock")
@Configurations(WireMockConfiguration.class)
public class WireMockExtension {

}
