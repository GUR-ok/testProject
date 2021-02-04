import java.io.*;
import java.net.URL;
import java.util.*;

public class Test {

    public static Map<String,Integer> wordMap = new TreeMap<>();
    public static int wordCounter = 0;

    public static void main(String[] args) throws IOException {
        //Ввод URL-ссылки на файл и загрузка файла
        String url = "http://madbrains.github.io/java_course_test";
        File tmpFile = File.createTempFile("file",null);
        downloadUsingStream(url,tmpFile);

        //Считывание файла в буфер
        BufferedReader reader = new BufferedReader(new FileReader(tmpFile));

        String str = reader.readLine();
        while (str != null) {
            String[] wordsInLine = str.replace(" — "," ").replace("- "," ").replace(" -"," ").split(" ");
            wordCounter += wordsInLine.length;
            for (int i = 0; i < wordsInLine.length; i++) {
                if (wordsInLine[i].length()>0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(wordsInLine[i].replaceAll("\\.|\\,|\\)|\\(",""));
                    if (countUpperLiter(sb.toString()) <= 1) {
                        sb.setCharAt(0,Character.toUpperCase(sb.charAt(0)));
                    }
                    String s = sb.toString();
                    if (!wordMap.keySet().contains(s))
                        wordMap.put(s,1);
                    else
                        {wordMap.put(s, wordMap.get(s)+1);}
                }
            }
            str = reader.readLine();
        }
        reader.close();

        //Вывод информации на экран
        for (Map.Entry<String,Integer> pair : wordMap.entrySet()) {
            System.out.println(pair.getKey()+" - "+pair.getValue());
        }
        System.out.println("\nКоличество слов в тексте: "+wordCounter);

    }

    //Загрузка файла по URL-ссылке
    private static void downloadUsingStream(String urlStr, File file) throws IOException{
        URL url = new URL(urlStr);
        try (BufferedInputStream bis = new BufferedInputStream(url.openStream());
             FileOutputStream fis = new FileOutputStream(file))
        {
            byte[] buffer = new byte[1024];
            int count=0;
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

}
