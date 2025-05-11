import java.io.FileWriter;
import java.io.IOException;

public class FillSourceFile {
    private String fileName;
    FillSourceFile(String fileName){
        this.fileName = fileName;
    }
    private int max = (int) Math.pow(2, 15);
    private void fillFileByArray(int size){
        try(FileWriter fileWriter = new FileWriter(fileName, true)){
            int x = randomGenerateInt();
            fileWriter.write(String.valueOf(x));
            for(int i = 0; i < size; i++){
                x = randomGenerateInt();
                fileWriter.write(" ");
                fileWriter.write(String.valueOf(x));
            }
            fileWriter.write("\n");
        }catch(IOException e){
            System.out.println("Error");
        }
    }

    public void fillFile(int amount){
        try (FileWriter fileWriter = new FileWriter(fileName)) {
        } catch (IOException e) {
            System.out.println("An error occurred while clearing the file.");
            e.printStackTrace();
        }//очищает файл


        fillFileByArray(amount);

    }
    public int randomGenerateInt(){
        return (int) (Math.random() * ++max);
    }
}
