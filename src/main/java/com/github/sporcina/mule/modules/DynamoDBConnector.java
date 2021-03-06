/**
 * The software in this package is published under the terms of the Apache
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package com.github.sporcina.mule.modules;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.*;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.*;
import org.apache.commons.lang.StringUtils;
import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.annotations.*;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.modules.dynamodb.exceptions.TableNeverWentActiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * AWS DynamoDB Cloud Connector - provides MuleESB connectivity with the Amazon DynamoDB web service.  DynamoDB is a
 * fast, fully managed NoSQL database service (http://aws.amazon.com/dynamodb/)
 *
 * @author Sheldon Porcina
 *
 * notes:
 *
 * {@code @NotNull} is only used in cases where the cost of failure is high and where enforcement is not handled via other mechanisms.
 */
@Connector(name = "dynamodb",
        friendlyName="Amazon DynamoDB",
        schemaVersion = "1.0",
        minMuleVersion="3.4",
        description="Mule Cloud Connector for Amazon DynamoDB")
public class DynamoDBConnector {

    private static final Logger LOG = LoggerFactory.getLogger(DynamoDBConnector.class);
    private static AmazonDynamoDBClient dynamoDBClient;

    private static final String PAYLOAD = "#[payload]";
    private static final int TWENTY_SECONDS = 20 * 1000;


    /**
     * Configurable
     */
    @Configurable
    private String region;


    /**
     * Set the AWS region we are targeting
     *
     * @param region
     *         Defines the AWS region to target.  Use the strings provided in the com.amazonaws.regions.Regions class.
     *
     * @see com.amazonaws.regions.Regions
     */
    public void setRegion(@NotNull String region) {
        this.region = region;
    }


    /**
     * Get aws region we are targeting (e.g. US_WEST_1)
     */
    String getRegion() {
        return this.region;
    }


    @NotNull
    private Regions getRegionAsEnum() {
        return Regions.valueOf(getRegion());
    }


    @NotNull
    private static AmazonDynamoDBClient getDynamoDBClient() {
        return dynamoDBClient;
    }


    private static void setDynamoDBClient(@NotNull AmazonDynamoDBClient dynamoDBClient) {
        DynamoDBConnector.dynamoDBClient = dynamoDBClient;
    }


    @NotNull
    private static Boolean isDynamoDBClientConnected() {
        return getDynamoDBClient() != null;
    }


    /**
     * Connect to the DynamoDB service
     *
     * @param accessKey
     *         the access key provided to you through your Amazon AWS account
     * @param secretKey
     *         the secret key provided to you through your Amazon AWS account
     */
    @Connect
    // TODO: try this => @Default (value = Query.MILES) @Optional String unit
    public void connect(@ConnectionKey String accessKey, String secretKey) throws ConnectionException {

        if (StringUtils.isNotEmpty(accessKey) && StringUtils.isNotEmpty(secretKey)) {
            createDynamoDBClient(accessKey, secretKey);
        } else {
            createDynamoDBClient();
        }

        Region regionEnum = Region.getRegion(getRegionAsEnum());
        getDynamoDBClient().setRegion(regionEnum);
    }

    /**
     * Creates a DynamoDB client using the security values from the AWSCredentials.properties file
     *
     * @throws ConnectionException
     */
    private void createDynamoDBClient() throws ConnectionException {
        AWSCredentialsProvider credentialsProvider = new ClasspathPropertiesFileCredentialsProvider();
        try {
            credentialsProvider.getCredentials();
        } catch (AmazonClientException e) {
            LOG.warn("AWSCredentials.properties file was not found.  Attempting to acquire credentials from the default provider chain.");
            throw new ConnectionException(ConnectionExceptionCode.INCORRECT_CREDENTIALS, null, e.getMessage(), e);
        } catch (Exception e) {
            LOG.warn(e.getMessage() + "  Are you missing the AWSCredentials.properties file?");
            throw new ConnectionException(ConnectionExceptionCode.UNKNOWN, null, e.getMessage(), e);
        }

        try {
            setDynamoDBClient(new AmazonDynamoDBClient(credentialsProvider));
        } catch (Exception e) {
            throw new ConnectionException(ConnectionExceptionCode.UNKNOWN, null, e.getMessage(), e);
        }
    }

