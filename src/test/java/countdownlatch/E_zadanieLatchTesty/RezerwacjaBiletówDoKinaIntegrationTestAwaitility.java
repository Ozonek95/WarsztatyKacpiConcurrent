package countdownlatch.E_zadanieLatchTesty;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.fieldIn;
import static org.hamcrest.core.IsEqual.equalTo;

import countdownlatch.E_zadanieLatchTesty.RezerwacjaBiletówDoKina.MetadaneRezerwacji;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Kacper Staszek
 * @author Marcin Ogorzałek
 *
 * TODO:Napisz odpowiednie testy bazując na nazwie metod testowych.
 *  Użyj zewnętrznej biblioteki Awaitility.
 *  Każdy test uruchamiaj minimum 10 razy, użyj puli wątków.
 */
@Test
public class RezerwacjaBiletówDoKinaIntegrationTestAwaitility {

  @Test(invocationCount = 10, threadPoolSize = 10, dataProvider = "numberOfTicketsAndExecutionTime")
  public void allTicketsGetsSold(int numberOfTickets, int executionTime) {
    // Given
    RezerwacjaBiletówDoKina rezerwacja = new RezerwacjaBiletówDoKina(numberOfTickets);
    MetadaneRezerwacji metadaneRezerwacji = rezerwacja.stworzeniePotrzebnychObiektów();
    // When
    rezerwacja.uruchomienieRezerwacji(metadaneRezerwacji);
    // Then
    await().atMost(executionTime, TimeUnit.SECONDS).until(metadaneRezerwacji.latch::getCount,equalTo(0L));
  }

  @Test (invocationCount = 10, threadPoolSize = 10, dataProvider = "numberOfTicketsAndExecutionTime")
  public void properNumberOfClientsOnReservationList(int numberOfTickets, int executionTime) throws Exception {
    // Given
    RezerwacjaBiletówDoKina rezerwacja = new RezerwacjaBiletówDoKina(numberOfTickets);
    MetadaneRezerwacji metadaneRezerwacji = rezerwacja.stworzeniePotrzebnychObiektów();
    // When
    rezerwacja.uruchomienieRezerwacji(metadaneRezerwacji);
    // Then
    await().atMost(executionTime, TimeUnit.SECONDS).until(fieldIn(metadaneRezerwacji.kino)
        .ofType(List.class).andWithName("klienciZRezerwacją").call()::size,equalTo(numberOfTickets));
  }

  @Test(invocationCount = 10, threadPoolSize = 10, dataProvider = "numberOfTicketsAndExecutionTime")
  public void reservationLockGetsLocked(int numberOfTickets, int executionTime){
    // Given
    RezerwacjaBiletówDoKina rezerwacja = new RezerwacjaBiletówDoKina(numberOfTickets);
    MetadaneRezerwacji metadaneRezerwacji = rezerwacja.stworzeniePotrzebnychObiektów();
    ReentrantLock myLock = (ReentrantLock) metadaneRezerwacji.kino.lock;
    // When
    rezerwacja.uruchomienieRezerwacji(metadaneRezerwacji);
    //Then
    await().atMost(executionTime,TimeUnit.SECONDS).until(myLock::isLocked,equalTo(true));
  }

  @Test(invocationCount = 10, threadPoolSize = 10, dataProvider = "numberOfTicketsAndExecutionTime")
  public void lockHasNoWaitingThreadsAfterReservationEnds(int numberOfTickets, int executionTime){
    // Given
    RezerwacjaBiletówDoKina rezerwacja = new RezerwacjaBiletówDoKina(numberOfTickets);
    MetadaneRezerwacji metadaneRezerwacji = rezerwacja.stworzeniePotrzebnychObiektów();
    ReentrantLock myLock = (ReentrantLock) metadaneRezerwacji.kino.lock;
    // When
    rezerwacja.uruchomienieRezerwacji(metadaneRezerwacji);
    //Then
    await().atMost(executionTime,TimeUnit.SECONDS).until(myLock::hasQueuedThreads,equalTo(false));
  }

  @DataProvider
  public static Object[][] numberOfTicketsAndExecutionTime() {
      return new Object[][] {
          {5,16},
          {3,12},
          {8,20}
      };
  }
}

//TODO: JAK TO DZIAŁA, DLACZEMU NIE TAK JAK SIĘ SPODZIEWAMY.
//  @Test(invocationCount = 1000, threadPoolSize = 1000, successPercentage = 95)
//  public void allTicketsGetsSold() {
//    // Given
//    RezerwacjaBiletówDoKina rezerwacja = new RezerwacjaBiletówDoKina(5);
//    MetadaneRezerwacji matadaneKina = rezerwacja.stworzeniePotrzebnychObiektów();
//    // When
//    rezerwacja.uruchomienieRezerwacji(matadaneKina);
//    // Then
//    await().atMost(5, TimeUnit.SECONDS).until(matadaneKina.latch::getCount,equalTo(0L));
//  }