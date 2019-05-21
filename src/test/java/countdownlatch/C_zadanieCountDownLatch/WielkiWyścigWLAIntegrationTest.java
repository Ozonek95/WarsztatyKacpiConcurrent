package countdownlatch.C_zadanieCountDownLatch;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.fieldIn;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import countdownlatch.C_zadanieCountDownLatch.WielkiWyścigWLA.MetadaneWyścigu;
import java.util.concurrent.CopyOnWriteArrayList;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *@author Kacper Staszek
 *@author Marcin Ogorzałek
 */
@Test(successPercentage = 95)
public class WielkiWyścigWLAIntegrationTest {


    @Test(invocationCount = 100, threadPoolSize = 10, dataProvider = "numberOfDrivers")
    public void ifLatchBiggerThanZeroThenNotAllDriversReadyToStart(int numberOfDrivers) {
        // Given
        WielkiWyścigWLA wielkiWyścigWLA = new WielkiWyścigWLA(numberOfDrivers);
        MetadaneWyścigu given = wielkiWyścigWLA.stwórzPotrzebneObiekty();
        // When
        wielkiWyścigWLA.uruchomWątki(given);
        // Then
        await().until(() -> given.latch.getCount() > 0, equalTo(true));
        assertTrue(given.wyścig.getKierowcyGotowiDoStartu() < numberOfDrivers);
    }

    @Test(invocationCount = 100, threadPoolSize = 10, dataProvider = "numberOfDrivers")
    public void raceHasNoDriversAtBeginning(int numberOfDrivers) {
        // Given
        WielkiWyścigWLA wielkiWyścigWLA = new WielkiWyścigWLA(numberOfDrivers);
        MetadaneWyścigu given = wielkiWyścigWLA.stwórzPotrzebneObiekty();
        // When
        wielkiWyścigWLA.uruchomWątki(given);
        // Then
        assertEquals(given.wyścig.getKierowcyGotowiDoStartu(), 0);
    }

    @Test(invocationCount = 100, threadPoolSize = 10, dataProvider = "numberOfDrivers")
    public void allDriversWouldReachTheStartingLine(int numberOfDrivers) throws Exception {
        // Given
        WielkiWyścigWLA wielkiWyścigWLA = new WielkiWyścigWLA(numberOfDrivers);
        MetadaneWyścigu given = wielkiWyścigWLA.stwórzPotrzebneObiekty();
        // When
        wielkiWyścigWLA.uruchomWątki(given);
        // Then
        await().until(fieldIn(given.wyścig).ofType(CopyOnWriteArrayList.class)
            .andWithName("kierowcyGotowiDoStartu")
            .call()::size, equalTo(numberOfDrivers));
    }

    @Test(invocationCount = 100, threadPoolSize = 10, dataProvider = "numberOfDrivers")
    public void latchHasCountForAllDriversBeforeRaceBegins(int numberOfDrivers){
        // Given
        WielkiWyścigWLA wielkiWyścigWLA = new WielkiWyścigWLA(numberOfDrivers);
        MetadaneWyścigu given = wielkiWyścigWLA.stwórzPotrzebneObiekty();
        // When
        wielkiWyścigWLA.uruchomWątki(given);
        // Then
        assertEquals(given.latch.getCount(), numberOfDrivers);
    }

    @Test(invocationCount = 100, threadPoolSize = 10, dataProvider = "numberOfDrivers")
    public void latchIsZeroWhenAllDriversArrivedAtStartingLine(int numberOfDrivers){
        // Given
        WielkiWyścigWLA wielkiWyścigWLA = new WielkiWyścigWLA(numberOfDrivers);
        MetadaneWyścigu given = wielkiWyścigWLA.stwórzPotrzebneObiekty();
        // When
        wielkiWyścigWLA.uruchomWątki(given);
        // Then
        await().until(given.latch::getCount,equalTo((long) numberOfDrivers));
    }

    @Test(invocationCount = 100, threadPoolSize = 10,
        expectedExceptions = IllegalArgumentException.class,
        dataProvider = "negativeNumbersToTestException")
    public void negativeNumberPassedToLatchThrowsIllegalArgumentException(int negativeNumber){
        // Given
        WielkiWyścigWLA wielkiWyścigWLA = new WielkiWyścigWLA(negativeNumber);
        MetadaneWyścigu given = wielkiWyścigWLA.stwórzPotrzebneObiekty();
        // When
        wielkiWyścigWLA.uruchomWątki(given);
        // Then
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
