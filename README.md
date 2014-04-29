
Welcome
=======
The "DynamoDB Mule Connector" is a wrapper around the AWS Java libraries allowing Mule flows to perform CRUD operations
against AWS DynamoDB tables.  This wrapper allows you to easily interact with AWS tables through Mule.  You can also
leverage the connector directly through Java if you desire.


Setup
=====

To run the tests create the file "AwsCredentials.properties" in your test resources folder
(e.g. /src/test/resources/AwsCredentials.properties).  Place the following content in the file, adding your
AWS access and security keys:


        # Fill in your AWS Access Key ID and Secret Access Key
        # http://aws.amazon.com/security-credentials

        accessKey = <insert your key here>
        secretKey = <insert your key here>

If you do not know your keys, you can go here for more information:
http://docs.aws.amazon.com/AWSSecurityCredentials/1.0/AboutAWSCredentials.html#AccessKeys

Building
========
1. Download the code and navigate to the root folder that houses the pom.xml file.
2. Enter "mvn clean install".

![Alt text](/readme_images/Building_From_Terminal.png "Building from the Mac Terminal")

The project will build and execute the acceptance tests automatically.


Running Tests Independently
===========================
Windows Command Prompt or Mac Terminal
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

