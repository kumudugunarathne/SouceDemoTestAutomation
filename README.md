# Playwright-Based Automated Testing Framework
 
# Overview
 
 This project is an automated testing framework designed using Playwright, TestNG, and Java to perform end-to-end testing of a web application. The framework covers login validation, product filtering, cart verification, checkout workflow, API testing, visual regression, and performance testing.
 
 
 # Features
 
 Dynamic Login Validation with multiple users.
 
 Data-driven testing using JSON/CSV.
 
 Product filtering and cart verification with dynamic product selection.
 
 Multi-step checkout workflow with form validation and price calculation.
 
 API Testing & UI-API Validation to verify data consistency.
 
 Visual and Performance Testing for UI consistency and load time tracking.
 
 Logging, Screenshots, and CI/CD integration for better reporting and test execution.
 
 
 # Test Scenarios
 
 Dynamic Login Validation
 
 Login with different users, including valid and locked accounts.
 
 Validate error messages and detect slow-loading behavior.
 
 Take screenshots on failure and log response times.
 
 
 # Requirements:
 
 Data-driven testing (CSV/JSON as input).
 
 Test fails if login takes more than 5 seconds.
 
 
 Complex Product Filtering & Cart Verification
 
 Sort products (Low to High Price) and validate sorting.
 
 Add randomly selected products to the cart.
 
 Verify cart prices and remove the most expensive item.
 
 
 
 # Requirements:
 
 Dynamic product selection using randomization.
 
 Assertions to validate sorting and cart updates.
 
 JSON structured logs for cart data.
 
 
 
 Multi-Step Checkout Workflow
 
 Perform checkout with form validation.
 
 Enter incorrect details (e.g., missing ZIP) and validate error messages.
 
 Validate total price (including tax) before confirming the order.
 
 
 
 # Requirements:
 
 Dynamic test data generation (JavaFaker).
 
 Screenshot capture before confirming the order.
 
 Ensure price calculations include tax/discounts.
 
 
 
# API Testing & UI-API Validation
 
 GET request to https://reqres.in/api/users?page=2.
 
 Pick a random user and search for them on the UI.
 
 Validate that API data matches UI data.
 
 
 # Requirements:
 
 Playwright’s API testing features.
 
 Compare API vs UI data.
 
 Implement error handling for API failures.
 
 
 
 # Visual & Performance Testing
 
 Perform visual regression testing on the product listing page.
 
 Compare UI snapshots to detect layout changes.
 
 Measure page load times (fail if >3 seconds).
 
 
 
 # Requirements:
 
 Visual comparison with Playwright’s snapshot feature.
 
 Performance metrics logging.
 
 Retry mechanism for network issues.
 
 
 
 # Bonus Features:
 
 Parallel execution for faster results.
 
 Test Report Generation with structured logs and screenshots.
 
 
 
 # Tech Stack 
 
 Framework: Playwright, TestNG
 
 Programming Language: Java
 
 Data Handling: OpenCSV, JSON
 
 Test Data Generation: JavaFaker
 
 Screenshots & Image Comparison: ru.yandex.qatools.ashot, org.openpnp
 
 File Handling: commons-io
 
 
# Setup & Installation
 
 Prerequisites:
 
 Install Java 11+
 
 Install Maven
 
 Install Node.js (for Playwright dependencies)
 
 Steps:
 
 Clone the repository:
 
 git clone [https://github.com/your-username/your-repo.git
 cd your-repo](https://github.com/kumudugunarathne/SouceDemoTestAutomation)
 
 Install dependencies:
 
 mvn clean install
 
 Run tests:
 
 mvn test
 
 Generate reports:
 
 mvn surefire-report:report
 
 
 # Reporting
 
 Test Logs are stored in JSON format.

 Extent reports available to see the test executions. 
 
 Screenshots are captured for failed test cases.
 
 Performance Metrics logged for each test.
 
 Reports generated using TestNG and stored in /target/surefire-reports/.
