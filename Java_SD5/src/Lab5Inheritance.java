import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Лабораторна робота №5.
 * Тема: Наслідування та поліморфізм.
 *
 * Номер залікової книжки: 3413
 *
 * Обчислення:
 *   C13 = 3413 mod 13 = 7
 *
 * Відповідно до варіанту (C13 = 7):
 *   - Визначити ієрархію рухомого складу залізничного транспорту.
 *   - Створити пасажирський потяг.
 *   - Порахувати загальну чисельність пасажирів і багажу в потязі.
 *   - Провести сортування вагонів потягу за рівнем комфортності.
 *   - Знайти вагон у потязі, що відповідає заданому діапазону кількості пасажирів.
 *
 * У даній програмі:
 *   - RailCar — абстрактний базовий клас (узагальнений клас).
 *   - CoupeCar, PlatzkartCar, LuxCar — не менше трьох класів-нащадків.
 *   - Train — клас, що працює з масивом вагонів:
 *       * рахує загальну кількість пасажирів та багажу;
 *       * сортує вагони за комфортністю;
 *       * шукає вагони за діапазоном кількості пасажирів.
 */
public class Lab5Inheritance {

    public static void main(String[] args) {
        try {
            RailCar[] cars = new RailCar[] {
                    new CoupeCar("C1", 3, 40, 40, 32, 30),
                    new PlatzkartCar("P1", 2, 54, 54, 20, 18),
                    new LuxCar("L1", 5, 20, 20, 15, 14),
                    new CoupeCar("C2", 3, 40, 40, 28, 26),
                    new PlatzkartCar("P2", 1, 54, 54, 10, 8)
            };

            Train train = new Train(cars);

            System.out.println("=== Initial train composition ===");
            train.printCars();

            int totalPassengers = train.getTotalPassengers();
            int totalBaggage = train.getTotalBaggage();

            System.out.println("\nTotal passengers in train: " + totalPassengers);
            System.out.println("Total baggage items in train: " + totalBaggage);

            // Сортування вагонів за рівнем комфортності (від найвищого до найнижчого)
            train.sortByComfortDescending();

            System.out.println("\n=== Train composition sorted by comfort level (desc) ===");
            train.printCars();

            // Пошук вагонів за діапазоном кількості пасажирів
            int minPassengers = 20;
            int maxPassengers = 35;
            List<RailCar> foundCars = train.findCarsByPassengerRange(minPassengers, maxPassengers);

            System.out.println("\n=== Search cars by passenger count in range [" +
                    minPassengers + "; " + maxPassengers + "] ===");
            if (foundCars.isEmpty()) {
                System.out.println("No cars found in the given passenger range.");
            } else {
                for (RailCar car : foundCars) {
                    System.out.println(car);
                }
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid data: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

/**
 * Абстрактний базовий клас, що описує залізничний вагон (рухомий склад).
 */
abstract class RailCar {

    private final String carNumber;       // номер вагона
    private final int comfortLevel;      // рівень комфортності (1..5)
    private final int passengerCapacity; // максимальна кількість пасажирів
    private final int baggageCapacity;   // максимальна кількість місць для багажу

    private int currentPassengers;       // поточна кількість пасажирів
    private int currentBaggageItems;     // поточна кількість одиниць багажу

    public RailCar(String carNumber,
                   int comfortLevel,
                   int passengerCapacity,
                   int baggageCapacity,
                   int currentPassengers,
                   int currentBaggageItems) {

        if (carNumber == null || carNumber.isEmpty()) {
            throw new IllegalArgumentException("Car number must not be null or empty");
        }
        if (comfortLevel < 1 || comfortLevel > 5) {
            throw new IllegalArgumentException("Comfort level must be in range 1..5");
        }
        if (passengerCapacity < 0 || baggageCapacity < 0) {
            throw new IllegalArgumentException("Capacities must not be negative");
        }
        if (currentPassengers < 0 || currentPassengers > passengerCapacity) {
            throw new IllegalArgumentException("Invalid current passengers value");
        }
        if (currentBaggageItems < 0 || currentBaggageItems > baggageCapacity) {
            throw new IllegalArgumentException("Invalid current baggage items value");
        }

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

    public void setCurrentPassengers(int currentPassengers) {
        if (currentPassengers < 0 || currentPassengers > passengerCapacity) {
            throw new IllegalArgumentException("Invalid current passengers value");
        }
        this.currentPassengers = currentPassengers;
    }

    public void setCurrentBaggageItems(int currentBaggageItems) {
        if (currentBaggageItems < 0 || currentBaggageItems > baggageCapacity) {
            throw new IllegalArgumentException("Invalid current baggage items value");
        }
        this.currentBaggageItems = currentBaggageItems;
    }

    /**
     * Кожен нащадок може додати свою інформацію до опису.
     */
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
}

/**
 * Купейний вагон.
 */
class CoupeCar extends RailCar {

    private final int compartments; // кількість купе

    public CoupeCar(String carNumber,
                    int comfortLevel,
                    int passengerCapacity,
                    int baggageCapacity,
                    int currentPassengers,
                    int currentBaggageItems) {
        super(carNumber, comfortLevel, passengerCapacity, baggageCapacity,
                currentPassengers, currentBaggageItems);
        // для прикладу, приблизно 4 пасажири на купе
        this.compartments = Math.max(1, passengerCapacity / 4);
    }

    public int getCompartments() {
        return compartments;
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
                ", compartments=" + compartments +
                '}';
    }
}

/**
 * Плацкартний вагон.
 */
class PlatzkartCar extends RailCar {

    private final boolean hasSidePlaces; // наявність бокових місць

    public PlatzkartCar(String carNumber,
                        int comfortLevel,
                        int passengerCapacity,
                        int baggageCapacity,
                        int currentPassengers,
                        int currentBaggageItems) {
        super(carNumber, comfortLevel, passengerCapacity, baggageCapacity,
                currentPassengers, currentBaggageItems);
        // умовно: у плацкарті завжди є бокові місця
        this.hasSidePlaces = true;
    }

    public boolean hasSidePlaces() {
        return hasSidePlaces;
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
                ", hasSidePlaces=" + hasSidePlaces +
                '}';
    }
}

/**
 * Люксовий вагон.
 */
class LuxCar extends RailCar {

    private final boolean hasPrivateBathroom; // наявність окремого санвузла

    public LuxCar(String carNumber,
                  int comfortLevel,
                  int passengerCapacity,
                  int baggageCapacity,
                  int currentPassengers,
                  int currentBaggageItems) {
        super(carNumber, comfortLevel, passengerCapacity, baggageCapacity,
                currentPassengers, currentBaggageItems);
        this.hasPrivateBathroom = true;
    }

    public boolean hasPrivateBathroom() {
        return hasPrivateBathroom;
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
                ", hasPrivateBathroom=" + hasPrivateBathroom +
                '}';
    }
}

/**
 * Клас потяга, що містить масив вагонів та виконує дії з ними.
 */
class Train {

    private final RailCar[] cars;

    public Train(RailCar[] cars) {
        if (cars == null || cars.length == 0) {
            throw new IllegalArgumentException("Train must have at least one car");
        }
        this.cars = Arrays.copyOf(cars, cars.length);
    }

    /**
     * Повертає загальну кількість пасажирів у всіх вагонах.
     */
    public int getTotalPassengers() {
        int sum = 0;
        for (RailCar car : cars) {
            sum += car.getCurrentPassengers();
        }
        return sum;
    }

    /**
     * Повертає загальну кількість одиниць багажу у всіх вагонах.
     */
    public int getTotalBaggage() {
        int sum = 0;
        for (RailCar car : cars) {
            sum += car.getCurrentBaggageItems();
        }
        return sum;
    }

    /**
     * Сортує вагони за рівнем комфортності за спаданням
     * (спочатку найкомфортніші).
     */
    public void sortByComfortDescending() {
        Arrays.sort(cars, (c1, c2) ->
                Integer.compare(c2.getComfortLevel(), c1.getComfortLevel()));
    }

    /**
     * Повертає список вагонів, у яких кількість пасажирів
     * лежить у діапазоні [minPassengers; maxPassengers].
     */
    public List<RailCar> findCarsByPassengerRange(int minPassengers, int maxPassengers) {
        if (minPassengers < 0 || maxPassengers < 0 || minPassengers > maxPassengers) {
            throw new IllegalArgumentException("Invalid passenger range");
        }
        List<RailCar> result = new ArrayList<>();
        for (RailCar car : cars) {
            int p = car.getCurrentPassengers();
            if (p >= minPassengers && p <= maxPassengers) {
                result.add(car);
            }
        }
        return result;
    }

    /**
     * Друкує всі вагони потяга.
     */
    public void printCars() {
        for (RailCar car : cars) {
            System.out.println(car);
        }
    }
}
