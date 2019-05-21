package countdownlatch.D_zadanieCountDownLatch;

import fabryczkapomocnicza.MyThreadFactory;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Aby utrwalić wiedzę, kolejne zadanie z użyciem klasy CountDownLatch.
 *
 * @see CountDownLatch
 * @see countdownlatch.A_wprowadzenieCountDownLatch.WprowadzenieCountDownLatch
 *
 * @author Kacper Staszek
 * @author Marcin Ogorzałek
 *
 * TODO: Napisz program, który będzie symulował wielki internetowy konkurs wiedzy z historii. Pięciu
 *  uczestników podchodzi do wypełnienia kwestionariusza osobowego. Dopiero jak wszyscy skończą, test
 *  może się rozpocząć. Po rozpoczęciu testu, czekamy na wszystkich aż skończą i dopiero wtedy
 *  ogłaszamy wyniki.
 */
class KonkursWiedzyOHistorii {

  private final int liczbaUczestnków;

  KonkursWiedzyOHistorii(int liczbaUczestnków) {
    this.liczbaUczestnków = liczbaUczestnków;
  }

  public static void main(String[] args) {
    KonkursWiedzyOHistorii konkurs = new KonkursWiedzyOHistorii(5);
    MetadaneKonkursu metadaneKonkursu = konkurs.stwórzPotrzebneObiekty();
    konkurs.startujWątki(metadaneKonkursu);
  }

  MetadaneKonkursu stwórzPotrzebneObiekty() {
    return new MetadaneKonkursu(
        new CountDownLatch(liczbaUczestnków),
        new CountDownLatch(liczbaUczestnków),
        Executors.newFixedThreadPool(liczbaUczestnków, new MyThreadFactory("Uczestnik konkursu")));
  }

  void startujWątki(MetadaneKonkursu metadaneKonkursu) {
    new Thread(new Test(metadaneKonkursu.zatrzaskDoRozpoczęciaTestu,
        metadaneKonkursu.zatrzaskDoOgłoszeniaWyników),
        "Wielki test z historii").start();
    for (int i = 0; i < liczbaUczestnków; i++) {
      metadaneKonkursu.executorService.submit(
          new UczestnikKonkursu(metadaneKonkursu.zatrzaskDoRozpoczęciaTestu
              , metadaneKonkursu.zatrzaskDoOgłoszeniaWyników));
    }
    metadaneKonkursu.executorService.shutdown();
  }

  class MetadaneKonkursu {

    final CountDownLatch zatrzaskDoRozpoczęciaTestu;
    final CountDownLatch zatrzaskDoOgłoszeniaWyników;
    final ExecutorService executorService;

    MetadaneKonkursu(CountDownLatch zatrzaskDoRozpoczęciaTestu,
        CountDownLatch zatrzaskDoOgłoszeniaWyników,
        ExecutorService executorService) {
      this.zatrzaskDoRozpoczęciaTestu = zatrzaskDoRozpoczęciaTestu;
      this.zatrzaskDoOgłoszeniaWyników = zatrzaskDoOgłoszeniaWyników;
      this.executorService = executorService;
    }
  }
}
