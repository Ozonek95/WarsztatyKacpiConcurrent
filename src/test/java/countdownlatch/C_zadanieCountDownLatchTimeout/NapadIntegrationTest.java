package countdownlatch.C_zadanieCountDownLatchTimeout;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import countdownlatch.C_zadanieCountDownLatchTimeout.Main.MetadaneNapadu;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Kacper Staszek
 * @author Marcin Ogorzałek
 */
@Test(successPercentage = 95)
public class NapadIntegrationTest {

  @Test(invocationCount = 1000, threadPoolSize = 100,
      dataProvider = "failedRobberyConditions")
  public void whenThievesAreCatchThenGoToJail(int teamSize,
      long timeToPoliceArrive, int robberyTime) {
    // Given
    Main main = new Main(teamSize, timeToPoliceArrive, robberyTime);
    MetadaneNapadu given = main.tworzeniePotrzebnychObiektów();
    // When
    main.uruchamianieWątków(given);
    try {
      MILLISECONDS.sleep(robberyTime);
    } catch (InterruptedException ignored) {
      System.err.println(ignored.getMessage());
    }
    // Then
    assertEquals(given.więzienie.getWięźniowie().size(), given.latch.getCount());
  }

  @Test(invocationCount = 1000, threadPoolSize = 100,
      dataProvider = "succeedRobberyConditions")
  public void whenPoliceIsLateToRobberyJailIsEmpty(int teamSize,
      long timeToPoliceArrive, int robberyTime) {
    // Given
    Main main = new Main(teamSize, timeToPoliceArrive, robberyTime);
    MetadaneNapadu given = main.tworzeniePotrzebnychObiektów();
    // When
    main.uruchamianieWątków(given);
    try {
      MILLISECONDS.sleep(robberyTime);
    } catch (InterruptedException ignored) {
      System.err.println(ignored.getMessage());
    }
    // Then
    assertEquals(given.więzienie.getWięźniowie().size(), 0);
  }

  @Test(invocationCount = 1000, threadPoolSize = 100,
      dataProvider = "illegalTeamSize", expectedExceptions = IllegalArgumentException.class)
  public void whenTeamSizeZeroOrLessThenThrowsIllegalArgumentException(int teamSize,
      long timeToPoliceArrive, int robberyTime) {
    // Given
    Main main = new Main(teamSize, timeToPoliceArrive, robberyTime);
    MetadaneNapadu given = main.tworzeniePotrzebnychObiektów();
    // When
    main.uruchamianieWątków(given);
    try {
      MILLISECONDS.sleep(robberyTime);
    } catch (InterruptedException ignored) {
      System.err.println(ignored.getMessage());
    }
    // Then
    fail();
  }

  @DataProvider
  public static Object[][] failedRobberyConditions() {
    return new Object[][]{
        {4, 5000, 10_000},
        {5, 1000, 1500},
        {6, 110, 150},
    };
  }

  @DataProvider
  public static Object[][] succeedRobberyConditions() {
    return new Object[][]{
        {4, 10_000, 5000},
        {5, 1500, 1000},
        {6, 150, 110},
        {6, 1000, 500},
    };
  }

  @DataProvider
  public static Object[][] illegalTeamSize() {
    return new Object[][]{
        {0, 0, 0},
        {-5, 1500, 1000},
        {0, 1500, 1000},
        {-6, 150, 110},
        {-1, 1000, 500},
    };
  }
}