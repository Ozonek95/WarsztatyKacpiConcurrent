package G_zadanieZCalosci;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Kacper Staszek
 */
class GraczGrającyWGrę implements Runnable {


  @Override
  public void run() {
    System.out.println("Rozpoczynam wyścig!");
    System.out.println("Dojechałem na metę!");
  }

  @Override
  public String toString() {
    return Thread.currentThread().getName();
  }

  public void setCyclicBarrier(CyclicBarrier cyclicBarrier) {

  }
}
