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
import java.util.concurrent.atomic.AtomicInteger;
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
  private static final String KOMUNIKAT_WAGONIKA = "Wszyscy wsiedli, wyjeżdżam na górę!";

  //TODO: Napisz wyciąg na stok narciarski! Wagonik ma miejsce na 4 osoby, rusza kiedy wszyscy się załadują i tak w kółko ;)
  public static void main(String[] args) {
    CyclicBarrier barrier = new CyclicBarrier(4, ()->System.out.println(KOMUNIKAT_WAGONIKA));
    BlockingQueue<Narciarz> chętni = stwórzKolejkęNarciarzy(barrier);
    uruchomWszystkichNarciarzy(chętni);
  }

  private static BlockingQueue<Narciarz> stwórzKolejkęNarciarzy(CyclicBarrier barrier) {
    return Stream.generate(() -> new Narciarz(barrier))
        .limit(ILOSC_NARCIARZY_CHETNYCH_DO_WYJAZDU)
        .collect(Collectors
            .toCollection(() -> new ArrayBlockingQueue<>(ILOSC_NARCIARZY_CHETNYCH_DO_WYJAZDU)));
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

class Narciarz implements Runnable {

  private final CyclicBarrier barrier;

  Narciarz(CyclicBarrier barrier) {
    this.barrier = barrier;
  }

  @Override
  public void run() {
    System.out.println("Idę do wagonika " + this);
    try {
      Thread.sleep(ThreadLocalRandom.current().nextInt(5000));
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