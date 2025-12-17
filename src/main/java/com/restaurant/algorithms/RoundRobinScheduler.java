package com.restaurant.algorithms;

import com.restaurant.models.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple round‑robin scheduler.
 * <p>
 * For now we treat each order as a single time slice and
 * preserve the incoming order list – this keeps behavior
 * predictable while still allowing us to plug in a more
 * sophisticated time‑slice based implementation later.
 */
public class RoundRobinScheduler implements SchedulingAlgorithm {

    @Override
    public String getName() {
        return "Round Robin Scheduling";
    }

    @Override
    public List<Order> schedule(List<Order> orders) {
        // Copy so callers can freely mutate the result without affecting the original list
        return new ArrayList<>(orders);
    }
}
