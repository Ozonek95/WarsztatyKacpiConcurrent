package countdownlatch.C1_zadanieCountDownLatch;

import fabryczkapomocnicza.MyThreadFactory;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Marcin Ogorzalek
 */
@Test
public class KierowcaTest {

  private static final int ILOSC_LUDZI_W_EKIPIE = 4;
  private static final long CZAS_OCZEKIWANIA_KIEROWCY = 5000;

  @Test(invocationCount = 100, threadPoolSize = 100)
  public void checkIfWhenThiefAreCatchTheirGoingToJail(){
    CountDownLatch latch = new CountDownLatch(ILOSC_LUDZI_W_EKIPIE);

    ExecutorService executorService = Executors.newFixedThreadPool(ILOSC_LUDZI_W_EKIPIE,
        new MyThreadFactory("Członek ekipy"));

    kierowcaPodjeżdzaNaRobotę(latch, executorService);
    Więzienie więzienie = new Więzienie();
    ekipaZaczynaObrabiaćJubilera(latch, executorService, więzienie);

    try {
      Thread.sleep(CZAS_OCZEKIWANIA_KIEROWCY);
    } catch (InterruptedException ignored) {
      System.err.println(ignored.getMessage());
    }
    Assert.assertEquals(więzienie.getWięźniowie().size(),latch.getCount());
  }

  private void ekipaZaczynaObrabiaćJubilera(CountDownLatch latch, ExecutorService executorService,
      Więzienie więzienie) {
      for (int i = 0; i < ILOSC_LUDZI_W_EKIPIE; i++) {
        executorService.submit(new CzłonekEkipy(latch, więzienie, 10000));
      }
      executorService.shutdown();
  }

  private void kierowcaPodjeżdzaNaRobotę(CountDownLatch latch, ExecutorService executorService) {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.submit(new Kierowca(latch, executorService, CZAS_OCZEKIWANIA_KIEROWCY));
    executor.shutdown();
  }

}