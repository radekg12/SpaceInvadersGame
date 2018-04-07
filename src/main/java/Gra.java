import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Gawryś Radosław, Bąk Michał, Bancerz Patrycja
 * @version 1.0
 * Klasa jest najważniejszą klasą, zawiera najważniejsze metody potrzebne do funkcjonowania gry.
 */

class Gra extends JFrame implements ActionListener {
    private final static int MAX_ZYC = 5;
    private final static char PIERWSZY_POTWOREK = 98;
    private static Timer timer;
    private final Belka belka = new Belka();
    private final ArrayList<Pocisk> pociski = new ArrayList<>();
    private final ArrayList<Bomba> bomby = new ArrayList<>();
    private final ArrayList<Potworek> potworki = new ArrayList<>();
    private final ArrayList<Murek> murki = new ArrayList<>();
    private final ArrayList<Color> kolor = new ArrayList<>();
    private final JFrame ramka = new JFrame();
    private BufferedImage ikona;
    private BufferedImage logo;
    private BufferedImage pasekPotworkow;
    private BufferedImage potworek;
    private BufferedImage zycieSerce;
    private BufferedImage zycieCzaszka;
    private boolean klik = false;
    private boolean start = false;
    private boolean przegrana = false;
    private boolean wygrana = false;
    private int punkty = 0;
    private int zycia = MAX_ZYC;
    private boolean pause = false;
    private int krokI;
    private int krokJ;
    private int kierunek;
    private char nrPotworka = PIERWSZY_POTWOREK;

    /**
     * Konstruktor klasy Gra.
     */
    private Gra() {
        timer = new Timer(25, this);
        ramka.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ramka.setTitle("Space Invaders");
        ramka.setSize(800, 700);
        Panel panel = new Panel();
        ramka.add(panel);
        ramka.addKeyListener(new RuchBelki());
        try {
            ikona = ImageIO.read(getClass().getResource("/images/logo.jpg"));
            logo = ImageIO.read(getClass().getResource("/images/si_logo2.png"));
            pasekPotworkow = ImageIO.read(getClass().getResource("/images/si_p3.png"));
            potworek = ImageIO.read(getClass().getResource("/images/sss.png"));
            zycieSerce = ImageIO.read(getClass().getResource("/images/heart.png"));
            zycieCzaszka = ImageIO.read(getClass().getResource("/images/skull.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ramka.setIconImage(ikona);
        ramka.setResizable(false);
        ramka.setVisible(true);
    }

    /**
     * Metoda główna main
     *
     * @param args Parametr wywołania.
     */
    public static void main(String[] args) throws InterruptedException {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Gra();
            }
        });
    }


    /**
     * Metoda odpowiedzialna za ustawienie początkowych wartości, w przypadku rozpoczęcia gry.
     */
    private void initGame() {
        kolor.addAll(Arrays.asList(Color.WHITE, Color.green, Color.CYAN, Color.MAGENTA));
        zycia = MAX_ZYC;
        nrPotworka = PIERWSZY_POTWOREK;
        for (int i = 0; i < 3; i++) dodajPotworki();
        for (int i = 0; i < 4; i++) murki.add(new Murek(40 + i * 185));
        krokI = 0;
        krokJ = 0;
        kierunek = 1;
        punkty = 0;
        //timer.start();
    }

    /**
     * Metoda czyszcząca kolekcje z powstałych w czasie gry obiektów.
     */
    private void konczGre() {
        timer.stop();
        start = false;
        przegrana = true;
        potworki.clear();
        murki.clear();
        bomby.clear();
        pociski.clear();
        kolor.clear();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (krokJ % 32 == 0)
            for (Potworek p : potworki) p.setZnak(p.getZ1());
        if (krokJ % 64 == 0)
            for (Potworek p : potworki) p.setZnak(p.getZ2());
        lecaPociski();
        lecaBomby();
        if (sprKolizje()) punkty += 25;
        if (sprStrzal()) punkty += 10;
        if (sprPostrzelenie()) zycia--;
        if (zycia <= 0) konczGre();
        if (potworki.size() == 0 && !przegrana) wygrana = true;
        sprStrzalMurek();
        sprBombaMurek();
        if (krokJ % 2 == 0) chodzaPotworki(kierunek);
        ramka.repaint();
        if (krokJ % 30 == 0) zrzucBombe();
        if (krokJ == 128) {
            kierunek *= -1;
            krokJ = 0;
            krokI++;
        }
        if (krokI == 8) {
            dodajPotworki();
            krokI = 0;
        }
        krokJ++;
        if (czyPotworkiWyszly()) konczGre();
    }

