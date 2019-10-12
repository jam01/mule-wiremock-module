package com.jam01.mule4.wiremock.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Config;

public class WireMockOperations {

  @MediaType(value = ANY, strict = false)
  public void start(@Config WireMockConfiguration config) {
    config.start();
  }

  @MediaType(value = ANY, strict = false)
  public void stop(@Config WireMockConfiguration config) {
    config.stop();
  }

  @MediaType(value = ANY, strict = false)
  public void stub(@Config WireMockConfiguration config) {

  }

  @MediaType(value = ANY, strict = false)
  public void verify(@Config WireMockConfiguration config) {

  }

}