    /**
     * Creates a DynamoDB client using the security values passed in
     *
     * @param accessKey
     *         the access key provided to you through your Amazon AWS account
     * @param secretKey
     *         the secret key provided to you through your Amazon AWS account
     *
     * @throws ConnectionException
     */
    private void createDynamoDBClient(String accessKey, String secretKey) throws ConnectionException {
        try {
            AWSCredentials credentialsProvider = new BasicAWSCredentials(accessKey, secretKey);
            setDynamoDBClient(new AmazonDynamoDBClient(credentialsProvider));
        } catch (Exception e) {
            throw new ConnectionException(ConnectionExceptionCode.UNKNOWN, null, e.getMessage(), e);
        }
    }


    /**
     * Disconnect from DynamoDB
     */
    @Disconnect
    public void disconnect() {
        setDynamoDBClient(null);
    }


    /**
     * Are we connected to DynamoDB?
     */
    @NotNull
    @ValidateConnection
    public boolean isConnected() {
        return isDynamoDBClientConnected();
    }


    /**
     * A unique identifier for the connection, used for logging and debugging
     */
    @NotNull
    @ConnectionIdentifier
    public String connectionId() {
        return "AWS DynamoDB Mule Connector";
    }


    /**
     * Create a new table
     * <p/>
     * {@sample.xml ../../../doc/DynamoDB-connector.xml.sample dynamodb:create-table}
     *
     * @param tableName
     *         title of the table
     * @param readCapacityUnits
     *         dedicated read units per second
     * @param writeCapacityUnits
     *         dedicated write units per second
     * @param primaryKeyName
     *         the name of the primary key for the table
     * @param waitFor
     *         the number of minutes to wait for the table to become active
     *
     * @return the status of the table
     *
     * @throws TableNeverWentActiveException
     *         the table never became ACTIVE within the time allotted
     */
    @NotNull
    @Processor
    public String createTable(@NotNull final String tableName,
                              @NotNull final Long readCapacityUnits,
                              @NotNull final Long writeCapacityUnits,
                              @NotNull final String primaryKeyName,
                              @NotNull final Integer waitFor)
            throws TableNeverWentActiveException {

        try {
            return describeTable(tableName);
        } catch (ResourceNotFoundException e) {

            CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
                    .withKeySchema(new KeySchemaElement().withAttributeName(primaryKeyName).withKeyType(KeyType.HASH))
                    .withAttributeDefinitions(new AttributeDefinition().withAttributeName(primaryKeyName).withAttributeType(ScalarAttributeType.S))
                    .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(readCapacityUnits).withWriteCapacityUnits(writeCapacityUnits));

            CreateTableResult result = getDynamoDBClient().createTable(createTableRequest);

            waitForTableToBecomeAvailable(tableName, waitFor);

            return result.getTableDescription().getTableStatus().toString();
        }
    }


    /**
     * Delete a table
     * <p/>
     * {@sample.xml ../../../doc/DynamoDB-connector.xml.sample dynamodb:delete-table}
     *
     * @param tableName
     *         title of the table
     * @param waitFor
     *         the number of minutes to wait for the table to become active
     *
     * @return the status of the table
     *
     * @throws TableNeverWentActiveException
     *         the table never became ACTIVE within the specified period of time
     */
    @Processor
    public String deleteTable(final String tableName, final Integer waitFor) throws TableNeverWentActiveException {

        DeleteTableRequest deleteTableRequest = new DeleteTableRequest().withTableName(tableName);
        DeleteTableResult result = getDynamoDBClient().deleteTable(deleteTableRequest);

        waitForTableToBeDeleted(tableName, waitFor);

        return result.getTableDescription().getTableStatus().toString();
    }


    /**
     * Acquire information about a table
     * <p/>
     * {@sample.xml ../../../doc/DynamoDB-connector.xml.sample dynamodb:describe-table}
     *
     * @param tableName
     *         title of the table
     *
     * @return the state of the table
     *
     * @throws ResourceNotFoundException
     *         if the table was not found
     */
    @Processor
    public String describeTable(String tableName) throws ResourceNotFoundException {
        DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
        TableDescription description = getDynamoDBClient().describeTable(describeTableRequest).getTable();

        // The table could be in several different states: CREATING, UPDATING, DELETING, & ACTIVE.
        LOG.warn(tableName + " already exists and is in the state of " + description.getTableStatus());
        return description.getTableStatus();
    }


    /**
     * Save a document to a DynamoDB table
     * <p/>
     * {@sample.xml ../../../doc/DynamoDB-connector.xml.sample dynamodb:save-document}
     *
     * @param tableName
     *         the table to update
     * @param document
     *         the object to save to the table as a document.  If not explicitly provided, it defaults to PAYLOAD.
     *
     * @return Object the place that was stored
     */
    @Processor
    public Object saveDocument(final String tableName, @Optional @Default(PAYLOAD) final Object document) {
        DynamoDBMapper mapper = getDbObjectMapper(tableName);
        mapper.save(document);
        // the document is automatically updated with the data that was stored in DynamoDB
        return document;
    }


    /**
     * Acquire a document processor
     * <p/>
     * {@sample.xml ../../../doc/DynamoDB-connector.xml.sample dynamodb:get-document}
     *
     * @param tableName
     *         the name of the table to get the document from
     * @param template
     *         an object with the document data that DynamoDB will match against
     *
     * @return Object the document from the table
     */
    @Processor
    public Object getDocument(final String tableName, @Optional @Default(PAYLOAD) final Object template) {
        DynamoDBMapper mapper = getDbObjectMapper(tableName);
        return mapper.load(template);
    }


    /**
     * Update document processor
     * <p/>
     * {@sample.xml ../../../doc/DynamoDB-connector.xml.sample dynamodb:update-document}
     *
     * @param tableName
     *         the table to update
     * @param document
     *         the object to save to the table as a document.  If not explicitly provided, it defaults to PAYLOAD.
     *
     * @return Object the place that was stored
     */
    @Processor
    public Object updateDocument(final String tableName, @Optional @Default(PAYLOAD) final Object document) {
        DynamoDBMapperConfig config = new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.UPDATE);
        DynamoDBMapper mapper = getDbObjectMapper(tableName);
        mapper.save(document, config);

        // save does not return the modified document.  Just return the original.
        return document;
    }


    /**
     * Processor to delete a document
     * <p/>
     * {@sample.xml ../../../doc/DynamoDB-connector.xml.sample dynamodb:get-all-documents}
     *
     * @param tableName
     *         the name of the table to get the document from
     * @param template
     *         an object with the document data that DynamoDB will match against
     *
     * @return Object a list of all the documents
     */
    @Processor
    public Object getAllDocuments(String tableName, @Optional @Default(PAYLOAD) final Object template) {

        Class templateClass = template.getClass();

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        DynamoDBMapper mapper = getDbObjectMapper(tableName);
        return mapper.scan(templateClass, scanExpression);
    }


    /**
     * Processor to delete a document
     * <p/>
     * {@sample.xml ../../../doc/DynamoDB-connector.xml.sample dynamodb:delete-document}
     *
     * @param tableName
     *         the name of the table to get the document from
     * @param template
     *         an object with the document data that DynamoDB will match against
     */
    @Processor
    public void deleteDocument(final String tableName, @Optional @Default(PAYLOAD) final Object template) {
        DynamoDBMapper mapper = getDbObjectMapper(tableName);
        mapper.delete(template);
    }


    /**
     * Processor to delete a document
     * <p/>
     * {@sample.xml ../../../doc/DynamoDB-connector.xml.sample dynamodb:delete-all-documents}
     *
     * @param tableName
     *         the name of the table to get the document from
     * @param template
     *         the object to use as a document.  If not explicitly provided, it defaults to PAYLOAD.
     */
    @Processor
    public void deleteAllDocuments(String tableName, @Optional @Default(PAYLOAD) final Object template) {
        List<Object> documents = (List<Object>) getAllDocuments(tableName, template);
        DynamoDBMapper mapper = getDbObjectMapper(tableName);
        mapper.batchDelete(documents);
    }


    /**
     * Builds a database object mapper for a dynamodb table
     *
     * @param tableName
     *         the name of the table
     *
     * @return DynamoDBMapper a new DynamoDB mapper for the targeted table
     */
    private DynamoDBMapper getDbObjectMapper(String tableName) {
        DynamoDBMapperConfig.TableNameOverride override = new DynamoDBMapperConfig.TableNameOverride(tableName);
        DynamoDBMapperConfig config = new DynamoDBMapperConfig(override);
        return new com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper(getDynamoDBClient(), config);
    }


    // TODO: waitForTableToBecomeAvailable() & waitForTableToBeDeleted() are similar.  Combine them. - sporcina (Oct.13,2013)


    /**
     * Wait for the table to become active
     * <p/>
     * DynamoDB takes some time to create a new table, depending on the complexity of the table and the requested
     * read/write capacity.  Performing any actions against the table before it is active will result in a failure. This
     * method periodically checks to see if the table is active for the requested wait period.
     *
     * @param tableName
     *         the name of the table to create
     * @param waitFor
     *         number of minutes to wait for the table
     *
     * @throws TableNeverWentActiveException
     *         the table never became ACTIVE within the time allotted
     */
    private void waitForTableToBecomeAvailable(final String tableName, final Integer waitFor) throws TableNeverWentActiveException {

        LOG.info("Waiting for table " + tableName + " to become ACTIVE...");

        final long millisecondsToWaitFor = (waitFor * 60 * 1000);
        final long startTime = System.currentTimeMillis();
        final long endTime = startTime + millisecondsToWaitFor;

        while (System.currentTimeMillis() < endTime) {

            try {
                Thread.sleep(TWENTY_SECONDS);
            } catch (Exception e) { /*ignore sleep exceptions*/ }

            try {
                DescribeTableRequest request = new DescribeTableRequest().withTableName(tableName);
                TableDescription tableDescription = getDynamoDBClient().describeTable(request).getTable();

                String tableStatus = tableDescription.getTableStatus();
                LOG.info("  - current state: " + tableStatus);
                if (tableStatus.equals(TableStatus.ACTIVE.toString())) {
                    return;
                }

            } catch (AmazonServiceException ase) {
                if (!ase.getErrorCode().equalsIgnoreCase("ResourceNotFoundException")) {
                    throw ase;
                }
            }
        }

        throw new TableNeverWentActiveException("Table " + tableName + " never went active");
    }


    /**
     * Wait for the table to be deleted
     * <p/>
     * DynamoDB takes some time to delete a table.  This method periodically checks to see if the table is deleted for
     * the requested wait period.
     *
     * @param tableName
     *         the name of the table to create
     * @param waitFor
     *         number of minutes to wait for the table
     *
     * @throws TableNeverWentActiveException
     *         the table never became ACTIVE within the time allotted
     */
    private void waitForTableToBeDeleted(final String tableName, final Integer waitFor) throws TableNeverWentActiveException {

        LOG.info("Waiting for table " + tableName + " to be DELETED...");

        final long millisecondsToWaitFor = (waitFor * 60 * 1000);
        final long startTime = System.currentTimeMillis();
        final long endTime = startTime + millisecondsToWaitFor;

        while (System.currentTimeMillis() < endTime) {

            try {
                Thread.sleep(TWENTY_SECONDS);
            } catch (Exception e) { /*ignore sleep exceptions*/ }

            try {
                DescribeTableRequest request = new DescribeTableRequest().withTableName(tableName);
                TableDescription tableDescription = getDynamoDBClient().describeTable(request).getTable();
                String tableStatus = tableDescription.getTableStatus();
                LOG.info("  - current state: " + tableStatus);
            } catch (ResourceNotFoundException e) {
                // the table was successfully deleted
                return;
            }
        }

        throw new TableNeverWentActiveException("Table " + tableName + " was never deleted within the wait limit provided of " + waitFor + " minutes");
    }
}