    /**
     * Metoda ta dodaje nowy wiersz potworków.
     */
    private void dodajPotworki() {
        int s = 50, w = 35, p = 30;
        if (nrPotworka == 103) nrPotworka = PIERWSZY_POTWOREK;
        char nrPotworka2 = nrPotworka++;
        Color kolorek = kolor.get(0);
        for (Potworek pot : potworki) pot.setY(pot.getY() + 45);
        for (int j = 0; j < 8; j++) {
            potworki.add(new Potworek(kolorek, nrPotworka, nrPotworka2, 30 + j * (s + p), s, w));
        }
        kolor.remove(0);
        kolor.add(kolorek);
        nrPotworka++;
    }

    /**
     * Metoda odpowiadająca za poruszanie się potworków w bok.
     *
     * @param k oznacza kierunek chodzenia potworków (1- w prawo, -1- w lewo).
     */
    private void chodzaPotworki(int k) {
        for (Potworek p : potworki) {
            p.move(k);
        }
    }

    /**
     * Metoda odpowiadająca za poruszanie się pocisków w górę.
     */
    private void lecaPociski() {
        for (Pocisk p : pociski) {
            p.move(1);
        }
    }

    /**
     * Metoda odpowiadająca za poruszanie się bomb w dół.
     */
    private void lecaBomby() {
        for (Bomba p : bomby) {
            p.move(-1);
        }
    }


    /**
     * Metoda odpowiadająca za generowanie bomby od losowo wybranego potworka.
     */
    private void zrzucBombe() {
        if (potworki.size() > 0) {
            int random;
            random = (int) Math.round(Math.random() * (potworki.size() - 1));
            Potworek pot = potworki.get(random);
            Color kolorek = pot.getKolor();
            bomby.add(new Bomba(kolorek, pot.getY() + pot.getWysokosc(), pot.getX() + (pot.getSzerkosc() / 2)));
        }
    }

