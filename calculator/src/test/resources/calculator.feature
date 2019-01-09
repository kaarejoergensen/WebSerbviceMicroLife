Feature: No Numbers
  Is no numbers correct?

  Scenario: No numbers return 0
    Given An empty string
    When Calculating sum
    Then I should return 0