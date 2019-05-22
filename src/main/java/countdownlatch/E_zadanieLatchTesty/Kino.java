package countdownlatch.E_zadanieLatchTesty;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Kacper Staszek
 * @author Marcin Ogorzałek
 */

class Kino implements Runnable {
  private final CountDownLatch latch;
  List<KlientKina> klienciZRezerwacją = new ArrayList<>();
  Lock lock = new ReentrantLock();

  Kino(CountDownLatch latch) {
    this.latch = latch;
  }

  @Override
  public void run() {
    try {
      latch.await();
      System.out.println("Startujemy seans!");
    } catch (InterruptedException ignore) {

    }
  }

  void rezerwujBilet(KlientKina klientKina, int ileCzasuKlientZajmujeBilet)
      throws InterruptedException {
    try {
      lock.lock();
      System.out.println("Ktoś rezerwuje u nas bilecik.");
      Thread.sleep(ileCzasuKlientZajmujeBilet);
      klienciZRezerwacją.add(klientKina);
      if(latch.getCount()>1) {
        System.out.println("Bilecik zarezerwowany, zostało jeszcze " + (latch.getCount()-1));
      } else {
        System.out.println("Ostatni bilet zarezerwowany!");
      }
    } finally {
      lock.unlock();
    }
  }
}
