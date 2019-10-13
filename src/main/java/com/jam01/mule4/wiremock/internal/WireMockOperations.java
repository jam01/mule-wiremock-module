package com.jam01.mule4.wiremock.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.common.JsonException;
import com.github.tomakehurst.wiremock.standalone.JsonFileMappingsSource;
import com.github.tomakehurst.wiremock.standalone.MappingFileException;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.github.tomakehurst.wiremock.stubbing.StubMappingCollection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

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
  public void stub(@Config WireMockConfiguration config,
                   @DisplayName("JSON Stub Mapping") String jsonMapping) {
    // See: com.github.tomakehurst.wiremock.standalone.JsonFileMappingsSource.loadMappingsInto
    try {
      StubMappingCollection stubCollection = Json.read(jsonMapping, StubMappingCollection.class);
      for (StubMapping mapping : stubCollection.getMappingOrMappings()) {
        mapping.setDirty(false);
        config.getMockServer().addStubMapping(mapping);
      }
    } catch (JsonException e) {
      throw new IllegalArgumentException(String.format("Error loading json mapping:\n%s", e.getErrors().first().getDetail()));
    }
  }

  @MediaType(value = ANY, strict = false)
  public void verify(@Config WireMockConfiguration config) {

  }

}
