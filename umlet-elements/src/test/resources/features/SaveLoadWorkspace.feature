Feature: Save Load
  To allow a developer to save his Diagram to disk and load it again at another time. When the Diagram has been loaded again it's elements should all be in the same position as when it was saved.

  Scenario: Load a UFX File that has been stored previously
    Given a Diagram with a Element positioned at '1000,1000'
    When the Diagram has been saved
    Then load the Diagram again
    Then verify that Element's positioned is '1000,1000' again
