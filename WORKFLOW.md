# Restaurant Queue System - Complete Workflow
## From Order Creation to Completion - Every Detail Explained

---

## 🎬 The Big Picture: What Your System Does

Imagine a real restaurant scenario:
1. Customer arrives and orders food
2. Order goes to kitchen
3. Chef needs to decide: "Which order should I cook first?"
4. Your system helps make that decision
5. Chef cooks in optimized order
6. Food gets served

**Your application automates step 3** - the decision-making process.

---

## 🏗️ System Architecture Overview

```
┌─────────────────────────────────────────────────────┐
│                 USER INTERFACE                      │
│  (JavaFX - What the restaurant manager sees)        │
│  - Shows tables, orders, staff                      │
│  - Has buttons to add orders, run algorithms        │
└────────────────────┬────────────────────────────────┘
                     │
                     │ User clicks buttons,
                     │ selects options
                     ▼
┌─────────────────────────────────────────────────────┐
│              CONTROLLER LAYER                       │
│  (MainController.java - Handles user actions)       │
│  - "User clicked Add Order button"                  │
│  - "User selected SPT algorithm"                    │
│  - Coordinates between UI and business logic        │
└────────────────────┬────────────────────────────────┘
                     │
                     │ Calls methods,
                     │ requests data
                     ▼
┌─────────────────────────────────────────────────────┐
│           BUSINESS LOGIC LAYER                      │
│  (Algorithm Classes - The brain)                    │
│  - SPTScheduler.java                                │
│  - RoundRobinScheduler.java                         │
│  - PriorityQueueScheduler.java                      │
│  "Given these orders, what's the optimal sequence?" │
└────────────────────┬────────────────────────────────┘
                     │
                     │ Needs data,
                     │ saves results
                     ▼
┌─────────────────────────────────────────────────────┐
│           DATA ACCESS LAYER                         │
│  (DatabaseManager.java - CRUD operations)           │
│  - addOrder()                                       │
│  - getAllOrders()                                   │
│  - updateOrderStatus()                              │
└────────────────────┬────────────────────────────────┘
                     │
                     │ Executes SQL,
                     │ manages connection
                     ▼
┌─────────────────────────────────────────────────────┐
│         DATABASE CONNECTION LAYER                   │
│  (DatabaseConnection.java - Manages connection)     │
│  - Opens connection to database                     │
│  - Ensures only one connection exists              │
└────────────────────┬────────────────────────────────┘
                     │
                     │ Reads/writes data
                     ▼
┌─────────────────────────────────────────────────────┐
│              DATABASE (SQLite)                      │
│  (restaurant.db file - Stores everything)           │
│  - tables table                                     │
│  - staff table                                      │
│  - orders table                                     │
│  - order_queue table                                │
└─────────────────────────────────────────────────────┘
```

---

## 📖 Complete Workflow: Step-by-Step

---

## 🚀 PHASE 1: Application Startup

### Step 1.1: User Launches Application

**What happens:**
```
User double-clicks application icon
    ↓
Operating system runs: java -jar RestaurantQueueSystem.jar
    ↓
JVM (Java Virtual Machine) starts
    ↓
Finds Main.java class
    ↓
Executes: public static void main(String[] args)
```

**In Main.java:**
```java
public static void main(String[] args) {
    launch(args);  // JavaFX magic starts here
}
```

**What launch() does behind the scenes:**
1. Creates JavaFX Application Thread
2. Initializes JavaFX runtime
3. Calls your start() method
4. Prepares to show UI

### Step 1.2: JavaFX Initializes

**In Main.java start() method:**
```java
public void start(Stage primaryStage) {
    // This is called automatically by JavaFX
}
```

**What happens:**
1. JavaFX creates a Stage (window)
2. Calls your start() method
3. Passes you the primaryStage to configure

### Step 1.3: Load UI (FXML)

