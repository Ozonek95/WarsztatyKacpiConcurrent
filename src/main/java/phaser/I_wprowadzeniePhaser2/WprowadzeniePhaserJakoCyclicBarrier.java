package phaser.I_wprowadzeniePhaser2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Phaser;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Pokaz możliwoścy klasy Phaser, zastosowany do trzymania kontroli
 * nad cyklicznym wykonaniem zadań w zależności od faz.
 *
 * @see Phaser
 * @author Kacper Staszek
 */
class WprowadzeniePhaserJakoCyclicBarrier {

  private static final int ILOŚĆ_FAZ_W_KTÓRYCH_UCZESTNICZY_ZADANIE_DŁUGIE = 4;
  private static final int ILOŚĆ_FAZ_W_KTÓRYCH_UCZESTNICZY_ZADANIE_KRÓTKIE = 2;

  public static void main(String[] args) {
    Phaser phaser = new Phaser();

    ZarządcaZadań<Zadanie> zarządcaZadań = new ZarządcaZadań<>();

    ArrayBlockingQueue<Zadanie> zadaniaKrótkie = stwórzKrótkieZadania(phaser,2);

    ArrayBlockingQueue<Zadanie> zadaniaDługie = stwórzDługieZadania(phaser,4);

    zarządcaZadań.wykonajZadania(zadaniaKrótkie,"Zadanie krótkie");
    zarządcaZadań.wykonajZadania(zadaniaDługie,"Zadanie długie");

    /*
    * Zauważ, że przy zmianie fazy wątek główny będzie mógł kontynuować pracę.
    * */
    phaser.awaitAdvance(0);
    System.out.println("Wykonam się po fazie 0");
    phaser.awaitAdvance(1);
    System.out.println("Wykonam się po fazie 1");
    phaser.awaitAdvance(2);
    System.out.println("Wykonam się po fazie 2");
    phaser.awaitAdvance(3);
    System.out.println("Wykonam się po fazie 3");
    phaser.awaitAdvance(4);
    System.out.println("Wykonam się po fazie 4");

    /*
    * Do phasera możemy się zarejestrować w każdym momencie, jeżeli isTerminated() zwraca false.
    */
    phaser.register();

    try {
      Thread.sleep(2000);
    } catch (InterruptedException ignored) {
      ignored.printStackTrace();
    }

    phaser.arriveAndAwaitAdvance();
    System.out.println("Wszystkie zadania zakończone!");
  }

  private static ArrayBlockingQueue<Zadanie> stwórzDługieZadania(Phaser phaser, int ilośćZadań) {
    return Stream
          .generate(() -> new ZadanieDługie(phaser, ILOŚĆ_FAZ_W_KTÓRYCH_UCZESTNICZY_ZADANIE_DŁUGIE))
          .limit(ilośćZadań)
          .collect(Collectors.toCollection(() -> new ArrayBlockingQueue<>(ilośćZadań)));
  }

  private static ArrayBlockingQueue<Zadanie> stwórzKrótkieZadania(Phaser phaser, int ilośćZadań) {
    return Stream
          .generate(() -> new ZadanieKrótkie(phaser, ILOŚĆ_FAZ_W_KTÓRYCH_UCZESTNICZY_ZADANIE_KRÓTKIE))
          .limit(ilośćZadań)
          .collect(Collectors.toCollection(() -> new ArrayBlockingQueue<>(ilośćZadań)));
  }

}
