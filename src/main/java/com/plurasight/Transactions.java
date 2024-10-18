package com.plurasight;

import java.time.LocalDate;
import static com.plurasight.Main.transactions;

public class Transactions {

    // Make the Transaction class static
    public static class Transaction {
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

    // Method to display transactions based on type
    public static void displayTransactions(String type) {
        if (transactions.isEmpty()) {
            System.out.println("No transactions available.");
            return;
        }

        // Display transactions in reverse order (newest first)
        for (int i = transactions.size() - 1; i >= 0; i--) {
            com.plurasight.Transaction transaction = transactions.get(i);  // No need to qualify the class now
            double amount = transaction.getAmount();

            // Filter based on type
            if (type.equals("ALL") ||
                    (type.equals("DEPOSIT") && amount > 0) ||
                    (type.equals("PAYMENT") && amount < 0)) {
                System.out.println(transaction);
            }
        }
    }
}