**Your code:**
```
Load main-view.fxml file
    ↓
FXMLLoader reads the XML
    ↓
Creates all UI components (buttons, tables, labels)
    ↓
Connects to MainController.java
    ↓
Returns the root layout node
```

**Behind the scenes:**
```
FXMLLoader:
1. Parses main-view.fxml XML
2. For each <Button>, creates Button object
3. For each <Label>, creates Label object
4. For each <TableView>, creates TableView object
5. Looks for fx:controller attribute
6. Creates instance of MainController
7. Injects UI components into controller (using @FXML annotations)
8. Calls controller's initialize() method
```

### Step 1.4: Create Scene and Show Window

**What happens:**
```
Scene scene = new Scene(root, 800, 600);
    ↓
Creates a Scene object containing your UI
    ↓
primaryStage.setScene(scene);
    ↓
Attaches scene to stage
    ↓
primaryStage.show();
    ↓
Window appears on screen!
```

### Step 1.5: Controller Initializes

**In MainController.java:**
```java
@FXML
public void initialize() {
    // This runs automatically after FXML loads
}
```

**What happens here:**
1. Initialize database connection
2. Load initial data from database
3. Populate TableViews with data
4. Set up event listeners
5. Configure UI components
6. Display welcome message

**Database initialization:**
```
MainController calls: DatabaseConnection.getConnection()
    ↓
DatabaseConnection checks: "Do I have a connection?"
    ↓
NO → Create new connection
    ↓
Open restaurant.db file (or create if doesn't exist)
    ↓
Execute schema.sql to create tables
    ↓
Insert sample data (if tables are empty)
    ↓
Return connection object
    ↓
YES → Return existing connection
```

### Step 1.6: Load Initial Data

**MainController does:**
```
Call: DatabaseManager.getAllOrders()
    ↓
DatabaseManager executes: SELECT * FROM orders
    ↓
Gets ResultSet with all orders
    ↓
Loops through each row
    ↓
Creates Order objects (or arrays) for each row
    ↓
Returns list of orders
    ↓
MainController receives the list
    ↓
Populates TableView with orders
    ↓
User sees orders displayed on screen
```

**Same process for:**
- getAllTables() → Display tables status
- getAllStaff() → Display staff availability

---

## 📝 PHASE 2: Customer Places Order (Adding New Order)

### Step 2.1: User Clicks "Add Order" Button

**What happens:**
```
User clicks [Add Order] button
    ↓
Mouse click event generated by OS
    ↓
JavaFX captures the event
    ↓
Looks for onAction handler
    ↓
Finds: @FXML handleAddOrder() method
    ↓
Calls MainController.handleAddOrder()
```

### Step 2.2: Show Order Input Dialog

**MainController does:**
```java
handleAddOrder() {
    // Opens a dialog window
}
```

**What happens:**
1. Create new Dialog window
2. Create form fields:
   - ComboBox for table selection (populated from database)
   - TextField for items description
   - Spinner for estimated prep time
   - ComboBox for priority level
3. Show dialog
4. Wait for user input
5. User fills in form:
   ```
   Table: 3
   Items: "Burger and Fries"
   Prep Time: 15 minutes
   Priority: 2
   ```
6. User clicks [Submit]

### Step 2.3: Validate Input

**Before saving, check:**
```
Is table selected? ✓
Is items field empty? ✗ (has text)
Is prep time > 0? ✓
Is priority between 1-5? ✓

All valid → Proceed
Any invalid → Show error message, stay in dialog
```

### Step 2.4: Save Order to Database

**MainController calls:**
```
DatabaseManager.addOrder(
    tableId: 3,
    staffId: 1,
    prepTime: 15,
    priority: 2,
    items: "Burger and Fries"
)
```

**Inside DatabaseManager.addOrder():**

**Step A: Prepare SQL Statement**
```
String sql = "INSERT INTO orders (table_id, staff_id, estimated_prep_time, priority, items) VALUES (?, ?, ?, ?, ?)"

What are the ? marks?
- Placeholders for values
- Prevents SQL injection attacks
- Will be replaced with actual values
```

