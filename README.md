# CinemaBookingApp

CinemaBookingApp is a Java-based application that provides a user-friendly cinema seat booking system. It manages seat arrangements, validates user input, and ensures a smooth booking process. The application is modular, maintainable, and equipped with unit tests.

---

## Features

### Core Functionalities
- **Input Screen**: Allows user to configure the Movie title, total number of rows and seats in the cinema hall.
- **Welcome Screen**: Provides user with 3 choices to book seats, check existing booking and exit the application
- **Book Seats**:
    - Allows users to book a specified number of seats, ensuring seat availability.
    - Validates user input to prevent overbooking.
    - Shows the seat plan post booking along with Booking Id.
    - User can also rebook the seats if existing booking is not preferred.
    - Supports multiple booking
- **Check Booking**: - Provides a clear summary of the booking with details about seats and tickets.
- **Exit Application**: Allows use to exit the application.

---

## Modules and Design

### Components

1. **CinemaBookingApp**:
    - The central application class that handles the setup, initialization, and start of the cinema booking process.

2. **CinemaBookingManager**:
    - Main orchestrator of the application
    - Manages communication between components like user input, seat booking, and display features.

3. **SeatingPlanService**:
    - Responsible for initializing, updating, and managing seat availability in the cinema hall.
    - Provides features to check and update the total number of available seats or seating configuration.

4. **InputHandler**:
    - Captures user input such as seat selections, total tickets, or menu options.
    - Ensures that input is well-formed and valid.

5. **Display**:
    - Handles outputs to the user, such as the seat plan, booking summaries, and error messages.

6. **SeatingConfigurationValidator**:
    - Validates important configuration settings such as the number of rows, seats per row, and ensuring movie title data is correct.
    - Provides error feedback for invalid input directly to the user.

---

## Requirements

### Prerequisites
- Java 17 or higher.
- Maven for dependency management.
- JUnit 5 framework for running the test suite.

---

## Running the Application

Follow the steps below to run the CinemaBookingApp:

1. **Build the Application**:
    - Use `mvn clean package` 

2. **Run the Application**:
    - Run the compiled `.jar` file:
      ```bash
      java -jar CinemaBookingApp.jar
      ```

3. **Follow On-Screen Instructions**:
    - View the cinema seating plan.
    - Select the number of tickets and enter seat configuration settings.
    - Check the booking summary and confirm your selection.

---

## Testing

The project includes a comprehensive test suite to ensure functionality and maintainability.

### How to Run Tests
Use the following Maven or Gradle command to execute the tests:
```bash
mvn test
```
Alternatively, tests can be run directly from your IDE if set up with JUnit support.

---

## Example Usage

1. Launch the application and follow the welcome instructions.
2. View the cinema seating plan with available (`.`) and booked seats (`#`).
3. Input the number of tickets to book and make seat selections.
4. Receive a booking summary and seat confirmation.

---

