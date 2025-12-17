package com.restaurant.algorithms;

import com.restaurant.models.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoundRobinSchedulerTest {

    @Test
    void preservesInputOrder() {
        List<Order> orders = new ArrayList<>();
        orders.add(buildOrder(1));
        orders.add(buildOrder(2));
        orders.add(buildOrder(3));

        SchedulingAlgorithm algo = new RoundRobinScheduler();
        List<Order> result = algo.schedule(orders);

        assertEquals(List.of(1, 2, 3),
                result.stream().map(Order::getOrderId).toList());
    }

    @Test
    void returnsCopyNotSameInstance() {
        List<Order> orders = new ArrayList<>();
        orders.add(buildOrder(1));

        SchedulingAlgorithm algo = new RoundRobinScheduler();
        List<Order> result = algo.schedule(orders);

        result.clear();
        // Original list should remain untouched
        assertEquals(1, orders.size());
    }

    private Order buildOrder(int id) {
        Order o = new Order();
        o.setOrderId(id);
        return o;
    }
}


