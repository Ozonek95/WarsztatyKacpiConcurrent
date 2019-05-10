package G_zadanieZCalosci;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Kacper Staszek
 */
class GraczRozwiązującyTest implements Runnable {
  private final CountDownLatch latch;
  private final TestZGry testZGry;

  GraczRozwiązującyTest(CountDownLatch latch, TestZGry testZGry) {
    this.latch = latch;
    this.testZGry = testZGry;
  }

  GraczGrającyWGrę przeobraź(){
    return new GraczGrającyWGrę();
  }
  @Override
  public void run() {
    System.out.println("Zaczynam rozwiązywać test "+this);
    try {
      Thread.sleep(ThreadLocalRandom.current().nextInt(5000));
      System.out.println("ROZWIĄZAŁEM! "+this);
      latch.countDown();
      testZGry.getListaGraczyKtórzyPrzeszli().add(this.przeobraź());
    } catch (InterruptedException e) {
      return;
    }
  }

  @Override
  public String toString() {
    return Thread.currentThread().getName();
  }
}
