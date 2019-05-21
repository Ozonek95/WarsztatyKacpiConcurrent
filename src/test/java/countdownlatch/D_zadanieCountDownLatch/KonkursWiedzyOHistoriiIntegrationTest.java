package countdownlatch.D_zadanieCountDownLatch;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import countdownlatch.D_zadanieCountDownLatch.KonkursWiedzyOHistorii.MetadaneKonkursu;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Kacper Staszek
 * @author Marcin Ogorzałek
 *
 * Testy bez użycia awaitility - dla porównania.
 */
@Test
public class KonkursWiedzyOHistoriiIntegrationTest {

  @Test(invocationCount = 10, threadPoolSize = 10, dataProvider = "numberOfParticipants")
  public void testStartsWithinSpecificTime(int numberOfParticipants)
      throws InterruptedException {
    // Given
    KonkursWiedzyOHistorii konkurs = new KonkursWiedzyOHistorii(numberOfParticipants);
    MetadaneKonkursu metadaneKonkursu = konkurs.stwórzPotrzebneObiekty();
    // When
    konkurs.startujWątki(metadaneKonkursu);
    //Thread.sleep(3500); DO TESTU Z %
    Thread.sleep(5000);
    // Then
    assertEquals(metadaneKonkursu.zatrzaskDoRozpoczęciaTestu.getCount(), 0);
  }

  @Test(invocationCount = 10, threadPoolSize = 10, dataProvider = "numberOfParticipants")
  public void testEndsWithinSpecificTime(int numberOfParticipants)
      throws InterruptedException {
    // Given
    KonkursWiedzyOHistorii konkurs = new KonkursWiedzyOHistorii(numberOfParticipants);
    MetadaneKonkursu metadaneKonkursu = konkurs.stwórzPotrzebneObiekty();
    // When
    konkurs.startujWątki(metadaneKonkursu);
    Thread.sleep(10000);
    // Then
    assertEquals(metadaneKonkursu.zatrzaskDoOgłoszeniaWyników.getCount(), 0);
  }

  @Test(invocationCount = 10, threadPoolSize = 10, dataProvider = "negativeNumberOfParticipants",
      expectedExceptions = IllegalArgumentException.class)
  public void invalidNumberOfParticipantsThrowException(int numberOfParticipants) {
    // Given
    KonkursWiedzyOHistorii konkurs = new KonkursWiedzyOHistorii(numberOfParticipants);
    // When
    konkurs.stwórzPotrzebneObiekty();
    // Then
    fail();
  }

  @DataProvider
  public static Object[][] numberOfParticipants() {
    return new Object[][]{
        {1},
        {2},
        {3},
        {5},
        {10},
        {100},
    };
  }

  @DataProvider
  public static Object[][] negativeNumberOfParticipants() {
    return new Object[][]{
        {-1},
        {-5},
        {-10},
        {0}
    };
  }


}