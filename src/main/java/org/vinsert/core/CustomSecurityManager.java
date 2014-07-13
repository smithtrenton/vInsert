package org.vinsert.core;

import java.io.FileDescriptor;
import java.lang.reflect.ReflectPermission;
import java.net.InetAddress;
import java.security.Permission;

/**
 * A basic security manager that's going to need a lot of expansion to be truly useful.
 */
public final class CustomSecurityManager extends SecurityManager {

    @Override
    public void checkPropertyAccess(String key) {

    }

    public void checkConnect(String host, int port, Object context) {
    }

    public void checkConnect(String host, int port) {
    }

    public void checkPermission(Permission perm) {
        if (perm instanceof ReflectPermission) {
            ReflectPermission refl = (ReflectPermission) perm;
            if (refl.getName().equals("suppressAccessChecks")) {
                Class[] callers = getClassContext();
                int expectedBaseDepth = 4;
                if (callers.length >= expectedBaseDepth && callers[expectedBaseDepth].getCanonicalName() != null &&
                        callers[expectedBaseDepth].getCanonicalName().startsWith("org.vinsert.api.script.")) {
                    throw new SecurityException("vInsert is not allowed to use reflection!");
                }
            }
        }
    }

    public void checkPermission(Permission perm, Object context) {
    }

    public void checkCreateClassLoader() {
    }

    public void checkAccess(Thread t) {

    }

    public void checkAccess(ThreadGroup g) {

    }

    public void checkExit(int status) {

    }

    public void checkExec(String cmd) {

    }

    public void checkLink(String lib) {

    }

    public void checkRead(FileDescriptor fd) {

    }

    public void checkRead(String file) {

    }

    public void checkRead(String file, Object context) {

    }

    public void checkWrite(FileDescriptor fd) {

    }

    public void checkWrite(String file) {

    }

    public void checkDelete(String file) {

    }

    public void checkListen(int port) {

    }

    public void checkAccept(String host, int port) {

    }

    public void checkMulticast(InetAddress maddr) {

    }

    public void checkPropertiesAccess() {

    }

    public void checkPrintJobAccess() {

    }

    public void checkSystemClipboardAccess() {

    }

    public void checkAwtEventQueueAccess() {

    }

    public void checkPackageAccess(String pkg) {

    }

    public void checkPackageDefinition(String pkg) {

    }

    public void checkSetFactory() {

    }

    public void checkMemberAccess(Class<?> clazz, int which) {

    }

    public void checkSecurityAccess(String target) {
    }
}
