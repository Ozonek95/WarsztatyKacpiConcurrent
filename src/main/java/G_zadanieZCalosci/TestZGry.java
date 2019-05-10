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

  private final int liczbaGraczyBiorących;
  private CountDownLatch countDownLatch;
  private List<GraczRozwiązującyTest> listaGraczyRozwiązującychTest;
  private CopyOnWriteArrayList<GraczGrającyWGrę> listaGraczyKtórzyPrzeszli = new CopyOnWriteArrayList<>();

  TestZGry(int liczbaGraczyBiorących, int liczbaGraczyKtóraPrzejdzieDalej) {
    this.liczbaGraczyBiorących = liczbaGraczyBiorących;
    countDownLatch = new CountDownLatch(liczbaGraczyKtóraPrzejdzieDalej);
    listaGraczyRozwiązującychTest = generujGraczy();
  }

  List<GraczRozwiązującyTest> generujGraczy() {
    return Stream
        .generate(() -> new GraczRozwiązującyTest(countDownLatch, this))
        .limit(liczbaGraczyBiorących)
        .collect(Collectors.toCollection(ArrayList::new));
  }

  List<GraczGrającyWGrę> startujTest() {
    ExecutorService executorService = Executors
        .newFixedThreadPool(liczbaGraczyBiorących, new MyThreadFactory("Gracz rozwiązujący test "));

    for (int i = 0; i < liczbaGraczyBiorących; i++) {
      executorService.submit(listaGraczyRozwiązującychTest.get(i));
    }

    try {
      countDownLatch.await();
      executorService.shutdownNow();
      System.out.println("Mamy zwycięsców testu!");
      return listaGraczyKtórzyPrzeszli;
    } catch (InterruptedException ignored) {
      ignored.getMessage();
    }
    throw new UnsupportedOperationException("Ktoś przecież musiał rozwiązać test!");
  }

  CopyOnWriteArrayList<GraczGrającyWGrę> getListaGraczyKtórzyPrzeszli() {
    return listaGraczyKtórzyPrzeszli;
  }

}
