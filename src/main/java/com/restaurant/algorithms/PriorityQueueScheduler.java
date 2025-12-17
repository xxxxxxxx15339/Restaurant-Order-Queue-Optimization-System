package com.restaurant.algorithms;

import com.restaurant.models.Order;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Priority‑based scheduler.
 * <p>
 * Lower numeric priority value means higher importance
 * (1 = highest priority).
 */
public class PriorityQueueScheduler implements SchedulingAlgorithm {

    @Override
    public String getName() {
        return "Priority Queue Scheduling";
    }

    @Override
    public List<Order> schedule(List<Order> orders) {
        List<Order> result = new ArrayList<>(orders);
        result.sort(Comparator
                .comparing((Order o) -> o.getPriority() == null ? Integer.MAX_VALUE : o.getPriority())
                .thenComparing(o -> o.getEstimatedPrepTime() == null ? Integer.MAX_VALUE : o.getEstimatedPrepTime())
                .thenComparing(o -> o.getOrderId() == null ? Integer.MAX_VALUE : o.getOrderId()));
        return result;
    }
}
