package zadanieBarieraLatch;

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
 * Klasa symuluje wykonywanie testu o grze. Kiedy określona ilość graczy rozwiąże test,
 * jest dopuszczana do demonstracji gry.
 * @see DemonstracjaGry
 * Wszystkim pozostałym graczom przerywamy rozwiązywanie testu.
 * @see InterruptedException
 *
 * @author Kacper Staszek
 * @author Marcin Ogorzałek
 */
class TestWiedzyZGry {

  private final int liczbaGraczyBiorących;
  private CountDownLatch countDownLatch;
  private List<GraczRozwiązującyTest> listaGraczyRozwiązującychTest;
  private CopyOnWriteArrayList<GraczGrającyWGrę> listaGraczyKtórzyPrzeszli = new CopyOnWriteArrayList<>();
  ExecutorService executorService;

  TestWiedzyZGry(int liczbaGraczyBiorących, int liczbaGraczyKtóraPrzejdzieDalej) {
    this.liczbaGraczyBiorących = liczbaGraczyBiorących;
    countDownLatch = new CountDownLatch(liczbaGraczyKtóraPrzejdzieDalej);
    listaGraczyRozwiązującychTest = generujGraczy();
  }

  List<GraczGrającyWGrę> startujTest() {
      executorService = Executors
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

  List<GraczRozwiązującyTest> generujGraczy() {
    return Stream
        .generate(() -> new GraczRozwiązującyTest(countDownLatch, this))
        .limit(liczbaGraczyBiorących)
        .collect(Collectors.toCollection(ArrayList::new));
  }

  CopyOnWriteArrayList<GraczGrającyWGrę> getListaGraczyKtórzyPrzeszli() {
    return listaGraczyKtórzyPrzeszli;
  }
}