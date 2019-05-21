package countdownlatch.D_zadanieCountDownLatch;

import java.util.concurrent.CountDownLatch;

/**
 * @author Kacper Staszek
 * @author Marcin Ogorzałek
 */
class Test implements Runnable {

  final CountDownLatch zatrzaskDoRozpoczęciaTestu;
  final CountDownLatch zatrzaskDoOgłoszeniaWynikówJakWszyscySkończąTest;

  Test(CountDownLatch zatrzaskDoRozpoczęciaTestu,
      CountDownLatch zatrzaskDoOgłoszeniaWynikówJakWszyscySkończąTest) {
    this.zatrzaskDoRozpoczęciaTestu = zatrzaskDoRozpoczęciaTestu;
    this.zatrzaskDoOgłoszeniaWynikówJakWszyscySkończąTest = zatrzaskDoOgłoszeniaWynikówJakWszyscySkończąTest;
  }

  @Override
  public void run() {
    try {
      System.out.println("\033[1;32mWitamy na wielkim teście z historii! \033[0m");
      zatrzaskDoRozpoczęciaTestu.await();
      System.out.println("\033[1;32mWszyscy gotowi, rozpoczynamy test! \033[0m");
    } catch (InterruptedException ignored) {

    }
    try {
      zatrzaskDoOgłoszeniaWynikówJakWszyscySkończąTest.await();
      System.out.println(
          "\033[1;32mWszyscy skończyli pisać test! Gratulujemy, ogłaszamy wyniki! \033[0m");
    } catch (InterruptedException ignored) {

    }
  }
}