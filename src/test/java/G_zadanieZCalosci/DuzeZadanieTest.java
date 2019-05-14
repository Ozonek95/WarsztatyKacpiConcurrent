package G_zadanieZCalosci;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.fieldIn;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.testng.annotations.Test;

/**
 * @author Kacper Staszek
 */
@Test
public class DuzeZadanieTest {
  private static final int LICZBA_GRACZY_BIORĄCYCH_UDZIAŁ=10;
  private static final int LICZBA_GRACZY_KTÓRZY_PRZEJDĄ_DALEJ=5;
  private static final int LICZBA_RUND_DO_ROZEGRANIA=3;

  @Test(invocationCount = 10,threadPoolSize = 10)
  public void testIfLatchInTestReachZero(
  ) throws Exception {
    TestZGry testZGry = new TestZGry(LICZBA_GRACZY_BIORĄCYCH_UDZIAŁ,LICZBA_GRACZY_KTÓRZY_PRZEJDĄ_DALEJ);
    testZGry.generujGraczy();
    testZGry.startujTest();
    await().until(fieldIn(testZGry).ofType(CountDownLatch.class).andWithName("countDownLatch").call()::getCount,equalTo(0L));
  }

  @Test(invocationCount = 10,threadPoolSize = 10)
  public void testIfListOfPlayersReachItsNumber(){
    TestZGry testZGry = new TestZGry(LICZBA_GRACZY_BIORĄCYCH_UDZIAŁ,LICZBA_GRACZY_KTÓRZY_PRZEJDĄ_DALEJ);
    testZGry.generujGraczy();
    List<GraczGrającyWGrę> graczGrającyWGrę = testZGry.startujTest();
    await().until(graczGrającyWGrę::size,equalTo(LICZBA_GRACZY_KTÓRZY_PRZEJDĄ_DALEJ));
  }

  @Test(invocationCount = 10,threadPoolSize = 10)
  public void testIfBarrierRunsProperNumberOfTimes() throws Exception {
    List<GraczGrającyWGrę> graczGrającyWGrę = Stream.generate(GraczGrającyWGrę::new)
        .limit(LICZBA_GRACZY_KTÓRZY_PRZEJDĄ_DALEJ).collect(Collectors.toList());

    DemonstracjaGry demonstracjaGry = new DemonstracjaGry(graczGrającyWGrę,LICZBA_RUND_DO_ROZEGRANIA);
    demonstracjaGry.startujGrę();

    await().until(fieldIn(demonstracjaGry).ofType(CountDownLatch.class).andWithName("latch").call()::getCount,equalTo(0L));
  }
}