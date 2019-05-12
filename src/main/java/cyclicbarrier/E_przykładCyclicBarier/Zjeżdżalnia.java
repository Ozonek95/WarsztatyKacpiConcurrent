package cyclicbarrier.E_przykładCyclicBarier;

import java.util.concurrent.CyclicBarrier;

/**
 * Nasza zjeżdżalnia służy do wyścigów. Mamy x pozycji do startu, dlatego nasza bariera
 * będzie oczekiwać na x gotowych chętnych. Dopiero wtedy osoba pilnująca zjeżdżali
 * da sygnał do startu.
 *
 * @author Kacper Staszek
 */
class Zjeżdżalnia implements Runnable {
    private final int iloscTorow;
    private CyclicBarrier cyklicznaBariera;

    Zjeżdżalnia(int iloscTorow) {
        this.iloscTorow = iloscTorow;
        this.cyklicznaBariera = new CyclicBarrier(iloscTorow, this);
    }

    @Override
    public void run() {
        System.out.println("\033[1;32mMamy " + iloscTorow + " chętych, odjeżdżamy!");
        System.out.print("\033[0m");
    }

    CyclicBarrier getBarrier() {
        return cyklicznaBariera;
    }
}
