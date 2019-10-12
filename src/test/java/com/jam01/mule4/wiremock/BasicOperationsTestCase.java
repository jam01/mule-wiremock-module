package com.jam01.mule4.wiremock;

import org.junit.Test;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;

public class BasicOperationsTestCase extends MuleArtifactFunctionalTestCase {

  @Override
  protected String getConfigFile() {
    return "test-mule-config.xml";
  }

  @Test
  public void executeStubOperation() throws Exception {
    flowRunner("hello-world-flow").run();

  }

  // @Test
  public void executeVerifyOperation() throws Exception {

  }
}
