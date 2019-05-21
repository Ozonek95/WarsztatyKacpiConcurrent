package countdownlatch.E_zadanieLatchTesty;

import fabryczkapomocnicza.MyThreadFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Kacper Staszek
 * @author Marcin Ogorzałek
 */
class RezerwacjaBiletówDoKina {
  private static final int LICZBA_DOSTĘPNYCH_BIETÓW = 10;

  int liczbaBiletów;

  public RezerwacjaBiletówDoKina(int liczbaBiletów) {
    this.liczbaBiletów = liczbaBiletów;
  }

  public static void main(String[] args) {
    RezerwacjaBiletówDoKina rezerwacjaBiletówDoKina =
        new RezerwacjaBiletówDoKina(LICZBA_DOSTĘPNYCH_BIETÓW);
    MatadaneKina metadaneKina = rezerwacjaBiletówDoKina.stworzeniePotrzebnychObiektów();
    rezerwacjaBiletówDoKina.uruchomienieRezerwacji(metadaneKina);
  }

  MatadaneKina stworzeniePotrzebnychObiektów() {
    CountDownLatch latch = new CountDownLatch(10);
    Kino kino = new Kino(latch);
    ExecutorService service = Executors.newFixedThreadPool(10,
        new MyThreadFactory("Klient"));

    return new MatadaneKina(latch, kino, service);
  }

  void uruchomienieRezerwacji(MatadaneKina metadaneKina) {
    new Thread(metadaneKina.kino,"Kino").start();
    for (int i = 0; i < 10; i++) {
      metadaneKina.service.submit(new KlientKina(metadaneKina.kino, metadaneKina.latch));
    }
    metadaneKina.service.shutdown();
  }

  class MatadaneKina {
    CountDownLatch latch;
    Kino kino;
    ExecutorService service;

    public MatadaneKina(CountDownLatch latch, Kino kino, ExecutorService service) {
      this.latch = latch;
      this.kino = kino;
      this.service = service;
    }
  }
}

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
      kino.rezerwujBilet(this, ThreadLocalRandom.current().nextInt(5000));
    } catch (InterruptedException ignore) {

    }
  }
}

class Kino implements Runnable {
  private final CountDownLatch latch;
  private List<KlientKina> klienciZRezerwacją = new ArrayList<>();
  private Lock lock = new ReentrantLock();

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
      latch.countDown();
      klienciZRezerwacją.add(klientKina);
      if(latch.getCount()>0) {
        System.out.println("Bilecik zarezerwowany, zostało jeszcze " + latch.getCount());
      } else {
        System.out.println("Ostatni bilet zarezerwowany!");
      }
    } finally {
      lock.unlock();
    }
  }
}
