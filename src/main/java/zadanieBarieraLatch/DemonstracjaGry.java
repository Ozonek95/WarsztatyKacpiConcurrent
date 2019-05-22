package zadanieBarieraLatch;

import fabryczkapomocnicza.MyThreadFactory;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Gracze, którzy zakończyli test jako pierwsi są dopuszczani do zagrania kilku rund
 * w przepremierową wersję gry. Aby im to umożliwić, wykorzystujemy CyclicBarrier,
 * żeby przy każdej rundzie zaczekać, aż wszyscy skończą.
 * @see CyclicBarrier
 *
 * Używamy też CountDownLatcha, żeby móc zakończyć demonstrację po określonej
 * liczbie rund.
 * @see CountDownLatch
 *
 * @author Kacper Staszek
 * @author Marcin Ogorzałek
 */
class DemonstracjaGry {

  private CyclicBarrier barrier;
  private List<GraczGrającyWGrę> listaGraczyKtórzyPrzesli;
  private CountDownLatch latch;
  private int ilośćRundDoRozegrania;

  DemonstracjaGry(List<GraczGrającyWGrę> listaGraczyKtórzyPrzesli, int ilośćRundDoRozegrania) {
    this.listaGraczyKtórzyPrzesli=listaGraczyKtórzyPrzesli;
    this.ilośćRundDoRozegrania = ilośćRundDoRozegrania;
    this.latch = new CountDownLatch(ilośćRundDoRozegrania);


    barrier = new CyclicBarrier(listaGraczyKtórzyPrzesli.size(),
        () -> {
          latch.countDown();
          if(latch.getCount()>0)
          System.out.println("Rozpoczynam nową grę.");
        });
  }

  void startujGrę() {
    ustawBarieręDlaGraczy();
    System.out.println("Rozpoczynamy wyścig!");
    ExecutorService executorService = Executors
        .newFixedThreadPool(listaGraczyKtórzyPrzesli.size(),
            new MyThreadFactory("Gracz rozgrywający grę "));

    for (int i = 0; i < ilośćRundDoRozegrania; i++) {

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