import java.io.*;
import java.net.URL;
import java.util.*;

public class Test {

    public static Map<String,Integer> wordMap = new TreeMap<>();
    public static int wordCounter = 0;

    public static void main(String[] args) throws IOException {

        //Ввод URL-ссылки на файл и загрузка файла
        BufferedReader bufReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите URL файла для подсчета количества слов:");

        File tmpFile = File.createTempFile("file",null);
        while (tmpFile.length() == 0) {
            try {
                //String url = "http://madbrains.github.io/java_course_test";
                String url = bufReader.readLine();
                downloadUsingStream(url, tmpFile);
            } catch (Exception e) {
                System.out.println("Некорректный URL! Пожалуйста введите корректный адрес файла:");
            }
        }
        bufReader.close();

        //Считывание файла в буфер и подсчет количества слов
        BufferedReader reader = new BufferedReader(new FileReader(tmpFile));
        while (reader.ready()) {
            String str = reader.readLine();
            str = str.replaceAll("\\.|\\,|\\)|\\(|\\?|\\!|\\:|\\;"," ").
                    replace(" -"," ").replace("- "," ").
                    replace(" — "," ").replaceAll("\\s+"," ");
            String[] wordsInLine = str.split(" ");
            for (int i = 0; i < wordsInLine.length; i++) {
                if (wordsInLine[i].length() > 0) {
                    String s = wordsInLine[i];
                    StringBuilder sb;
                    if (countUpperLiter(wordsInLine[i]) == 0) {
                        sb = new StringBuilder();
                        sb.append(wordsInLine[i]);
                        sb.setCharAt(0,Character.toUpperCase(sb.charAt(0)));
                        s = sb.toString();
                    }
                    if (!wordMap.keySet().contains(s))
                        wordMap.put(s,1);
                    else
                        {wordMap.put(s, wordMap.get(s)+1);}
                    wordCounter++;
                }
            }
        }
        reader.close();

        //Вывод результата на экран
        printWords(wordMap);
        System.out.println("\nОбщее количество слов в тексте: " + wordCounter);

    }

    //Загрузка файла по URL-ссылке
    private static void downloadUsingStream(String urlStr, File file) throws IOException{
        URL url = new URL(urlStr);
        try (BufferedInputStream bis = new BufferedInputStream(url.openStream());
             FileOutputStream fis = new FileOutputStream(file))
        {
            byte[] buffer = new byte[1024];
            int count = 0;
            while((count = bis.read(buffer,0,1024)) != -1)
                {
                    fis.write(buffer, 0, count);
                }
        }
    }

    //Подсчет букв верхнего регистра в строке
    public static int countUpperLiter (String string) {
        int counter = 0;
        for (int i = 0; i < string.length(); i++) {
            if (Character.toString(string.charAt(i)).matches("[A-Z]|[А-Я]")) counter++;
        }
        return counter;
    }

    //Вывод на экран карты с сортировкой по убыванию value
    public static void printWords(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());
        list.sort(Map.Entry.<String, Integer>comparingByValue().reversed());
        for (Map.Entry<String, Integer> pair : list) {
            System.out.println(pair.getKey() + " - " + pair.getValue());
        }
    }

}
