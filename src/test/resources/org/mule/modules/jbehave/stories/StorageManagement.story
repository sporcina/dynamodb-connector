Meta:

Narrative:
As a DynamoDB mule connector user
I want to create a storage space
So that I can manage my data


Scenario: DynamoDB repository created
Meta:
!--@skip
Given I have DynamoDB credentials
When I request a new repository
Then I have a repository that is ACTIVE


Scenario: Document is saved
Meta:
!--@skip
Given I have a repository that is ACTIVE
When I save a document
Then the document is saved in the repository


Scenario: Document is updated
Meta:
!--@skip
Given I have a repository that is ACTIVE
And I have a saved document
When I update the document
Then the document is updated in the repository


Scenario: All documents are retrieved
Meta:
!-- @skip
Given I have a repository that is ACTIVE
And I have a saved document
When I get all documents
Then all documents are returned from the repository


Scenario: Document is deleted
Meta:
!--  @skip
Given I have a repository that is ACTIVE
And I have a saved document
When I delete the document
Then the document is deleted in the repository


Scenario: All documents are deleted
Meta:
!--@skip
Given I have a repository that is ACTIVE
And I have a saved document
When I delete all the documents
Then there are no documents in the repository


Scenario: DynamoDB repository is deleted
Meta:
@skip
Given I have DynamoDB credentials
And I have a repository that is ACTIVE
When I request to delete the repository
Then the repository does not exist