package countdownlatch.C_zadanieCountDownLatchTimeout;

import fabryczkapomocnicza.MyThreadFactory;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Witamy w zadaniu drugim z CountDownLatch.
 * Ma ono na celu pokazdanie możliwości ustawienia czasu oczekiwania na CountDownLatch.
 * Po zdobyciu podstaw przedstawionych w poprzednich
 * przykładach powinno Ci ono pójść jak z płatka.
 *
 * @see CountDownLatch
 *
 * @author Marcin Ogorzalek
 * @author Kacper Staszek
 *
 * TODO: Kucharzz (czyli Kuchar wraz z kompanami) obrabia jubilera. Policja już jedzie na miejsce zdarzenia
 *  i kierowca ekipy nie może dłużej czekać
 *  Napisz program który to sumuluje. Czas oczekiwania kierowcy nie może być dłuższy niż 5000 ms
 *  podczas gdy chłopaki moga obrabiać jubilera nawet w 10_000 ms.
 *  Kto zdąży ten ucieknie, kto nie - idzie do paki.
 *  Skorzystaj śmiało z fabryki wątków, którą umieściłem w projekcie.
 */

public class Napad {
  private int ilośćLudziWEkipie;
  private long czasDoPrzyjazduPolicji;
  private int maksymalnyCzasRoboty;


  Napad(int ilośćLudziWEkipie, long czasDoPrzyjazduPolicji, int maksymalnyCzasRoboty) {
    this.ilośćLudziWEkipie = ilośćLudziWEkipie;
    this.czasDoPrzyjazduPolicji = czasDoPrzyjazduPolicji;
    this.maksymalnyCzasRoboty = maksymalnyCzasRoboty;
  }

  public static void main(String[] args) {
    Napad napad = new Napad(4, 5000, 10_000);
    napad.uruchamianieWątków(napad.tworzeniePotrzebnychObiektów());
  }

  MetadaneNapadu tworzeniePotrzebnychObiektów() {
    CountDownLatch latch = new CountDownLatch(ilośćLudziWEkipie);
    ExecutorService kierowcaExecutor = Executors.newSingleThreadExecutor();
    ExecutorService ekipaExecutor = Executors.newFixedThreadPool(ilośćLudziWEkipie,
        new MyThreadFactory("Członek ekipy"));
    Więzienie więzienie = new Więzienie();
    return new MetadaneNapadu(latch, kierowcaExecutor, ekipaExecutor, więzienie);
  }

  void uruchamianieWątków(MetadaneNapadu metadaneNapadu) {
    metadaneNapadu.kierowcaExecutor.submit(new Kierowca(metadaneNapadu.latch,
        metadaneNapadu.ekipaExecutor, czasDoPrzyjazduPolicji));
    metadaneNapadu.kierowcaExecutor.shutdown();

    for (int i = 0; i < ilośćLudziWEkipie; i++) {
      metadaneNapadu.ekipaExecutor.submit(new CzłonekEkipy(metadaneNapadu.latch,
          metadaneNapadu.więzienie, maksymalnyCzasRoboty));
    }
    metadaneNapadu.ekipaExecutor.shutdown();
  }

  class MetadaneNapadu {
    CountDownLatch latch;
    ExecutorService kierowcaExecutor;
    ExecutorService ekipaExecutor;
    Więzienie więzienie;

     MetadaneNapadu(CountDownLatch latch, ExecutorService kierowcaExecutor,
        ExecutorService ekipaExecutor, Więzienie więzienie) {
      this.latch = latch;
      this.kierowcaExecutor = kierowcaExecutor;
      this.ekipaExecutor = ekipaExecutor;
      this.więzienie = więzienie;
    }
  }
}


