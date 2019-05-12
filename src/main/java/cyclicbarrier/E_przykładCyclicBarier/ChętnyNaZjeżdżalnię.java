package cyclicbarrier.E_przykładCyclicBarier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Zadaniem chętnego jest podejść do stanowiska i zająć miejsce
 *
 * @author Kacper Staszek
 */
class ChętnyNaZjeżdżalnię implements Runnable {

    private final CyclicBarrier barrier;

    ChętnyNaZjeżdżalnię(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            System.out.println("Podchodzę do stanowiska " + this);
            Thread.sleep(ThreadLocalRandom.current().nextInt(5000));
            System.out.println("Ok, jestem gotowy! " + this);
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException ignore) {
            System.err.println(ignore.getMessage());
        }

    }

    /**
     * @return zależy nazwy fabryki.
     * @see fabryczkapomocnicza.MyThreadFactory
     */
    @Override
    public String toString() {
        return Thread.currentThread().getName();
    }
}
