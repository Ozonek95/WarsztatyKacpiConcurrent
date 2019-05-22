package zadanieBarieraLatch;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Kacper Staszek
 * @author Marcin Ogorzałek
 */
class GraczGrającyWGrę implements Runnable {
  private CyclicBarrier cyclicBarrier;

  @Override
  public void run() {
    try {
      Thread.sleep(ThreadLocalRandom.current().nextInt(5000));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    try {
      System.out.println("Skończyłem wyścig "+this);
      cyclicBarrier.await();
    } catch (InterruptedException | BrokenBarrierException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String toString() {
    return Thread.currentThread().getName();
  }

  void setCyclicBarrier(CyclicBarrier cyclicBarrier) {
    this.cyclicBarrier = cyclicBarrier;
  }
}