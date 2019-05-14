package G_zadanieZCalosci;

import java.util.concurrent.CountDownLatch;

/**
 * @author Kacper Staszek
 */
class GraczRozwiązującyTest implements Runnable {

  GraczRozwiązującyTest(CountDownLatch latch, TestZGry testZGry) {

  }

  GraczGrającyWGrę przeobraź(){
    return new GraczGrającyWGrę();
  }
  @Override
  public void run() {
    System.out.println("Zaczynam rozwiązywać test "+this);

    System.out.println("ROZWIĄZAŁEM! "+this);

    //Co jeżeli 5 graczy zdąży już rozwiązać test?
  }

  @Override
  public String toString() {
    return Thread.currentThread().getName();
  }
}