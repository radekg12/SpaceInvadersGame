import java.util.ArrayList;

/**
 * Klasa reprezentująca murek.
 */
class Murek {
    private static final int szerokosc = 17 * Cegielka.getSzerokosc();
    /**
     * Tablica z rozkładem cegiełek na murku.
     */
    private final static int[][] wzor = {
            {0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0},
            {0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 0},
            {1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1},
            {1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1},
            {1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1},
            {1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1},
            {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
    };
    private static final int wysokosc = 8 * Cegielka.getWysokosc();
    private static int y;
    final ArrayList<Cegielka> murek = new ArrayList<>();
    private final int x;

    /**
     * Konstruktor klasy Murek.
     *
     * @param x Płożenie na osi X.
     */
    public Murek(int x) {
        this.x = x;
        Murek.y = 400;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 17; j++) {
                if (wzor[i][j] == 1)
                    murek.add(new Cegielka(x + j * Cegielka.getSzerokosc(), y + i * Cegielka.getSzerokosc()));
            }
    }

    public static int getY() {
        return y;
    }

    public static int getWysokosc() {
        return wysokosc;
    }

    public int getSzerokosc() {
        return szerokosc;
    }


    public int getX() {
        return x;
    }


}
