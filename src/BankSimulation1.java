import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class BankAccount {
    private static final Logger logger = LogManager.getLogger(BankAccount.class);
    private int balance = 1000;

    public synchronized void deposit(int amount) {
        balance += amount;
        logger.info("Deposited {}, New Balance: {}", amount, balance);
    }

    public synchronized void withdraw(int amount) {
        if (balance >= amount) {
            balance -= amount;
            logger.info("Withdrawn {}, New Balance: {}", amount, balance);
        } else {
            logger.warn("Insufficient funds for withdrawal of {}. Current Balance: {}", amount, balance);
        }
    }

    public int getBalance() {
        return balance;
    }
}

class Customer implements Runnable {
    private static final Logger logger = LogManager.getLogger(Customer.class);
    private BankAccount account;
    private String action;
    private int amount;

    public Customer(BankAccount account, String action, int amount) {
        this.account = account;
        this.action = action;
        this.amount = amount;
    }

    public void run() {
        logger.debug("Customer started with action: {} and amount: {}", action, amount);
        if (action.equalsIgnoreCase("deposit")) {
            account.deposit(amount);
        } else if (action.equalsIgnoreCase("withdraw")) {
            account.withdraw(amount);
        } else {
            logger.error("Unknown action: {}", action);
        }
    }
}

public class BankSimulation1 {
    private static final Logger logger = LogManager.getLogger(BankSimulation1.class);

    public static void main(String[] args) {
        BankAccount account = new BankAccount();
        Thread[] customers = new Thread[3];
        customers[0] = new Thread(new Customer(account, "deposit", 500));
        customers[1] = new Thread(new Customer(account, "withdraw", 700));
        customers[2] = new Thread(new Customer(account, "withdraw", 600));

        logger.info("Bank Simulation Started");

        for (Thread t : customers) {
            t.start();
        }
    }
}
