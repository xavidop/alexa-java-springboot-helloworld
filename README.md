# Alexa Skill with Spring Boot

You can build a custom skill for Alexa by extending a servlet that accepts requests from and sends responses to the Alexa service in the cloud.

This project will walk through how to build a Alexa Skill with Spring Boot and Http Servlet mapping example.
 
Servlet mapping can be achieved either by using `ServletRegistrationBean` in Spring Boot as well as using Spring annotations. 
In this example we are going to use the `ServletRegistrationBean` class to register the Alexa Servlet as a Spring bean.

## Prerequisites

Here you have the technologies used in this project
1. Java 1.8
2. Alexa Skill Kit 2.29.0
3. Spring Boot 2.5.0.RELEASE
4. Maven 3.6.1
5. IntelliJ IDEA
6. ngrok

## Structure of the project
Find below the structure of this project:

```bash
  ├────src
  │    └───main
  │        ├───java
  │        │   └───com
  │        │       └───xavidop
  │        │           └───alexa
  │        │               ├───configuration
  │        │               ├───handlers
  │        │               ├───interceptors
  │        │               ├───localization
  │        │               ├───properties
  │        │               └───servlet
  │        │
  │        └───resources
  │                └───locales
  │                application.properties
  │                log4j2.xml
  └── pom.xml
```

These are the main folders and files of this project:
* **configuration**: this folder has the `WebConfig` class which will register the Alexa Http Servlet.
* **handlers**: all the Alexa handlers. They will be process all Alexa requests.
* **properties**: here you can find the `PropertiesUtils` class that read Spring `application.propeties` file.
* **interceptors**: loggers and localization interceptors.
* **localization**: Manager that will manage i18n.
* **servlet**: the entry point of POST Requests is here. This is the `AlexaServlet`.
* **resources**: Alexa, Spring and Log4j2 configurations.
* **pom.xml**: dependencies of this project.

## Maven dependencies

These are the dependencies used in this example. You can find them in `pom.xml` file:

* Spring Boot:
```xml
   <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.5.RELEASE</version>
  </parent>
```

* Alexa Skill Kit:
```xml
  <dependency>
      <groupId>com.amazon.alexa</groupId>
      <artifactId>ask-sdk</artifactId>
      <version>2.29.0</version>
  </dependency>
```

* Spring Boot starter web:
```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
```

* Log4j2:
```xml
  <dependency>
      <groupId>org.apache.logging.log4j</groupId>
      <artifactId>log4j-api</artifactId>
      <version>2.13.1</version>
  </dependency>
  <dependency>
      <groupId>org.apache.logging.log4j</groupId>
      <artifactId>log4j-core</artifactId>
      <version>2.13.1</version>
  </dependency>
```

* Spring Boot Maven build plug-in:
```xml
  <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

## The Alexa Http Servlet

Thanks to Alexa Http Servlet Support that you can find in the [Alexa official GitHub repository](https://github.com/alexa/alexa-skills-kit-sdk-for-java/tree/2.0.x/ask-sdk-servlet-support) and its  `SkillServlet` class we can register it with Spring Boot using `ServletRegistrationBean` as following.

The `SkillServlet` class registers the skill instance from the SkillBuilder object, and provides a doPost method which is responsible for deserialization of the incoming request, verification of the input request before invoking the skill, and serialization of the generated response.

Our `AlexaServlet` class, located in the `servlet` folder, extends `SkillServlet` and after its registrations as a servlet, it will be our main entry point. 

This is how this class looks like:

```java
    public class AlexaServlet extends SkillServlet {
    
        public AlexaServlet() {
            super(getSkill());
        }
    
        private static Skill getSkill() {
            return Skills.standard()
                    .addRequestHandlers(
                            new CancelandStopIntentHandler(),
                            new HelloWorldIntentHandler(),
                            new HelpIntentHandler(),
                            new LaunchRequestHandler(),
                            new SessionEndedRequestHandler(),
                            new FallbackIntentHandler(),
                            new ErrorHandler())
                    .addExceptionHandler(new MyExceptionHandler())
                    .addRequestInterceptors(
                            new LogRequestInterceptor(),
                            new LocalizationRequestInterceptor())
                    .addResponseInterceptors(new LogResponseInterceptor())
                    // Add your skill id below
                    //.withSkillId("[unique-value-here]")
                    .build();
        }
    
    }
```

It will receive all POST requests from Alexa and will send them to a specific handler, located in `handlers` folders, that can manage those requests.

## Registering the Alexa Http Servlet as Spring Beans using ServletRegistrationBean

`ServletRegistrationBean` is used to register Servlets. We need to create a bean of this class in `WebConfig`, our Spring Configuration class. 
The most relevant methods of the `ServletRegistrationBean` class that we used in this project are:
* `setServlet`: Sets the servlet to be registered. In our case, `AlexaServlet`.
* `addUrlMappings`: Add URL mappings for the Servlet. We used `/alexa`.
* `setLoadOnStartup`: Sets priority to load Servlet on startup. It is not as important as the two methods above because we only have one http Servlet in this example.

The `WebConfig` class is where we register the Alexa Http Servlet. This is how we register the servlet:

```Java
  @Bean
  public ServletRegistrationBean<HttpServlet> alexaServlet() {

      loadProperties();

      ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
      servRegBean.setServlet(new AlexaServlet());
      servRegBean.addUrlMappings("/alexa/*");
      servRegBean.setLoadOnStartup(1);
      return servRegBean;
  }
