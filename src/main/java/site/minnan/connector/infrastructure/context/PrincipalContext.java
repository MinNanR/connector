package site.minnan.connector.infrastructure.context;

import org.apache.logging.log4j.spi.ThreadContextMapFactory;
import site.minnan.connector.domain.aggregrate.AuthUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class PrincipalContext {

    private PrincipalContext() {
    }

    private static final Map<Long, ThreadLocal<AuthUser>> userMap;

    static {
        userMap = new HashMap<>();
    }

    public static void setUser(AuthUser user) {
        Thread currentThread = Thread.currentThread();
        long threadId = currentThread.getId();
        ThreadLocal<AuthUser> threadLocal = new ThreadLocal<>();
        threadLocal.set(user);
        userMap.put(threadId, threadLocal);
    }

    public static AuthUser getUser() {
        Thread currentThread = Thread.currentThread();
        Optional<ThreadLocal<AuthUser>> threadOptional = Optional.ofNullable(userMap.getOrDefault(currentThread.getId(),
                null));
        return threadOptional.map(ThreadLocal::get).orElse(null);
    }

    public static void removeUser(){
        Thread currentThread = Thread.currentThread();
        userMap.remove(currentThread.getId());
    }
}
