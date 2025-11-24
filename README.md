## 🚧 Project Roadmap

### Database Layer
- [ ] Finalize `DatabaseConnection` (singleton + schema initialization)
- [ ] Implement `DatabaseManager` CRUD + special queries
- [ ] (Optional) Seed initial sample data
- [ ] Add unit tests for DB operations (use a temp/test DB file)

### Domain Models & Algorithms
- [ ] Polish `Order`, `Table`, `Staff` models (fields, helpers)
- [ ] Validate scheduling algorithms (SPT, Round Robin, Priority)
- [ ] Add unit tests for algorithm edge cases (empty queue, equal priorities, etc.)

### Controller & UI Integration
- [ ] Load tables/orders/staff on startup
- [ ] Implement Add/Edit/Remove Order flows (dialogs → DatabaseManager)
- [ ] Wire “Optimize Queue” button to run algorithms + update `order_queue`
- [ ] Implement status transitions (Pending → In Progress → Ready → Served)
- [ ] Auto-refresh UI tables/lists (timer or change listeners)
- [ ] Show alerts for long wait times or stalled orders

### Queue Visualization
- [ ] Display optimized queue (list view/cards with positions)
- [ ] Show current algorithm name + runtime info
- [ ] (Optional) Compare algorithms side by side

### Error Handling & UX Polish
- [ ] Centralize DB/algorithm errors (user-friendly alerts/logs)
- [ ] Validate user inputs (prep time, priority, items) before DB writes
- [ ] Add loading indicators for long-running actions

### DevOps & Packaging
- [ ] Dockerize app (Java + SQLite setup) if needed
- [ ] Verify Maven build (fat JAR / JavaFX packaging)
- [ ] Refresh README (setup, run instructions, screenshots)
- [ ] (Optional) Hook into existing Jenkins pipeline

### Monitoring & Extras
- [ ] Wire Prometheus/Grafana configs (if using)
- [ ] Ensure logging feeds metrics (pending orders, avg prep time, etc.)
- [ ] Validate Kubernetes manifests (if deploying beyond desktop)

### Final QA
- [ ] Full workflow test: add order → optimize → cook → serve
- [ ] Test on clean machine (fresh DB)
- [ ] Run automated tests (JUnit) + manual UI testing

### Release Prep
- [ ] Update version, changelog, README “Features” & “How to Run”
- [ ] Capture final screenshots for docs
- [ ] Tag release / push to GitHub
