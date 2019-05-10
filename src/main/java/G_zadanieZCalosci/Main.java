package G_zadanieZCalosci;

import java.util.List;

/**
 * @author Kacper Staszek
 */
class Main {

  public static void main(String[] args) {

    TestZGry testZGry = new TestZGry(10,5);
    testZGry.generujGraczy();
    List<GraczGrającyWGrę> gracze = testZGry.startujTest();
    DemonstracjaGry demonstracjaGry = new DemonstracjaGry(gracze,3);
    demonstracjaGry.startujGrę();
  }
}
