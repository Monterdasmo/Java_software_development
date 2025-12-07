import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Лабораторна робота №6.
 * Тема: Робота з колекціями в мові програмування Java.
 *
 * Номер залікової книжки: 3413
 *
 * Обчислення:
 *   C2 = 3413 mod 2 = 1
 *   C3 = 3413 mod 3 = 2
 *
 * Відповідно до варіанту:
 *   C2 = 1 → інтерфейс: Set
 *   C3 = 2 → внутрішня структура: двозв’язний список
 *
 * Тип колекції:
 *   Колекція містить об'єкти базового класу ієрархії з ЛР5 (рухомий склад) - RailCar.
 *
 * У цій програмі:
 *   - Реалізовано типізовану колекцію RailCarSet<T extends RailCar>, що реалізує Set<T>.
 *   - Внутрішня структура — власний двозв’язний список (Node<T>).
 *   - Є три конструктори:
 *       1) порожній;
 *       2) з одним елементом;
 *       3) з будь-якої стандартної колекції (Collection<? extends T>).
 *   - У main() показано приклад використання.
 */
public class Lab6Collections {

    public static void main(String[] args) {
        // Створимо кілька вагонів (як у ЛР5)
        CoupeCar c1 = new CoupeCar("C1", 3, 40, 40, 32, 30);
        CoupeCar c2 = new CoupeCar("C2", 3, 40, 40, 28, 26);
        LuxCar   l1 = new LuxCar("L1", 5, 20, 20, 15, 14);

        // 1) Порожній набір
        RailCarSet<RailCar> set1 = new RailCarSet<>();
        System.out.println("=== Empty set1 ===");
        System.out.println("set1 size = " + set1.size());

        // 2) Набір з одним елементом
        RailCarSet<RailCar> set2 = new RailCarSet<>(c1);
        System.out.println("\n=== set2 (created with single element C1) ===");
        printSet(set2);

        // 3) Набір з колекції
        Collection<RailCar> initialCollection = new ArrayList<>();
        initialCollection.add(c1);
        initialCollection.add(c2);
        initialCollection.add(l1);

        RailCarSet<RailCar> trainSet = new RailCarSet<>(initialCollection);
        System.out.println("\n=== trainSet (created from standard collection) ===");
        printSet(trainSet);

        // Перевірка add() (множина не повинна додавати дублікат)
        System.out.println("\nAdding C1 again to trainSet...");
        boolean addedDuplicate = trainSet.add(c1);
        System.out.println("Was duplicate added? " + addedDuplicate);
        System.out.println("trainSet size = " + trainSet.size());

        // Перевірка contains()
        System.out.println("\ntrainSet contains L1? " + trainSet.contains(l1));

        // Перевірка remove()
        System.out.println("\nRemoving C2 from trainSet...");
        boolean removed = trainSet.remove(c2);
        System.out.println("Removed? " + removed);
        printSet(trainSet);

        // Перевірка addAll()
        RailCarSet<RailCar> extraSet = new RailCarSet<>();
        extraSet.add(new PlatzkartCar("P1", 2, 54, 54, 20, 18));
        extraSet.add(new PlatzkartCar("P2", 1, 54, 54, 10,  8));

        System.out.println("\n=== extraSet ===");
        printSet(extraSet);

        System.out.println("\nAdding all from extraSet to trainSet...");
        trainSet.addAll(extraSet);
        printSet(trainSet);

        // Перевірка retainAll() - залишимо тільки елементи, які є в extraSet
        System.out.println("\nRetaining only elements that are in extraSet...");
        trainSet.retainAll(extraSet);
        printSet(trainSet);

        // Очищення
        System.out.println("\nClearing trainSet...");
        trainSet.clear();
        System.out.println("trainSet is empty? " + trainSet.isEmpty());
    }

    private static void printSet(Set<RailCar> set) {
        System.out.println("Set contents (size=" + set.size() + "):");
        for (RailCar car : set) {
            System.out.println("  " + car);
        }
    }
}

/**
 * Власна типізована колекція на основі двозв’язного списку,
 * що реалізує інтерфейс Set<T> для елементів типу RailCar.
 */
class RailCarSet<T extends RailCar> implements Set<T> {

    /**
     * Внутрішній вузол двозв’язного списку.
     */
    private static class Node<E> {
        E value;
        Node<E> prev;
        Node<E> next;

