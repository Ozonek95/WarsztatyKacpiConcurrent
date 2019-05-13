package countdownlatch.C_zadanieCountDownLatch;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * <p>Czas na pierwsze większe zadanko dla Ciebie :) Będzie ono bardzo podobne do zadania z
 * przykładu, jednak dla własnego dobra staraj się tam nie zaglądać. Użyj CountDownLatch, poczytaj
 * dokumentację jeżeli nie jesteś czegoś pewien. Jeżeli zrozumiałeś koncept, z zadaniem powinieneś
 * sobie poradzić sam :)</p>
 *
 * @author Kacper Staszek
 */
public class Main {

  //TODO: Napisz program, który symuluje NIELEGALNE WYSCIGI W LA! Coś jak Need For Speed ;D
  //TODO:5 kierowców otrzymało smsa z miejscem rozpoczęcia, czym prędzej jadą na miejsce! Jednak każdy jest w innym
  //TODO:miejscu miasta, więc czas dotarcia będzie różny. Dopiero kiedy wszyscy pojawią się na miejscu - wyścig ruszy!
  //TODO:Skorzystaj śmiało z fabryki wątków, którą umieściłem w projekcie.

  static final int LICZBA_UCZESTNIKÓW = 5;

  public static void main(String[] args) {

  }
}

class Race implements Runnable {

  private final CopyOnWriteArrayList<Driver> driversReadyToStart = new CopyOnWriteArrayList<>();

  Race(CountDownLatch countDownLatch) {

  }

  @Override
  public void run() {
    organiseRace();
  }

  void organiseRace() {
    System.out.println("Wysyłam info o wyścigu. Czekamy na uczestników");
//        Pomyślcie czy czegoś tu nie brakuje
    System.out.println("Wszyscy są, ruszamy");
  }

  void addDriver(Driver driver) {

  }

  int getDriversReadyToStart() {
    return driversReadyToStart.size();
  }

}

class Driver implements Runnable {

  Driver(CountDownLatch latch, Race race) {

  }

  @Override
  public void run() {
    System.out.println("Dostałem sms o wyścigu. Wyruszam na start ");
    driveToStartLine();
    comeToStartLine();
  }

  void driveToStartLine() {
    System.out.println("Jadę na start " + Thread.currentThread().getName());
  }

  void comeToStartLine() {
    System.out.println("Dojechałem na start i jestem gotowy ");
  }
}

