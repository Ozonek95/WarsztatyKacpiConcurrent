package countdownlatch.A_wprowadzenieCountDownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * W tej klasie zapoznamy się z CountDownLatch
 *
 * <p>CountDownLatch to klasa, która pozwala nam sterować naszym programem tak, żeby jeden wątek poczekał
 * z rozpoczęciem swojego zadania, aż kilka innych skończy pracę.</p>
 * <p>Wątki te będą współdzieliły referencję do naszej
 * instancji CountDownLatch. Na przykład, jeżeli mamy do wykonania 3 zadania, ale trzecie może ruszyć dopiero jak dwa pierwsze,
 * które mogą działać równolegle - się zakończą.</p>
 * <p>W konstruktorze CountDownLatch przekazujemy  ile wątków musi zakończyć swoją pracę, żeby kolejny mógł rozpocząć</p>
 *
 * @see CountDownLatch
 *
 * @author Kacper Staszek
 * @author Marcin Ogorzałek
 *
 * TODO: Uruchom program, przeanalizuj kod i działanie.
 *  Poznaj odpowiedź na pytanie co zmienia parametr dodany do konstruktora CountDownLatch
 */
public class WprowadzenieCountDownLatch {


    private static final int WIELKOSC_LICZNIKA = 3;

    public static void main(String[] args) {

        CountDownLatch countDownLatch = new CountDownLatch(WIELKOSC_LICZNIKA);
        for (int i = 0; i < WIELKOSC_LICZNIKA; i++) {
            Thread thread = new Thread(new Task(countDownLatch), "Wątek wykonujący zadanie numer  " + i);
            thread.start();
        }

        System.out.println("Jestem wątek, który musi poczekać na inne operacje do zakończenia, zanim pójdę dalej");
        try {
            countDownLatch.await(); // metoda, która zatrzymuje dalsze instrukcje, dopóki countDownLatch nie spadnie do 0;
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Ok, wszyscy skończyli, to lecę dalej!");
    }

}

class Task implements Runnable {
    private final CountDownLatch latch;

    Task(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        System.out.println("Doing stuff");
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(10000));  //Symulujemy, że zadanie zajmuje troszkę czasu
        } catch (InterruptedException ignore) {
            System.err.println(ignore.getMessage());
        }
        System.out.println("Skończyłem moje zadanko! " + Thread.currentThread().getName());
        latch.countDown(); //Dekrementuje nasz licznik o jeden. Kiedy licznik osiągnie zero, zadanie oczekujące się rozpocznie.
    }
}
