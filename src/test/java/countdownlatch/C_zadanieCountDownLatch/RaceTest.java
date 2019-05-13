package countdownlatch.C_zadanieCountDownLatch;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.fieldIn;
import static org.hamcrest.CoreMatchers.equalTo;

import fabryczkapomocnicza.MyThreadFactory;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test
public class RaceTest {

  private Race race;
  private CountDownLatch latch;
  private static final int NUMBER_OF_DRIVERS = 5;

  @BeforeMethod
  public synchronized void init() {
    latch = new CountDownLatch(NUMBER_OF_DRIVERS);
    race = new Race(latch);
    new Thread(race, "Wyścig").start();
    ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_DRIVERS,
        new MyThreadFactory("Kierowca"));
    for (int i = 0; i < NUMBER_OF_DRIVERS; i++) {
      executorService.submit(new Driver(latch, race));
    }
    executorService.shutdown();
  }

  @Test(invocationCount = 10, threadPoolSize = 10)
  public void testIfRaceIsEmptyAtBegin() {
    Assert.assertEquals(0, race.getDriversReadyToStart());
    await().until(race::getDriversReadyToStart, equalTo(NUMBER_OF_DRIVERS));
    await().until(latch::getCount, equalTo(0L));
    Assert.assertEquals(latch.getCount(), 0L);
  }

  @Test(invocationCount = 10, threadPoolSize = 10)
  public void testIfRaceReachAllStarters() throws Exception {
    await().until(fieldIn(race).ofType(CopyOnWriteArrayList.class)
        .andWithName("driversReadyToStart")
        .call()::size, equalTo(NUMBER_OF_DRIVERS));
  }

  @Test(invocationCount = 10, threadPoolSize = 10)
  public void testIfRaceLatchIsFiveAtBeginning() {
    Assert.assertEquals(NUMBER_OF_DRIVERS, latch.getCount());
  }

  @Test(invocationCount = 10, threadPoolSize = 10)
  public void testIfRaceLatchIsZeroAfterFinish() {
    await().until(latch::getCount, equalTo((long) NUMBER_OF_DRIVERS));
  }

  @Test(invocationCount = 10, threadPoolSize = 10)
  public void testIfRaceHaveFiveDriversAfterLatchIsZero() {
    await().until(latch::getCount, equalTo(0L));
    await().until(race::getDriversReadyToStart, equalTo(NUMBER_OF_DRIVERS));
    Assert.assertEquals(race.getDriversReadyToStart(), NUMBER_OF_DRIVERS);
  }

  @Test(invocationCount = 10, threadPoolSize = 10)
  public void testIfBeforeLatchIsZeroDriversIsNotFive() {
    await().until(() -> latch.getCount() > 0, equalTo(true));
    Assert.assertTrue(race.getDriversReadyToStart() < NUMBER_OF_DRIVERS);
  }
}