**Step B: Get Database Connection**
```
Connection conn = DatabaseConnection.getConnection()

If connection exists → Use it
If not → Create new connection
```

**Step C: Create PreparedStatement**
```
PreparedStatement pstmt = conn.prepareStatement(sql)

This creates a "prepared" SQL statement
Ready to receive values for the ? placeholders
```

**Step D: Set Values**
```
pstmt.setInt(1, 3);              // table_id = 3
pstmt.setInt(2, 1);              // staff_id = 1
pstmt.setInt(3, 15);             // estimated_prep_time = 15
pstmt.setInt(4, 2);              // priority = 2
pstmt.setString(5, "Burger...");  // items = "Burger and Fries"

Now SQL looks like:
INSERT INTO orders (...) VALUES (3, 1, 15, 2, 'Burger and Fries')
```

**Step E: Execute Statement**
```
pstmt.executeUpdate()

This tells database: "Execute this INSERT statement"
    ↓
Database processes the statement:
1. Validates data types match
2. Checks foreign key constraints (table_id 3 exists?)
3. Checks CHECK constraints (priority between 1-5?)
4. Generates new order_id (AUTOINCREMENT)
5. Sets order_time to CURRENT_TIMESTAMP
6. Sets status to DEFAULT 'PENDING'
7. Writes row to orders table
8. Commits transaction
    ↓
Returns number of rows affected: 1
```

**Step F: Check Success**
```
int rowsAffected = pstmt.executeUpdate()

if (rowsAffected > 0) {
    // Success! Order was inserted
    return true;
} else {
    // Failed! Nothing inserted
    return false;
}
```

**Step G: Close Resources**
```
PreparedStatement automatically closes (try-with-resources)
Connection stays open (singleton pattern)
```

### Step 2.5: Update UI

**Back in MainController:**
```
boolean success = DatabaseManager.addOrder(...)

if (success) {
    // Refresh the orders display
    refreshOrdersTable();
    
    // Show success message
    showAlert("Success", "Order added!");
    
    // Close dialog
    dialog.close();
} else {
    // Show error
    showAlert("Error", "Failed to add order");
}
```

**refreshOrdersTable() does:**
```
1. Call: DatabaseManager.getAllOrders()
2. Get updated list (now includes new order)
3. Clear TableView
4. Add all orders to TableView
5. User sees new order appear in list
```

### Step 2.6: Update Table Status

**Optionally, mark table as occupied:**
```
DatabaseManager.updateTableStatus(tableId: 3, status: "OCCUPIED")

Similar process:
1. Prepare SQL: UPDATE tables SET status = ? WHERE table_id = ?
2. Set values
3. Execute
4. Table #3 now shows as OCCUPIED in UI
```

---

## 🧮 PHASE 3: Running Scheduling Algorithm

### Step 3.1: User Selects Algorithm

**In the UI:**
```
[Algorithm Selector: SPT ▼]
     Options shown:
     - SPT
     - Round Robin
     - Priority Queue

User selects: SPT
```

**What happens:**
```
Selection changes
    ↓
JavaFX fires event
    ↓
Calls: handleAlgorithmSelection()
    ↓
MainController stores selected algorithm: "SPT"
```

### Step 3.2: User Clicks "Optimize Queue"

**User clicks [Optimize Queue] button**

**MainController.handleOptimizeQueue():**
```java
handleOptimizeQueue() {
    String algorithm = algorithmSelector.getValue(); // "SPT"
    
    // Get all pending orders
    // Run algorithm
    // Display results
}
```

### Step 3.3: Get All Pending Orders

**MainController calls:**
```
List<Order> orders = DatabaseManager.getAllOrders()
```

