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
import org.testng.annotations.Test;

@Test
public class RaceTest {

    private static final int NUMBER_OF_DRIVERS = 5;

    @Test(invocationCount = 100, threadPoolSize = 10)
    public void testIfBeforeLatchIsZeroDriversIsNotFive() {
        CountDownLatch latch = new CountDownLatch(NUMBER_OF_DRIVERS);
        Race race = runRace(latch);
        await().until(() -> latch.getCount() > 0, equalTo(true));
        Assert.assertTrue(race.getDriversReadyToStart() < NUMBER_OF_DRIVERS);
    }

    @Test(invocationCount = 100, threadPoolSize = 100)
    public void testIfRaceIsEmptyAtBegin() {
        CountDownLatch latch = new CountDownLatch(NUMBER_OF_DRIVERS);
        Race race = runRace(latch);
        Assert.assertEquals(race.getDriversReadyToStart(), 0);
        await().until(race::getDriversReadyToStart,equalTo(NUMBER_OF_DRIVERS));
        await().until(latch::getCount,equalTo(0L));
        Assert.assertEquals(latch.getCount(), 0L);
    }

    @Test(invocationCount = 100, threadPoolSize = 100)
    public void testIfRaceReachAllStarters() throws Exception {
        CountDownLatch latch = new CountDownLatch(NUMBER_OF_DRIVERS);
        Race race = runRace(latch);
        await().until(fieldIn(race).ofType(CopyOnWriteArrayList.class)
            .andWithName("driversReadyToStart")
            .call()::size, equalTo(NUMBER_OF_DRIVERS));
    }

    @Test(invocationCount = 100, threadPoolSize = 100)
    public void testIfRaceLatchIsFiveAtBeginning(){
        CountDownLatch latch = new CountDownLatch(NUMBER_OF_DRIVERS);
        runRace(latch);
        Assert.assertEquals(latch.getCount(), NUMBER_OF_DRIVERS);
    }

    @Test(invocationCount = 100, threadPoolSize = 100)
    public void testIfRaceLatchIsZeroAfterFinish(){
        CountDownLatch latch = new CountDownLatch(NUMBER_OF_DRIVERS);
        runRace(latch);
        await().until(latch::getCount,equalTo((long) NUMBER_OF_DRIVERS));
    }

    @Test(invocationCount = 100, threadPoolSize = 100)
    public void testIfRaceHaveFiveDriversAfterLatchIsZero(){
        CountDownLatch latch = new CountDownLatch(NUMBER_OF_DRIVERS);
        Race race = runRace(latch);
        await().until(latch::getCount,equalTo(0L));
        await().until(race::getDriversReadyToStart,equalTo(NUMBER_OF_DRIVERS));
        Assert.assertEquals(race.getDriversReadyToStart(), NUMBER_OF_DRIVERS);
    }

    private Race runRace(CountDownLatch latch) {
        Race race = new Race(latch);
        new Thread(race, "Wyścig").start();
        ExecutorService executorService = Executors
            .newFixedThreadPool(NUMBER_OF_DRIVERS, new MyThreadFactory("Kierowca"));
        for (int i = 0; i < NUMBER_OF_DRIVERS; i++) {
            executorService.submit(new Driver(latch, race));
        }
        executorService.shutdown();
        return race;
    }

}
