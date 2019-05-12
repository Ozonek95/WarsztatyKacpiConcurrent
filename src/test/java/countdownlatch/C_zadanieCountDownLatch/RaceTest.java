package countdownlatch.C_zadanieCountDownLatch;

import fabryczkapomocnicza.MyThreadFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.*;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.fieldIn;
import static org.hamcrest.CoreMatchers.equalTo;

@Test
public class RaceTest {

    private Race race;
    private CountDownLatch latch;

    @BeforeMethod
    public synchronized void init(){
        latch = new CountDownLatch(5);
        race = new Race(latch);
        new Thread(race,"Wyścig").start();
        ThreadFactory threadFactory = new MyThreadFactory("Kierowca");
        ExecutorService executorService = Executors.newFixedThreadPool(5, threadFactory);
        for(int i = 0; i < 5; i++) {
            executorService.submit(new Driver(latch, race));
        }
        executorService.shutdown();
    }

    @Test(invocationCount = 5, threadPoolSize = 5)
    public void testIfRaceIsEmptyAtBegin() {
        Assert.assertEquals(0, race.getDriversReadyToStart());
        await().until(race::getDriversReadyToStart,equalTo(5));
        await().until(latch::getCount,equalTo(0L));
        Assert.assertEquals(latch.getCount(), 0L);
    }

    @Test(invocationCount = 5, threadPoolSize = 5)
    public void testIfRaceReachAllStarters() throws Exception {
        await().until(fieldIn(race).ofType(CopyOnWriteArrayList.class)
                .andWithName("driversReadyToStart").call()::size,equalTo(5));
    }

    @Test(invocationCount = 5, threadPoolSize = 5)
    public void testIfRaceLatchIsFiveAtBeginning(){
        Assert.assertEquals(5, latch.getCount());
    }

    @Test(invocationCount = 5, threadPoolSize = 5)
    public void testIfRaceLatchIsZeroAfterFinish(){
        await().until(latch::getCount,equalTo(0L));
    }

    @Test(invocationCount = 5, threadPoolSize = 5)
    public void testIfRaceHaveFiveDriversAfterLatchIsZero(){
        await().until(latch::getCount,equalTo(0L));
        await().until(race::getDriversReadyToStart,equalTo(5));
        Assert.assertEquals(5, race.getDriversReadyToStart());
    }

    @Test(invocationCount = 5, threadPoolSize = 5)
    public void testIfBeforeLatchIsZeroDriversIsNotFive(){
        await().until(latch::getCount,equalTo(2L));
        Assert.assertTrue(race.getDriversReadyToStart()<5);
        System.out.println();
    }
}
