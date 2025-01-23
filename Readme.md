# Web UI Test Framework with Maven, Cucumber, and Docker

## 📄 Project Overview

This project is a web UI testing framework built using:

- **Java** for the core implementation.
- **Maven** for dependency management and build automation.
- **Cucumber** for Behavior-Driven Development (BDD).
- **Selenium WebDriver** for browser automation.
- **Docker Compose** for setting up a scalable test environment.

The framework supports:

- Automated web UI tests written in Gherkin syntax.
- Running tests on local browsers or Selenium Grid using Docker.
- Generating detailed test reports using Allure.

---

## 🚀 Features

- Cross-browser testing with **[WebDriverManager](https://github.com/bonigarcia/webdrivermanager)**.
- Behavior-Driven Development with **Cucumber**.
- Parallel test execution with Docker Selenium Grid.
- Containerized test execution with **Docker Compose**.
- Allure reporting for detailed test results.

---

## 📂 Project Structure

```
├── src
│   ├── main
│   └── test
│       ├── java
│       │   ├── config                     # Configuration classes (e.g., TestConfig.java)
│       │   ├── pages                      # Page Object Model classes
│       │   ├── runner                     # Cucumber test runner
│       │   ├── steps                      # Step definition classes
│       │   └── utils                      # Utility classes (e.g., ScreenshotUtils)
│       ├── resources
│       │   ├── features                   # Gherkin feature files  
│       │   ├── junit-platform.properties  # Parallel test property 
├── pom.xml                                # Maven configuration
├── docker-compose.yml                     # Docker Compose configuration
├── Dockerfile                             # Docker configuration
└── allure-results                         # Test result files
└── README.md                              # Project documentation
```

---

## 🛠️ Prerequisites

Before running the project, ensure the following are installed on your system:

1. **Java 23** (or later)
2. **Maven 3.9.x**
3. **Docker** and **Docker Compose**

Verify installations:

```bash
java -version
mvn -version
docker --version
docker-compose --version
```

---

## ⚙️ Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/BerkayDemr/WebUIAutomationBDD_Docker
cd WebUIAutomationBDD_Docker
```

### 2. Install Dependencies

Run the following Maven command to install all project dependencies:

```bash
mvn clean install
```

### 3. Run Tests Locally

To run tests on your local machine:

```bash
mvn test                  #(defualt will run on local chrome browser)
mvn test "-Dbrowser=edge" #(to use any other browser)
```

### 4. Run Tests with Docker Compose

To run tests in a containerized Selenium Grid environment:

```bash
docker-compose up --build #Recreates images and initialises containers
docker-compose up -d      #Starts images in the background without recreates
```

The results will be available in the `allure-results` directory.

**Note :** When run with Docker compose, it may request sms verification in the user login process. Therefore, user login can be bypassed especially when you want to run the test with docker compose.

---

## 🧬 Writing Tests

### Feature Files

Feature files are located in `src/test/resources/features` and written in **Gherkin** syntax. Example:

```gherkin
Feature: Add product to basket

  Scenario: Add a specific product to the shopping basket
    Given I am on the login page
    When I log in with valid credentials
    And I search for a product
    Then I should see the product in the basket
```

### Step Definitions

Step definitions for the above feature are located in `src/test/java/steps`.

Example:

```java
@Given("I am on the login page")
public void i_am_on_the_login_page() {
    loginPage.navigateToLoginPage();
}
```

---

## 📊 Generating Test Reports

This project uses **Allure** for reporting. After running the tests:

1. Install Allure if not already installed:
   ```bash
   npm install -g allure-commandline --save-dev
   ```
2. Generate the report:
   ```bash
   allure serve allure-results
   ```

---

## 🐳 Docker Compose Details

The `docker-compose.yml` file defines:

- A **Selenium Grid Hub**.
- Multiple browser nodes (e.g., Chrome and Firefox).
- A volume for sharing test results.

Example configuration:

```yaml
version: "3.7"
services:
  selenium-hub:
    image: selenium/hub:latest
    container_name: selenium-hub
    ports:
      - "4444:4444"

  chrome:
    image: selenium/node-chrome:latest
    depends_on:
      - selenium-hub
    environment:
      - HUB_HOST=selenium-hub
    volumes:
      - /dev/shm:/dev/shm

  firefox:
    image: selenium/node-firefox:latest
    depends_on:
      - selenium-hub
    environment:
      - HUB_HOST=selenium-hub
    volumes:
      - /dev/shm:/dev/shm
```

Run the following to start the Selenium Grid and execute tests:

```bash
docker-compose up --build
```

---

## 💡 Troubleshooting

1. **"WebDriver başlatılırken bir hata oluştu"**

    - Ensure the `grid` property is correctly set in your Docker environment.
    - Verify the `docker-compose.yml` file and Selenium Grid are running.

2. **Docker WSL Errors**

    - Ensure Docker Desktop is installed and WSL2 is enabled.
    - Check that virtualization is enabled in your system's BIOS.

3. **Tests not running in parallel**

    - Ensure the `parallel` property is set in your Maven configuration.

---

## 📧 Contact

For any questions or issues, feel free to reach out:

- Email: [berkaydmr93@gmail.com](mailto\:berkaydmr93@gmail.com)