**DatabaseManager does:**
```
SELECT * FROM orders WHERE status = 'PENDING'
    ↓
Database returns ResultSet
    ↓
Loop through each row:
    Order order = new Order();
    order.setOrderId(rs.getInt("order_id"));
    order.setTableId(rs.getInt("table_id"));
    order.setEstimatedPrepTime(rs.getInt("estimated_prep_time"));
    order.setPriority(rs.getInt("priority"));
    order.setItems(rs.getString("items"));
    orders.add(order);
    ↓
Return list of Order objects
```

**Example returned list:**
```
Order #1: Pizza, 15 min, Priority 2
Order #2: Steak, 30 min, Priority 1
Order #3: Salad, 5 min, Priority 2
Order #4: Pasta, 20 min, Priority 1
Order #5: Burger, 10 min, Priority 3
```

### Step 3.4: Create Algorithm Instance

**Based on selection:**
```java
SchedulingAlgorithm scheduler;

switch(algorithm) {
    case "SPT":
        scheduler = new SPTScheduler();
        break;
    case "ROUND_ROBIN":
        scheduler = new RoundRobinScheduler();
        break;
    case "PRIORITY":
        scheduler = new PriorityQueueScheduler();
        break;
}
```

### Step 3.5: Run the Algorithm

**MainController calls:**
```java
List<Order> optimizedQueue = scheduler.scheduleOrders(orders);
```

**Inside SPTScheduler.scheduleOrders():**

**Step A: Receive List of Orders**
```
Input: [Order #1, Order #2, Order #3, Order #4, Order #5]
```

**Step B: Sort by Estimated Prep Time**
```java
Collections.sort(orders, new Comparator<Order>() {
    public int compare(Order o1, Order o2) {
        return o1.getEstimatedPrepTime() - o2.getEstimatedPrepTime();
    }
});
```

**What this does:**
```
Compare each pair:
- Order #1 (15 min) vs Order #2 (30 min) → 15 < 30, Order #1 stays first
- Order #1 (15 min) vs Order #3 (5 min) → 15 > 5, swap! Order #3 now first
- Continue comparing...

Final sorted order:
Order #3: Salad, 5 min
Order #5: Burger, 10 min
Order #1: Pizza, 15 min
Order #4: Pasta, 20 min
Order #2: Steak, 30 min
```

**Step C: Assign Positions**
```java
for (int i = 0; i < orders.size(); i++) {
    orders.get(i).setQueuePosition(i + 1);
}
```

**Result:**
```
Order #3: Position 1 (Salad, 5 min)
Order #5: Position 2 (Burger, 10 min)
Order #1: Position 3 (Pizza, 15 min)
Order #4: Position 4 (Pasta, 20 min)
Order #2: Position 5 (Steak, 30 min)
```

**Step D: Return Optimized List**
```
return orders; // Now sorted by SPT logic
```

### Step 3.6: Save Queue to Database

**MainController receives optimized queue:**
```java
List<Order> optimizedQueue = scheduler.scheduleOrders(orders);
```

**For each order in optimized queue:**
```java
for (Order order : optimizedQueue) {
    DatabaseManager.addToQueue(
        order.getOrderId(),
        order.getQueuePosition(),
        "SPT"
    );
}
```

**DatabaseManager.addToQueue() does:**
```sql
INSERT INTO order_queue (order_id, position, algorithm_used) 
VALUES (3, 1, 'SPT')
VALUES (5, 2, 'SPT')
VALUES (1, 3, 'SPT')
...
```

**Database now has:**
```
order_queue table:
queue_id | order_id | position | algorithm_used
---------|----------|----------|---------------
1        | 3        | 1        | SPT
2        | 5        | 2        | SPT
3        | 1        | 3        | SPT
4        | 4        | 4        | SPT
5        | 2        | 5        | SPT
```

### Step 3.7: Display Queue in UI

**MainController updates queue display:**
```java
queueListView.getItems().clear();

for (Order order : optimizedQueue) {
    String display = String.format(
        "Position %d: Order #%d - %s (%d min)",
        order.getQueuePosition(),
        order.getOrderId(),
        order.getItems(),
        order.getEstimatedPrepTime()
    );
    queueListView.getItems().add(display);
}
```

