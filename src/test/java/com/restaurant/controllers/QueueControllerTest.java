package com.restaurant.controllers;

import com.restaurant.algorithms.RoundRobinScheduler;
import com.restaurant.algorithms.SchedulingAlgorithm;
import com.restaurant.database.DatabaseConnection;
import com.restaurant.models.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QueueControllerTest {

    @TempDir
    Path tempDir;

    private final QueueController controller = new QueueController();

    @BeforeEach
    void setUp() {
        // Use a temp SQLite file so Jenkins/workspace paths don't matter
        Path dbPath = tempDir.resolve("queue-test-" + System.nanoTime() + ".sqlite");
        DatabaseConnection.setDatabaseUrl("jdbc:sqlite:" + dbPath.toAbsolutePath());
        DatabaseConnection.getConnection(); // trigger schema creation
    }

    @AfterEach
    void tearDown() {
        DatabaseConnection.closeConnection();
    }

    @Test
    void optimizeOrdersHandlesNullsGracefully() {
        SchedulingAlgorithm algo = new RoundRobinScheduler();

        // Null list
        assertTrue(controller.optimizeOrders(null, algo).isEmpty());
        // Null algorithm
        assertTrue(controller.optimizeOrders(new ArrayList<>(), null).isEmpty());
    }

    @Test
    void saveQueueAcceptsEmptyOrNullInput() {
        // null algorithm name
        controller.saveQueue(null, List.of());

        // null list
        controller.saveQueue("TEST_ALGO", null);

        // list with order that has null id (should be ignored safely)
        Order order = new Order();
        order.setOrderId(null);
        List<Order> optimized = List.of(order);
        controller.saveQueue("TEST_ALGO", optimized);

        // If we reach here without exceptions the behavior is acceptable;
        // persistence is already covered in DatabaseManagerTest.queueOperations.
        assertTrue(true);
    }
}


