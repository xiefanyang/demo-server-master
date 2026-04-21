package com.hnyr.sys.config;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.Session;
import org.apache.catalina.session.ManagerBase;

import java.io.IOException;

/**
 * @ClassName: NoSessionManager
 * @Description: 改造 session 管理器
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
public class NoSessionManager extends ManagerBase implements Lifecycle {
    @Override
    protected synchronized void startInternal() throws LifecycleException {
        super.startInternal();
        setState(LifecycleState.STARTING);
    }

    @Override
    protected synchronized void stopInternal() throws LifecycleException {
        setState(LifecycleState.STOPPING);
        super.stopInternal();
    }

    @Override
    public void load() throws ClassNotFoundException, IOException {

    }

    @Override
    public void unload() throws IOException {
    }

    @Override
    public Session createSession(String sessionId) {
        return null;
    }

    @Override
    public Session createEmptySession() {
        return null;
    }

    @Override
    public void add(Session session) {
    }

    @Override
    public Session findSession(String id) throws IOException {
        return null;
    }

    @Override
    public Session[] findSessions() {
        return null;
    }

    @Override
    public void processExpires() {
    }
}
