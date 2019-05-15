package phaser.I_wprowadzeniePhaser2;

import java.util.concurrent.Phaser;

/**
 * Abstrakcja do tworzenia konkretnych typów zadań.
 *
 * @author Kacper Staszek
 */
abstract class Zadanie implements  Runnable {
  final Phaser phaser;
  private final int ilośćCykliDoWykonania;
  boolean jobDone = false;

  /**
   * @param phaser do niego odnosiła się będzie logika cyklicznego wykonania zadania.
   * @param ilośćCykliDoWykonania po określonej fazie, zadanie nie będzie dłużej wykonywane.
   */
  Zadanie(Phaser phaser, int ilośćCykliDoWykonania) {
    this.phaser = phaser;
    this.ilośćCykliDoWykonania = ilośćCykliDoWykonania;
  }

  /**
   * Jeżeli dana faza Phasera jest równa ilości cyklów do wykonania, zadanie skończy się wykonywać i wyrejestruje się z Phasera.
   * inaczej będzie kontynuowane.
   *
   */
  void sprawdźCzyKontynuować() {
    if (phaser.getPhase() == ilośćCykliDoWykonania) {
      System.out.println("Wyrejestrowuje się");
      phaser.arriveAndDeregister();
      jobDone=true;
    } else {
      phaser.arriveAndAwaitAdvance();
    }
  }
}
