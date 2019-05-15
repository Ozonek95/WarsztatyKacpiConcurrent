package phaser.H_wprowadzeniePhaser1;

import fabryczkapomocnicza.MyThreadFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Klasa Phaser wprowadzona w Java 7 zapewnia funkcjonbalności podobne do CountDOwnLatch oraz
 * CyclicBarrier. Pozwala ona na utworzenie barriey synchornizującej ze sobą wiele wątków
 *
 * @author Marcin Ogorzalek
 * @see Phaser
 *
 * W przeciwieństwie do poprzednio opisanych klas jest ona bardziej elastyczna. Pozwala ona w czasie
 * wykonywania programu na zwiększanie (Registration) oraz zmniejszanie (Deregistration) liczy
 * synchronizowanych wątków.
 *
 * W poniższym przykłądzie symulujemy użycie Fazera jako CountDownLatch. Różnicą jest to, że ten sam
 * obiek Fazera może zostać użyty wielokrotnie dla różych grup wątków.
 */
public class WprowadzeniePhaserJakoCountDownLatch {

  public static void main(String[] args) {
    Phaser phaser = new Phaser(
        1); // Fazer rejestruje wątek w którym się znajduje, konstrukcja ta jest równoważna z:
//    Phaser phaser = new Phaser();
//    phaser.register();
//    Musimy zarejestrować wątek w którym jest Fazer ponieważ ma on pełnić role koordynatora dla pozostałych wąktów
    ThreadFactory threadFactory = new MyThreadFactory(
        "Zadanie"); // Ponieważ potrzeba stałego licznika
    // wątków musimy przekazywać referencję do tej samej fabryki do każdej instancji ExecutorService

    System.out.println("Liczba wątków zarejestrowanych w fazerze=" + phaser.getRegisteredParties());
    System.out.println("Czy fazer jest zakończony? " + phaser.isTerminated());

//    Faza 0
    System.out.println("Faza " + phaser.getPhase());
    startTasks(4, phaser, threadFactory);
    phaser.arriveAndAwaitAdvance();

//   Faza 1
    System.out.println("Faza " + phaser.getPhase());
    startTasks(2, phaser, threadFactory);
    phaser.arriveAndAwaitAdvance();

//    Faza 2
    System.out.println("Faza " + phaser.getPhase());
    phaser.bulkRegister(4); // masowa rejestracja zadań w Fazerze
    for (int i = 0; i < 4; i++) {
      new Thread(() -> {
        System.out.println(
            "Zadanko z nowo dodanych parties wymagające synchronizacji: " + Thread.currentThread()
                .getName());
        try {
          Thread.sleep(2000); //Symulujemy, że zadanie zajmuje troszkę czasu
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println("Zadanko skonczone: " + Thread.currentThread().getName());
        phaser
            .arriveAndDeregister(); // wyrejestrowanie z Fazera w momęcie gdy synchronizacja nie jest już potrzebana
      }, "Zadanko " + i).start();
    }
    phaser.arriveAndAwaitAdvance();

//    Faza 3
    System.out.println("Faza " + phaser.getPhase());
    phaser.arriveAndDeregister(); // wyrejestrowanie fazera i jego zakończenie
    System.out.println("Liczba wątków zarejestrowanych w fazerze=" + phaser.getRegisteredParties());
    System.out.println("Czy fazer jest zakończony? " + phaser.isTerminated());
  }

  private static void startTasks(int numberOfTasks, Phaser phaser, ThreadFactory threadFactory) {
    ExecutorService service = Executors.newFixedThreadPool(numberOfTasks, threadFactory);
    for (int i = 0; i < numberOfTasks; i++) {
      service.submit(new Task(phaser));
    }
    service.shutdown();
  }
}

class Task implements Runnable {

  public static final String GREEN = "\033[0;32m";
  public static final String RESET = "\033[0m";

  private final Phaser phaser;

  public Task(Phaser phaser) {
    this.phaser = phaser;
  }

  @Override
  public void run() {
    phaser.register(); // rejestracja do Fazera
    System.out.println(GREEN + "Zadanie wymagające synchronizacji: " + this + RESET);
    try {
      Thread.sleep(ThreadLocalRandom.current()
          .nextInt(1000));  //Symulujemy, że zadanie zajmuje troszkę czasu
    } catch (InterruptedException ignore) {
      System.err.println(ignore.getMessage());
    }
    System.out.println(GREEN + "Skończyłem moje zadanko! " + this + RESET);
    phaser
        .arriveAndDeregister(); // wyrejestrowanie z Fazera w momęcie gdy synchronizacja nie jest już potrzebana
  }

  @Override
  public String toString() {
    return Thread.currentThread().getName();
  }
}
