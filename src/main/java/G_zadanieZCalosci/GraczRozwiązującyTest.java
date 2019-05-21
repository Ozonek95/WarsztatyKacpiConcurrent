package G_zadanieZCalosci;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Kacper Staszek
 * @author Marcin Ogorzałek
 */
class GraczRozwiązującyTest implements Runnable {
  private final CountDownLatch latch;
  private final TestWiedzyZGry testZGry;

  GraczRozwiązującyTest(CountDownLatch latch, TestWiedzyZGry testWiedzyZGry) {
    this.latch = latch;
    this.testZGry = testWiedzyZGry;
  }

  @Override
  public void run() {
    System.out.println("Zaczynam rozwiązywać test "+this);
    try {
      Thread.sleep(ThreadLocalRandom.current().nextInt(5000));
      System.out.println("ROZWIĄZAŁEM! "+this);
      latch.countDown();
      testZGry.getListaGraczyKtórzyPrzeszli().add(new GraczGrającyWGrę());
    } catch (InterruptedException e) {
      return;
    }
  }

  @Override
  public String toString() {
    return Thread.currentThread().getName();
  }
}