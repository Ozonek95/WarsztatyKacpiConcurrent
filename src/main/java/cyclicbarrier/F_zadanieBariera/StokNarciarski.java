package cyclicbarrier.F_zadanieBariera;

import fabryczkapomocnicza.MyThreadFactory;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Zadanko z bariery ;D Znowu zachęcam, aby nie zaglądać do kodu z przykładu, tylko próbować samemu
 * :) Powodzenia!
 *
 * @author Kacper Staszek
 * @author Marcin Ogorzałek
 *
 * TODO: Napisz wyciąg na stok narciarski! Wagonik ma miejsce na 4 osoby, rusza kiedy wszyscy się
 *  załadują i tak w kółko ;)
 */
public class StokNarciarski {

  final int wielkośćWagonika;
  final int iloscNarciarzyChetnychDoWyjazdu;
  final CyclicBarrier barrier;
  ExecutorService executorService;

  public StokNarciarski(int wielkośćWagonika, int iloscNarciarzyChetnychDoWyjazdu,
      CyclicBarrier barrier) {
    this.wielkośćWagonika = wielkośćWagonika;
    this.iloscNarciarzyChetnychDoWyjazdu = iloscNarciarzyChetnychDoWyjazdu;
    this.barrier = barrier;
  }

  public static void main(String[] args) {
    int wielkśćWagonika = 4;
    CyclicBarrier barrier = new CyclicBarrier(wielkśćWagonika,
        () -> System.out.println("Wagonik dojechał!"));
    StokNarciarski stokNarciarski = new StokNarciarski(wielkśćWagonika,20, barrier);

    BlockingQueue<Narciarz> chętni = stokNarciarski.stwórzKolejkęNarciarzy(stokNarciarski.barrier);
    stokNarciarski.uruchomWszystkichNarciarzy(chętni);
  }

  BlockingQueue<Narciarz> stwórzKolejkęNarciarzy(CyclicBarrier barrier) {
    return Stream.generate(() -> new Narciarz(barrier))
        .limit(iloscNarciarzyChetnychDoWyjazdu)
        .collect(Collectors
            .toCollection(() -> new ArrayBlockingQueue<>(iloscNarciarzyChetnychDoWyjazdu)));
  }

  void uruchomWszystkichNarciarzy(BlockingQueue<Narciarz> chętni) {
    executorService = Executors
        .newFixedThreadPool(wielkośćWagonika, new MyThreadFactory("Narciarz "));

    for (int i = 0; i < iloscNarciarzyChetnychDoWyjazdu; i++) {
      try {
        executorService.submit(Objects.requireNonNull(chętni.take()));
      } catch (InterruptedException ignored) {
        System.err.println(ignored.getMessage());
      }
    }
    executorService.shutdown();
  }
}

class Narciarz implements Runnable {

  private final CyclicBarrier barrier;

  Narciarz(CyclicBarrier barrier) {
    this.barrier = barrier;
  }

  @Override
  public void run() {
    System.out.println("Idę do wagonika " + this);
    try {
      Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
      wsiadamDoWagonika();
    } catch (InterruptedException | BrokenBarrierException ignore) {
      System.err.println(ignore.getMessage());
    }
  }

  int wsiadamDoWagonika() throws InterruptedException, BrokenBarrierException {
    System.out.println("Ok, jestem gotów do wyjazdu! " + this);
    System.out.println("W wagoniku siedzi już "
        + (barrier.getNumberWaiting() + 1) + " narciarzy");
    return barrier.await();
  }

  @Override
  public String toString() {
    return Thread.currentThread().getName();
  }
}