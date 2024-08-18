package malkawi.logging.utilities;

import malkawi.logging.utilities.interfaces.Condition;

public class Sleep {

    public static boolean sleepUntil(Condition condition, int millis, int polling) {
        long start = System.currentTimeMillis();
        while(!condition.verify() && (System.currentTimeMillis() - start) < millis) {
            try {
                Thread.sleep(polling);
            } catch (InterruptedException e) {
                break;
            }
        }
        return condition.verify();
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Sleep() {}

}
