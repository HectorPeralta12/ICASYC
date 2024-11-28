# Stock Management Simulation

This program is a stock management simulation that controls the inventory of a company that transports canned fruit between several European cities. It uses a clock that can be turned on and off to show the time of each delivery.

## Features

### Clock Mode:
- Displays the time of delivery.
- It is updated while the simulation is running.
- It can be turned on or off before starting.

### Inventory Management by City:
- Saves the can inventory for each city.
- Makes sure that each city meets minimum stock requirements by transferring cans from one city to another.
- Calculates the distance between cities to prioritize transfers.

### User Choice:
Option to enable or disable clock mode before the simulation continues:
- **Option 1**: Enables the clock, and a delay is added to make the simulation more realistic.
- **Option 2**: Disables the clock mode.

### Dynamic Transfer Delays:
- You can adjust the transfer delay when the clock is activated.

## Setup Instructions

### Prerequisites:
- Ensure you have a working installation of **Clojure**.
- A terminal or IDE that supports running Clojure files.

### Clone or Download:
- Save the program as `main.clj` in your working directory.

### Run the Program:
- Click on the green button that turns on the REPL and run the program.

## How it Works

### User Input:
At startup, the program will ask:
`Do you want to use the function of the clock mode? (1: Yes, 2: No)`

- **1**: Activates the clock mode.
- **2**: Deactivates the clock mode.

### Clock Behavior:

#### Enabled:
- Displays a real-time clock on the terminal while running the simulation.
- The transfer between each city occurs with a delay of 2 seconds.

#### Disabled:
- Does not display the clock.
- Transfers between cities and the breakdown occur instantly.

### Simulation Steps:

1. **Inventory Check**:
   - For each city, the program checks if it has the minimum number of cans required.

2. **Transfers**:
   - If any city has less than the minimum, the program sends cans from another city that has excess stock.

3. **Prioritization**:
   - Transfers are prioritized based on distance.

4. **Final Status**:
   - At the end of the simulation, prints the final inventory for each city.

## Code Structure:

### 1. Database:
- **`cities`**: Marks each city with its inventory, minimum, and maximum capacity.
- **`distances`**: Defines the distances between all cities.

### 2. Clock Functions:
- **`display-clock`**: Displays and updates the current time every second.
- **`update-time`**: Increments the clock's time.

### 3. Simulation Logic:
- **`meet-minimums`**: Ensures all cities meet their minimum stock requirements.
- **`transfer-products`**: Transfers goods between cities based on availability and capacity.
- **`plan-routes`**: Orchestrates the overall simulation.

### 4. User Interaction:
- **`main`**: Handles user input to toggle clock mode and runs the simulation accordingly.

## Customization:

### Add New Cities:
- Update `cities` within the atom using this format: `:initial`, `:min`, `:max`, and `:current`.

### Adjust the Delay Time:
- Modify the delay in the `plan-routes` function:

```clojure
(plan-routes 2000) ;; Delay in milliseconds
```
## Future Changes

While the project meets the current requirements, there are opportunities to improve its functionality in future iterations:

1. **Dynamic Route Planning**  
   Algorithms could be implemented to dynamically calculate the most efficient delivery routes based on real-time stock needs and distance adjustments, as the current system relies on static distances and predefined logic.

2. **Database Integration**  
   Hardcoded data could be replaced with a database system to allow for scalability, persistent storage, and easier updates.

3. **Enhanced Error Handling**  
   Detailed error messages and robust handling for edge cases could be added, such as invalid inputs or failed stock transfers.

4. **Graphical User Interface (GUI)**  
   A user-friendly GUI could be developed to replace the current command-line interface, making the system easier for operators to navigate.

5. **Scalability for Large Networks**  
   The system could be adapted to handle a larger network with more cities, warehouses, and routes while maintaining performance and efficiency.


