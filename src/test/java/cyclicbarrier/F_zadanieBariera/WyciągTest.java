package cyclicbarrier.F_zadanieBariera;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.core.IsEqual.equalTo;

import fabryczkapomocnicza.MyThreadFactory;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Marcin Ogorzalek
 */
@Test(successPercentage = 90)
public class WyciągTest {

  private static final int POJEMNOŚĆ_WAGONIKA = 4;
  private static final int ILOSC_NARCIARZY_CHETNYCH_DO_WYJAZDU = 20;

  @Test(invocationCount = 100, threadPoolSize = 100)
  public void checkIfSkiersQueueGetsSmaller() {
    CyclicBarrier barrier = getBarrier();
    BlockingQueue<Narciarz> chętni = stwórzKolejkę(barrier);
    uruchomWszystkichNarciarzy(chętni);
    await().until(chętni::size, equalTo(0));
  }

  @Test(invocationCount = 100, threadPoolSize = 100)
  public void checkIfBarrierShrinks() {
    CyclicBarrier barrier = getBarrier();
    BlockingQueue<Narciarz> chętni = stwórzKolejkę(barrier);
    Assert.assertEquals(barrier.getNumberWaiting(), 0);
    uruchomWszystkichNarciarzy(chętni);
    await().until(barrier::getNumberWaiting, equalTo(POJEMNOŚĆ_WAGONIKA - 1));
  }

  @Test(invocationCount = 100, threadPoolSize = 100)
  public void checkIfBarrierMakeFullCircle() {
    CyclicBarrier barrier = getBarrier();
    BlockingQueue<Narciarz> chętni = stwórzKolejkę(barrier);
    Assert.assertEquals(barrier.getNumberWaiting(), 0);
    uruchomWszystkichNarciarzy(chętni);
    await().until(barrier::getNumberWaiting, equalTo(POJEMNOŚĆ_WAGONIKA - 1));
    await().until(barrier::getNumberWaiting, equalTo(0));
  }

  @Test(invocationCount = 100, threadPoolSize = 100)
  public void checkIfBarrierAwaitIsCalledProperInOneCircle()
      throws BrokenBarrierException, InterruptedException {
    CyclicBarrier barrier = getBarrier();
    CyclicBarrier mock = Mockito.spy(barrier);
    BlockingQueue<Narciarz> chętni = stwórzKolejkę(mock);
    uruchomWszystkichNarciarzy(chętni);
    await()
        .with().pollInterval(1, TimeUnit.MILLISECONDS)
        .until(mock::getNumberWaiting, equalTo(POJEMNOŚĆ_WAGONIKA-1));

    Mockito.verify(mock, Mockito.times(POJEMNOŚĆ_WAGONIKA-1)).await();
  }

  @Test(invocationCount = 100, threadPoolSize = 100)
  public void checkIfBarrierMakeCircleProperNumberOfTimes()
      throws BrokenBarrierException, InterruptedException {
    CyclicBarrier barrier = getBarrier();
    CyclicBarrier mock = Mockito.spy(barrier);
    BlockingQueue<Narciarz> chętni = stwórzKolejkę(mock);
    uruchomWszystkichNarciarzy(chętni);
    int IlośćWyjazdówKtóreMaWykonaćKolejka = (ILOSC_NARCIARZY_CHETNYCH_DO_WYJAZDU / POJEMNOŚĆ_WAGONIKA);
    for (int i=0; i < IlośćWyjazdówKtóreMaWykonaćKolejka; i++){
      await()
          .with().pollInterval(1, TimeUnit.MILLISECONDS)
          .until(mock::getNumberWaiting, equalTo(POJEMNOŚĆ_WAGONIKA-1));

      Mockito.verify(mock, Mockito.times(POJEMNOŚĆ_WAGONIKA-1)).await();
    }
  }


  private BlockingQueue<Narciarz> stwórzKolejkę(CyclicBarrier barrier) {
    return Stream.generate(() -> new Narciarz(barrier))
        .limit(ILOSC_NARCIARZY_CHETNYCH_DO_WYJAZDU)
        .collect(Collectors
            .toCollection(() -> new ArrayBlockingQueue<>(ILOSC_NARCIARZY_CHETNYCH_DO_WYJAZDU)));
  }

  private CyclicBarrier getBarrier() {
    return new CyclicBarrier(POJEMNOŚĆ_WAGONIKA, () -> System.out.println("ODJEŻDŻAMY!"));
  }

  private static void uruchomWszystkichNarciarzy(BlockingQueue<Narciarz> chętni) {
    ExecutorService executorService = Executors
        .newFixedThreadPool(POJEMNOŚĆ_WAGONIKA, new MyThreadFactory("Narciarz "));

    for (int i = 0; i < ILOSC_NARCIARZY_CHETNYCH_DO_WYJAZDU; i++) {
      try {
        executorService.submit(Objects.requireNonNull(chętni.take()));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    executorService.shutdown();
  }
}