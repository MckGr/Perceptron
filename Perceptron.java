import java.io.*;
import java.util.*;

public class Perceptron {
    private double[] wagi;
    private double wspolczynnikUczenia;
    private int epoki;
    private Map<String, Double> mapaKlas = new HashMap<>();

    public static void main(String[] args) throws IOException {
        if (args.length != 4) {
            System.out.println("Error");
            return;
        }

        String plikTreningowy = args[0];
        String plikTestowy = args[1];
        double wspolczynnikUczenia = Double.parseDouble(args[2]);
        int epoki = Integer.parseInt(args[3]);

        Perceptron perceptron = new Perceptron(plikTreningowy, wspolczynnikUczenia, epoki);

        List<double[]> daneTreningowe = perceptron.czytajDane(plikTreningowy);
        List<double[]> daneTestowe = perceptron.czytajDane(plikTestowy);

        perceptron.trenuj(daneTreningowe, daneTestowe);

        Scanner skaner = new Scanner(System.in);
        while (true) {
            System.out.println("Podaj cechy lub 'exit' aby wyjść: ");
            String wejscie = skaner.nextLine();
            if ("exit".equals(wejscie)) {
                break;
            }

            double[] cechy = Arrays.stream(wejscie.split(",")).mapToDouble(Double::parseDouble).toArray();
            String przewidywanie = perceptron.klasyfikuj(cechy);
            System.out.println("Przewidywana klasa: " + przewidywanie);
        }
        skaner.close();
    }

    public Perceptron(String plikTreningowy, double wspolczynnikUczenia, int epoki) throws IOException {
        this.wspolczynnikUczenia = wspolczynnikUczenia;
        this.epoki = epoki;
        List<double[]> dane = czytajDane(plikTreningowy);
        this.wagi = new double[dane.get(0).length - 1];  // inicjalizujemy wagi iloscia cech
        for (int i = 0; i < wagi.length; i++) {
            wagi[i] = (Math.random() * 2) - 1;  // inicjalizujemy wagi
        }
    }

    public void trenuj(List<double[]> daneTreningowe, List<double[]> daneTestowe) {
        for (int epoka = 0; epoka < epoki; epoka++) {
            Collections.shuffle(daneTreningowe);
            for (double[] dane : daneTreningowe) {
                double przewidywane = przewiduj(dane);
                double blad = dane[dane.length - 1] - przewidywane;

                // Regula delta
                for (int i = 0; i < wagi.length; i++) {
                    wagi[i] += wspolczynnikUczenia * blad * dane[i]; // aktualizacja wag
                }
                wagi[wagi.length - 1] += wspolczynnikUczenia * blad; //teta
            }

            double dokladnosc = testuj(daneTestowe);
            System.out.println("Epoka " + (epoka + 1) + ": Dokładność = " + dokladnosc * 100 + " %");
        }
    }

    public double przewiduj(double[] wejscia) {
        double suma = wagi[wagi.length - 1];
        for (int i = 0; i < wagi.length; i++) {
            suma += wagi[i] * wejscia[i];
        }
        return funkcjaAktywacji(suma);
    }

    private double funkcjaAktywacji(double suma) {
        return suma >= 0 ? 1.0 : 0.0;
    }

    public String klasyfikuj(double[] dane) {
        double przewidywanie = przewiduj(dane);
        for (Map.Entry<String, Double> wpis : mapaKlas.entrySet()) {
            if (wpis.getValue().equals(przewidywanie)) {
                return wpis.getKey();
            }
        }
        return "";
    }

    public double testuj(List<double[]> daneTestowe) {
        int poprawne = 0;
        for (double[] dane : daneTestowe) {
            double wynik = przewiduj(dane);
            if (wynik == dane[dane.length - 1]) {
                poprawne++;
            }
        }
        return (double) poprawne / daneTestowe.size();
    }

    public List<double[]> czytajDane(String sciezkaPliku) throws IOException {
        List<double[]> dane = new ArrayList<>();
        try (BufferedReader czytnik = new BufferedReader(new FileReader(sciezkaPliku))) {
            String linia;
            while ((linia = czytnik.readLine()) != null) {
                if (linia.trim().isEmpty()) {
                    continue;
                }

                String[] tokeny = linia.split(",");
                double[] cechy = new double[tokeny.length];
                for (int i = 0; i < tokeny.length - 1; i++) {
                    cechy[i] = Double.parseDouble(tokeny[i]);
                }

                String ostatniToken = tokeny[tokeny.length - 1];
                if (!mapaKlas.containsKey(ostatniToken)) {
                    mapaKlas.put(ostatniToken, (double) mapaKlas.size());
                }
                    cechy[cechy.length - 1] = mapaKlas.get(ostatniToken);
                    dane.add(cechy);
            }
            return dane;
        }
    }
}
