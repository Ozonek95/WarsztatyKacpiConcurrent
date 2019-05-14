package G_zadanieZCalosci;

import fabryczkapomocnicza.MyThreadFactory;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Kacper Staszek
 */
class DemonstracjaGry {

  private CyclicBarrier barrier;
  private List<GraczGrającyWGrę> listaGraczyKtórzyPrzesli;
  private CountDownLatch latch;

  DemonstracjaGry(List<GraczGrającyWGrę> listaGraczyKtórzyPrzesli, int ilośćRundDoRozegrania) {
    this.listaGraczyKtórzyPrzesli=listaGraczyKtórzyPrzesli;
    this.latch = new CountDownLatch(ilośćRundDoRozegrania);


    barrier = new CyclicBarrier(listaGraczyKtórzyPrzesli.size(),
        () -> {
          System.out.println("Rozpoczynam nową grę.");
          latch.countDown();
        });
  }

  void startujGrę() {
    ustawBarieręDlaGraczy();
    System.out.println("Rozpoczynamy wyścig!");
    ExecutorService executorService = Executors
        .newFixedThreadPool(listaGraczyKtórzyPrzesli.size(),
            new MyThreadFactory("Gracz rozgrywający grę "));

    for (int i = 0; i < 3; i++) {
      for (GraczGrającyWGrę graczGrającyWGrę : listaGraczyKtórzyPrzesli) {
        executorService.submit(graczGrającyWGrę);
      }
    }
    executorService.shutdown();

    try {
      latch.await();
      System.out.println("Demo zakończone!");
    } catch (InterruptedException ignored) {
      ignored.printStackTrace();
    }
  }

  private void ustawBarieręDlaGraczy(){
    listaGraczyKtórzyPrzesli.forEach(graczGrającyWGrę -> graczGrającyWGrę.setCyclicBarrier(barrier));
  }
}
