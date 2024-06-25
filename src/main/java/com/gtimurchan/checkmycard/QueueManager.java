package com.gtimurchan.checkmycard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueManager<T> {
    private final LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<>();

    public void addElements(Collection<T> elements) {
        queue.addAll(elements);
        System.out.println("--> Produced: Elements.sz: " + elements.size() + ", queue size " + queue.size());
    }

    public Collection<T> getElements(int N) throws InterruptedException {
        int restAmount = N;
        List<T> elements = new ArrayList<>();
        while (true) {
            elements.add(queue.take());  // Блокирует поток, пока не появится элемент
            restAmount = restAmount - 1;
            int addedAmount = queue.drainTo(elements, restAmount);
            System.out.println("--> Consumed: Elements.sz: " + elements.size() + ", queue size " + queue.size());
            if(queue.size() <= 0) {
                System.out.println("--> Queue is empty(!)");
            }
            if (addedAmount == restAmount) {
                break;
            }
            restAmount = restAmount - addedAmount;
        }
        return elements;
    }
}
