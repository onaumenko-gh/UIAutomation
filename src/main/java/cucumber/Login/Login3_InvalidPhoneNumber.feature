@login
Feature: Grasshopper Smoke Test

  Scenario Outline: Verify Logging In with invalid phone number
    Given Grasshopper is installed on a clean device
    And user enters valid credentials
    When user enters invalid cell phone number
    Then invalid number error <ErrorMessage> should be shown

    Examples:
      |ErrorMessage    |
      | The number you entered doesn't look valid.             |