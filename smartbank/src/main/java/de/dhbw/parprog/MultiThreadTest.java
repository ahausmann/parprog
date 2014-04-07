package de.dhbw.parprog;

import java.util.*;

/**
 * User: alexander
 * Date: 02.04.14
 * Time: 17:37
 */
public class MultiThreadTest implements Runnable {
    Account from, to;
    Bank using;
    int amount;

    public MultiThreadTest(Account from, Account to, int amount, Bank using) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.using = using;
    }

    public static void main(String[] args) {
        final int threadCount = 50;

        Bank bank = new Bank();
        Account account1 = bank.createAccount();
        Account account2 = bank.createAccount();
        bank.deposit(account1, threadCount*100);
        bank.deposit(account2, threadCount*100);
        // Ergebnis vorberechnen
        long outcome1, outcome2;
        outcome1 = outcome2 = threadCount*100;

        System.out.println("Thread count: " + threadCount);
        System.out.println("Total money: " + threadCount*100*2); // 100 pro thread; 2 für 2 accounts

        List<Thread> threads = new ArrayList<>(threadCount);
        Random r = new Random();
        for (int i = 0; i < threadCount; ++i) {
            Runnable runnable;
            int amount = r.nextInt(100);
            if (r.nextBoolean()) {
                runnable = new MultiThreadTest(account1, account2, amount, bank);
                outcome1 -= amount;
                outcome2 += amount;
//                System.out.println("1 -> 2: 100");
            } else {
                runnable = new MultiThreadTest(account2, account1, amount, bank);
                outcome1 += amount;
                outcome2 -= amount;
//                System.out.println("2 -> 1: 100");
            }

            Thread t = new Thread(runnable);
            t.start();
            threads.add(t);
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ignored) {
            }
        }
        System.out.println("Account 1: " + account1.getMoney() + " Soll: " + outcome1);
        System.out.println("Account 2: " + account2.getMoney() + " Soll: " + outcome2);
        System.out.println("Total money: " + (account1.getMoney() + account2.getMoney()));
    }

    @Override
    public void run() {
        using.transfer(from, to, amount);
    }
}
