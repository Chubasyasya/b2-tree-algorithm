import java.util.ArrayDeque;
import java.util.Deque;

import static jdk.nashorn.internal.objects.NativeMath.max;

public class TwoThreeTree<T extends Comparable<T>> {
    private Node<T> root;
    private int amountOperations;

    public T search(T x) {
        amountOperations = 0;
        Node<T> t = root;
        while (t != null && t.length != 0) {
            amountOperations++;
            if (t.length == 2) {
                if (t.keys[0].compareTo(x) < 0) t = t.sons[1];
                else t = t.sons[0];
            } else if (t.keys[1].compareTo(x) < 0) {
                t = t.sons[2];
            } else if (t.keys[0].compareTo(x) < 0) {
                t = t.sons[1];
            } else {
                t = t.sons[0];
            }
        }
        return t.keys[0];
    }
    public Node<T> searchNode(T x){
        Node<T> t = root;
        while (t != null && t.length != 0) {
            amountOperations++;
            if(t.keys[1] == null && t.length == 3){
                updateKeys(t.sons[1]);
            }
            if (t.length == 2) {
                if (t.keys[0].compareTo(x) < 0) t = t.sons[1];
                else t = t.sons[0];
            } else if (t.keys[1].compareTo(x) < 0) {
                t = t.sons[2];
            } else if (t.keys[0].compareTo(x) < 0) {
                t = t.sons[1];
            } else {
                t = t.sons[0];
            }
        }
        return t;
    }
    public void splitParent(Node<T> t) {
        amountOperations++;
        if (t.length > 3) {
            T maxKey = max(t.sons[2]);
            Node<T> a = new Node<T>(new Node[]{t.sons[2], t.sons[3]}, new Object[]{maxKey}, t.parent, 2);
            t.sons[2].parent = a;
            t.sons[3].parent = a;
            t.keys[1] = null;
            t.keys[2] = null;
            t.length = 2;
            t.sons[2] = null;
            t.sons[3] = null;
            if (t.parent != null) {
                t.parent.sons[t.parent.length] = a;
                t.parent.length++;
                t.parent.sortSons();
//    сортируем сыновей у t.parent
                splitParent(t.parent);
            } else {             // мы расщепили корень, надо подвесить его к общему родителю, который будет новым корнем
                root = new Node<T>(new Node[]{t, a}, new Object[]{t.keys[1] == null?t.keys[0]:t.keys[1]}, null, 2);
                root.sons[0] = t;
                root.sons[1] = a;
                //вот тут у меня у a значение ключа нул когда до этого разделили уже сыновей как это исправить
                t.parent = root;
                a.parent = root;
                root.length = 2;
                root.sortSons();
            }
//    сортируем сыновей у root
        }
    }

    public void updateKeys(Node t) {
        Node a = t.parent;
        while (a != null){
            a.nullKeys();
            for (int i = 0; i < a.length-1; i++) {
                a.keys[i] = max(a.sons[i]);
                amountOperations+=1;
            }
            a = a.parent;
        }
    }

