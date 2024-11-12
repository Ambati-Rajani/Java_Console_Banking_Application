import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;

// Represents a user with username, password, and a list of accounts
class User {
    String username;
    String password;
    ArrayList<Account> accounts = new ArrayList<>();

    User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

// Represents a bank account with account details and transactions
class Account {
    static int accountCounter = 1000;// Static counter for unique account numbers
    String accountNumber;
    String accountHolderName;
    String accountType;
    double balance;
    ArrayList<Transaction> transactions = new ArrayList<>();

// Constructor to initialize account with holder's name, type, and initial deposit
    Account(String accountHolderName, String accountType, double initialDeposit) {
        this.accountNumber = "ACC" + (++accountCounter);
        this.accountHolderName = accountHolderName;
        this.accountType = accountType;
        this.balance = initialDeposit;
    }


 // Method to deposit an amount and add a transaction record
    void deposit(double amount) {
        balance += amount;
        transactions.add(new Transaction("Deposit", amount));
    }


 // Method to withdraw an amount with overdraft prevention
    boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactions.add(new Transaction("Withdrawal", amount));
            return true;
        }
        return false;
    }

 // Adds monthly interest to the balance for savings accounts only
    void addMonthlyInterest() {
        if (accountType.equalsIgnoreCase("savings")) {
            double interestRate = 0.03; // 3% monthly interest for simplicity
            double interest = balance * interestRate;
            deposit(interest);
        }
    }
}


// Represents a transaction with unique ID, date, type, and amount
class Transaction {
    static int transactionCounter = 0;
    String transactionId;
    Date date;
    String type;
    double amount;

    Transaction(String type, double amount) {
        this.transactionId = "TXN" + (++transactionCounter);
        this.date = new Date(); // Transaction date set to current date
        this.type = type;
        this.amount = amount;
    }
}

public class BankingApplication {
    static ArrayList<User> users = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static User loggedInUser = null;

    public static void main(String[] args) {
 // Main menu loop for registration, login, or exit
        while (true) {
            System.out.println("=== Banking Application ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> register();
                case 2 -> login();
                case 3 -> {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option, please try again!!.");
            }
        }
    }

// Register a new user with username and password
    static void register() {
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();

        users.add(new User(username, password));
        System.out.println("Registration successful!");
    }


// Login functionality to validate user credentials
    static void login() {
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();

// Verify credentials and set loggedInUser if matched
        for (User user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                loggedInUser = user;
                System.out.println("Login successful!");
                userMenu();
                return;
            }
        }
        System.out.println("Invalid credentials, please try again.");
    }



    // User menu for account operations
    static void userMenu() {
        while (true) {
            System.out.println("\n=== User Menu ===");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. View Statement");
            System.out.println("5. Check Balance");
            System.out.println("6. Calculate Interest (Savings Accounts Only)");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> openAccount();
                case 2 -> deposit();
                case 3 -> withdraw();
                case 4 -> viewStatement();
                case 5 -> checkBalance();
                case 6 -> calculateInterest();
                case 7 -> {
                    loggedInUser = null;
                    System.out.println("Logged out successfully.");
                    return;
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }


 // Function to open a new account for the logged-in user
    static void openAccount() {
        System.out.print("Enter account holder's name: ");
        String name = scanner.next();
        System.out.print("Enter account type (savings/checking): ");
        String type = scanner.next();
        System.out.print("Enter initial deposit amount: ");
        double initialDeposit = scanner.nextDouble();

     // Create a new account and add it to the user's account list
        Account account = new Account(name, type, initialDeposit);
        loggedInUser.accounts.add(account);
        System.out.println("Account opened successfully with Account Number: " + account.accountNumber);
    }

    // Helper function to select an account from the user's list
    static Account selectAccount() {
        System.out.println("Select an account:");
        for (int i = 0; i < loggedInUser.accounts.size(); i++) {
            System.out.println((i + 1) + ". " + loggedInUser.accounts.get(i).accountNumber + " - " + loggedInUser.accounts.get(i).accountType);
        }
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        return loggedInUser.accounts.get(choice - 1);
    }

// Function to handle deposits
    static void deposit() {
        Account account = selectAccount();
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();
        account.deposit(amount);
        System.out.println("Deposit successful! New balance: " + account.balance);
    }


    // Function to handle withdrawals with overdraft protection
    static void withdraw() {
        Account account = selectAccount();
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();
        if (account.withdraw(amount)) {
            System.out.println("Withdrawal successful! New balance: " + account.balance);
        } else {
            System.out.println("Insufficient funds.");
        }
    }

// Function to display the transaction history (statement) for a selected account
    static void viewStatement() {
        Account account = selectAccount();
        System.out.println("Transaction History for Account " + account.accountNumber + ":");
        for (Transaction transaction : account.transactions) {
            System.out.printf("ID: %s | Date: %s | Type: %s | Amount: %.2f%n",
                    transaction.transactionId, transaction.date, transaction.type, transaction.amount);
        }
    }


 // Function to display the current balance of a selected account
    static void checkBalance() {
        Account account = selectAccount();
        System.out.println("Current balance for Account " + account.accountNumber + ": " + account.balance);
    }


// Function to calculate and add monthly interest for all savings accounts of the user
    static void calculateInterest() {
        for (Account account : loggedInUser.accounts) {
            if (account.accountType.equalsIgnoreCase("savings")) {
                account.addMonthlyInterest();
                System.out.println("Monthly interest added to Account " + account.accountNumber + ". New balance: " + account.balance);
            }
        }
    }
}
