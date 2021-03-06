
Welcome
=======
The "DynamoDB Mule Connector" is a wrapper around the AWS Java libraries allowing Mule flows to perform CRUD operations
against AWS DynamoDB tables.  If you don't use Mule, you can also leverage the connector directly through Java if you desire.


Setup
=====

These instructions are a simple introduction to acquiring and running the project.  It assumes that you are using
[Maven](http://maven.apache.org) to manage your dependencies.  If [Anypoint Studio](http://www.mulesoft.org/download-mule-esb-community-edition) is your environment of choice, you can skip these
instructions and jump to the detailed hands-on walk-through on integrating and using this connector in Anypoint Studio, located at
https://github.com/sporcina/dynamodb-connector/blob/master/doc/sample.md.

If you plan to consume this connector within your own Maven project, all you have to do is add this dependency to your pom.xml:
 
         <dependency>
             <groupId>com.github.sporcina.mule.modules</groupId>
             <artifactId>dynamodb-connector</artifactId>
             <version>1.0</version>
         </dependency>
         
If you are using a different dependency framework, you may find the command for integrating this connector under the "Dependency Information"
section of the [Maven Central Repo entry](http://search.maven.org/#artifactdetails|com.github.sporcina.mule.modules|dynamodb-connector|1.0|mule-module) for this project. 

Building
========

If you do not want too use the released version of this project via Maven, you can build the project independently by
following these instructions.

1. Download the code from this repository
2. Add your AWS Keys

    Before building the project you'll need to ensure the appropriate AWS credentials are in place.  These credentials are
    used when executing the automated acceptance tests.  Create the file "AwsCredentials.properties" in your test
    resources folder (e.g. /src/test/resources/AwsCredentials.properties).  Place the following content in the file, adding
    your AWS access and security keys:
    
    
            # Fill in your AWS Access Key ID and Secret Access Key
            # http://aws.amazon.com/security-credentials
    
            accessKey = <insert your key here>
            secretKey = <insert your key here>
    
    If you do not know your keys, you can go here for more information:
    http://docs.aws.amazon.com/AWSSecurityCredentials/1.0/AboutAWSCredentials.html#AccessKeys
    
    If you'd prefer not to manage your credentials in a property file, you could alternatively add them to the connector
    config in /src/test/resources/mule-config.xml.  Just update the following:
    
            <!-- see com.amazonaws.regions.Regions for the enumerated region names -->
            <dynamodb:config region="US_WEST_1"
                            accessKey="<insert your key here>"
                            secretKey="<insert your key here>"/>
                        
3. Navigate to the root folder that houses the pom.xml file.
4. Enter "mvn clean install".

![Alt text](/readme_images/Building_From_Terminal.png "Building from the Mac Terminal")

This will build the project and execute the acceptance tests automatically.  Please note that the tests will create a
new table in dynamoDB under your account and interact with it.


Running Tests Independently
===========================
Execute "mvn integration-test” in the root folder of the project.

![Alt text](/readme_images/Running_Tests_From_Terminal.png "Running acceptance tests from Mac Terminal")

Within your IDE
Execute "DynamoDBConnectorTestCase.java" or "JBehaveStories.java".  Both are provided because JBehave was used to drive
the development of this connector, yet some test environments require only JUnit.  Each test suite is conceptually identical with
small variations in syntax.  By default, "DynamoDBConnectorTestCases.java" executes when you build from the command
prompt or terminal.  In your IDE (e.g. IntelliJ) you can right-click on either of these files and run or debug it:

![Alt text](/readme_images/Execute_Stories_In_IntelliJ.png "Executing the acceptance tests from Intelli-J")

The tests will create a new table titled "DynamoDbConnectorTestTable_toDelete" & exercise it with documents.  Creating a
new DynamoDB table takes time, so to save some, the test that deletes the table is disabled using @Ignore.  Additionally,
JUnit does not guarantee the order of the tests.  So the table deletion could occur before the other tests execute.  Ultimately,
the proper way to execute these tests would be to include the creation and deletion of the table within each test to make
them independent of each other.  There are other solutions like using @Before/@After or @FixMethodOrder annotations to fix
the order of the tests.  These solutions are generally discouraged since they introduce inter-test dependencies that adds
complexity to the code.  For small projects this would likely not be an issue.  For larger projects, it could become a
development tax over time.


Important Files
===============

*./LICENSE.md* <br/>
The open source license text for this project.

*./pom.xml* <br/>
A maven project descriptor that describes how to build this module.

*./src/test/resources/org/mule/modules/jbehave/stories/StorageManagement.story* <br/>
The JBehave acceptance tests.

*./src/test/resources/mule-config.xml* <br/>
The mule configuration for the tests.  Review this to understand how to leverage the connector from Mule.

*./src/test/java/org/mule/modules/jbehave/JBehaveStories.java* <br/>
*./src/test/java/org/mule/modules/DynamoDBConnectorTestCase.java* <br/>
Configuration and execution of the acceptance tests.  You can execute either of these files in your IDE to run the tests.
You can run either test suite, which ever your prefer.  Running both is not necessary.  Maven will automatically execute
the acceptance tests when building from the Windows command prompt or Mac Terminal window.
(e.g. "mvn clean install").

*./src/main/java/org/mule/modules/DynamoDBConnector.java* <br/>
The DynamoDB Mule connector itself.



Resources
=========

An introduction to DynamoDB can be found here:
http://aws.amazon.com/dynamodb/

Everything you need to know about getting started with Mule can be found here:
http://www.mulesoft.org/documentation/display/MULE3INTRO/Home

Icons used for this connector can be found here:
http://aws.amazon.com/architecture/icons/

Installation and use instructions for Maven can be found here:
http://maven.apache.org

