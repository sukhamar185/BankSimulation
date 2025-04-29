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
    private final BankAccount account;
    private final String action;
    private final int amount;

    public Customer(BankAccount account, String action, int amount) {
        this.account = account;
        this.action = action;
        this.amount = amount;
    }

    @Override
    public void run() {
        logger.debug("Customer thread started: action={}, amount={}", action, amount);
        switch (action.toLowerCase()) {
            case "deposit":
                account.deposit(amount);
                break;
            case "withdraw":
                account.withdraw(amount);
                break;
            default:
                logger.error("Unknown action: {}", action);
        }
    }
}

public class BankSimulation2 {
    private static final Logger logger = LogManager.getLogger(BankSimulation2.class);

    public static void main(String[] args) {
        BankAccount account = new BankAccount();
        Thread[] customers = {
            new Thread(new Customer(account, "deposit", 500)),
            new Thread(new Customer(account, "withdraw", 700)),
            new Thread(new Customer(account, "withdraw", 600))
        };

        logger.info("Bank Simulation 2 Started");

        for (Thread t : customers) {
            t.start();
        }

        for (Thread t : customers) {
            try {
                t.join();
            } catch (InterruptedException e) {
                logger.error("Thread interrupted", e);
            }
        }

        logger.info("Final Balance: {}", account.getBalance());
    }
}
