package app;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();
        AccountService accountService = new AccountService();
        TransactionService transactionService = new TransactionService();
        LoanService loanService = new LoanService();

        System.out.println("=== Welcome to Bank Management System ===");

        String loggedInEmail = null;
        int choice = -1;

        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.print("Choose option: ");
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine();
                if (choice == 1 || choice == 2) break;
                else System.out.println("❌ Invalid choice. Enter 1 or 2.");
            } else {
                sc.nextLine();
                System.out.println("❌ Please enter numeric choice.");
            }
        }

        if (choice == 1) {
            String email, name, phone, friendId;
            double hotcash, budget;
            int notificationNo, incomeNo, accountNo, expenseNo;

            do {
                System.out.print("Enter a valid Email: ");
                email = sc.nextLine();
            } while (!isValidEmail(email));

            System.out.print("Name: ");
            name = sc.nextLine();

            do {
                System.out.print("Phone Number (10 digits): ");
                phone = sc.nextLine();
            } while (!isValidPhoneNumber(phone));

            hotcash = getValidatedDouble(sc, "Hotcash: ");
            budget = getValidatedDouble(sc, "Budget: ");
            notificationNo = getValidatedInt(sc, "Notification No: ");
            incomeNo = getValidatedInt(sc, "Income No: ");
            accountNo = getValidatedInt(sc, "Account No: ");
            expenseNo = getValidatedInt(sc, "Expense No: ");
            System.out.print("Friend ID (or NULL): ");
            friendId = sc.nextLine();
            if (friendId.equalsIgnoreCase("null")) friendId = null;

            userService.registerUser(email, name, phone, hotcash, budget, notificationNo, incomeNo, accountNo, expenseNo, friendId);
        
        } else if (choice == 2) {
            String email;
            do {
                System.out.print("Enter your registered Email: ");
                email = sc.nextLine();
            } while (!isValidEmail(email));

            if (userService.login(email)) {
                System.out.println("✅ Login successful!");
                loggedInEmail = email;
            } else {
                System.out.println("❌ Login failed. Email not found. Please register first.");
                System.exit(0);
            }
        }

        if (loggedInEmail != null) {
            int opt;
            do {
                System.out.println("\n--- Dashboard ---");
                System.out.println("1. Show Accounts");
                System.out.println("2. Add Account");
                System.out.println("3. Update Account Balance");
                System.out.println("4. Delete Account");

                System.out.println("5. Add Income");
                System.out.println("6. Update Income");
                System.out.println("7. Delete Income");
                System.out.println("8. Show Total Income");

                System.out.println("9. Add Expense");
                System.out.println("10. Update Expense");
                System.out.println("11. Delete Expense");
                System.out.println("12. Show Total Expenses");

                System.out.println("13. Show Loan Details");
                System.out.println("14. Update Loan");
                System.out.println("15. Delete Loan");

                System.out.println("16. Show EMI Details");
                System.out.println("17. Update EMI");
                System.out.println("18. Delete EMI");

                System.out.println("19. View My User Profile");

                System.out.println("0. Logout");
                System.out.print("Choose option: ");
                opt = getValidatedInt(sc, "");

                switch (opt) {
                    case 1:
                        accountService.showUserAccounts(loggedInEmail);
                        break;
                    case 2:
                        int accNo = getValidatedInt(sc, "Account No: ");
                        System.out.print("Account Type: ");
                        String accType = sc.nextLine();
                        double accBal = getValidatedDouble(sc, "Balance: ");
                        accountService.createAccount(accNo, accType, accBal, loggedInEmail);
                        break;
                    case 3:
                        int accToUpdate = getValidatedInt(sc, "Account No: ");
                        double newBal = getValidatedDouble(sc, "New Balance: ");
                        accountService.updateAccountBalance(accToUpdate, newBal);
                        break;
                    case 4:
                        System.out.print("Enter Email ID to delete account(s): ");
                        String emailToDelete = sc.nextLine();
                        if (isValidEmail(emailToDelete)) {
                            accountService.deleteAccountByEmail(emailToDelete);
                        } else {
                            System.out.println("❌ Invalid email format.");
                        }
                        break;
                    case 5:
                        int incomeId = getValidatedInt(sc, "Income ID: ");
                        double incomeAmt = getValidatedDouble(sc, "Amount: ");
                        System.out.print("Description: ");
                        String incomeDesc = sc.nextLine();
                        transactionService.addIncome(incomeId, incomeAmt, incomeDesc, loggedInEmail);
                        break;
                    case 6:
                        int incomeUpd = getValidatedInt(sc, "Income ID to Update: ");
                        double incAmtUpd = getValidatedDouble(sc, "New Amount: ");
                        System.out.print("New Description: ");
                        String incDescUpd = sc.nextLine();
                        transactionService.updateIncome(incomeUpd, incAmtUpd, incDescUpd);
                        break;
                    case 7:
                        int incomeDel = getValidatedInt(sc, "Income ID to Delete: ");
                        transactionService.deleteIncome(incomeDel);
                        break;
                    case 8:
                        transactionService.showTotalIncome(loggedInEmail);
                        break;
                    case 9:
                        int expId = getValidatedInt(sc, "Expense ID: ");
                        double expAmt = getValidatedDouble(sc, "Amount: ");
                        System.out.print("Category: ");
                        String cat = sc.nextLine();
                        System.out.print("Location: ");
                        String loc = sc.nextLine();
                        System.out.print("Date (YYYY-MM-DD): ");
                        String date = sc.nextLine();
                        int expAcc = getValidatedInt(sc, "Account No: ");
                        transactionService.addExpense(expId, expAmt, cat, loc, date, expAcc);
                        break;
                    case 10:
                        int expNo = getValidatedInt(sc, "Expense ID to Update: ");
                        double amtUpd = getValidatedDouble(sc, "Amount: ");
                        System.out.print("Category: ");
                        String catUpd = sc.nextLine();
                        System.out.print("Location: ");
                        String locUpd = sc.nextLine();
                        System.out.print("Date: ");
                        String dateUpd = sc.nextLine();
                        int accUpd = getValidatedInt(sc, "Account No: ");
                        transactionService.updateExpense(expNo, amtUpd, catUpd, locUpd, dateUpd, accUpd);
                        break;
                    case 11:
                        int expDel = getValidatedInt(sc, "Expense ID to Delete: ");
                        transactionService.deleteExpense(expDel);
                        break;
                    case 12:
                        int showExpAcc = getValidatedInt(sc, "Account No: ");
                        transactionService.showTotalExpenses(showExpAcc);
                        break;
                    case 13:
                        loanService.showLoanDetails(loggedInEmail);
                        break;
                    case 14:
                        int loanNo = getValidatedInt(sc, "Loan No: ");
                        double loanAmt = getValidatedDouble(sc, "Amount: ");
                        double interest = getValidatedDouble(sc, "Interest (%): ");
                        double balLoanAmt = getValidatedDouble(sc, "Balance Amount: ");
                        sc.nextLine();
                        System.out.print("Start Date (YYYY-MM-DD): ");
                        String startL = sc.nextLine();
                        System.out.print("End Date (YYYY-MM-DD): ");
                        String endL = sc.nextLine();
                        System.out.print("Loan Type: ");
                        String loanType = sc.nextLine();
                        int accLoan = getValidatedInt(sc, "Account No: ");
                        loanService.updateLoan(loanNo, loanAmt, interest, balLoanAmt, startL, endL, loanType, accLoan);
                        break;
                    case 15:
                        int loanToDel = getValidatedInt(sc, "Loan No to delete: ");
                        loanService.deleteLoan(loanToDel);
                        break;
                    case 16:
                        transactionService.showEMIsForUser(loggedInEmail);
                        break;
                    case 17:
                        int emiNo = getValidatedInt(sc, "EMI No: ");
                        double emiAmt = getValidatedDouble(sc, "Amount: ");
                        sc.nextLine();
                        System.out.print("Status (Paid/Unpaid): ");
                        String pay = sc.nextLine();
                        System.out.print("Due Date (YYYY-MM-DD): ");
                        String due = sc.nextLine();
                        System.out.print("Start Date (YYYY-MM-DD): ");
                        String start = sc.nextLine();
                        System.out.print("Description: ");
                        String emiDesc = sc.nextLine();
                        double balAmt = getValidatedDouble(sc, "Balance Amount: ");
                        int loan = getValidatedInt(sc, "Loan No: ");
                        transactionService.updateEMI(emiNo, emiAmt, pay, due, start, emiDesc, balAmt, loan);
                        break;
                    case 18:
                        int emiDel = getValidatedInt(sc, "EMI No to delete: ");
                        transactionService.deleteEMI(emiDel);
                        break;
                    case 19:
                        userService.showUserProfile(loggedInEmail);
                        break;
                    case 0:
                        System.out.println("👋 Logged out successfully.");
                        break;
                    default:
                        System.out.println("❌ Invalid option.");
                }

            } while (opt != 0);
        }

        sc.close();
    }

    private static boolean isValidEmail(String email) {
        boolean valid = Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", email);
        if (!valid) System.out.println("⚠️ Invalid email format.");
        return valid;
    }

    private static boolean isValidPhoneNumber(String phone) {
        boolean valid = Pattern.matches("^[0-9]{10}$", phone);
        if (!valid) System.out.println("⚠️ Phone number must be 10 digits and numeric.");
        return valid;
    }

    private static int getValidatedInt(Scanner sc, String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                value = sc.nextInt();
                sc.nextLine();
                break;
            } else {
                System.out.println("❌ Invalid input. Please enter a number.");
                sc.nextLine();
            }
        }
        return value;
    }

    private static double getValidatedDouble(Scanner sc, String prompt) {
        double value;
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextDouble()) {
                value = sc.nextDouble();
                sc.nextLine();
                break;
            } else {
                System.out.println("❌ Invalid input. Please enter a valid number.");
                sc.nextLine();
            }
        }
        return value;
    }
}


