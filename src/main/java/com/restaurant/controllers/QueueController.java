package com.restaurant.controllers;

import com.restaurant.algorithms.SchedulingAlgorithm;
import com.restaurant.database.DatabaseManager;
import com.restaurant.models.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper controller responsible for turning a list of orders into
 * an optimized queue and (optionally) persisting that queue in the
 * {@code order_queue} table for historical analysis.
 *
 * This controller is intentionally UI‑agnostic so it can be reused
 * from different screens if needed.
 */
public class QueueController {

    /**
     * Applies the provided scheduling algorithm to the given orders.
     */
    public List<Order> optimizeOrders(List<Order> source, SchedulingAlgorithm algorithm) {
        if (source == null || algorithm == null) {
            return List.of();
        }
        return new ArrayList<>(algorithm.schedule(source));
    }

    /**
     * Persists the optimized queue into the database, replacing any
     * previous queue entries for the same algorithm name.
     */
    public void saveQueue(String algorithmName, List<Order> optimized) {
        if (algorithmName == null || optimized == null) {
            return;
        }

        // Best‑effort: clear previous queue entries and insert current ones.
        DatabaseManager.clearQueue(algorithmName);
        int position = 1;
        for (Order order : optimized) {
            if (order.getOrderId() != null) {
                DatabaseManager.insertQueueEntry(order.getOrderId(), position++, algorithmName);
            }
        }
    }
}
