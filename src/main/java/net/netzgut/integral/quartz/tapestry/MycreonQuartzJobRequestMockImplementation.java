package net.netzgut.integral.quartz.tapestry;

import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;

public final class MycreonQuartzJobRequestMockImplementation implements Request {

    @Override
    public void setAttribute(String name, Object value) {
        // noop
    }

    @Override
    public boolean isXHR() {
        return false;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public Session getSession(boolean create) {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public String[] getParameters(String name) {
        return null;
    }

    @Override
    public List<String> getParameterNames() {
        return null;
    }

    @Override
    public String getParameter(String name) {
        return null;
    }

    @Override
    public String getMethod() {
        return null;
    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public List<String> getHeaderNames() {
        return null;
    }

    @Override
    public String getHeader(String name) {
        return null;
    }

    @Override
    public long getDateHeader(String name) {
        return 0;
    }

    @Override
    public String getContextPath() {
        // HACK: we return an empty string here and later prepend the MYCREON_CORE_BASEURL
        // which includes the context
        return "";
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public boolean isSessionInvalidated() {
        return false;
    }

    @Override
    public List<String> getAttributeNames() {
        return null;
    }
}
