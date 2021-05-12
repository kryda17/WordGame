package wordgame.chris;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class VerticalWordsGenerator {

    public void fillDBFromHorisontalWords(String[][] table) {
        String[][] strings = readAndFillTableWithHorisonWordsFromFile("src/main/resources/szövegek/szavak.txt", table, Table2.GRID_SIZE);
        List<String> stringList = gen("src/main/resources/szövegek/szavak.txt", strings, Table2.GRID_SIZE);
        writeVerticalWords( stringList, "src/main/resources/szövegek/szavak2.txt");
    }

    public List<String> gen(String file, String[][] table, int gridSize) {
        List<String> strings = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                String s = table[j][i];
                if (!s.equals("#")) {
                    sb.append(s);
                } else {
                    if (sb.isEmpty()) {
                        continue;
                    }
                    strings.add(sb.toString());
                    //strings.add(" ");
                    sb = new StringBuilder();
                }
            }
            if (!sb.isEmpty()) {
                strings.add(sb.toString());
                sb = new StringBuilder();
            }

            //strings.add("\n");
        }
        return strings;
    }

    public void writeVerticalWords(List<String> words, String file) {
        try(BufferedWriter writer = Files.newBufferedWriter(Path.of(file))) {
            for (String word : words) {
                writer.write(word);
                writer.newLine();
            }
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Can't write the file.");
        }
    }

    public String[][] readAndFillTableWithHorisonWordsFromFile(String file, String[][] randBlakcs, int grid) {
        List<String> sl = new ArrayList<>();
        try(BufferedReader br = Files.newBufferedReader(Path.of(file))) {
            String line;
            while((line = br.readLine()) != null) {
                String [] words = line.split(" ");
                for (int i = 0; i < words.length ; i++) {
                    for (int j = 0; j < words[i].length(); j++) {
                        Character c = words[i].charAt(j);
                        String s = String.valueOf(c);
                        sl.add(s);
                    }
                    //sl.add(" ");
                }
            }
            int counter = 0;
            for (int i = 0; i < grid; i++) {
                for (int j = 0; j < grid; j++) {
                    if ("#".equals(randBlakcs[i][j])) {
                        continue;
                    }
                    /*if (sl.get(counter).equals(" ")) {
                        randBlakcs[i][j] = "#";
                        ++counter;
                    } else {

                     */
                        randBlakcs[i][j] = sl.get(counter).toUpperCase();
                        ++counter;
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return randBlakcs;
    }
}
