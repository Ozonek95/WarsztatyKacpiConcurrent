package phaser.I_wprowadzeniePhaser2;

import fabryczkapomocnicza.MyThreadFactory;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Generyczny wykonawca kolejek zadań.
 *
 * @author Kacper Staszek
 * @see ExecutorService
 */
class ZarządcaZadań<T extends Runnable> {

  /**
   * @param kolejkaZadań metoda uruchomi wszystkie zadania z kolejki.
   * @param nazwaZadań wątki będą nazwane według przekazanego parametru i ponumerowane.
   * @see MyThreadFactory
   */

  void wykonajZadania(BlockingQueue<T> kolejkaZadań, String nazwaZadań) {
    ExecutorService executorService = Executors
        .newCachedThreadPool(new MyThreadFactory(nazwaZadań));
    for (T zadanie : kolejkaZadań) {
      executorService.submit(zadanie);
    }
    executorService.shutdown();
  }
}