    /**
     * Metoda sprawdze czy pocisk i bomba się zderzyły.
     *
     * @return true- gdy dojdzie do kolizji, false- gdy nie dojdzie
     */
    private boolean sprKolizje() {
        for (Bomba b : bomby) {
            for (Pocisk p : pociski) {
                if (b.getY() >= p.getY() && ((b.getX() + b.getSzerokosc() > p.getX() && b.getX() < p.getX() + p.getSzerokosc()) || (p.getX() + p.getSzerokosc() > b.getX() && p.getX() < b.getX() + b.getSzerokosc()))) {
                    bomby.remove(b);
                    pociski.remove(p);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Metoda sprawdza czy pocisk zabił potworka.
     *
     * @return true- pocisk zabił potworka, false- nie zabił potworka
     */
    private boolean sprStrzal() {
        for (Pocisk p : pociski) {
            for (Potworek pot : potworki) {
                if (p.getY() <= pot.getY() + pot.getWysokosc() && p.getY() >= pot.getY() && (p.getX() + (p.getSzerokosc() / 2) > pot.getX() && p.getX() + (p.getSzerokosc() / 2) < pot.getX() + pot.getSzerkosc())) {
                    //pot.setZnak('z');
                    pociski.remove(p);
                    potworki.remove(pot);
                    ramka.repaint();
                    return true;
                }
            }
            if (p.getY() <= 0) {
                pociski.remove(p);
                return false;
            }
        }
        return false;
    }

    /**
     * Metoda sprawdza czy pocisk uderzył w murek, gdy dojdzie do zderzenia pocisk i cegiełka murku są usuwane.
     */
    private void sprStrzalMurek() {
        for (Pocisk p : pociski) {
            if (p.getY() > 380) {
                for (Murek m : murki) {
                    if (p.getX() + p.getSzerokosc() / 2 > m.getX() && p.getX() + p.getSzerokosc() / 2 < m.getX() + m.getSzerokosc()) {
                        for (Cegielka c : m.murek) {
                            if (p.getY() <= c.getY() + Cegielka.getWysokosc() && p.getX() + p.getSzerokosc() / 2 >= c.getX() && p.getX() + p.getSzerokosc() / 2 <= c.getX() + Cegielka.getSzerokosc()) {
                                m.murek.remove(c);
                                pociski.remove(p);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Metoda sprawdza czy bomba uderzyła w murek, gdy dojdzie do zderzenia bomba i cegiełka murku są usuwane.
     */
    private void sprBombaMurek() {
        for (Bomba b : bomby) {
            if (b.getY() > 380) {
                for (Murek m : murki) {
                    if (b.getX() + b.getSzerokosc() / 2 >= m.getX() && b.getX() + b.getSzerokosc() / 2 <= m.getX() + m.getSzerokosc()) {
                        for (Cegielka c : m.murek) {
                            if (b.getY() + b.getWysokosc() >= c.getY() && b.getX() + b.getSzerokosc() / 2 >= c.getX() && b.getX() + b.getSzerokosc() / 2 <= c.getX() + Cegielka.getSzerokosc()) {
                                m.murek.remove(c);
                                //pociski.remove(p);
                                bomby.remove(b);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Metoda sprawdza czy bomba trafiła w belkę.
     *
     * @return true- gdy trafi w belkę, false- jeśli nie trafi w belkę
     */
    private boolean sprPostrzelenie() {
        for (Bomba b : bomby) {
            if (b.getY() + b.getWysokosc() >= belka.getLokalizacja_y() && b.getY() <= belka.getLokalizacja_y() + belka.getWysokosc() && (b.getX() + b.getSzerokosc() / 2 > belka.getLokalizacja_x() && b.getX() + b.getSzerokosc() / 2 < belka.getLokalizacja_x() + belka.getSzerokosc())) {
                bomby.remove(b);
                return true;
            }
            if (b.getY() + b.getWysokosc() > 600) {
                bomby.remove(b);
                return false;
            }
        }

        return false;
    }

    /**
     * Metoda sprawdza czy potworki nie przekroczyły lini murków.
     *
     * @return true- wyszły poza obszar, false- nie wyszły poza obszar
     */
    private boolean czyPotworkiWyszly() {
        for (Potworek p : potworki) {
            if (p.getY() + p.getWysokosc() > Murek.getY() + Murek.getWysokosc()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Klasa reprezentująca kontener Panel.
     */
    public class Panel extends JPanel {
        public Panel() {
            setBackground(Color.BLACK);
        }

        /**
         * Metoda odpowiadająca za wyświetlanie komponentów na ekranie.
         */
        public void paintComponent(Graphics g) {
            super.paintComponents(g);
            g.setColor(Color.black);
            g.fillRect(0, 0, ramka.getWidth(), ramka.getHeight());
            if (!start && !przegrana && !wygrana) {
                g.setColor(Color.black);
                g.fillRect(0, 0, ramka.getWidth(), ramka.getHeight());
                g.drawImage(pasekPotworkow, 200, 350, null);
                //g.drawImage(new ImageIcon("si_logo2.png").getImage(), 50, 50, null);
                g.drawImage(logo, 50, 50, null);
                g.setColor(Color.white);
                g.setFont(new Font("Space Invaders", Font.PLAIN, 15));
                g.drawString("Nacisnij ENTER, aby rozpoczac... ", 235, 500);
            }
            if (przegrana) {
                g.setColor(Color.black);
                g.fillRect(0, 0, ramka.getWidth(), ramka.getHeight());
                g.setColor(Color.white);
                g.setFont(new Font("Space Invaders", Font.PLAIN, 80));
                g.drawString("GAME OVER", 150, 200);
                g.setFont(new Font("Space Invaders", Font.PLAIN, 20));
                g.drawString("Zdobyles  " + punkty + "  punktow", 250, 300);
                g.drawImage(pasekPotworkow, 220, 350, null);
                g.setColor(Color.white);
                g.setFont(new Font("Space Invaders", Font.PLAIN, 15));
                g.drawString("Nacisnij ENTER, aby zagrac jeszcze raz... ", 210, 500);
                timer.stop();
            }
            if (wygrana) {
                g.setColor(Color.white);
                g.setFont(new Font("Space Invaders", Font.PLAIN, 80));
                g.drawString("YOU WIN !", 200, 200);
                g.setFont(new Font("Space Invaders", Font.PLAIN, 20));
                g.drawString("Zdobyles  " + punkty + "  punktow", 250, 300);
                //g.drawImage(pasekPotworkow, 220, 350, null);
                g.setColor(Color.white);
                g.setFont(new Font("Space Invaders", Font.PLAIN, 15));
                g.drawString("Nacisnij ENTER, aby zagrac jeszcze raz... ", 210, 500);
                timer.stop();
            }
            if (start && !przegrana) {
                g.setColor(belka.getC());
                g.fillRect(belka.getLokalizacja_x(), belka.getLokalizacja_y(), belka.getSzerokosc(), belka.getWysokosc());
                g.fillRect(belka.getLokalizacja_x() + 7, belka.getLokalizacja_y() - 3, belka.getSzerokosc() - 14, 3);
                g.fillRect(belka.getLokalizacja_x() + 53, belka.getLokalizacja_y() - 8, 14, 5);
                g.setColor(Color.YELLOW);
                for (Pocisk p : pociski) g.fillRect(p.getX(), p.getY(), p.getSzerokosc(), p.getWysokosc());
                g.setColor(Color.WHITE);
                g.setFont(new Font("invanders from space", Font.PLAIN, 45));
                for (Potworek p : potworki) {
                    g.setColor(p.getKolor());
                    //g.fillRect(p.getX(), p.getY(), p.getSzerkosc(), p.getWysokosc());
                    //g.drawImage(potworek, p.getX(), p.getY(), null);
                    g.drawString(String.valueOf(p.getZnak()), p.getX(), p.getY() + 32);
                }

                for (Bomba b : bomby) {
                    g.setColor(b.getKolor());
                    g.fillRect(b.getX(), b.getY(), b.getSzerokosc(), b.getWysokosc());
                }
                g.setColor(Color.WHITE);
                g.setFont(new Font("Space Invaders", Font.PLAIN, 15));
                g.drawString("Score: " + punkty, 20, 30);
                g.setFont(new Font("Space Invaders", Font.BOLD, 10));
                g.drawString("(P) - PAUSE", ramka.getWidth() - 100, ramka.getHeight() - 40);
                //g.setFont(new Font("invanders from space", Font.PLAIN, 45));
                //g.drawString("b z", 30, 80);
                g.setColor(Color.WHITE);
                for (Murek m : murki) {
                    for (Cegielka c : m.murek) {
                        g.fillRect(c.getX(), c.getY(), Cegielka.getSzerokosc(), Cegielka.getWysokosc());
                    }
                }
                for (int i = 0; i < zycia; i++) {
                    g.drawImage(zycieSerce, 30 + i * 50, 600, null);
                    for (int j = zycia; j < MAX_ZYC; j++) {
                        g.drawImage(zycieCzaszka, 30 + j * 50, 600, null);
                    }
                }
            }
        }
    }

    /**
     * Klasa odpowiadająca za obsługę klawiatury
     */
    public class RuchBelki implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        /**
         * Metoda odpowiadająca za ruch belki
         *
         * @param e wciśnięty klawisz na klawiaturze
         */
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_RIGHT && belka.getLokalizacja_x() < ramka.getWidth() - belka.getSzerokosc())
                belka.move(1);
            if (key == KeyEvent.VK_LEFT && belka.getLokalizacja_x() > 0) belka.move(-1);
            if (key == KeyEvent.VK_SPACE && !klik) {
                Pocisk p1 = new Pocisk(belka.getLokalizacja_y(), belka.getLokalizacja_x(), belka.getSzerokosc());
                pociski.add(p1);
                klik = true;
            }
            if (key == KeyEvent.VK_P) {
                if (!pause) {
                    pause = true;
                    timer.stop();
                } else {
                    pause = false;
                    timer.start();
                }
            }
            if (key == KeyEvent.VK_ENTER && (wygrana || przegrana || !start)) {
                konczGre();
                initGame();
                start = true;
                przegrana = false;
                wygrana = false;
                timer.start();

            }
            ramka.repaint();
        }

        /**
         * Metoda zmieniająca wartośc pola klik w przypadku zwolnienia klawisza.
         *
         * @param e wciśnięty klawisz na klawiaturze
         */
        @Override
        public void keyReleased(KeyEvent e) {
            klik = false;
        }
    }
}