**User sees on screen:**
```
Optimized Queue (SPT):
┌─────────────────────────────────────┐
│ Position 1: Order #3 - Salad (5 min)    │
│ Position 2: Order #5 - Burger (10 min)  │
│ Position 3: Order #1 - Pizza (15 min)   │
│ Position 4: Order #4 - Pasta (20 min)   │
│ Position 5: Order #2 - Steak (30 min)   │
└─────────────────────────────────────┘
```

---

## 👨‍🍳 PHASE 4: Chef Starts Cooking

### Step 4.1: Chef Selects First Order

**Chef looks at screen, sees:**
```
Next to cook: Order #3 - Salad (5 min)
```

**Chef clicks [Start Cooking] button next to Order #3**

### Step 4.2: Update Order Status

**MainController.handleStartCooking():**
```java
handleStartCooking(orderId: 3) {
    DatabaseManager.updateOrderStatus(3, "IN_PROGRESS");
    refreshUI();
}
```

**DatabaseManager.updateOrderStatus():**
```sql
UPDATE orders 
SET status = 'IN_PROGRESS' 
WHERE order_id = 3
```

**Database updates:**
```
orders table - Order #3:
status: PENDING → IN_PROGRESS
```

### Step 4.3: UI Updates

**refreshUI() does:**
```
1. Reload orders from database
2. Update TableView
3. Change row color:
   - PENDING orders: White background
   - IN_PROGRESS orders: Yellow background
   - READY orders: Green background
```

**User sees:**
```
Orders Table:
Order #3: Salad (5 min) - IN_PROGRESS [Yellow highlight]
Order #5: Burger (10 min) - PENDING
Order #1: Pizza (15 min) - PENDING
...
```

### Step 4.4: Chef Completes Order

**5 minutes later, chef finishes the salad**

**Chef clicks [Mark as Ready] button**

**MainController does:**
```java
handleMarkReady(orderId: 3) {
    DatabaseManager.updateOrderStatus(3, "READY");
    DatabaseManager.updateActualPrepTime(3, 5); // took 5 min
    refreshUI();
}
```

**Database updates:**
```sql
UPDATE orders 
SET status = 'READY', actual_prep_time = 5 
WHERE order_id = 3
```

**UI shows:**
```
Order #3: Salad - READY [Green highlight]
```

### Step 4.5: Server Delivers Order

**Server takes order to Table #3**

**Server clicks [Mark as Served]**

```
UPDATE orders SET status = 'SERVED' WHERE order_id = 3
```

**Order #3 is complete!**

### Step 4.6: Process Next Order

**Chef looks at queue again:**
```
Next to cook: Order #5 - Burger (10 min)
```

**Repeat the process:**
- Start cooking → IN_PROGRESS
- Finish cooking → READY
- Deliver → SERVED

---

## 🔄 PHASE 5: Real-Time Updates & Monitoring

### Continuous Background Processes:

**Auto-Refresh Timer:**
```java
Timeline timeline = new Timeline(new KeyFrame(
    Duration.seconds(5),
    event -> refreshUI()
));
timeline.setCycleCount(Timeline.INDEFINITE);
timeline.play();
```

**Every 5 seconds:**
1. Query database for latest orders
2. Update TableViews
3. Update status indicators
4. Refresh queue display

**Status Monitoring:**
```
Check each order:
- If IN_PROGRESS > 30 min → Alert: "Order taking too long!"
- If PENDING > 60 min → Alert: "Order not started!"
```

---

## 📊 PHASE 6: Comparing Algorithms

### Step 6.1: User Wants to Compare

**User clicks [Compare Algorithms] button**

### Step 6.2: Run All Algorithms

