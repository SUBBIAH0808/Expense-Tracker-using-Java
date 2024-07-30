package srce_code;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Enhanced_Expense_Tracker {
    private static final String FILE_NAME = "expenses.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static ArrayList<Expense> expenses = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadExpenses();
        while (true) {
            System.out.println("\n1. Add Expense");
            System.out.println("2. Delete Expense");
            System.out.println("3. View Expenses");
            System.out.println("4. View Category Totals");
            System.out.println("5. View Expenses by Date Range");
            System.out.println("6. View Summary");
            System.out.println("7. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addExpense();
                    break;
                case 2:
                    deleteExpense();
                    break;
                case 3:
                    viewExpenses();
                    break;
                case 4:
                    viewCategoryTotals();
                    break;
                case 5:
                    viewExpensesByDateRange();
                    break;
                case 6:
                    viewSummary();
                    break;
                case 7:
                    saveExpenses();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void addExpense() {
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);
        expenses.add(new Expense(description, amount, category, date));
    }

    private static void deleteExpense() {
        viewExpenses();
        System.out.print("Enter the number of the expense to delete: ");
        int index = scanner.nextInt();
        if (index > 0 && index <= expenses.size()) {
            expenses.remove(index - 1);
            System.out.println("Expense deleted.");
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void viewExpenses() {
        System.out.println("Expenses:");
        for (int i = 0; i < expenses.size(); i++) {
            System.out.println((i + 1) + ". " + expenses.get(i));
        }
    }

    private static void viewCategoryTotals() {
        Map<String, Double> categoryTotals = new HashMap<>();
        for (Expense expense : expenses) {
            categoryTotals.put(expense.getCategory(), categoryTotals.getOrDefault(expense.getCategory(), 0.0) + expense.getAmount());
        }
        System.out.println("Category Totals:");
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            System.out.println(entry.getKey() + ": $" + entry.getValue());
        }
    }

    private static void viewExpensesByDateRange() {
        System.out.print("Enter start date (YYYY-MM-DD): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);
        System.out.print("Enter end date (YYYY-MM-DD): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);
        System.out.println("Expenses from " + startDate + " to " + endDate + ":");
        for (Expense expense : expenses) {
            if ((expense.getDate().isAfter(startDate) || expense.getDate().isEqual(startDate)) &&
                (expense.getDate().isBefore(endDate) || expense.getDate().isEqual(endDate))) {
                System.out.println(expense);
            }
        }
    }

    private static void viewSummary() {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        double average = expenses.size() > 0 ? total / expenses.size() : 0;
        System.out.println("Summary:");
        System.out.println("Total Expenses: $" + total);
        System.out.println("Average Expense: $" + average);
    }

    private static void saveExpenses() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Expense expense : expenses) {
                writer.write(expense.getDescription() + ";" + expense.getAmount() + ";" + expense.getCategory() + ";" + expense.getDate());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while saving expenses.");
        }
    }

    private static void loadExpenses() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    expenses.add(new Expense(parts[0], Double.parseDouble(parts[1]), parts[2], LocalDate.parse(parts[3], DATE_FORMATTER)));
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while loading expenses.");
        }
    }
}

class Expense {
    private String description;
    private double amount;
    private String category;
    private LocalDate date;

    public Expense(String description, double amount, String category, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return description + " - $" + amount + " (" + category + ") on " + date;
    }
}
