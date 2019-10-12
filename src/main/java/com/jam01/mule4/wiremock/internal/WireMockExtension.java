package com.jam01.mule4.wiremock.internal;

import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;

@Xml(prefix = "wiremock")
@Extension(name = "wiremock")
@Configurations(WireMockConfiguration.class)
public class WireMockExtension {

}
