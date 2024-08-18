package malkawi.project.net.global.connections.pollers;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractPoller implements Runnable {

    @Getter @Setter
    private boolean enabled = true;

    @Override
    public void run() {
        while(enabled && validatePolling())
            pollAction();
    }

    protected abstract void pollAction();

    protected abstract boolean validatePolling();

}
