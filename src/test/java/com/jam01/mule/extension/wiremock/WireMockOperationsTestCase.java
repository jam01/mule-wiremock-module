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
package com.jam01.mule.extension.wiremock;

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
