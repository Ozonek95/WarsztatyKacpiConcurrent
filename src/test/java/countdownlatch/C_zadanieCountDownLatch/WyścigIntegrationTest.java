package countdownlatch.C_zadanieCountDownLatch;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.fieldIn;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import countdownlatch.C_zadanieCountDownLatch.Main.MetadaneWyścigu;
import java.util.concurrent.CopyOnWriteArrayList;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *@author Kacper Staszek
 *@author Marcin Ogorzałek
 */
@Test(successPercentage = 95)
public class WyścigIntegrationTest {


    @Test(invocationCount = 100, threadPoolSize = 10, dataProvider = "numberOfDrivers")
    public void ifLatchBiggerThanZeroThanNotAllDriversReadyToStart(int numberOfDrivers) {
        //Given
        Main main = new Main(numberOfDrivers);
        MetadaneWyścigu given = main.stwórzPotrzebneObiekty();
        //When
        main.uruchomWątki(given);
        //Then
        await().until(() -> given.latch.getCount() > 0, equalTo(true));
        assertTrue(given.wyścig.getDriversReadyToStart() < numberOfDrivers);
    }

    @Test(invocationCount = 100, threadPoolSize = 10, dataProvider = "numberOfDrivers")
    public void raceHaveNoReadyDriversAtBeginning(int numberOfDrivers) {
        //Given
        Main main = new Main(numberOfDrivers);
        MetadaneWyścigu given = main.stwórzPotrzebneObiekty();
        //When
        main.uruchomWątki(given);
        //Then
        assertEquals(given.wyścig.getDriversReadyToStart(), 0);
    }

    @Test(invocationCount = 100, threadPoolSize = 10, dataProvider = "numberOfDrivers")
    public void allDriversWouldReachTheRaceBeginning(int numberOfDrivers) throws Exception {
        //Given
        Main main = new Main(numberOfDrivers);
        MetadaneWyścigu given = main.stwórzPotrzebneObiekty();
        //When
        main.uruchomWątki(given);
        //Then
        await().until(fieldIn(given.wyścig).ofType(CopyOnWriteArrayList.class)
            .andWithName("driversReadyToStart")
            .call()::size, equalTo(numberOfDrivers));
    }

    @Test(invocationCount = 100, threadPoolSize = 10, dataProvider = "numberOfDrivers")
    public void latchGetCountEqualsNumberOfDriversAtBeginningOfRace(int numberOfDrivers){
        //Given
        Main main = new Main(numberOfDrivers);
        MetadaneWyścigu given = main.stwórzPotrzebneObiekty();
        //When
        main.uruchomWątki(given);
        //Then
        assertEquals(given.latch.getCount(), numberOfDrivers);
    }

    @Test(invocationCount = 100, threadPoolSize = 10, dataProvider = "numberOfDrivers")
    public void latchIsZeroWhenAllDriversArrivedToTheStartLine(int numberOfDrivers){
        //Given
        Main main = new Main(numberOfDrivers);
        MetadaneWyścigu given = main.stwórzPotrzebneObiekty();
        //When
        main.uruchomWątki(given);
        //Then
        await().until(given.latch::getCount,equalTo((long) numberOfDrivers));
    }

    @Test(invocationCount = 100, threadPoolSize = 10,
        expectedExceptions = IllegalArgumentException.class,
        dataProvider = "negativeNumbersToTestException")
    public void negativeNumberPassedToLatchThrowsIllegalArgumentException(int negativeNumber){
        //Given
        Main main = new Main(negativeNumber);
        MetadaneWyścigu given = main.stwórzPotrzebneObiekty();
        //When
        main.uruchomWątki(given);
        //Then
        fail();
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
