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

  }

  private static BlockingQueue<Narciarz> stwórzKolejkęNarciarzy(CyclicBarrier barrier) {
    return null;
  }

  private static void uruchomWszystkichNarciarzy(BlockingQueue<Narciarz> chętni) {
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
   wsiadamDoWagonika();
  }

  void wsiadamDoWagonika()  {
    System.out.println("Ok, jestem gotów do wyjazdu! " + this);
  //    Co więcej tu potrzeba?
  }

  @Override
  public String toString() {
    return Thread.currentThread().getName();
  }
}