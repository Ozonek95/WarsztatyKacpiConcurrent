package G_zadanieZCalosci;

import fabryczkapomocnicza.MyThreadFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
