package countdownlatch.C_zadanieCountDownLatch;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

class Wyścig implements Runnable {
  private final CountDownLatch latch;
  private final CopyOnWriteArrayList<Kierowca> kierowcyGotowiDoStartu = new CopyOnWriteArrayList<>();

  Wyścig(CountDownLatch countDownLatch) {
    this.latch = countDownLatch;
  }

  @Override
  public void run() {
    organizujWyścig();
  }

  void organizujWyścig() {
    try {
      latch.await();
      rozpocznijWyścig();
    } catch (InterruptedException ignore) {

    }
  }

  void rozpocznijWyścig() {
    System.out.println("Wszyscy są, ruszamy");
  }

  void dodajKierowcę(Kierowca kierowca) {
    kierowcyGotowiDoStartu.add(kierowca);
  }

  int getKierowcyGotowiDoStartu() {
    return kierowcyGotowiDoStartu.size();
  }

}