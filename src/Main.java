import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String menu = """
                Выбери действие, введя его номер:
                1) зашифровать текст в файле с помощью ключа
                2) расшифровать текст в файле с помощью ключа
                3) расшифровать текст в файле методом перебора ключей
                4) расшифровать текст в файле методом статистического анализа.
                """;
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
