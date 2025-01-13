package middle;

import catalogue.Basket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SharedOrderQueue {
    private static final BlockingQueue<Basket> orderQueue = new LinkedBlockingQueue<>();

    public static void addOrder(Basket basket) {
        orderQueue.offer(basket);
    }

    public static Basket getOrder() throws InterruptedException {
        return orderQueue.take();
    }
}
