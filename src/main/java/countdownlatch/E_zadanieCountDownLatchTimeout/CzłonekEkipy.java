package countdownlatch.E_zadanieCountDownLatchTimeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Kacper Staszek
 * @author Marcin Ogorzałek
 */
class CzłonekEkipy implements Runnable {

  private final CountDownLatch latch;
  private final Więzienie więzienie;
  private final int maksymalnyCzasRoboty;

  CzłonekEkipy(CountDownLatch latch, Więzienie więzienie, int maksymalnyCzasRoboty) {
    this.latch = latch;
    this.więzienie = więzienie;
    this.maksymalnyCzasRoboty = maksymalnyCzasRoboty;
  }

  @Override
  public void run() {
    kradzież();
  }

  void kradzież() {
    System.out.println(Thread.currentThread().getName() + " Chwila, zagarniam jeszcze łupy!!");
    try {
      Thread.sleep(ThreadLocalRandom.current().nextInt(maksymalnyCzasRoboty));
      czasUciekać();
    } catch (InterruptedException e) {
      System.err.println(Thread.currentThread().getName() + " Dostałem kulkę! Już po mnie");
      więzienie.złapZłodzieja(this);
    }
  }

  void czasUciekać() {
    System.out.println(Thread.currentThread().getName() + " Mam wszystko, czas się zwijać.");
    latch.countDown();
  }
}