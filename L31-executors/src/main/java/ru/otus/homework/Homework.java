package ru.otus.homework;

public class Homework {
    private String lastThread = "thread2";
    private int thread1Value = 0;
    private int thread2Value = 0;
    private boolean increaseMode = true;
    private boolean programFinished = false;

    public static void main(String[] args) {
        Homework hw = new Homework();
        new Thread(() -> hw.action("thread1")).start();
        new Thread(() -> hw.action("thread2")).start();
    }

    private synchronized void action(String thread) {
        while(!Thread.currentThread().isInterrupted() && !programFinished) {
            try {
                while (lastThread.equals(thread)) {
                    this.wait();
                }
                lastThread = thread;

                if (thread.equals("thread1")) {
                    thread1Value = changeValueAndPrint(thread1Value, thread);
                } else {
                    thread2Value = changeValueAndPrint(thread2Value, thread);
                }

                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private int changeValueAndPrint(int value, String thread) {
        if (value == 10) {
            increaseMode = false;
        }

        if (increaseMode) {
            value++;
        } else {
            value--;
        }

        if (value > 0) {
            System.out.println(thread + ": " + value);
        }

        if (value == 0 && thread.equals("thread1")) {
            programFinished = true;
        }

        return value;
    }
}
