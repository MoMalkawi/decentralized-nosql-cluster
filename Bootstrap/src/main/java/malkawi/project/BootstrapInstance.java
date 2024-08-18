package malkawi.project;

import lombok.Getter;
import malkawi.project.data.Config;
import malkawi.project.io.BaseLoader;
import malkawi.project.managers.UserManager;
import malkawi.project.net.server.impl.BootstrapServer;
import malkawi.project.utilities.Utils;

import java.io.File;

@Getter
public class BootstrapInstance implements AutoCloseable {

    private BootstrapServer server;

    private final UserManager userManager;

    public BootstrapInstance() {
        this.userManager = new UserManager(this);
        new BaseLoader(this).load();
    }

    public void start() {
        server = new BootstrapServer(this);
        server.start();
    }


    @Override
    public void close() {
        if(server != null)
            server.close();
    }

    public File getBaseFile(String name) {
        return new File(Utils.buildPath(Config.get().getBasePath(), name));
    }

}
