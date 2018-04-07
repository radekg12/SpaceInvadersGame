/**
 * Klasa reprezentująca cegiełkę w murku.
 */
class Cegielka {
    private final static int szerokosc = 6;
    private final static int wysokosc = 6;
    private final int x;
    private final int y;

    /**
     * Konstruktor klasy Cegielka.
     *
     * @param x Płożenie na osi X.
     * @param y Płożenie na osi Y.
     */
    public Cegielka(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static int getSzerokosc() {
        return szerokosc;
    }

    public static int getWysokosc() {
        return wysokosc;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
