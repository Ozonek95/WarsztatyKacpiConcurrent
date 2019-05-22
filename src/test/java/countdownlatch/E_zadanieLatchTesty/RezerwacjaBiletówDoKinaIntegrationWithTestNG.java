package countdownlatch.E_zadanieLatchTesty;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import countdownlatch.E_zadanieLatchTesty.RezerwacjaBiletówDoKina.MetadaneRezerwacji;
import java.util.concurrent.locks.ReentrantLock;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Kacper Staszek
 * @author Marcin Ogorzałek
 *
 * TODO:Napisz odpowiednie testy bazując na nazwie metod testowych.
 *  Tym razem korzystaj tylko z TestNG
 *  Każdy test uruchamiaj minimum 10 razy, użyj puli wątków.
 *  Gdy skończysz, zaobserwuj różnicę w wykonywaniu testów z użyciem Awaitility.
 */
@Test
public class RezerwacjaBiletówDoKinaIntegrationWithTestNG {
  @Test(invocationCount = 10, threadPoolSize = 10, dataProvider = "numberOfTicketsAndExecutionTime")
  public void allTicketsGetsSold(int numberOfTickets, int executionTime)
      throws InterruptedException {
    // Given
    RezerwacjaBiletówDoKina rezerwacja = new RezerwacjaBiletówDoKina(numberOfTickets);
    MetadaneRezerwacji metadaneRezerwacji = rezerwacja.stworzeniePotrzebnychObiektów();
    // When
    rezerwacja.uruchomienieRezerwacji(metadaneRezerwacji);
    Thread.sleep(executionTime*1000);
    // Then
    assertEquals(metadaneRezerwacji.latch.getCount(),0L);
  }

  @Test (invocationCount = 10, threadPoolSize = 10, dataProvider = "numberOfTicketsAndExecutionTime")
  public void properNumberOfClientsOnReservationList(int numberOfTickets, int executionTime) throws Exception {
    // Given
    RezerwacjaBiletówDoKina rezerwacja = new RezerwacjaBiletówDoKina(numberOfTickets);
    MetadaneRezerwacji metadaneRezerwacji = rezerwacja.stworzeniePotrzebnychObiektów();
    // When
    rezerwacja.uruchomienieRezerwacji(metadaneRezerwacji);
    Thread.sleep(executionTime*1000);
    // Then
    //FIXME: Na potrzeby tego testu możesz zmienić widoczność pola z listą rezerwacji.
    // zauważ, że dzięki Awaitility nie musiałeś tego robić.
    assertEquals(metadaneRezerwacji.kino.klienciZRezerwacją.size(),numberOfTickets);
  }

  @Test(invocationCount = 10, threadPoolSize = 10, dataProvider = "numberOfTicketsAndExecutionTime")
  public void reservationLockGetsLocked(int numberOfTickets, int executionTime)
      throws InterruptedException {
    // Given
    RezerwacjaBiletówDoKina rezerwacja = new RezerwacjaBiletówDoKina(numberOfTickets);
    MetadaneRezerwacji metadaneRezerwacji = rezerwacja.stworzeniePotrzebnychObiektów();
    ReentrantLock myLock = (ReentrantLock) metadaneRezerwacji.kino.lock;
    // When
    rezerwacja.uruchomienieRezerwacji(metadaneRezerwacji);
    Thread.sleep(executionTime);
    //Then
    assertTrue(myLock.isLocked());
  }

  @Test(invocationCount = 10, threadPoolSize = 10, dataProvider = "numberOfTicketsAndExecutionTime")
  public void lockHasNoWaitingThreadsAfterReservationEnds(int numberOfTickets, int executionTime)
      throws InterruptedException {
    // Given
    RezerwacjaBiletówDoKina rezerwacja = new RezerwacjaBiletówDoKina(numberOfTickets);
    MetadaneRezerwacji metadaneRezerwacji = rezerwacja.stworzeniePotrzebnychObiektów();
    ReentrantLock myLock = (ReentrantLock) metadaneRezerwacji.kino.lock;
    // When
    rezerwacja.uruchomienieRezerwacji(metadaneRezerwacji);
    Thread.sleep(executionTime*1000);
    //Then
    assertFalse(myLock.hasQueuedThreads());
  }

  @DataProvider
  public static Object[][] numberOfTicketsAndExecutionTime() {
    return new Object[][] {
        {5,16},
        {3,13},
        {8,20}
    };
  }
}