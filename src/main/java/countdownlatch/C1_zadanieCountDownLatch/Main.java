package countdownlatch.C1_zadanieCountDownLatch;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * Witamy w zadaniu drugim z CountDownLatch. Po zdobyciu podstaw przedstawionych w poprzednich
 * przykładach powinno Ci ono pójść jak z płatka.
 *
 * @author Marcin Ogorzalek
 */

public class Main {
  // TODO: Michale wraz z kompanami obrabia jubilera. Policja już jedzie na miejsce zdarzenia
  // TODO: i kierowca ekipy nie może dłużej czekać
  // TODO: Napisz program który to sumuluje. Czas oczekiwania kierowcy nie może być dłuższy jak 5000 ms
  // TODO: podczas gdy chłopaki moga obrabiać jubilera nawet do 10_000 ms.
  // TODO: Kto zdąży ten ucieknie, kto nie - idzie do paki.
  // TODO: Skorzystaj śmiało z fabryki wątków, którą umieściłem w projekcie. 

  static final int ILOSC_LUDZI_W_EKIPIE = 4;

  public static void main(String[] args) {

    CountDownLatch latch = null;

    ExecutorService executorService = null;

    kierowcaPodjeżdzaNaRobotę(latch, executorService);
    Więzienie więzienie = new Więzienie();
    ekipaZaczynaObrabiaćJubilera(latch, executorService, więzienie);
  }

  private static void kierowcaPodjeżdzaNaRobotę(CountDownLatch latch,
      ExecutorService executorService) {

  }

  private static void ekipaZaczynaObrabiaćJubilera(CountDownLatch latch,
      ExecutorService executorService, Więzienie więzienie) {

  }
}

class Kierowca implements Runnable {

  public Kierowca(CountDownLatch latch, ExecutorService executorService, long timeout) {

  }

  @Override
  public void run() {
    System.out.println("Szybko chłopaki, do samochodzu. Policja jest w drodze");
    ucieczka();
  }

  void ucieczka() {
    System.out.println("No najwyższy czas");
    System.err.println("Dopadli nas!");
  }
}

class CzłonekEkipy implements Runnable {

  CzłonekEkipy(CountDownLatch latch, Więzienie więzienie) {
  }


  @Override
  public void run() {
    kradzież();
  }

  void kradzież() {
    System.out.println(Thread.currentThread().getName() + " Chwila, zagarniam jeszcze łupy!!");
    czasUciekać();
    System.err.println(Thread.currentThread().getName() + " Dostałem kulkę! Już po mnie");
  }

  void czasUciekać() {
    System.out.println(Thread.currentThread().getName() + " Mam wszystko, czas się zwijać.");
  }
}

class Więzienie {

  private final CopyOnWriteArrayList<CzłonekEkipy> więźniowie = new CopyOnWriteArrayList<>();

  void złapZłodzieja(CzłonekEkipy członekEkipy) {
    System.out.println("Bandzior złapany");
    więźniowie.add(członekEkipy);
  }

  public CopyOnWriteArrayList<CzłonekEkipy> getWięźniowie() {
    return więźniowie;
  }
}