```
## Setting properties

The servlet must meet certain requirements to handle requests sent by Alexa and adhere to the Alexa Skills Kit interface standards. 
For more information, see Host a Custom Skill as a Web Service in the [Alexa Skills Kit technical documentation](https://developer.amazon.com/es-ES/docs/alexa/custom-skills/host-a-custom-skill-as-a-web-service.html).

In this example you have 4 properties that you can set in `application.properties` file:

* server.port: the port that the Spring Boot app will be use.
* com.amazon.ask.servlet.disableRequestSignatureCheck: disable/enable security.
* com.amazon.speech.speechlet.servlet.timestampTolerance: the maximum gap between the timestamps of the request and the current local time of execution. In miliseconds.
* javax.net.ssl.keyStore: if the first property is set to `false` then you have to specify the path of your keystore file.
* javax.net.ssl.keyStorePassword: if the first property is set to `false` then you have to specify the password of your keystore.

## Build the Skill with Spring Boot

As it is a maven project, you can build the Spring Boot application running this command:

```bash
  mvn clean package
```

## Run the Skill with Spring Boot

Run the AlexaSkillAppStarter.java class as Java application i.e. go to Run→ Run as → Java Application

Or, you can use
```bash
mvn spring-boot:run
```

After executing the main class, you can send Alexa POST requests to http://localhost:8080/alexa.

## Debug the Skill with Spring Boot

For debugging the Spring boot app as Java application i.e. go to Debug→ Debug as → Java Application

Or, if you use IntelliJ IDEA, you can do a right click in Main method of `AlexaSkillAppStarter` class:

![image](https://xavidop.github.io/assets/img/blog/tutorials/alexa-springboot/debug.png)

After executing the main class in debug mode, you can send Alexa POST requests to http://localhost:8080/alexa and debug the Skill.

## Test requests locally

I'm sure you already know the famous tool call [Postman](https://www.postman.com/). REST APIs have become the new standard in providing a public and secure interface for your service. Though REST has become ubiquitous, it's not always easy to test. Postman, makes it easier to test and manage HTTP REST APIs. Postman gives us multiple features to import, test and share APIs, which will help you and your team be more productive in the long run.

After run your application you will have an endpoint available at http://localhost:8080/alexa. With Postman you can emulate any Alexa Request. 

For example, you can test a `LaunchRequest`:

```json
  {
    "version": "1.0",
    "session": {
      "new": true,
      "sessionId": "amzn1.echo-api.session.[unique-value-here]",
      "application": {
        "applicationId": "amzn1.ask.skill.[unique-value-here]"
      },
      "user": {
        "userId": "amzn1.ask.account.[unique-value-here]"
      },
      "attributes": {}
    },
    "context": {
      "AudioPlayer": {
        "playerActivity": "IDLE"
      },
      "System": {
        "application": {
          "applicationId": "amzn1.ask.skill.[unique-value-here]"
        },
        "user": {
          "userId": "amzn1.ask.account.[unique-value-here]"
        },
        "device": {
          "supportedInterfaces": {
            "AudioPlayer": {}
          }
        }
      }
    },
    "request": {
      "type": "LaunchRequest",
      "requestId": "amzn1.echo-api.request.[unique-value-here]",
      "timestamp": "2020-03-22T17:24:44Z",
      "locale": "en-US"
    }
  }

```

Pay attention with the timestamp field of the request to accomplish with the property `com.amazon.speech.speechlet.servlet.timestampTolerance`.

## Test requests directly from Alexa

ngrok is a very cool, lightweight tool that creates a secure tunnel on your local machine along with a public URL you can use for browsing your local site or APIs.

When ngrok is running, it listens on the same port that you’re local web server is running on and proxies external requests to your local machine

From there, it’s a simple step to get it to listen to your web server. Say you’re running your local web server on port 8080. In terminal, you’d type in: `ngrok http 8080`. This starts ngrok listening on port 8080 and creates the secure tunnel:

![image](https://xavidop.github.io/assets/img/blog/tutorials/alexa-springboot/tunnel.png)

So now you have to go to [Alexa Developer console](https://developer.amazon.com/alexa/console/ask), go to your skill > endpoints > https, add the https url generated above followed by /alexa. Eg: https://fe8ee91c.ngrok.io/alexa.

Select the My development endpoint is a sub-domain.... option from the dropdown and click save endpoint at the top of the page.

Go to Test tab in the Alexa Developer Console and launch your skill.

The Alexa Developer Console will send a HTTPS request to the ngrok endpoint (https://fe8ee91c.ngrok.io/alexa) which will route it to your skill running on Spring Boot server at http://localhost:8080/alexa.


## Conclusion 

This example can be useful for all those developers who do not want to host their code in the cloud or do not want to use AWS Lambda functions. This is not a problem since, as you have seen in this example, Alexa gives you the possibility to create skills in different ways. I hope this example project is useful to you.

That's all folks!

Happy coding!
