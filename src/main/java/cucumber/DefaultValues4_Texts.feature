@DefaultValues
Feature: Grasshopper Smoke Test

  Scenario: Verify Texts Dropbox Default Values
    Given Grasshopper is installed on a clean device
    And user logs in with default credentials
    When user navigates to Texts screen
    Then each item from Texts dropdown can be selected
