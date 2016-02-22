package reit;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

class RepairTool implements Comparable<RepairTool> {

    private Semaphore quantity;
    private String name;
    private AtomicInteger totalAcquired;

    RepairTool(String name, int quantity) {
        this.name = name;
        this.quantity = new Semaphore(quantity);
        totalAcquired = new AtomicInteger(0);
    }

    void acquireTool(int quantity) {
        try {
            this.quantity.acquire(quantity);
            totalAcquired.addAndGet(quantity);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void releaseTool(int amount) {
        quantity.release(amount);
    }

    String getName() {
        return name;
    }

    int getQuantity() {
        return quantity.availablePermits();
    }

    public int getTotalAcquired(){ return totalAcquired.get();}

    public String toString() {
        return "[" + name + " - " + quantity + "]";
    }

    @Override
    public int compareTo(RepairTool o) {
        return name.compareTo(o.name);
    }
}
