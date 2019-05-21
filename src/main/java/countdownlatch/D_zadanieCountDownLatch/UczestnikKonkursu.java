package countdownlatch.D_zadanieCountDownLatch;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Kacper Staszek
 * @author Marcin Ogorzałek
 */
class UczestnikKonkursu implements Runnable {

  private final CountDownLatch zatrzaskDoRozpoczęciaTestu;
  private final CountDownLatch zatrzaskDoOgłoszeniaWynikówJakWszyscySkończąTest;

  UczestnikKonkursu(CountDownLatch zatrzaskDoRozpoczęciaTestu,
      CountDownLatch zatrzaskDoOgłoszeniaWynikówJakWszyscySkończąTest) {
    this.zatrzaskDoRozpoczęciaTestu = zatrzaskDoRozpoczęciaTestu;
    this.zatrzaskDoOgłoszeniaWynikówJakWszyscySkończąTest = zatrzaskDoOgłoszeniaWynikówJakWszyscySkończąTest;
  }

  @Override
  public void run() {
    wypełnianieKwestionariusza();
    czekanieNaWszystkichUczestników();
    pisanieTestu();
  }

  private void wypełnianieKwestionariusza() {
    System.out.println("Wypełniam formularze niezbędne do wzięcia udziału w teście - " + this);
    symulacjaTrawniaZadania();
    //FIXME: Popraw drukowanie tak, żeby wyświetlało prawidłowy komunikat. NIE UŻYWAJ ŻADNEGO POMOCNICZEGO LICZNIKA.
    if (zatrzaskDoRozpoczęciaTestu.getCount() > 1) {
      System.out.println("Skończyłem wypełniać kwestionariusz, czekam na resztę - " + this);
    } else {
      System.out.println("Skończyłem wypełniać kwestionariusz, chyba byłem ostatni :( - " + this);
    }
    zatrzaskDoRozpoczęciaTestu.countDown();
  }

  private void symulacjaTrawniaZadania() {
    try {
      MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(1000, 4000));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void czekanieNaWszystkichUczestników() {
    try {
      zatrzaskDoRozpoczęciaTestu.await();
    } catch (InterruptedException ignored) {

    }
  }

  private void pisanieTestu() {
    try {
      MILLISECONDS.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Piszę sobie test - " + this);
    symulacjaTrawniaZadania();
    if (zatrzaskDoOgłoszeniaWynikówJakWszyscySkończąTest.getCount() - 1 > 0) {
      //FIXME: Popraw drukowanie tak, żeby wyświetlało prawidłową liczbę uczestników. NIE UŻYWAJ ŻADNEGO POMOCNICZEGO LICZNIKA.
      System.out.printf("Skończyłem - %s, zostało jeszcze %d uczestników, którzy piszą%n",
          this, zatrzaskDoOgłoszeniaWynikówJakWszyscySkończąTest.getCount() - 1);
    } else {
      System.out.println("Skończyłem, byłem ostatni - " + this);
    }
    zatrzaskDoOgłoszeniaWynikówJakWszyscySkończąTest.countDown();
  }

  @Override
  public String toString() {
    return Thread.currentThread().getName();
  }
}