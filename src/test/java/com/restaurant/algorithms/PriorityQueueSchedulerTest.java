package com.restaurant.algorithms;

import com.restaurant.models.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriorityQueueSchedulerTest {

    @Test
    void sortsByPriorityThenEstimatedTime() {
        List<Order> orders = new ArrayList<>();
        orders.add(buildOrder(1, 3, 15));  // lower priority
        orders.add(buildOrder(2, 1, 20));  // highest priority
        orders.add(buildOrder(3, 1, 10));  // same priority, shorter time

        SchedulingAlgorithm algo = new PriorityQueueScheduler();
        List<Order> result = algo.schedule(orders);

        // Priority asc, then estTime asc -> #3, #2, #1
        assertEquals(List.of(3, 2, 1),
                result.stream().map(Order::getOrderId).toList());
    }

    private Order buildOrder(int id, int priority, int estTime) {
        Order o = new Order();
        o.setOrderId(id);
        o.setPriority(priority);
        o.setEstimatedPrepTime(estTime);
        return o;
    }
}