    public void insert(T x) {
        amountOperations = 0;
        Node<T> n = new Node();
        n.keys[0] = x;
        if (root == null) {
            amountOperations++;
            root = n;
            return;
        }
        Node a = (Node) searchNode(x);
        if (a.parent == null) {
            Node t = new Node<T>(new Node[]{}, new Object[]{root.keys[0]}, root, 0);
            root.sons[0] = t;
            root.sons[1] = n;
            t.parent = root;
            n.parent = root;
            root.length = 2;
            amountOperations += root.sortSons();
//        сортируем сыновей у root
        }else {
            Node p = a.parent;
            p.sons[p.length] = n;
            p.length++;
            n.parent = p;
            amountOperations += p.sortSons();
            updateKeys(n);
            splitParent(n.parent);
        }
    updateKeys(n);

    }
    public T max(Node n){
        if(n.length == 0){
            return (T) n.keys[0];
        }
        return max(n.sons[n.length-1]);
    }
    public void remove(T x){
        amountOperations = 0;
        Node n = searchNode(x);
        removeRec(n, null, 0);
    }
    public void removeRec(Node n, Node tempNode, int recDeep){
        amountOperations++;
        Node p = n.parent;
        if(n.parent.parent == null){
            if(p.length == 2) {
                Node b = p.sons[0].equals(n) ? p.sons[1] : p.sons[0];
                root = b;
                root.parent = null;
            }else{
                int shift = 0;
                for(int i = 0; i < p.length; i++) {
                    if (n.equals(p.sons[i])) {
                        shift = 1;
                    }
                    p.sons[i] = p.sons[i + shift];
                }
                p.length--;
                updateKeys(p.sons[0]);
            }
        }else {
            if(p.length == 2) {
                Node b = p.sons[0].equals(n) ? p.sons[1] : p.sons[0];
                Node nb = b.keys[0].compareTo(n.keys[0])>0 ? nextNode(b) : nextNode(n);
                if(nb == null){
                    nb = b.keys[0].compareTo(n.keys[0])>0 ? preNode(n) : preNode(b);
                }
                Node np = nb.parent;
                np.sons[np.length] = b;
                np.length++;
                np.keys[1] = max(np.sons[1]);
                b.parent = np;
                np.sortSons();
                splitParent(np);
                p.sons[0] = null;
                p.sons[1] = null;
                p.length = 0;
                p.keys[0] = n.keys[0];
                if(recDeep == 0) {
                    tempNode = b;
                    recDeep++;
                }

                removeRec(p, tempNode, recDeep);
            }else{
                int shift = 0;
                for(int i = 0; i < p.length; i++){
                    if(n.equals(p.sons[i])){
                        shift = 1;
                    }
                    p.sons[i] = p.sons[i+shift];
                }//переопределили сыновей у родителя так как их три товсе окей толко нужно обоновить ключи
                p.length--;
                updateKeys(p.sons[0]);
            }
            //два случая удаляем элемнт и остается один брат тогда подцепляем его к соседнему родителю
            //удалили элемент осталось два брата переопределили ключи родителей
        }
        if(tempNode!=null) updateKeys(tempNode);
    }


    public Node nextNode(Node t) {
        Node<T> tCopy = t;
        int counter = 0;
        while (t != null) {
            amountOperations++;
            Node<T> parent = t.parent;
            if (parent == null) {
                return null;// Не существует следующего объекта
            }
            int index = -1;
            for (int i = 0; i < parent.length; i++) {
                amountOperations++;
                if (parent.sons[i].equals(t)) {
                    index = i;
                    break;
                }
            }
            if (index < parent.length - 1) {
                t = parent.sons[index + 1];
                while (counter > 0) {
                    amountOperations++;
                    t = t.sons[0];
                    counter--;
                }
                return t;
            }
            t = parent;
            counter++;
        }
        return null; // Не существует следующего объекта
    }

    public Node<T> preNode(Node t) {
        int counter = 0;
        while (t != null) {
            amountOperations++;
            Node<T> parent = t.parent;
            int index = -1;
            for (int i = 0; i < parent.length; i++) {
                amountOperations++;
                if (parent.sons[i].equals(t)) {
                    index = i;
                    break;
                }
            }
            if (index > 0) {
                t = parent.sons[index - 1];
                while (counter > 0) {
                    amountOperations++;
                    t = t.sons[t.length - 1];
                    counter--;
                }
                return t;
            }
            t = parent;
            counter++;
        }
        return null; // Не существует предыдущего объекта
    }

    public int getAmountOperations() {
        return amountOperations;
    }
}

//public void pr(){
//    System.out.println("\nTree");
//    Deque q = new ArrayDeque();
//    q.add(root);
//    while(!q.isEmpty()){
//        for(int i = 0; i < q.size(); i++){
//            Node n = (Node) q.pop();
//            if(n.length == 3 && n.keys[1] == null){
//                System.out.println("Error");
//            }
//            System.out.print("  ");
//            for(Node elem : n.sons){
//                if (elem != null) q.add(elem);
//            }
//
//        }
//    }
//}

