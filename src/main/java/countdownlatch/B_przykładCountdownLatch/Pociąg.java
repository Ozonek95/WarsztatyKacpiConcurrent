package countdownlatch.B_przykładCountdownLatch;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * Odjedzie dopiero po dotarciu wszystkich pasażerów.
 *
 * @see Pasażer
 * @author Kacper
 */
class Pociąg {
    private final int zarezerwowaneBilety;
    private Queue<Pasażer> pasażerowieWPociągu;
    private final CountDownLatch latch;

    /**
     * @param zarezerwowaneBilety na ich podstawie tworzymy obiekt CountDownLatch.
     * @param pasażerowieWPociągu kolejka pasażerów, którzy już wsiedli.
     */
    Pociąg(int zarezerwowaneBilety, BlockingQueue pasażerowieWPociągu) {
        System.out.println("Pociąg przyjechał na stację!!");
        this.zarezerwowaneBilety = zarezerwowaneBilety;
        this.pasażerowieWPociągu = pasażerowieWPociągu;
        this.latch = new CountDownLatch(zarezerwowaneBilety);
    }

    void pasażerStawiłSięNaPociąg(Pasażer pasażer) {
        pasażerowieWPociągu.add(pasażer);
        System.out.println(pasażer + " przyszedł! zostało " + (zarezerwowaneBilety - pasażerowieWPociągu.size()) + " miejsc");
    }

    void pociągOdjeżdża() throws InterruptedException {
        latch.await(); // zakomentuj tę linijkę i zobacz jak program zadziała. Teraz powinieneś zrozumieć, co tak naprawdę robi await();
        System.out.println("Jedziemy do Hogwartu!");
    }

    CountDownLatch getCountDownLatch() {
        return latch;
    }
}