package G_zadanieZCalosci;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * @author Kacper Staszek
 */
class TestZGry {

  private CountDownLatch countDownLatch;
  private List<GraczRozwiązującyTest> listaGraczyRozwiązującychTest;
  private CopyOnWriteArrayList<GraczGrającyWGrę> listaGraczyKtórzyPrzeszli;

  TestZGry(int liczbaGraczyBiorących, int liczbaGraczyKtóraPrzejdzieDalej) {
    listaGraczyRozwiązującychTest = generujGraczy();
  }

  List<GraczRozwiązującyTest> generujGraczy() {
    return null;
  }

  List<GraczGrającyWGrę> startujTest() {

    System.out.println("Mamy zwycięsców testu!");
    return listaGraczyKtórzyPrzeszli;
  }

  CopyOnWriteArrayList<GraczGrającyWGrę> getListaGraczyKtórzyPrzeszli() {
    return listaGraczyKtórzyPrzeszli;
  }

}