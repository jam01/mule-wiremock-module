package com.jam01.mule4.wiremock.internal;

import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

public class StubParameter {

  @Parameter
  @DisplayName("JSON Stub Mapping")
  Object jsonMapping;
}
