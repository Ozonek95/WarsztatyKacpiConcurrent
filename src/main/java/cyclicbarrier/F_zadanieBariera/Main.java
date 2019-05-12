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
 * :)
 * <p>
 * Powodzenia!
 *
 * @author Kacper Staszek
 */
public class Main {

  private static final int ILOSC_NARCIARZY_CHETNYCH_DO_WYJAZDU = 20;

  //TODO: Napisz wyciąg na stok narciarski! Wagonik ma miejsce na 4 osoby, rusza kiedy wszyscy się załadują i tak w kółko ;)
  public static void main(String[] args) {
    BlockingQueue<Narciarz> chętni = null;
    uruchomWszystkichNarciarzy(chętni);
  }

  private static BlockingQueue<Narciarz> stwórzKolejkęNarciarzy(CyclicBarrier barrier) {
    return null;
  }

  private static void uruchomWszystkichNarciarzy(BlockingQueue<Narciarz> chętni) {
    ExecutorService executorService = Executors
        .newFixedThreadPool(4, new MyThreadFactory("Narciarz "));

    for (int i = 0; i < ILOSC_NARCIARZY_CHETNYCH_DO_WYJAZDU; i++) {
      try {
        executorService.submit(Objects.requireNonNull(chętni.take()));
      } catch (InterruptedException ignored) {
        System.err.println(ignored.getMessage());
      }
    }
    executorService.shutdown();
  }
}

class Wagonik implements Runnable {

  @Override
  public void run() {
    System.out.println("Wszyscy wsiedli, wyjeżdżam na górę!");
  }

  public CyclicBarrier getBarrier() {
    return null;
  }
}

class Narciarz implements Runnable {

  @Override
  public void run() {
    System.out.println("Idę do wagonika " + this);

  }

  int wsiadamDoWagonika() throws InterruptedException, BrokenBarrierException {
//    To z pewnością nie jest dobra wartość
    return -1;
  }

  @Override
  public String toString() {
    return Thread.currentThread().getName();
  }
}