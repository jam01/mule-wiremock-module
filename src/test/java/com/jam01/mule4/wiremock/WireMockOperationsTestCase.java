package com.jam01.mule4.wiremock;

import org.junit.Test;
import org.mule.extension.http.api.HttpResponseAttributes;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.mule.runtime.core.api.event.CoreEvent;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class WireMockOperationsTestCase extends MuleArtifactFunctionalTestCase {

  @Override
  protected String getConfigFile() {
    return "test-mule-config.xml";
  }

  @Test
  public void autoStubAndVerify() throws Exception {
    CoreEvent result = flowRunner("hello-world-flow").run();
    assertThat(((HttpResponseAttributes) result.getMessage().getAttributes().getValue()).getStatusCode(), is(200));
  }
}
