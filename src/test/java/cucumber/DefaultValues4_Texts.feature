@defaultValues
Feature: Default dropdown values

  Scenario: Verify Call Dropbox Default Values
    Given Grasshopper is installed on a clean device
    And user logs in with default credentials
    When user navigates to Call screen
    Then each item from Call dropdown can be selected

