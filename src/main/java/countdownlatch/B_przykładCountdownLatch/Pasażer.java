package countdownlatch.B_przykładCountdownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Zadaniem każdego pasażera jest wsiąść do pociągu.
 * @author Kacper Staszek
 */
class Pasażer implements Runnable{
    private final Pociąg pociąg;
    private final CountDownLatch latch;

    Pasażer(Pociąg pociąg, CountDownLatch latch) {
        this.pociąg = pociąg;
        this.latch = latch;
    }

    @Override
    public void run() {
        System.out.println("O cholera! Lece na pociąg!");
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(10000));
            pociąg.pasażerStawiłSięNaPociąg(this);
            latch.countDown();
        } catch (InterruptedException ignored) {
            System.err.println(ignored.getMessage());
        }
    }


    /**
     * @return nazwa wątku zależna od nazwy fabryki wątków.
     * @see fabryczkapomocnicza.MyThreadFactory
     */
    @Override
    public String toString() {
        return Thread.currentThread().getName();
    }
}
