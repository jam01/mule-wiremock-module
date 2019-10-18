# Mule WireMock Module

When writing integration software for distributed systems the most critical part to test is the interaction between those systems. Obviously this makes integration tests vital. Traditionally we would test against live systems, but having a local instance of an upstream system or a dedicated test instance can be expensive and/or difficult to coordinate its state between tests.

Instead we can opt to verify the interactions against test doubles, also known as narrow integration tests. WireMock is a simulator for HTTP-based APIs that allows us to spin up a HTTP server and configure stubbed responses for particular requests. 

With the Mule WireMock Module we can spin up an in-process HTTP Server and run our integration tests against it. WireMock gives us a couple of advantages over MUnit's 'Mock When' processor:

1. We're actually sending requests over the wire so we get the confidence that HTTP Requester configuration and subsequent processors are correct.   
2. WireMock supports conditional responses based on scenarios, this way we can more easily test behavior that changes the external system's state as it executes.
3. With an actual HTTP Server serving requests it's simpler to implement your application if the real services are not yet available or it's difficult to control their state.


Resources worth checking out:

* [Martin Fowler: Integration Test](https://martinfowler.com/bliki/IntegrationTest.html)    
* [Ham Vocke: The Practical Test Pyramid - Integration Tests](https://martinfowler.com/articles/practical-test-pyramid.html#IntegrationTests)


## WireMock Key Features
-	HTTP response stubbing, matchable on URL, header and body content patterns
-	Request verification
-	Fault injection
-	Stateful behaviour simulation
-	Configurable response delays

Full documentation can be found at [wiremock.org](http://wiremock.org/ "wiremock.org")

## Using the connector

Add this dependency to your application pom.xml

```
<dependency>
    <groupId>com.jam01.mule</groupId>
    <artifactId>mule-wiremock-module</artifactId>
    <version>0.4.3-SNAPSHOT</version>
    <classifier>mule-plugin</classifier>
</dependency>
```

See the module's own [MUnit integration test](./src/test/munit/munit-integration-test-suite.xml) for a complete and commented example on how to use the connector.

For more details on verifying requests and stubbing responses, see [WireMock Stubbing](http://wiremock.org/docs/stubbing), [WireMock Verifying](http://wiremock.org/docs/stubbing), and [WireMock RequestMatching](http://wiremock.org/docs/request-matching/)
