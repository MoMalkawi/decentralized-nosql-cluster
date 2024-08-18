package malkawi.logging.net.global.data;

import java.util.concurrent.atomic.AtomicLong;

public class IdentifierGenerator {

    private IdentifierGenerator() {}

    private static final AtomicLong counter = new AtomicLong(System.currentTimeMillis());

    public static synchronized Long generate() {
        return counter.incrementAndGet();
    }

}
