@texts @MMS @ignored
Feature: MMS

  Scenario Outline: Verify sending new MMS message with existent taken image
    Given Grasshopper is installed on a clean device
    And user logs in with default credentials
    When user navigates to Texts screen
    And sends new MMS message with existent image from <numberFrom> to <numberTo>
    And message from <numberFrom> is displayed without New icon

    Examples:
      | numberFrom                                    | numberTo                |
      | (617) 249-0540                                | (617) 221-3553              |