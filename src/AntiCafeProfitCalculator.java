import java.util.Scanner;
import java.util.HashMap;
import java.time.Instant;


public class AntiCafeProfitCalculator {
    private static int wasGuests;
    private static int guestsCame;
    private static int allTime;
    //private static int[] sittingTimes = new int[10000];
    private static int costPerSecond;
    private static int[] earnList = new int[10];
    private static int[] ammountOfGuestsForTable = new int[10];
    private static long startTime;
    private static int timeUnitInSeconds;
    private static HashMap<Integer, Long> tableOccupancy = new HashMap<>();
    private static long totalEarnings = 0;

    public static void main(String[] args) {

        wasGuests = 0;
        guestsCame = 0;
        allTime = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("==========ANTICAFE MANAGER==========");
        System.out.print("Введите стоимость аренды за минуту: ");
        costPerSecond = scanner.nextInt();
        System.out.print("Введите, за сколько секунд условно будет проходить 1 минута: ");
        timeUnitInSeconds = scanner.nextInt();
        startTime = Instant.now().getEpochSecond();

        while (true) {
            System.out.println("");
            System.out.println("1 - Добавить гостей за стол");
            System.out.println("2 - Уйти гостям из стола");
            System.out.println("3 - Показать занятые столики, сумму к оплате и время занятости стола");
            System.out.println("4 - Показать общее время работы кафе сегодня");
            System.out.println("5 - Показать общий заработок за день");
            System.out.println("6 - Показать самый популярный стол сегодня");
            System.out.println("7 - Показать, сколько заплатят все гости, если уйдут");
            System.out.println("8 - Показать среднее время, которое люди занимают стол");
            System.out.println("9 - Показать самый прибыльный стол за день");
            System.out.println("10 - Выйти из программы");
            System.out.print("Выберите действие: ");
            int choice = scanner.nextInt();
            System.out.println("");

            switch (choice) {
                case 1:
                    addGuestsToTable(scanner);
                    break;
                case 2:
                    removeGuestsFromTable(scanner);
                    break;
                case 3:
                    showOccupiedTablesAndPayment();
                    break;
                case 4:
                    showTotalWorkTime();
                    break;
                case 5:
                    showTotalEarnings();
                    break;
                case 6:
                    showTheMostPopularTable();
                    break;
                case 7:
                    showTotalPaymentForOccupiedTables();
                    break;
                case 8:
                    showAvarageTime();
                    break;
                case 9:
                    showTheMostProfitableTable();
                    break;
                case 10:
                    System.out.println("Общая прибыль за время работы: " + totalEarnings);
                    return;
                default:
                    System.out.println("Некорректный выбор. Попробуйте снова.");
            }
        }
    }

    private static void addGuestsToTable(Scanner scanner) {
        System.out.print("Введите номер стола: ");
        int tableNumber = scanner.nextInt();
        if (tableOccupancy.containsKey(tableNumber)) {
            System.out.println("Стол уже занят.");
        } else if(tableNumber < 11 && tableNumber > 0) {
            tableOccupancy.put(tableNumber, Instant.now().getEpochSecond());
            System.out.println("Гости добавлены за стол номер " + tableNumber);
            ammountOfGuestsForTable[tableNumber-1] += 1;
            guestsCame += 1;
        } else System.out.println("В кафе есть только столики от 1 до 10");
    }

    private static void removeGuestsFromTable(Scanner scanner) {
        System.out.print("Введите номер стола: ");
        int tableNumber = scanner.nextInt();
        if(tableNumber < 11 && tableNumber > 0) {
            if (tableOccupancy.containsKey(tableNumber)) {
                long timeSpent = Instant.now().getEpochSecond() - tableOccupancy.get(tableNumber);
                totalEarnings += (timeSpent / timeUnitInSeconds) * costPerSecond;
                //sittingTimes[wasGuests] = (int) (timeSpent / timeUnitInSeconds);
                allTime += (timeSpent / timeUnitInSeconds);
                wasGuests += 1;
                earnList[tableNumber - 1] += (timeSpent / timeUnitInSeconds) * costPerSecond;
                tableOccupancy.remove(tableNumber);
                System.out.println("Гости ушли со стола номер " + tableNumber);
            } else {
                System.out.println("Стол свободен.");
            }
        } else System.out.println("В кафе есть только столики от 1 до 10");
    }

    private static void showOccupiedTablesAndPayment() {
        if (tableOccupancy.isEmpty()) {
            System.out.println("Все столики свободны.");
        } else {
            for (int tableNumber : tableOccupancy.keySet()) {
                long timeSpent = Instant.now().getEpochSecond() - tableOccupancy.get(tableNumber);
                int payment = (int) ((timeSpent / timeUnitInSeconds) * costPerSecond);
                System.out.println("Стол номер " + tableNumber + " - к оплате " + payment);
                System.out.println("Стол номер " + tableNumber + " - время за столом " + (int) (timeSpent / timeUnitInSeconds) + " минут(ы)");
            }
            //  showTotalPaymentForOccupiedTables();
        }
    }

    private static void showTotalWorkTime() {
        long currentTime = Instant.now().getEpochSecond() - startTime;
        long totalWorkTimeInSeconds = currentTime / timeUnitInSeconds;
        System.out.println("Общее время работы кафе: " + totalWorkTimeInSeconds + " минут(ы)");
    }

    private static void showTotalEarnings() {
        System.out.println("Общий заработок кафе: " + totalEarnings);
    }

    private static void showTotalPaymentForOccupiedTables() {
        if (tableOccupancy.isEmpty()) {
            System.out.println("Все столики свободны.");
        } else {
            int totalPayment = 0;
            for (int tableNumber : tableOccupancy.keySet()) {
                long timeSpent = Instant.now().getEpochSecond() - tableOccupancy.get(tableNumber);
                int payment = (int) ((timeSpent / timeUnitInSeconds) * costPerSecond);
                totalPayment += payment;
            }
            System.out.println("Общая сумма к оплате за все занятые столики: " + totalPayment);
        }
    }
    private static void showAvarageTime(){
        if (wasGuests > 0) {
            System.out.println("Среднее время, которое гости проводят за столом: " + (float) (allTime) / (float) (wasGuests) + " минут(ы)");
        } else System.out.println("Ещё ни один гость не закончил пребывание в кафе");
    }
    private static void showTheMostProfitableTable(){
        if (wasGuests > 0) {
            int maxNum = 0;
            for (int i = 0; i < 10; i++) {
                if (earnList[i] > earnList[maxNum]){
                    maxNum = i;
                }
            }
            System.out.println("Наиболее прибыльный стол - " + (maxNum+1) + ", он принёс в кассу: " + earnList[maxNum]);
        } else System.out.println("Ещё ни один гость не заплатил сегодня");
    }
    private static void showTheMostPopularTable(){
        if (guestsCame > 0){
            int maxG = 0;
            for (int i = 0; i < 10; i++){
                if (ammountOfGuestsForTable[i] > ammountOfGuestsForTable[maxG]){
                    maxG = i;
                }
            }
            System.out.println("Самый популярный стол - " + (maxG + 1) + ", его занимали " + ammountOfGuestsForTable[maxG] + " раз(а)");
        }else  System.out.println("Ещё ни один гость не заходил сегодня");
    }
}