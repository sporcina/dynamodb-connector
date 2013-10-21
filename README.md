
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

Building
========
1. Download the code and navigate to the root folder that houses the pom.xml file.
2. Enter "mvn clean install".

The project will build and execute the acceptance tests automatically.


Running Tests Independently
===========================
Windows Command Prompt or Mac Terminal
"mvn integration-test”

Within the IDE
Execute "JBehaveStories.java".  For example in

![Alt text](/readme_images/Execute_Stories_In_Intellij.png "Optional title")


Important Files
===============

./LICENSE.md:
The open source license text for this project.

./pom.xml:
A maven project descriptor that describes how to build this module.

./src/test/resources/org/mule/modules/jbehave/stories/StorageManagement.story
The JBehave acceptance tests.

./src/test/resources/mule-config.xml
The mule configuration for the tests.  Review this to understand how to leverage the connector from Mule.

./src/test/java/org/mule/modules/jbehave/JBehaveStories.java
Configuration and execution of the JBehave acceptance tests.  You can execute this in your IDE to run the tests.
Maven will automatically execute these test when building from the Windows command prompt or Mac Terminal window
(e.g. "mvn clean install").

./src/main/java/org/mule/modules/DynamoDBConnector.java
The Mule connector itself.



Resources
=========

An introduction to DynamoDB can be found here:
http://aws.amazon.com/dynamodb/

Everything you need to know about getting started with Mule can be found here:
http://www.mulesoft.org/documentation/display/MULE3INTRO/Home

