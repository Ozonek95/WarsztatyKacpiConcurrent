package cyclicbarrier.F_zadanieBariera;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.testng.Assert.fail;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import org.mockito.Mockito;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Marcin Ogorzalek
 */
@Test(successPercentage = 95)
public class WyciągIntegrationTest {

  @Test(invocationCount = 100, threadPoolSize = 100, dataProvider = "numberOfSkiersAndTrolleySize")
  public void skiersQueueIsEmptyAtTheEnd(int sizeOfTrolley, int numberOfSkiers) {
    // Given
    StokNarciarski stokNarciarski = new StokNarciarski(sizeOfTrolley, numberOfSkiers,
        getBarrier(sizeOfTrolley));
    BlockingQueue<Narciarz> chętni = stokNarciarski.stwórzKolejkęNarciarzy(stokNarciarski.barrier);
    // When
    stokNarciarski.uruchomWszystkichNarciarzy(chętni);
    // Then
    await().until(chętni::size, equalTo(0));
  }

  @Test(invocationCount = 100, threadPoolSize = 100, dataProvider = "numberOfSkiersAndTrolleySize")
  public void waitingForBarrierNumberIncrease(int sizeOfTrolley, int numberOfSkiers) {
    // Given
    StokNarciarski stokNarciarski = new StokNarciarski(sizeOfTrolley, numberOfSkiers,
        getBarrier(sizeOfTrolley));
    BlockingQueue<Narciarz> chętni = stokNarciarski.stwórzKolejkęNarciarzy(stokNarciarski.barrier);
    // When
    stokNarciarski.uruchomWszystkichNarciarzy(chętni);
    // Then
    await().until(stokNarciarski.barrier::getNumberWaiting, equalTo(0));
    await().until(stokNarciarski.barrier::getNumberWaiting, equalTo(sizeOfTrolley - 1));
  }

  @Test(invocationCount = 100, threadPoolSize = 100, dataProvider = "numberOfSkiersAndTrolleySize")
  public void barrierMakeFullCircle(int sizeOfTrolley, int numberOfSkiers) {
    // Given
    StokNarciarski stokNarciarski = new StokNarciarski(sizeOfTrolley, numberOfSkiers,
        getBarrier(sizeOfTrolley));
    BlockingQueue<Narciarz> chętni = stokNarciarski.stwórzKolejkęNarciarzy(stokNarciarski.barrier);
    // When
    stokNarciarski.uruchomWszystkichNarciarzy(chętni);
    // Then
    await().until(stokNarciarski.barrier::getNumberWaiting, equalTo(sizeOfTrolley - 1));
    await().until(stokNarciarski.barrier::getNumberWaiting, equalTo(0));
  }

  @Test(invocationCount = 100, threadPoolSize = 100, dataProvider = "numberOfSkiersAndTrolleySize")
  public void barrierAwaitIsCalledProperNumberOfTimesInOneCircle(int sizeOfTrolley,
      int numberOfSkiers)
      throws BrokenBarrierException, InterruptedException {
    // Given
    CyclicBarrier barrier = getBarrier(sizeOfTrolley);
    CyclicBarrier mock = Mockito.spy(barrier);
    StokNarciarski stokNarciarski = new StokNarciarski(sizeOfTrolley, numberOfSkiers, mock);
    BlockingQueue<Narciarz> chętni = stokNarciarski.stwórzKolejkęNarciarzy(mock);
    // When
    stokNarciarski.uruchomWszystkichNarciarzy(chętni);
    // Then
    await()
        .with().pollInterval(1, TimeUnit.MILLISECONDS)
        .until(mock::getNumberWaiting, equalTo(sizeOfTrolley - 1));
    Mockito.verify(mock, Mockito.times(sizeOfTrolley - 1)).await();
  }

  @Test(invocationCount = 100, threadPoolSize = 100, dataProvider = "numberOfSkiersAndTrolleySize")
  public void barrierMakeCircleProperNumberOfTimes(int sizeOfTrolley, int numberOfSkiers)
      throws BrokenBarrierException, InterruptedException {
    // Given
    CyclicBarrier barrier = getBarrier(sizeOfTrolley);
    CyclicBarrier mock = Mockito.spy(barrier);
    StokNarciarski stokNarciarski = new StokNarciarski(sizeOfTrolley, numberOfSkiers, mock);
    BlockingQueue<Narciarz> chętni = stokNarciarski.stwórzKolejkęNarciarzy(mock);
    // When
    stokNarciarski.uruchomWszystkichNarciarzy(chętni);
    // Then
    await().atMost(60, TimeUnit.SECONDS)
        .until(stokNarciarski.executorService::isTerminated, equalTo(true));
    Mockito.verify(mock, Mockito.times(stokNarciarski.iloscNarciarzyChetnychDoWyjazdu)).await();
  }

  @Test(invocationCount = 100, threadPoolSize = 100, dataProvider = "negativeNumbersProvider", expectedExceptions = IllegalArgumentException.class)
  public void numberZeroOrLessPassedAsArgumentThrowsException(int sizeOfTrolley, int numberOfSkiers){
      // Given
      StokNarciarski stokNarciarski = new StokNarciarski(sizeOfTrolley, numberOfSkiers,
          getBarrier(sizeOfTrolley));
      BlockingQueue<Narciarz> chętni = stokNarciarski.stwórzKolejkęNarciarzy(stokNarciarski.barrier);
      // When
      stokNarciarski.uruchomWszystkichNarciarzy(chętni);
      // Then
      fail();
  }

  private CyclicBarrier getBarrier(int wielkśćWagonika) {
    return new CyclicBarrier(wielkśćWagonika, () -> System.out.println("ODJEŻDŻAMY!"));
  }

  @DataProvider
  public static Object[][] numberOfSkiersAndTrolleySize() {
    return new Object[][]{
        {4, 20},
        {5, 20},
        {5, 30}
    };
  }

  @DataProvider
  public static Object[][] negativeNumbersProvider() {
    return new Object[][]{
        {0,10},
        {10,0},
        {0,0},
        {-2,10},
        {10,-2},
        {-2,-2}
    };
  }
}