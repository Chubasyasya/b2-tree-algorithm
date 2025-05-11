import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FillOutputFile {
    public void fillResult(List<Long> timeList, List<Integer> operationsList, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Записываем данные из списков в файл
            for (int i = 0; i < timeList.size(); i++) {
                writer.write(timeList.get(i) + ", " + operationsList.get(i) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

}
