package countdownlatch.C1_zadanieCountDownLatch;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.fieldIn;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.testng.Assert.*;

import countdownlatch.C1_zadanieCountDownLatch.Main.MetadaneNapadu;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.awaitility.Duration;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Marcin Ogorzalek
 */
@Test
public class NapadIntegrationTest {

  @DataProvider
  public static Object[][] robberyConditions() {
    return new Object[][]{
        {4, 5000, 10_000}
    };
  }

  @Test(invocationCount = 1000, threadPoolSize = 100,
      dataProvider = "robberyConditions", successPercentage = 97)
  public void whenThievesAreCatchThenGoToJail(int teamSize,
      long timeToPoliceArrive, int robberyTime) throws Exception{
    Main main = new Main(teamSize, timeToPoliceArrive, robberyTime);
//    Given
    MetadaneNapadu given = main.given();
//    When
    main.when(given);
//    Then
//    nie działa zawsze expected 4
//    przerzucić atLeat i atMost do Given kiedy test zadziała
//    Duration atLeast = new Duration(timeToPoliceArrive, TimeUnit.MILLISECONDS);
//    Duration atMost = new Duration(robberyTime, TimeUnit.MILLISECONDS);
//    await().between(atLeast, atMost).until(fieldIn(given.więzienie)
//        .ofType(CopyOnWriteArrayList.class).andWithName("więźniowie")
//        .call()::size, equalTo((int)given.latch.getCount()));

//    usunąć po porwaieniu testu z awaitility
    try {
      Thread.sleep(timeToPoliceArrive);
    } catch (InterruptedException ignored) {
      System.err.println(ignored.getMessage());
    }
    assertEquals(given.więzienie.getWięźniowie().size(), given.latch.getCount());
  }
}