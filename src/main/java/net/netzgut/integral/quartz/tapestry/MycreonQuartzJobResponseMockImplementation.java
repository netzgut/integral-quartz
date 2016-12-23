package net.netzgut.integral.quartz.tapestry;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.services.Response;

public final class MycreonQuartzJobResponseMockImplementation implements Response {

    @Override
    public void setStatus(int sc) {
        // noop
    }

    @Override
    public void setIntHeader(String name, int value) {
        // noop
    }

    @Override
    public void setHeader(String name, String value) {
        // noop
    }

    @Override
    public void setDateHeader(String name, long date) {
        // noop
    }

    @Override
    public void setContentLength(int length) {
        // noop
    }

    @Override
    public void sendRedirect(Link link) throws IOException {
        // noop
    }

    @Override
    public void sendRedirect(String URL) throws IOException {
        // noop
    }

    @Override
    public void sendError(int sc, String message) throws IOException {
        // noop
    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public PrintWriter getPrintWriter(String contentType) throws IOException {
        return null;
    }

    @Override
    public OutputStream getOutputStream(String contentType) throws IOException {
        return null;
    }

    @Override
    public String encodeURL(String URL) {
        return URL;
    }

    @Override
    public String encodeRedirectURL(String URL) {
        return null;
    }

    @Override
    public void disableCompression() {
        // noop
    }

    @Override
    public void addHeader(String name, String value) {
        // TODO Auto-generated method stub

    }
}
