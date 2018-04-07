import java.awt.*;

/**
 * Klasa reprezentująca ruchomą belkę sterowaną przez gracza
 */
class Belka {
    private final static int lokalizacja_y = 520;
    private final static int wysokosc = 20;
    private final static int szerokosc = 120;
    private final static int predkosc = 5;
    private final Color c = Color.GREEN;
    private int lokalizacja_x = 240;

    public Color getC() {
        return c;
    }


    public int getLokalizacja_x() {
        return lokalizacja_x;
    }


    public int getLokalizacja_y() {
        return lokalizacja_y;
    }

    public int getSzerokosc() {
        return szerokosc;
    }

    public int getWysokosc() {
        return wysokosc;
    }

    /**
     * Metoda odpowiadająca za poruszania belką.
     *
     * @param k Kierunek ruchu.
     */
    public void move(int k) {
        this.lokalizacja_x += predkosc * k;
    }

}
