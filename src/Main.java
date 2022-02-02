import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String menu = "Выбери действие, введя его номер:\n" +
                "1) зашифровать текст в файле с помощью ключа\n" +
                "2) расшифровать текст в файле с помощью ключа\n" +
                "3) расшифровать текст в файле методом перебора ключей\n" +
                "4) расшифровать текст в файле методом статистического анализа.\n";
        System.out.println(menu);
        System.out.println("Введи действие:");

        CryptoAnalyzer cryptoAnalyzer = new CryptoAnalyzer();

        switch (scanner.nextLine()) {
            case "1" -> cryptoAnalyzer.encrypt();
            case "2" -> cryptoAnalyzer.decryptKey();
            case "3" -> cryptoAnalyzer.decryptBrut();
            case "4" -> cryptoAnalyzer.decryptStat();
        }
    }
}
