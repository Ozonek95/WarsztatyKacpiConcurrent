package G_zadanieZCalosci;

import java.util.List;

/**
 * Zadanie wykorzystujące barierę i countDownLatch.
 * Firma tworzy wyścigi multiplayer. 10 graczy bierze udział w teście na temat
 * poprzedniej wersji gry. 5 pierwszych osób, które go rozwiążą, będą mogli
 * rozegrać 3 rudny we wstępną wersję nowej części!
 *
 * @author Kacper Staszek
 */
class Main {
  
  //TODO: 10 graczy jednocześnie rozpoczyna test. 5 pierwszych może rozegrać 3 wyścigi przeciwko sobie
  //TODO: Pozostałych 5 graczy może skończyć rozwiązywać test.
  //TODO: program kończy się, po rozegraniu 3 wyścigów dema. Wyścig kończy się, kiedy wszyscy gracze dojadą na metę.

  public static void main(String[] args) {

    //TODO: 10 graczy jednocześnie rozpoczyna test. 5 pierwszych może rozegrać 3 wyścigi przeciwko sobie
    //TODO: Pozostałych 5 graczy może skończyć rozwiązywać test.
    //TODO: program kończy się, po rozegraniu 3 wyścigów dema. Wyścig kończy się, kiedy wszyscy gracze dojadą na metę.

    TestZGry testZGry = new TestZGry(10,5);
    testZGry.generujGraczy();
    List<GraczGrającyWGrę> gracze = testZGry.startujTest();
    DemonstracjaGry demonstracjaGry = new DemonstracjaGry(gracze,3);
    demonstracjaGry.startujGrę();
  }
}
