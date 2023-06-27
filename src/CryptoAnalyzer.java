import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CryptoAnalyzer {

    String alphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя.,\":-!? ";

    //Шифрование текста заданным ключом шифрования
    public void encrypt() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введи полный путь к файлу, содержимое которого хочешь зашифровать:");
        String inputFileName = scanner.nextLine();
        System.out.println("Введи ключ шифрования:");
        int key = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Введи полный путь к файлу, в который хочешь записать зашифрованный текст:");
        String outputFileName = scanner.nextLine();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFileName)); BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFileName))) {
            ArrayList<String> data = new ArrayList<>();

            while (bufferedReader.ready()) {
                String string = bufferedReader.readLine();
                char[] chars = string.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    int index = alphabet.indexOf(Character.toLowerCase(chars[i]));
                    if (index == -1) {
                        continue;
                    }
                    int shift = (index + key) % alphabet.length();
                    if (shift < 0) shift += alphabet.length();
                    chars[i] = alphabet.charAt(shift);
                }
                data.add(new String(chars));
            }

            for (String string : data) {
                bufferedWriter.write(string + "\n");
            }
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }

    //Расшифровка текста заданным ключом шифрования
    public void decryptKey() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введи полный путь к файлу, содержимое которого хочешь расшифровать:");
        String inputFileName = scanner.nextLine();
        System.out.println("Введи ключ шифрования:");
        int key = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Введи полный путь к файлу, в который хочешь записать расшифрованный текст:");
        String outputFileName = scanner.nextLine();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFileName)); BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFileName))) {
            ArrayList<String> data = new ArrayList<>();
            while (bufferedReader.ready()) {
                String string = bufferedReader.readLine();
                char[] chars = string.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    int index = alphabet.indexOf(Character.toLowerCase(chars[i]));
                    if (index == -1) {
                        continue;
                    }

                    int shift = (index - key) % alphabet.length();
                    if (shift < 0) {
                        shift = shift + alphabet.length();
                    }
                    chars[i] = alphabet.charAt(shift);
                }
                data.add(new String(chars));
            }

            for (String string : data) {
                bufferedWriter.write(string + "\n");
            }
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }

    //Расшифровка текста путём подбора подходящего ключа шифрования
    public void decryptBrut() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введи полный путь к файлу, содержимое которого хочешь расшифровать:");
        String inputFileName = scanner.nextLine();
        System.out.println("Введи полный путь к файлу, в который хочешь записать расшифрованный текст:");
        String outputFileName = scanner.nextLine();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFileName)); BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFileName))) {
            ArrayList<String> inputData = new ArrayList<>();
            ArrayList<String> outputData = new ArrayList<>();

            while (bufferedReader.ready()) {
                inputData.add(bufferedReader.readLine());
            }

            for (int key = 1; key < alphabet.length(); key++) {
                for (String string : inputData) {
                    char[] chars = string.toCharArray();
                    for (int i = 0; i < chars.length; i++) {
                        int index = alphabet.indexOf(Character.toLowerCase(chars[i]));
                        if (index == -1) {
                            continue;
                        }

                        int shift = (index - key) % alphabet.length();
                        if (shift < 0) shift = shift + alphabet.length();
                        chars[i] = alphabet.charAt(shift);
                    }
                    outputData.add(new String(chars));
                }

                boolean isCorrectLength = true;
                boolean isCorrectPunct;
                int notCorrectPunct = 0;
                int countWords = 0;

                for (String string : outputData) {
                    if (string.matches("(.*)[a-zA-Z](.*)")) {
                        continue;
                    }

                    String[] stringsLength = string.split(" ");
                    for (String s : stringsLength) {
                        if (s.length() > 25) {
                            isCorrectLength = false;
                            break;
                        }
                    }

                    String[] stringsPunct = string.split("[?!.]");
                    for (String s : stringsPunct) {
                        if (stringsPunct.length == 1 | s.length() == 1 | s.isEmpty()) {
                            break;
                        }
                        if (!s.startsWith(" ")) {
                            notCorrectPunct++;
                        }
                    }
                }

                for (String string : outputData) {
                    String[] words = string.split(" ");
                    countWords += words.length;
                }

                isCorrectPunct = notCorrectPunct <= countWords / 10;

                if (isCorrectLength & isCorrectPunct) {
                    System.out.println("ключ подобран - " + key);
                    break;
                }
                outputData.clear();
            }

            for (String string : outputData) {
                bufferedWriter.write(string + "\n");
            }
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }

    //Расшифровка текста методом статистического анализа на основе загруженного дополнительного файла с текстом
    public void decryptStat() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введи полный путь к файлу, содержимое которого хочешь расшифровать:");
        String inputFileName = scanner.nextLine();
        System.out.println("Введи полный путь к файлу, в котором содержится текст для статистики:");
        String inputStatFileName = scanner.nextLine();
        System.out.println("Введи полный путь к файлу, в который хочешь записать расшифрованный текст:");
        String outputFileName = scanner.nextLine();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {

            List<String> inputData = Files.readAllLines(Path.of(inputFileName));
            List<String> statData = Files.readAllLines(Path.of(inputStatFileName));
            ArrayList<String> outputData = new ArrayList<>();

            HashMap<Character, Integer> inputChars = new HashMap<>();
            HashMap<Character, Integer> statChars = new HashMap<>();

            for (String string : inputData) {
                char[] chars = string.toLowerCase().toCharArray();
                for (char ch : chars) {
                    if (inputChars.get(ch) == null) {
                        inputChars.put(ch, 1);
                    } else inputChars.put(ch, inputChars.get(ch) + 1);
                }
            }

            for (String string : statData) {
                char[] chars = string.toLowerCase().toCharArray();
                for (char ch : chars) {
                    if (statChars.get(ch) == null) {
                        statChars.put(ch, 1);
                    } else statChars.put(ch, statChars.get(ch) + 1);
                }
            }

            ArrayList<Map.Entry<Character, Integer>> inputList = sortListMapEntry(inputChars);
            ArrayList<Map.Entry<Character, Integer>> statList = sortListMapEntry(statChars);

            HashMap<Character, Character> totalMap = new HashMap<>();

            for (int i = 0; i < inputList.size(); i++) {
                totalMap.put(inputList.get(i).getKey(), statList.get(i).getKey());
            }

            for (String string : inputData) {
                char[] chars = string.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    if (totalMap.containsKey(chars[i])) {
                        chars[i] = totalMap.get(chars[i]);
                    }
                }
                outputData.add(new String(chars));
            }

            for (String string : outputData) {
                writer.write(string + "\n");
            }
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }

    private ArrayList<Map.Entry<Character, Integer>> sortListMapEntry(HashMap<Character, Integer> chars) {
        ArrayList<Map.Entry<Character, Integer>> resultList = new ArrayList<>(chars.entrySet());
        resultList.sort((o1, o2) -> o2.getValue() - o1.getValue());
        return resultList;
    }
}
