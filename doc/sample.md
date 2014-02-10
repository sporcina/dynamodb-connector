[Purpose](#purpose)

[Prerequisites](#prerequisites)

[Step 1: Install DynamoDB Mule Connector from Update Site](#step1)

[Step 2: Create a Demo Project](#step2)

[Step 3: Create a Table](#step3)

[Step 4: Save a Document](#step4)

[Step 5: Run the Demo project](#step5)

[Step 6: Test the Flows](#step6)

[Flow XML](#flowXML)

[Other Resources](#other)





### Purpose



This document provides detailed instructions on how to acquire the DynamoDB connector and demonstrates how to create a simple demo application that leverages it.



### Prerequisites



In order to build and run this project you'll need:



* an [AWS account](http://aws.amazon.com/)

* [MuleStudio](http://www.mulesoft.org/download-mule-esb-community-edition)

* TBD: NOT NEEDED for this documentation?? - (optional) [Intelli-J IDEA](http://www.jetbrains.com/idea/download/) or [Eclipse](http://www.eclipse.org/downloads/)






### Step 1: Install DynamoDB Mule Connector

## From the Update Site (TBD)



*    In Mule Studio select **Help** \> **Install New Software...**.

*    Select **MuleStudio Cloud Connectors Update Site** in **Work With** drop-down.

*    Check two items from Community folder: **Mule DynamoDB Connector Mule Studio Extension** and click **Next**.

*    Follow installation steps.

## From a local build

*    Navigate to your DynamoDB root folder (e.g "~/git_repos/dynamodb-connector" -or- "c:\git_repos\dynamodb-connector")
*    Execute the this command: "mvn clean package -Ddevkit.studio.package.skip=false"
*    In Mule Studio select **Help** \> **Install New Software...**.
*    Select **Add**, then select **Archive**
*    Navigate to the **target** folder under your DynamoDB root folder
*    Select **UpdateSite.zip**, then select **Open**
*    In the **name** field, type in **DynamoDB Cloud Connector**, then select **OK**
*    Select the checkbox next to **Mule Cloud Connectors**
*    Select **Next** until you get the **License Agreement**
*    Accept the **License Agreement** and select **Finish**
*    If you are prompted with a warning that the connector is unsigned, just select **OK**
*    If prompted, restart Mule Studio

### Step 2: Create a Demo Project



*    Run Mule Studio and select **File \> New \> Mule Project** menu item.

*    Type **DynamoDBDemo** as a project name and click **Finish**.

![Create a new Demo project](images/Step2-1.png)

*   Double click on src/main/app/Demo.xml to open it, select Global Elements tab in view (located in the bottom of the next image).

![Create a new Demo project](images/Step2-2.png)

*   Click the Create button
*   In the "Filter" field, type **Amazon** to search for the connector, then under "Cloud Connectors" select **Amazon DynamoDB**.
*   Click OK

![Create a new Demo project](images/Step2-3.png)

*   In the **Global Element Properties** windows, enter your access and secret keys
*   Enter "US_WEST_1" as your region
*   Select **Test Connection...** to confirm that your configuration works before continuing.  If there is a problem, double-check the keys you entered.

Your screen should look like this:

![Create a new Demo project](images/Step2-4.png)

*   Select **OK**
*   Save your project using the **File \> Save All** menu item.


### Step 3: Create a Table

Now we're going to create some Mule work flows that demonstrate some of the DyanamoDB Connector features.  We will use the
HTTP endpoint to invoke our flows, and save some fake customer data to a new DynamoDB table.  If you wish you can use a different endpoint, though they are not covered in this
tutorial.

*   Select the **Message Flow** tab
*   From the Palette tool bar on the right side of the screen, under the "Endpoints" section, drag and drop an **HTTP** endpoint in to the Message Flow canvas
*   Rename the flow from "dynamodbdemoFlow1" to "CreateTableFlow" by right-clicking on the text and selecting **Rename**
*   Select the HTTP endpoint to display its property window at the bottom of the screen
*   In the path field, type in **createTable**

Your screen should should look like this:

![Create a table](images/Step3-1.png)

*   From the Palette tool bar, under the "Cloud Connectors" section, drag and drop an **Amazon DynamoDB** endpoint in to the flow
*   Select the **Amazon DynamoDB** endpoint and change the following properties:
    *   In the **Config Reference**, select "Amazon_DynamoDB"
    *   Ensure that **Create table** is selected under the Operation field
    *   For the **Table Name**, type "MyTestTable"
    *   Enter '1' for the Read and Write Capacity Units
    *   Type 'num' for the Primary Key Name
    *   Enter '10' for the Wait For value

Your **Amazon DynamoDB** endpoint should look like this:

![Create a table](images/Step3-2.png)

*   To test your flow:
    *   Select your flow to highlight it
    *   From the **Run** menu, select **Run As** then **Mule Application**
    *   Launch your web browser and navigate to "http://localhost:8081/createTable"
    *   Now go back to Mule Studio and select the **Console** tab at the bottom the screen.

You'll see log entries showing the DynamoDB Connector working.  Creating a new table in DynamoDB takes time.  Watch the
status logs until you see the message that the current state is **ACTIVE**.  It should look like this:

![Create a table - console window](images/Step3-4.png)

If you login to AWS and view your DynamoDB Management Console website, you'll see the newly created table.

![Create a table - DynamoDB Management Console website](images/Step3-5.png)

*   Terminate your Mule Application instance in Mule Studio by selecting the button shaped like a "red square" on the right side of the screen in the **Console** tab noted earlier

### Step 4: Save a Document

Now that we have a table, we can save information to it.  For the purpose of this demo, we'll save some fake customer data.

*   Create a new flow by again drag-and-dropping the HTTP endpoint from the Palette tool bar, to the Message Flow canvas, below "CreateTableFlow".
*   Rename it to read **SaveDocumentFlow**
*   Type **saveDocument** in the path field

Your screen will look like this:

![Save a Document - created a new flow](images/Step4-1.png)

*   Again, drag and drop the **Amazon DynamoDB** Cloud Connector from the Palette tool bar, in to the "saveDocument" flow
*   Set the "Config Reference" in the properties for the DynamoDB Cloud Connector to "Amazon_DynamoDB"
*   Change the Operation to "Save document"
*   In the **Table Name** field, type "MyTestTable".  This is the name of the table we create earlier.

![Save a Document - added DynamoDB connector](images/Step4-2.png)

We've got the basics the flow setup, which is very similar to the previous flow we created.  Now we'll add new elements to
the flow which will help us create and observe data to save to the table.

*

### Step 5: Run the Demo project

### Step 6: Test the Flows

[Flow XML](#flowXML)

[Other Resources](#other)