import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

public class BankSimulation3 {
    private static final Logger logger = LogManager.getLogger(BankSimulation3.class);

    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount();
        ExecutorService executor = Executors.newFixedThreadPool(2);

        logger.info("Bank Simulation 3 Started");

        executor.submit(new Customer(account, "deposit", 500));
        executor.submit(new Customer(account, "withdraw", 700));
        executor.submit(new Customer(account, "withdraw", 600));

        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(100);
        }

        logger.info("Final Balance: {}", account.getBalance());
    }
}
