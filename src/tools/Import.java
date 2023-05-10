package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Import {
    /**
     * Create List of Lists parametres (Integers) from text file
     * @param filePath - "fileName.txt" in "parameters" folder
     * @param separator
     * @return
     */
    public static List<List<Integer>> createListofParametersFromFile(String filePath,String separator) {
        String s;
        List<List<Integer>> listOfLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((s = br.readLine()) != null) {
                List<Integer> line = new ArrayList<>();
                List<String> listStringsFromLine = Arrays.asList(s.split(separator)); //zapisz do listy stringi, oddzielone separatorem
                for (String x : listStringsFromLine) { //robienie listy parametrów z jednej linijki
                    line.add(Integer.valueOf(x));
                }
                listOfLines.add(line);  //dodanie listy parametrów, do spisu linii
            }
        }
        catch (IOException e) {
            System.out.println("Nie znaleziono pliku");
        }
        return listOfLines;
    }

}
