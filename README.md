# CinemaBookingApp

CinemaBookingApp is a Java-based application designed to provide a user-friendly cinema seat booking system. It efficiently manages seat arrangements, validates user inputs, and ensures a seamless booking process. The application is modular, easy to maintain, and fully equipped with unit tests to ensure high-quality functionality.

---

## Features

### Core Functionalities

- **Input Screen**: Allows the user to configure movie title, the total number of rows, and seats in the cinema hall.
- **Welcome Screen**: Displays 3 options for the user:
    1. Book seats
    2. Check existing booking
    3. Exit the application
- **Book Seats**:
    - Users can book a specified number of seats, ensuring seat availability.
    - Input validation ensures no overbooking occurs.
    - Displays an updated seat plan after booking, including the Booking ID.
    - Users can rebook seats if they are not satisfied with the current booking.
    - Supports multiple bookings in a session.
- **Check Booking**:
    - Provides a clear summary of the booking with details about seats and tickets.
- **Exit Application**: Allows the user to exit the application.

---

## Architecture and Design

### High-Level Components

The application is structured into three core modules: **Controller**, **Domain (Model)**, and **View**.

1. **Domain**:
    - Contains the core business logic, including seat booking, re-booking, and managing seat availability.
2. **Controller**:
    - Handles the flow of application logic, processing user inputs and directing them to the appropriate modules.
3. **View**:
    - Manages user interactions, including receiving input from the user and displaying outputs like seat plans and booking information.

---

### Class-Level Breakdown

1. **CinemaBookingApp**:
    - The main entry point of the application, responsible for setup, initialization, and launching the cinema booking process.

2. **CinemaBookingManager**:
    - The core orchestrator that manages interactions between components, such as user input, seat bookings, and seat plan updates.

3. **SeatBookingService**:
    - Manages the booking of seats, updating seat availability based on user preferences, and providing booking details.

4. **SeatingPlanService**:
    - Responsible for initializing and updating the seating plan, managing seat availability, and configuring the seating arrangement.

5. **InputHandler**:
    - Captures and validates user input, ensuring it is properly formatted and valid for processing (e.g., seat selections, ticket counts, and menu options).

6. **CinemaSeatingDisplay**:
    - Handles the visual output to the user, including the seat plan, booking summaries, and error messages.

7. **SeatingConfigurationValidator**:
    - Validates critical configuration settings, such as the number of rows and seats per row, as well as ensuring the movie title input is valid.
    - Provides error messages for invalid input.

8. **BookingIdGenerator**:
    - Generates unique booking IDs in the required format for each reservation.

---

## Requirements

### Prerequisites

- **Java 17** or higher.
- **Maven** for dependency management and build automation.
- **JUnit 5** framework for running tests.

---


## Running the Application

Follow the steps below to run the CinemaBookingApp:

1. **Build the Application**:
    - Use `mvn clean package`

2. **Run the Application**:
    - Run the compiled `.jar` file:
      ```bash
      java -jar CinemaBookingSystem-1.0.0.jar
      ```

3. **Follow On-Screen Instructions**:
    - Configure the seat plan
    - Book Seats
    - View the cinema seating plan.
    - Update Seat number if required
    - Check the booking summary and confirm your selection.

---

## Testing

The project includes a comprehensive test suite to ensure functionality and maintainability.

### How to Run Tests
Use the following Maven command to execute the tests:
```bash
mvn test
```
Alternatively, tests can be run directly from your IDE if set up with JUnit support.

---

## Assumptions
1. Few error messages not part of document have been added to provide use more information
2. The confirmed tickets will be shown with 'C' when checking for booking
3. Movie title should be single word
