package countdownlatch.C_zadanieCountDownLatch;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.fieldIn;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.testng.Assert.*;

import countdownlatch.C_zadanieCountDownLatch.Main.MetadaneWyścigu;
import java.util.concurrent.CopyOnWriteArrayList;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(successPercentage = 95)
public class WyścigIntegrationTest {

    @Test(invocationCount = 100, threadPoolSize = 10, dataProvider = "numberOfDrivers")
    public void ifLatchBiggerThanZeroThanNotAllDriversReadyToStart(int numberOfDrivers) {
        Main main = new Main(numberOfDrivers);
        MetadaneWyścigu given = main.given();
        main.when(given);

        await().until(() -> given.latch.getCount() > 0, equalTo(true));
        assertTrue(given.wyścig.getDriversReadyToStart() < numberOfDrivers);
    }

    @Test(invocationCount = 100, threadPoolSize = 10, dataProvider = "numberOfDrivers")
    public void raceIsEmptyAtBegin(int numberOfDrivers) {
        Main main = new Main(numberOfDrivers);
        MetadaneWyścigu given = main.given();
        main.when(given);
        assertEquals(given.wyścig.getDriversReadyToStart(), 0);
    }

    @Test(invocationCount = 100, threadPoolSize = 10, dataProvider = "numberOfDrivers")
    public void allDriversReachTheRaceBeginning(int numberOfDrivers) throws Exception {
        Main main = new Main(numberOfDrivers);
        MetadaneWyścigu given = main.given();
        main.when(given);

        await().until(fieldIn(given.wyścig).ofType(CopyOnWriteArrayList.class)
            .andWithName("driversReadyToStart")
            .call()::size, equalTo(numberOfDrivers));
    }

    @Test(invocationCount = 100, threadPoolSize = 10, dataProvider = "numberOfDrivers")
    public void latchGetCountEqualsNumberOfDriversAtBeginningOfRace(int numberOfDrivers){
        Main main = new Main(numberOfDrivers);
        MetadaneWyścigu given = main.given();
        main.when(given);
        assertEquals(given.latch.getCount(), numberOfDrivers);
    }

    @Test(invocationCount = 100, threadPoolSize = 10, dataProvider = "numberOfDrivers")
    public void latchIsZeroWhenAllDriversArrive(int numberOfDrivers){
        Main main = new Main(numberOfDrivers);
        MetadaneWyścigu given = main.given();
        main.when(given);
        await().until(given.latch::getCount,equalTo((long) numberOfDrivers));
    }

    @Test(invocationCount = 100, threadPoolSize = 10,
        expectedExceptions = IllegalArgumentException.class,
        dataProvider = "negativeNumbersToTestException")
    public void testIfRaceHaveFiveDriversAfterLatchIsZero(int negativeNumber){
        Main main = new Main(negativeNumber);
        MetadaneWyścigu given = main.given();
        main.when(given);
    }

    @DataProvider
    public static Object[][] numberOfDrivers() {
        return new Object[][] {
            {1},
            {2},
            {5},
            {10},
        };
    }

    @DataProvider
    public static Object[][] negativeNumbersToTestException() {
        return new Object[][] {
            {-1},
            {-2},
            {-5},
            {-10},
        };
    }

}
