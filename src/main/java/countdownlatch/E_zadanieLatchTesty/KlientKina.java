package countdownlatch.E_zadanieLatchTesty;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Kacper Staszek
 * @author Marcin Ogorzałek
 */
class KlientKina implements Runnable{

  private final Kino kino;
  private final CountDownLatch latch;

  KlientKina(Kino kino, CountDownLatch latch) {
    this.kino = kino;
    this.latch = latch;
  }

  @Override
  public void run() {
    System.out.println("Ale sobie zarezerwuje bilecik!!");
    try {
      kino.rezerwujBilet(this, ThreadLocalRandom.current().nextInt(2000));
      latch.countDown();
    } catch (InterruptedException ignore) {

    }
  }
}
