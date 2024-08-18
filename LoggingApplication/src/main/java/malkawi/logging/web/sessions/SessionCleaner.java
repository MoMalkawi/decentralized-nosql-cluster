package malkawi.logging.web.sessions;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import malkawi.logging.utilities.Sleep;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class SessionCleaner implements Runnable {

    private final @NonNull Sessions sessions;

    private long oldestSession = -1;

    private @Setter boolean enabled = true;

    @Override
    public void run() {
        while(enabled) {
            if(sessions.getUsers().size() == 0) {
                Sleep.sleep(10);
                continue;
            }
            if(!shouldClean())
                Sleep.sleep((int) (System.currentTimeMillis() - oldestSession));
            oldestSession = clean();
        }
    }

    private long clean() {
        long time = Long.MAX_VALUE;
        List<String> keysToRemove = new ArrayList<>();
        for(UserSession session : sessions.getUsers().values()) {
            if(getMinutesDifference(session.getLastAccessTime()) >= 10) {
                keysToRemove.add(session.getSessionId());
                session.close();
                continue;
            }
            time = Math.min(time, session.getLastAccessTime());
        }
        keysToRemove.forEach(k -> sessions.getUsers().remove(k));
        return time != Long.MAX_VALUE ? time : -1;
    }

    private boolean shouldClean() {
        return oldestSession <= 0 || getMinutesDifference() >= 10;
    }

    private double getMinutesDifference() {
        return getMinutesDifference(oldestSession);
    }

    private double getMinutesDifference(long lastAccess) {
        return TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - lastAccess);
    }

}
