package de.dhbw.parprog;


import java.util.concurrent.locks.ReentrantLock;

public class Account {
    public static long LOWER_LIMIT = 0;
    public static long UPPER_LIMIT = 100000;

    private long money;

    ReentrantLock lock = new ReentrantLock();

    public Account() {
        money = 0;
    }

    /**
     * Legt Geld im Konto ab oder zieht einen Betrag vom Kontostand ab. Diese Methode ist threadsicher,
     * da der Kontostand nur verändert werden kann, wenn der Thread das entsprechende Lock besitzt.
     *
     * @param amount
     * @throws IllegalAccountStateException
     */
    public void addMoney(long amount) throws IllegalAccountStateException {
        lock.lock();
        try {
            long newMoney = money + amount;
            if (newMoney > UPPER_LIMIT || newMoney < LOWER_LIMIT) {
                throw new IllegalAccountStateException();
            } else {
                // Nur zuweisen, wenn der neue Betrag gültig ist.
                money = newMoney;
            }
        } finally {
            lock.unlock();
        }
    }

    public long getMoney() {
        lock.lock();
        try {
            return money;
        } finally {
            lock.unlock();
        }
    }
}
