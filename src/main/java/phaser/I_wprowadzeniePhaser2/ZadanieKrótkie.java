package phaser.I_wprowadzeniePhaser2;

import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implementacja krótko wykonującego się zadania.
 *
 * @author Kacper Staszek
 */
class ZadanieKrótkie extends Zadanie {


  ZadanieKrótkie(Phaser phaser, int ilośćCykliDoWykonania) {
    super(phaser, ilośćCykliDoWykonania);
  }

  @Override
  public void run() {
    phaser.register();
    while (!jobDone) {
      System.out.println("Rozpoczynam " + Thread.currentThread().getName());
      try {
        Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3500));
      } catch (InterruptedException ignored) {
        System.err.println(ignored.getMessage());
      }
      System.out.println("Skończyłem " + Thread.currentThread().getName());

      sprawdźCzyKontynuować();
    }
  }
}




