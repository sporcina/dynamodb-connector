Meta:

Narrative:
As a DynamoDB mule connector user
I want to create a storage space
So that I can manage my data


Scenario: DynamoDB repository created
Given I have DynamoDB credentials
When I request a new repository
Then I have a repository that is ACTIVE


Scenario: Document saved to repository
Given I have a repository that is ACTIVE
When I save a document
Then the document is saved in the repository

Scenario: Document is updated
Given I have a repository that is ACTIVE
And I have a saved document
When I update the document
Then the document is updated in the repository