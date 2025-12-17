package com.restaurant.algorithms;

import com.restaurant.models.Order;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Shortest Processing Time first:
 * orders with the lowest estimated preparation time are served first.
 */
public class SPTscheduler implements SchedulingAlgorithm {

    @Override
    public String getName() {
        return "SPT (Shortest Processing Time)";
    }

    @Override
    public List<Order> schedule(List<Order> orders) {
        List<Order> result = new ArrayList<>(orders);
        result.sort(Comparator
                .comparing((Order o) -> o.getEstimatedPrepTime() == null ? Integer.MAX_VALUE : o.getEstimatedPrepTime())
                .thenComparing(o -> o.getPriority() == null ? Integer.MAX_VALUE : o.getPriority())
                .thenComparing(o -> o.getOrderId() == null ? Integer.MAX_VALUE : o.getOrderId()));
        return result;
    }
}
