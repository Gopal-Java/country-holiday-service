# CountryHolidayServiceApplication
country-holiday-service
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.sample.holiday
│   │   │       ├── rest                                  # REST API Controllers
│   │   │       ├── exception                             # Custom exceptions and handlers
│   │   │       ├── model                                 # Models/DTOs for Holiday data
│   │   │       ├── service                               # Service and business logic classes
│   │   │       ├── CountryHolidayServiceApplication.java # Main Spring Boot application
│   │   └── resources
│   │       ├── application.properties                     # Configurations (API URL, cache settings)
├── pom.xml                                                # Project dependencies and build configuration

This is a Spring Boot application that retrieves holiday information for various countries.

## Prerequisites

- **Java 1.8** or higher
java -version
- **Maven** 3.X or higher
mvn -version

## Building the Application

1. Clone the repository:
   ```bash
   git clone [<repository-url>](https://github.com/Gopal-Java/country-holiday-service/)

## Run the Application

mvn spring-boot:run
java -jar target/CountryHolidayServiceApplication-0.0.1-SNAPSHOT.jar
## Run the Test class
mvn test
## Access API Endpoints
The following endpoints are available:

Get Last 3 Holidays for a Country

Endpoint: GET /api/holidays/v1/last-three/{countryCode}/{year}
Example: http://localhost:8080/v1/api/holidays/last-three/US/2024
Get Count of Holidays Not on Weekends

Endpoint: GET /api/holidays/v1/no-weekends/{year}?countryCodes=NL,BE,GB
Example: http://localhost:8080/api/holidays/v1/no-weekends/2024?countryCodes=NL,BE,GB
Get Shared Holidays Between Two Countries

Endpoint: GET /api/holidays/v1/shared/{year}?countryCode1=GB&countryCode2=NL
Example: http://localhost:8080/api/holidays/v1/shared/2024?countryCode1=GB&countryCode2=NL

## Utility Rest API to delete the caches if required.
# Specific country Purge the cache
http://localhost:8080/api/holidays/v1/cache/2024/GB
# All country Purge the cache
http://localhost:8080/api/holidays/v1/cache/all
