package countdownlatch.F_zadanieCountDownLatchTimeout;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Kacper Staszek
 * @author Marcin Ogorzałek
 */
class Więzienie {
  private final CopyOnWriteArrayList<CzłonekEkipy> więźniowie = new CopyOnWriteArrayList<>();

  void złapZłodzieja(CzłonekEkipy członekEkipy) {
    więźniowie.add(członekEkipy);
    System.out.println("Bandzior złapany");
  }

  CopyOnWriteArrayList<CzłonekEkipy> getWięźniowie() {
    return więźniowie;
  }
}