        Node(E value) {
            this.value = value;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    /**
     * Конструктор 1: порожній набір.
     */
    public RailCarSet() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Конструктор 2: набір із одним елементом.
     */
    public RailCarSet(T element) {
        this();
        if (element != null) {
            add(element);
        }
    }

    /**
     * Конструктор 3: набір з будь-якої стандартної колекції.
     * Дублікатів не буде (Set).
     */
    public RailCarSet(Collection<? extends T> collection) {
        this();
        if (collection != null) {
            addAll(collection);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Перевіряє, чи міститься елемент у множині (пошук по equals()).
     */
    @Override
    public boolean contains(Object o) {
        Node<T> current = head;
        while (current != null) {
            if (o == null ? current.value == null : o.equals(current.value)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Ітератор по множині — проходить двозв’язний список від head до tail.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;
            private Node<T> lastReturned = null;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                lastReturned = current;
                current = current.next;
                return lastReturned.value;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException("next() has not been called yet");
                }
                Node<T> toRemove = lastReturned;
                lastReturned = null;
                unlinkNode(toRemove);
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int idx = 0;
        Node<T> current = head;
        while (current != null) {
            arr[idx++] = current.value;
            current = current.next;
        }
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E[] toArray(E[] a) {
        if (a.length < size) {
            // створюємо новий масив потрібного розміру
            a = (E[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }
        int idx = 0;
        Node<T> current = head;
        while (current != null) {
            a[idx++] = (E) current.value;
            current = current.next;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    /**
     * Додає елемент у множину (якщо його ще немає).
     *
     * @return true, якщо елемент було додано; false, якщо такий вже був.
     */
    @Override
    public boolean add(T t) {
        if (contains(t)) {
            return false; // Set не зберігає дублікати
        }
        Node<T> node = new Node<>(t);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
        return true;
    }

    /**
     * Видаляє елемент із множини (якщо він є).
     *
     * @return true, якщо елемент було видалено.
     */
    @Override
    public boolean remove(Object o) {
        Node<T> current = head;
        while (current != null) {
            if (o == null ? current.value == null : o.equals(current.value)) {
                unlinkNode(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Внутрішній метод для від'єднання вузла від двозв’язного списку.
     */
    private void unlinkNode(Node<T> node) {
        Node<T> prev = node.prev;
        Node<T> next = node.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
        }

        node.prev = null;
        node.next = null;
        size--;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            return true;
        }
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c == null || c.isEmpty()) {
            return false;
        }
        boolean modified = false;
        for (T element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            clear();
            return true;
        }
        boolean modified = false;
        Node<T> current = head;
        while (current != null) {
            Node<T> next = current.next; // збережемо наступний вузол
            if (!c.contains(current.value)) {
                unlinkNode(current);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null || c.isEmpty()) {
            return false;
        }
        boolean modified = false;
        Node<T> current = head;
        while (current != null) {
            Node<T> next = current.next;
            if (c.contains(current.value)) {
                unlinkNode(current);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public void clear() {
        Node<T> current = head;
        while (current != null) {
            Node<T> next = current.next;
            current.prev = null;
            current.next = null;
            current = next;
        }
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Для коректності equals()/hashCode() Set-а
     * скористаємось тимчасовим HashSet на основі наших елементів.
     */
    @Override
    public int hashCode() {
        HashSet<T> temp = new HashSet<>();
        for (T element : this) {
            temp.add(element);
        }
        return temp.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Set<?> otherSet)) {
            return false;
        }
        // Два множини рівні, якщо розміри збігаються
        // і кожен елемент this є в іншому множині
        if (otherSet.size() != this.size()) {
            return false;
        }
        return this.containsAll(otherSet);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("RailCarSet{");
        sb.append("size=").append(size).append(", elements=[");
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]}");
        return sb.toString();
    }
}

/* ======== Прості класи з ЛР5 (узагальнений клас + нащадки) ======== */

/**
 * Базовий узагальнений клас рухомого складу (як у ЛР5).
 */
abstract class RailCar {

    private final String carNumber;
    private final int comfortLevel;
    private final int passengerCapacity;
    private final int baggageCapacity;
    private int currentPassengers;
    private int currentBaggageItems;

    public RailCar(String carNumber,
                   int comfortLevel,
                   int passengerCapacity,
                   int baggageCapacity,
                   int currentPassengers,
                   int currentBaggageItems) {
        this.carNumber = carNumber;
        this.comfortLevel = comfortLevel;
        this.passengerCapacity = passengerCapacity;
        this.baggageCapacity = baggageCapacity;
        this.currentPassengers = currentPassengers;
        this.currentBaggageItems = currentBaggageItems;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public int getComfortLevel() {
        return comfortLevel;
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public int getBaggageCapacity() {
        return baggageCapacity;
    }

    public int getCurrentPassengers() {
        return currentPassengers;
    }

    public int getCurrentBaggageItems() {
        return currentBaggageItems;
    }

    @Override
    public String toString() {
        return "RailCar{" +
                "carNumber='" + carNumber + '\'' +
                ", comfortLevel=" + comfortLevel +
                ", passengerCapacity=" + passengerCapacity +
                ", baggageCapacity=" + baggageCapacity +
                ", currentPassengers=" + currentPassengers +
                ", currentBaggageItems=" + currentBaggageItems +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RailCar that)) return false;

        if (comfortLevel != that.comfortLevel) return false;
        if (passengerCapacity != that.passengerCapacity) return false;
        if (baggageCapacity != that.baggageCapacity) return false;
        if (currentPassengers != that.currentPassengers) return false;
        if (currentBaggageItems != that.currentBaggageItems) return false;
        return carNumber != null ? carNumber.equals(that.carNumber) : that.carNumber == null;
    }

    @Override
    public int hashCode() {
        int result = carNumber != null ? carNumber.hashCode() : 0;
        result = 31 * result + comfortLevel;
        result = 31 * result + passengerCapacity;
        result = 31 * result + baggageCapacity;
        result = 31 * result + currentPassengers;
        result = 31 * result + currentBaggageItems;
        return result;
    }
}

/**
 * Приклад нащадка: купейний вагон.
 */
class CoupeCar extends RailCar {

    public CoupeCar(String carNumber,
                    int comfortLevel,
                    int passengerCapacity,
                    int baggageCapacity,
                    int currentPassengers,
                    int currentBaggageItems) {
        super(carNumber, comfortLevel, passengerCapacity, baggageCapacity,
                currentPassengers, currentBaggageItems);
    }

    @Override
    public String toString() {
        return "CoupeCar{" +
                "carNumber='" + getCarNumber() + '\'' +
                ", comfortLevel=" + getComfortLevel() +
                ", passengerCapacity=" + getPassengerCapacity() +
                ", baggageCapacity=" + getBaggageCapacity() +
                ", currentPassengers=" + getCurrentPassengers() +
                ", currentBaggageItems=" + getCurrentBaggageItems() +
                '}';
    }
}

/**
 * Приклад нащадка: люксовий вагон.
 */
class LuxCar extends RailCar {

    public LuxCar(String carNumber,
                  int comfortLevel,
                  int passengerCapacity,
                  int baggageCapacity,
                  int currentPassengers,
                  int currentBaggageItems) {
        super(carNumber, comfortLevel, passengerCapacity, baggageCapacity,
                currentPassengers, currentBaggageItems);
    }

    @Override
    public String toString() {
        return "LuxCar{" +
                "carNumber='" + getCarNumber() + '\'' +
                ", comfortLevel=" + getComfortLevel() +
                ", passengerCapacity=" + getPassengerCapacity() +
                ", baggageCapacity=" + getBaggageCapacity() +
                ", currentPassengers=" + getCurrentPassengers() +
                ", currentBaggageItems=" + getCurrentBaggageItems() +
                '}';
    }
}

/**
 * Приклад ще одного нащадка: плацкартний вагон.
 */
class PlatzkartCar extends RailCar {

    public PlatzkartCar(String carNumber,
                        int comfortLevel,
                        int passengerCapacity,
                        int baggageCapacity,
                        int currentPassengers,
                        int currentBaggageItems) {
        super(carNumber, comfortLevel, passengerCapacity, baggageCapacity,
                currentPassengers, currentBaggageItems);
    }

    @Override
    public String toString() {
        return "PlatzkartCar{" +
                "carNumber='" + getCarNumber() + '\'' +
                ", comfortLevel=" + getComfortLevel() +
                ", passengerCapacity=" + getPassengerCapacity() +
                ", baggageCapacity=" + getBaggageCapacity() +
                ", currentPassengers=" + getCurrentPassengers() +
                ", currentBaggageItems=" + getCurrentBaggageItems() +
                '}';
    }
}