**MainController does:**
```java
List<Order> orders = DatabaseManager.getAllOrders();

// Run SPT
SPTScheduler spt = new SPTScheduler();
List<Order> sptQueue = spt.scheduleOrders(orders);
saveQueue(sptQueue, "SPT");

// Run Round Robin
RoundRobinScheduler rr = new RoundRobinScheduler();
List<Order> rrQueue = rr.scheduleOrders(orders);
saveQueue(rrQueue, "ROUND_ROBIN");

// Run Priority
PriorityQueueScheduler pq = new PriorityQueueScheduler();
List<Order> pqQueue = pq.scheduleOrders(orders);
saveQueue(pqQueue, "PRIORITY");
```

### Step 6.3: Store All Results

**All three queues saved in order_queue table:**
```
order_queue table:
queue_id | order_id | position | algorithm_used
---------|----------|----------|---------------
1        | 3        | 1        | SPT
2        | 5        | 2        | SPT
3        | 1        | 3        | SPT
---------|----------|----------|---------------
4        | 1        | 1        | ROUND_ROBIN
5        | 2        | 2        | ROUND_ROBIN
6        | 3        | 3        | ROUND_ROBIN
---------|----------|----------|---------------
7        | 2        | 1        | PRIORITY
8        | 1        | 2        | PRIORITY
9        | 3        | 3        | PRIORITY
```

### Step 6.4: Display Comparison

**UI shows side-by-side:**
```
┌─────────────┬─────────────┬─────────────┐
│     SPT     │ ROUND ROBIN │  PRIORITY   │
├─────────────┼─────────────┼─────────────┤
│ 1. Order #3 │ 1. Order #1 │ 1. Order #2 │
│ 2. Order #5 │ 2. Order #2 │ 2. Order #1 │
│ 3. Order #1 │ 3. Order #3 │ 3. Order #3 │
└─────────────┴─────────────┴─────────────┘
```

---

## 🎯 Summary of Data Flow

```
1. User Action (UI)
      ↓
2. Controller receives event
      ↓
3. Controller calls DatabaseManager
      ↓
4. DatabaseManager calls DatabaseConnection
      ↓
5. DatabaseConnection opens restaurant.db
      ↓
6. SQL executed on database
      ↓
7. Database returns results
      ↓
8. DatabaseManager processes results
      ↓
9. Returns data to Controller
      ↓
10. Controller updates UI
      ↓
11. User sees changes
```

---

## 💾 Database State Changes Timeline

```
Time    Event                           Database State
-----   -----                           --------------
0:00    App starts                      Empty tables
0:01    schema.sql executes             Tables created
0:02    Sample data inserted            5 orders, 5 tables, 5 staff
0:10    User adds Order #6              6 orders total
0:15    SPT algorithm runs              order_queue has 6 entries
0:20    Chef starts Order #3            Order #3: status = IN_PROGRESS
0:25    Chef completes Order #3         Order #3: status = READY
0:30    Server delivers Order #3        Order #3: status = SERVED
0:35    Round Robin runs                order_queue has 12 entries now
```

---

## 🧵 Thread Model

**JavaFX Application Thread:**
- Handles all UI updates
- Responds to button clicks
- Updates TableViews
- **Rule:** Never block this thread!

**Database Operations:**
- Run on same thread (since SQLite is file-based)
- Fast enough not to freeze UI
- For larger databases, would use background threads

**Event Flow:**
```
User clicks button on UI Thread
    ↓
Event handler called (still on UI Thread)
    ↓
Database operation (quick, still UI Thread)
    ↓
UI updated (UI Thread)
    ↓
Ready for next user action
```

---

## 🔒 Transaction Safety

**Every database operation is atomic:**
```
BEGIN TRANSACTION
    INSERT INTO orders ...
    IF successful:
        COMMIT (save changes)
    IF error:
        ROLLBACK (undo changes)
END TRANSACTION
```

**This ensures:**
- No partial orders saved
- No orphaned queue entries
- Database always in consistent state

---

This is the complete, detailed workflow of your entire system from start to finish! Every click, every database call, every algorithm execution explained. 🚀