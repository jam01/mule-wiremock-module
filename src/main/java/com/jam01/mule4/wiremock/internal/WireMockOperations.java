package com.jam01.mule4.wiremock.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.common.JsonException;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.github.tomakehurst.wiremock.stubbing.StubMappingCollection;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;
import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

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
                   @DisplayName("JSON Stub Mapping") Object jsonMapping) {
    if (jsonMapping == null)
      return;

    StubMappingCollection stubCollection = fromObject(jsonMapping);

    // See: com.github.tomakehurst.wiremock.standalone.JsonFileMappingsSource.loadMappingsInto
    try {
      for (StubMapping mapping : stubCollection.getMappingOrMappings()) {
        mapping.setDirty(false);
        config.getMockServer().addStubMapping(mapping);
      }
    } catch (JsonException e) {
      throw new IllegalArgumentException(String.format("Error loading json mapping:\n%s", e.getErrors().first().getDetail()));
    }
  }

  // See: com.github.tomakehurst.wiremock.common.Json.read(java.lang.String, java.lang.Class<T>)
  private StubMappingCollection fromObject(@Nonnull Object object) {
    try {
      if (object instanceof InputStream) {
        return Json.getObjectMapper().readValue((InputStream) object, StubMappingCollection.class);
      } else if (object instanceof byte[]) {
        return Json.getObjectMapper().readValue((byte[]) object, StubMappingCollection.class);
      } else if (object instanceof CharSequence) {
        String trimmed = ((CharSequence) object).toString().trim();
        return Json.getObjectMapper().readValue(trimmed, StubMappingCollection.class);
      } else {
        throw new IllegalArgumentException(String.format("Unable to parse StubMapping from %s class", object.getClass()));
      }
    } catch (JsonProcessingException processingException) {
      throw JsonException.fromJackson(processingException);
    } catch (IOException ioe) {
      return throwUnchecked(ioe, StubMappingCollection.class);
    }
  }

  @MediaType(value = ANY, strict = false)
  public void verify(@Config WireMockConfiguration config) {

  }

}
