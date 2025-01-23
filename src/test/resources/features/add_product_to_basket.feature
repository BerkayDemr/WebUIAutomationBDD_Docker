Feature: Add product to basket

  Scenario Outline: Add a product to the basket
    Given the user is on the login page
    When the user logs in
    And the user searches for "<product>"
    And the user filters products with price range "<minPrice>" to "<maxPrice>"
    And the user selects a random product
    And the user adds the product of the lowest-rated seller to the basket
    Then the basket should contain the selected product
    And the user clears the basket

    Examples:
      | product       | minPrice | maxPrice |
      | Cep Telefonu  | 3000     | 5000     |
