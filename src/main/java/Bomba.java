import java.awt.*;

/**
 * Klasa reprezentująca bombę.
 */
class Bomba extends Pocisk {
    private final Color kolor;

    /**
     * Konstruktor klasy Bomba.
     *
     * @param kolor Kolor bomby.
     * @param y     Początkowe położenie na osi Y.
     * @param x     Początkowe położenie na osi X.
     */
    public Bomba(Color kolor, int y, int x) {
        super();
        this.y = y;
        this.x = x;
        this.szerokosc = 5;
        this.kolor = kolor;
        predkosc = 5;
    }

    public Color getKolor() {
        return kolor;
    }
}
