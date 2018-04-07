import java.awt.*;

/**
 * Klasa reprezentująca potworka.
 */
class Potworek {
    private static int szerkosc;
    private static int wysokosc;
    private static int predkosc;
    private final char Z1;
    private final char Z2;
    private final Color kolor;
    private int x;
    private int y;
    private char znak;

    /**
     * Konstruktor klasy Potworek
     *
     * @param kolor      Kolor potworka.
     * @param z_pierwszy Znak reprezentujący pierwszy stan potworka;
     * @param z_drugi    Znak reprezentujący drugi stan potworka;
     * @param x          Położenie w osi X.
     * @param szerkosc   Szerokość potworka.
     * @param wysokosc   Wysokość potworka.
     */
    public Potworek(Color kolor, char z_pierwszy, char z_drugi, int x, int szerkosc, int wysokosc) {
        this.x = x;
        this.y = 100;
        Potworek.szerkosc = szerkosc;
        Potworek.wysokosc = wysokosc;
        this.kolor = kolor;
        Z1 = z_pierwszy;
        Z2 = z_drugi;
        znak = z_pierwszy;
        predkosc = 1;
    }

    public char getZ1() {
        return Z1;
    }

    public char getZ2() {
        return Z2;
    }

    public Color getKolor() {
        return kolor;
    }

    public char getZnak() {
        return znak;
    }

    public void setZnak(char znak) {
        this.znak = znak;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSzerkosc() {
        return szerkosc;
    }

    public int getWysokosc() {
        return wysokosc;
    }

    /**
     * Metoda odpowiadająca za poruszanie się potworka.
     *
     * @param k kierunek poruszania
     */
    public void move(int k) {
        x += predkosc * k;
    }

}
