package phaser.I_wprowadzeniePhaser2;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.core.IsEqual.equalTo;
import static phaser.I_wprowadzeniePhaser2.WprowadzeniePhaserJakoCyclicBarrier.stwórzDługieZadania;
import static phaser.I_wprowadzeniePhaser2.WprowadzeniePhaserJakoCyclicBarrier.stwórzKrótkieZadania;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import org.mockito.Mockito;
import org.testng.annotations.Test;

/**
 * @author Kacper Staszek
 */
@Test
public class WprowadzeniePhaserJakoCyclicBarrierTest {
  static final int ILOSC_KRÓTKICH_ZADAŃ=2;
  static final int ILOSC_DŁUGICH_ZADAŃ=4;

  @Test(invocationCount = 10, threadPoolSize = 10)
  public void checkIfPhaserPartiesReachesProperCount() {
    Phaser phaser = new Phaser();

    ZarządcaZadań<Zadanie> zarządcaZadań = new ZarządcaZadań<>();

    ArrayBlockingQueue<Zadanie> zadaniaKrótkie = stwórzKrótkieZadania(phaser, ILOSC_KRÓTKICH_ZADAŃ);

    ArrayBlockingQueue<Zadanie> zadaniaDługie = stwórzDługieZadania(phaser, ILOSC_DŁUGICH_ZADAŃ);

    zarządcaZadań.wykonajZadania(zadaniaKrótkie, "Zadanie krótkie");
    zarządcaZadań.wykonajZadania(zadaniaDługie, "Zadanie długie");

    await().atMost(1,TimeUnit.MINUTES).until(phaser::getRegisteredParties,equalTo(ILOSC_DŁUGICH_ZADAŃ+ILOSC_KRÓTKICH_ZADAŃ));
  }

  @Test(invocationCount = 10, threadPoolSize = 10)
  public void checkIfTasksAreDeregister() {
    Phaser spyPhaser = Mockito.spy(Phaser.class);

    ZarządcaZadań<Zadanie> zarządcaZadań = new ZarządcaZadań<>();

    ArrayBlockingQueue<Zadanie> zadaniaKrótkie = stwórzKrótkieZadania(spyPhaser, ILOSC_KRÓTKICH_ZADAŃ);

    ArrayBlockingQueue<Zadanie> zadaniaDługie = stwórzDługieZadania(spyPhaser, ILOSC_DŁUGICH_ZADAŃ);

    zarządcaZadań.wykonajZadania(zadaniaKrótkie, "Zadanie krótkie");
    zarządcaZadań.wykonajZadania(zadaniaDługie, "Zadanie długie");

    await().atMost(1,TimeUnit.MINUTES).until(spyPhaser::getPhase,equalTo(Math.max(ILOSC_DŁUGICH_ZADAŃ,ILOSC_KRÓTKICH_ZADAŃ)));
    await().until(spyPhaser::getRegisteredParties,equalTo(0));
    Mockito.verify(spyPhaser,Mockito.times(ILOSC_DŁUGICH_ZADAŃ+ILOSC_KRÓTKICH_ZADAŃ)).arriveAndDeregister();

  }

  @Test(invocationCount = 10, threadPoolSize = 10)
  public void checkIfPhaserReachProperPhase() {
    Phaser phaser = new Phaser();

    ZarządcaZadań<Zadanie> zarządcaZadań = new ZarządcaZadań<>();

    ArrayBlockingQueue<Zadanie> zadaniaKrótkie = stwórzKrótkieZadania(phaser, ILOSC_KRÓTKICH_ZADAŃ);

    ArrayBlockingQueue<Zadanie> zadaniaDługie = stwórzDługieZadania(phaser, ILOSC_DŁUGICH_ZADAŃ);

    zarządcaZadań.wykonajZadania(zadaniaKrótkie, "Zadanie krótkie");
    zarządcaZadań.wykonajZadania(zadaniaDługie, "Zadanie długie");

    await().atMost(1, TimeUnit.MINUTES).until(phaser::getPhase, equalTo(Math.max(ILOSC_KRÓTKICH_ZADAŃ,ILOSC_DŁUGICH_ZADAŃ)));
  }
}