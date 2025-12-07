import java.util.Arrays;
import java.util.Comparator;

/**
 * Лабораторна робота №3. Класи.
 * Номер залікової книжки: 3413
 *
 * Обчислення:
 *  C11 = 3413 mod 11 = 3
 *
 * Відповідно до варіанту:
 *  C11 = 3 → Визначити клас "морський човен" (мінімум 5 полів).
 *
 * Програма:
 *  1) Описує клас SeaBoat (морський човен) з полями:
 *     name, type, yearBuilt, capacity, maxSpeed.
 *  2) Створює масив човнів.
 *  3) Сортує масив:
 *       - спочатку за yearBuilt (рік побудови) за зростанням,
 *       - при однаковому yearBuilt — за maxSpeed за спаданням.
 *  4) Шукає в масиві човен, ідентичний заданому (equals).
 *  5) Виводить результати.
 */
class Lab3Classes {

    public static void main(String[] args) {
        // Створення масиву морських човнів
        SeaBoat[] boats = new SeaBoat[] {
                new SeaBoat("Poseidon", "Submarine", 2015, 20, 45.5),
                new SeaBoat("Aqua Star", "Yacht",      2010, 10, 60.0),
                new SeaBoat("Sea Wolf",  "Patrol",     2015, 15, 50.0),
                new SeaBoat("Blue Bird", "Fishing",    2005,  6, 25.0),
                new SeaBoat("Ocean Fox", "Yacht",      2010, 12, 55.0)
        };

        System.out.println("=== Initial array of boats ===");
        printArray(boats);

        // Сортування:
        // 1) yearBuilt — за зростанням
        // 2) maxSpeed  — за спаданням (якщо рік однаковий)
        Arrays.sort(boats, new Comparator<SeaBoat>() {
            @Override
            public int compare(SeaBoat b1, SeaBoat b2) {
                int yearCompare = Integer.compare(b1.getYearBuilt(), b2.getYearBuilt());
                if (yearCompare != 0) {
                    // якщо роки різні — сортуємо за роком за зростанням
                    return yearCompare;
                }
                // якщо роки однакові — сортуємо за швидкістю за спаданням
                return Double.compare(b2.getMaxSpeed(), b1.getMaxSpeed());
            }
        });

        System.out.println("\n=== Sorted array of boats (by year ↑, then speed ↓) ===");
        printArray(boats);

        // Заданий човен, який потрібно знайти в масиві (ідентичний одному з елементів)
        SeaBoat target = new SeaBoat("Sea Wolf", "Patrol", 2015, 15, 50.0);

        int foundIndex = findBoatIndex(boats, target);

        System.out.println("\n=== Search result ===");
        System.out.println("Target boat: " + target);
        if (foundIndex >= 0) {
            System.out.println("Found at index: " + foundIndex);
            System.out.println("boats[" + foundIndex + "] = " + boats[foundIndex]);
        } else {
            System.out.println("Target boat not found in array.");
        }
    }

    /**
     * Пошук індексу човна, ідентичного target, в масиві boats.
     * Використовує equals() класу SeaBoat.
     */
    private static int findBoatIndex(SeaBoat[] boats, SeaBoat target) {
        if (boats == null || target == null) {
            return -1;
        }
        for (int i = 0; i < boats.length; i++) {
            if (target.equals(boats[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Виводить масив човнів у консоль.
     */
    private static void printArray(SeaBoat[] boats) {
        if (boats == null) {
            System.out.println("Array is null");
            return;
        }
        for (SeaBoat boat : boats) {
            System.out.println(boat);
        }
    }
}

/**
 * Клас, що описує морський човен.
 * Має як мінімум 5 полів:
 *  - name      — назва човна
 *  - type      — тип (яхта, патрульний, підводний тощо)
 *  - yearBuilt — рік побудови
 *  - capacity  — пасажиромісткість (кількість людей)
 *  - maxSpeed  — максимальна швидкість (вузли або км/год)
 */
class SeaBoat {

    private String name;
    private String type;
    private int yearBuilt;
    private int capacity;
    private double maxSpeed;

    public SeaBoat(String name, String type, int yearBuilt, int capacity, double maxSpeed) {
        this.name = name;
        this.type = type;
        this.yearBuilt = yearBuilt;
        this.capacity = capacity;
        this.maxSpeed = maxSpeed;
    }

    // Геттери (при потребі можна додати й сеттери)
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getYearBuilt() {
        return yearBuilt;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Рядкове представлення об'єкта для зручного виведення.
     */
    @Override
    public String toString() {
        return "SeaBoat{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", yearBuilt=" + yearBuilt +
                ", capacity=" + capacity +
                ", maxSpeed=" + maxSpeed +
                '}';
    }

    /**
     * equals() визначає, коли два човни вважаються "ідентичними".
     * Тут порівнюємо всі поля.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true; // порівнюємо з самим собою
        }
        if (o == null || getClass() != o.getClass()) {
            return false; // null або інший клас
        }

        SeaBoat seaBoat = (SeaBoat) o;

        if (yearBuilt != seaBoat.yearBuilt) {
            return false;
        }
        if (capacity != seaBoat.capacity) {
            return false;
        }
        if (Double.compare(seaBoat.maxSpeed, maxSpeed) != 0) {
            return false;
        }
        if (name != null ? !name.equals(seaBoat.name) : seaBoat.name != null) {
            return false;
        }
        return type != null ? type.equals(seaBoat.type) : seaBoat.type == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + yearBuilt;
        result = 31 * result + capacity;
        temp = Double.doubleToLongBits(maxSpeed);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
