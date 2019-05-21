package G_zadanieZCalosci;

import java.util.List;

/**
 * Zadanie wykorzystujące barierę i countDownLatch.
 * Firma tworzy wyścigi multiplayer. 10 graczy bierze udział w teście na temat
 * poprzedniej wersji gry. 5 pierwszych osób, które go rozwiążą, będą mogli
 * rozegrać 3 rudny we wstępną wersję nowej części!
 *
 * @author Kacper Staszek
 * @author Marcin Ogorzałek
 *
 * TODO: 10 graczy jednocześnie rozpoczyna test. 5 pierwszych może rozegrać 3 wyścigi przeciwko sobie
 *  Pozostałych 5 graczy może skończyć rozwiązywać test.
 *  program kończy się, po rozegraniu 3 wyścigów dema. Wyścig kończy się, kiedy wszyscy gracze dojadą na metę.
 */
class WydarzenieGamingowe {

  private static final int LICZBA_GRACZY_BIORĄCYCH_UDZIAŁ_W_TEŚCIE = 10;
  private static final int LICZBA_GRACZY_KTÓRZY_PRZEJDĄ_DALEJ = 5;
  private static final int ILOŚĆ_RUND_DO_ROZEGRANIA_PODCZAS_DEMONSTRACJI = 3;

  public static void main(String[] args) {
    TestWiedzyZGry testWiedzyZGry = new TestWiedzyZGry(LICZBA_GRACZY_BIORĄCYCH_UDZIAŁ_W_TEŚCIE,
        LICZBA_GRACZY_KTÓRZY_PRZEJDĄ_DALEJ);
    testWiedzyZGry.generujGraczy();
    List<GraczGrającyWGrę> gracze = testWiedzyZGry.startujTest();
    DemonstracjaGry demonstracjaGry = new DemonstracjaGry(gracze,
        ILOŚĆ_RUND_DO_ROZEGRANIA_PODCZAS_DEMONSTRACJI);
    demonstracjaGry.startujGrę();
  }
}
