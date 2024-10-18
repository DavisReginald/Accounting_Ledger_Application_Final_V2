package com.plurasight;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;

class Transaction {
    private double amount;
    private String description;
    private String vendor;
    private LocalDate date;

    public Transaction(double amount, String description, String vendor, LocalDate date) {
        this.amount = amount;
        this.description = description;
        this.vendor = vendor;
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Amount: $" + amount + ", Description: " + description + ", Vendor: " + vendor + ", Date: " + date;
    }
}

public class Main {
    public static double totalBalance = 0.0;
    public static Scanner myscanner = new Scanner(System.in);
    public static ArrayList<Transaction> transactions = new ArrayList<>(); // Store transactions

    public static void main(String[] args) throws IOException {
        loadTransactionsFromFile(); // Load saved transactions on startup
        displayMenu(); // Show the main menu
    }
                        // Display Menu //
    public static void displayMenu() throws IOException {
        while (true) {
            System.out.println("Welcome to Chase banking, how can I help you today?");
            System.out.println("D) Make Deposit \nP) Make Payment \nL) Ledger \nR) Reports \nX) Exit");
            String option = myscanner.nextLine();

            if (option.equalsIgnoreCase("D")) {
                addDeposit();
            } else if (option.equalsIgnoreCase("P")) {
                makePayment();
            } else if (option.equalsIgnoreCase("L")) {
                displayLedger();
            } else if (option.equalsIgnoreCase("R")) {
                displayReportsMenu();
            } else if (option.equalsIgnoreCase("X")) {
                System.out.println("Exiting Application");
                saveTransactionsToFile(); // Save transactions before exiting
                break;
            } else {
                System.out.println("Invalid Option. Try again.");
            }
        }
    }

                   // Deposit Function //
    public static void addDeposit() throws IOException {
        System.out.println("Enter your deposit amount:");
        double deposit = myscanner.nextDouble();
        myscanner.nextLine(); // Consume newline left-over

        System.out.println("Enter description for this deposit:");
        String description = myscanner.nextLine();

        // Add deposit transaction
        Transaction depositTransaction = new Transaction(deposit, description, "Deposit", LocalDate.now());
        transactions.add(depositTransaction);

        System.out.println("Deposit of $" + deposit + " has been added with description: " + description);
    }

                // Payment Function //
    public static void makePayment() throws IOException {
        System.out.println("Enter your payment amount:");
        double payment = myscanner.nextDouble();
        myscanner.nextLine();  // Consume the newline left over

        System.out.println("Enter description for this payment:");
        String description = myscanner.nextLine();

        // Record payment as negative amount
        Transaction paymentTransaction = new Transaction(-payment, description, "Payment", LocalDate.now());
        transactions.add(paymentTransaction);

        System.out.println("Payment of $" + payment + " has been recorded.");
    }

    // Ledger: Displays all, deposit, or payment transactions
    public static void displayLedger() {
        System.out.println("A) All Transactions \nD) Deposits \nP) Payments \nH) Home");
        String ledgerOption = myscanner.nextLine();

        if (ledgerOption.equalsIgnoreCase("A")) {
            displayTransactions("ALL");
        } else if (ledgerOption.equalsIgnoreCase("D")) {
            displayTransactions("DEPOSIT");
        } else if (ledgerOption.equalsIgnoreCase("P")) {
            displayTransactions("PAYMENT");
        } else if (ledgerOption.equalsIgnoreCase("H")) {
            return; // Go back to the main menu
        } else {
            System.out.println("Invalid option.");
        }
    }

    // Helper to display transactions based on type
    public static void displayTransactions(String type) {
        if (transactions.isEmpty()) {
            System.out.println("No transactions available.");
            return;
        }

        // Display transactions in reverse order (newest first)
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            double amount = transaction.getAmount();

            if (type.equals("ALL") ||
                    (type.equals("DEPOSIT") && amount > 0) ||
                    (type.equals("PAYMENT") && amount < 0)) {
                System.out.println(transaction);
            }
        }
    }

    // Reports Menu
    public static void displayReportsMenu() {
        System.out.println("1) Month To Date \n2) Previous Month \n3) Year To Date \n4) Previous Year \n5) Search by Vendor \n0) Back \nH) Home");
        String reportOption = myscanner.nextLine();

        switch (reportOption) {
            case "1":
                displayMonthToDate();
                break;
            case "2":
                displayPreviousMonth();
                break;
            case "3":
                displayYearToDate();
                break;
            case "4":
                displayPreviousYear();
                break;
            case "5":
                System.out.println("Enter vendor name:");
                String vendor = myscanner.nextLine();
                displayTransactionsByVendor(vendor);
                break;
            case "0":
            case "H":
                return;
            default:
                System.out.println("Invalid option.");
                break;
        }
    }

    // Example Report - Search by Vendor
    public static void displayTransactionsByVendor(String vendor) {
        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                System.out.println(transaction);
            }
        }
    }

    // Stub reports for date-based reports (implementation can be expanded based on LocalDate)
    public static void displayMonthToDate() {
        System.out.println("Displaying Month To Date transactions...");
        // Add logic for filtering based on current month
    }

    public static void displayPreviousMonth() {
        System.out.println("Displaying Previous Month transactions...");
        // Add logic for filtering based on previous month
    }

    public static void displayYearToDate() {
        System.out.println("Displaying Year To Date transactions...");
        // Add logic for filtering based on current year
    }

    public static void displayPreviousYear() {
        System.out.println("Displaying Previous Year transactions...");
        // Add logic for filtering based on previous year
    }

    // Saving Transactions to a File
    public static void saveTransactionsToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv"))) {
            for (Transaction transaction : transactions) {
                writer.write(transaction.getAmount() + "," + transaction.getDescription() + "," + transaction.getVendor() + "," + transaction.getDate());
                writer.newLine();
            }
        }
    }

    // Loading Transactions from a File
    public static void loadTransactionsFromFile() throws IOException {
        File file = new File("transactions.csv");
        if (!file.exists()) {
            return; // No transactions to load
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                double amount = Double.parseDouble(parts[0]);
                String description = parts[1];
                String vendor = parts[2];
                LocalDate date = LocalDate.parse(parts[3]);

                transactions.add(new Transaction(amount, description, vendor, date));
            }
        }
    }
}