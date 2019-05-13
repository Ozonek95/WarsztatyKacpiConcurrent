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

  DemonstracjaGry(List<GraczGrającyWGrę> listaGraczyKtórzyPrzesli, int ilośćRundDoRozegrania) {

  }

  void startujGrę() {
    ustawBarieręDlaGraczy();
    System.out.println("Rozpoczynamy wyścig!");
    rozegranieWyścigów();
    System.out.println("Demo zakończone!");
  }

  private void rozegranieWyścigów() {

  }

  void ustawBarieręDlaGraczy(){

  }
}
