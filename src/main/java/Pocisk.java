/**
 * Klasa reprezentująca pocisk.
 */
class Pocisk {
    private final static int wysokosc = 8;
    int y;
    int x;
    int szerokosc = 4;
    int predkosc = 10;

    /**
     * Konstukror klasy Pocisk.
     *
     * @param y           Początkowe położenie na osi Y.
     * @param x           Początkowe położenie na osi X.
     * @param szerokosc_b Szerokośc belki.
     */
    public Pocisk(int y, int x, int szerokosc_b) {
        this.y = y - 18;
        this.x = x + (szerokosc_b / 2) - (szerokosc / 2);
    }

    /**
     * Konstruktor klasy Pocisk, bez parametrów.
     */
    Pocisk() {
    }

    public int getY() {
        return y;
    }


    public int getX() {
        return x;
    }

    public int getWysokosc() {
        return wysokosc;
    }


    public int getSzerokosc() {
        return szerokosc;
    }

    /**
     * Metoda odpowiadająca za poruszanie pocisku.
     *
     * @param k Kierunek ruchu (1 - góra, -1 - dół)
     */
    public void move(int k) {
        this.y -= predkosc * k;
    }
}
