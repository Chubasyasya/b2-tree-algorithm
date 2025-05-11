import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class Node<T extends Comparable<T>> {
    T[] keys;
    Node<T> parent;
    Node<T>[] sons;
    int length;//количество сыновей

    public Node(Node<T>[] sons, Object[] keys, Node<T> parent, int length) {
        this.sons = new Node[4];
        this.keys = (T[]) new Comparable[3];
        this.parent = parent;
        this.length = length;
        for (int i = 0; i < sons.length; i++) {
            this.sons[i] = sons[i]; // Инициализация каждого сына пустым узлом
        }

        if (sons != null) {
            for (int i = 0; i < sons.length && i < 4; i++) {
                this.sons[i] = sons[i];
            }
        }
        if(keys != null){
            for (int i = 0; i < keys.length; i++) {
                this.keys[i] = (T)keys[i];
            }
        }

    }
    public Node(){
        keys = (T[]) new Comparable[3]; // Создание массива типа Comparable
        parent = null;
        sons = (Node<T>[]) new Node[4]; // Создание массива узлов
        length = 0;

    }

    public void nullKeys(){
        keys = (T[]) new Comparable[3];
    }
    public int sortSons() {
        int[] compareCount = new int[]{0}; // Инициализируем счетчик
        if (sons != null) {
            Arrays.sort(sons, new Comparator<Node<T>>() {
                @Override
                public int compare(Node<T> n1, Node<T> n2) {
                    compareCount[0]++; // Увеличиваем счетчик при каждом вызове compare()
                    if (n1 == null || n1.keys == null) {
                    }
                    if (n2 == null || n2.keys == null) {
                    }
                    if (n1 == null && n2 == null) {
                        return 0;
                    }
                    if (n1 == null) {
                        return 1;
                    }
                    if (n2 == null) {
                        return -1;
                    }
                    if (n1.keys == null && n2.keys == null) {
                        return 0;
                    }
                    if (n1.keys == null) {
                        return 1;
                    }
                    if (n2.keys == null) {
                        return -1;
                    }
                    return n1.keys[0].compareTo(n2.keys[0]);
                }
            });
        }
        return compareCount[0];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return length == node.length &&
                Arrays.equals(keys, node.keys) &&
                Objects.equals(parent, node.parent) &&
                Arrays.equals(sons, node.sons);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(parent, length);
        result = 31 * result + Arrays.hashCode(keys);
        result = 31 * result + Arrays.hashCode(sons);
        return result;
    }
}
