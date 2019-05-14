package G_zadanieZCalosci;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author Kacper Staszek
 */
class DemonstracjaGry {

  private CountDownLatch latch;

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