import com.sun.org.apache.xerces.internal.impl.xs.util.LSInputListImpl;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args){
        String inputFile = "src/resources/InputFile.txt";
        String outputFileInsertRes = "src/resources/outputFileInsertRes.txt";
        String outputFileSearchRes = "src/resources/outputFileSearchRes.txt";
        String outputFileRemoveRes = "src/resources/outputFileRemoveRes.txt";
        FillSourceFile f = new FillSourceFile(inputFile);
//        int amountElements = 10000;
//        f.fillFile(amountElements);
//        System.out.println("создали исходные данные");

        TwoThreeTree tree = new TwoThreeTree();
        List<Integer>  operationsToInsert = new LinkedList<>();
        List<Long> timeToInsert = new LinkedList<>();
        List<Long> timeToSearch = new LinkedList<>();
        List<Integer> operationsToSearch = new LinkedList<>();
        List<Long> timeToRemove = new LinkedList<>();
        List<Integer> operationsToRemove = new LinkedList<>();
        List array = new LinkedList();

        task3AddNumberInStructure(inputFile, tree, timeToInsert, operationsToInsert, array);
        System.out.println("сделали таск 3");
        task4SearchNumber( tree, timeToSearch, operationsToSearch, array);
        System.out.println("сделали таск 4");
        FillOutputFile fof = new FillOutputFile();
        task5RemoveNumber(tree, timeToRemove, operationsToRemove, array);
        System.out.println("сделали таск 5");

        fof.fillResult(timeToInsert, operationsToInsert, outputFileInsertRes);
        System.out.println("заполнили выходные данные таск 3");
        fof.fillResult(timeToSearch, operationsToSearch, outputFileSearchRes);
        System.out.println("заполнили выходные данные таск 4");
        fof.fillResult(timeToRemove, operationsToRemove, outputFileRemoveRes);
        System.out.println("заполнили выходные данные таск 5");

    }
    public static void task3AddNumberInStructure(String fileName, TwoThreeTree tree, List<Long> timeToInsert, List<Integer> operationsToInsert, List array){
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            String line;

            while((line = reader.readLine())!=  null){
                String[] numbers = line.split("\\s+");
                int[] arr = Arrays.stream(numbers).mapToInt(Integer::parseInt).toArray();

                Arrays.stream(arr).forEach(x -> {
                    long startTime = System.nanoTime();
                    tree.insert(x);
                    long endTime = System.nanoTime();
                    operationsToInsert.add(tree.getAmountOperations());
                    timeToInsert.add(endTime - startTime);
                    array.add(x);
                });

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void task4SearchNumber(TwoThreeTree tree, List<Long> timeToSearch, List<Integer> operationsToSearch, List array){
        Random random = new Random();
        for(int i = 0; i < 100; i++){
            int numberForSearch = (int) array.get(random.nextInt(array.size()));
            long startTime = System.nanoTime();
            tree.search(numberForSearch);
            long endTime = System.nanoTime();
            operationsToSearch.add(tree.getAmountOperations());
            timeToSearch.add(endTime - startTime);
        }
    }

    public static void task5RemoveNumber(TwoThreeTree tree, List<Long> timeToRemove, List<Integer> operationsToRemove, List<Integer> array) {
        Random random = new Random();
        int size = array.size();
        for (int i = 0; i < 1000 && size > 0; i++) {
            int index = random.nextInt(size);
            int numberForRemove = array.get(index);
            long startTime = System.nanoTime();
            tree.remove(numberForRemove);
            array.remove(index);
            size--;
            long endTime = System.nanoTime();
            operationsToRemove.add(tree.getAmountOperations());
            timeToRemove.add(endTime - startTime);
        }
    }
}
