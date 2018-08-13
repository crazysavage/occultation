package com.occultation.www.net.proxy;

import com.occultation.www.util.Assert;

import java.io.Serializable;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-08-13 15:10
 */
public class HttpProxy implements Cloneable, Serializable {

    private static final long serialVersionUID = -7529410654042457626L;

    public static final String DEFAULT_SCHEME_NAME = "http";

    protected final String hostname;

    protected final String lcHostname;

    protected final int port;

    protected final String schemeName;

    protected final Proxy proxy;


    static class Proxy {

        private final AtomicInteger failNum = new AtomicInteger(0);

        private final AtomicInteger reqNum = new AtomicInteger(0);

        private final AtomicInteger countFailNum = new AtomicInteger(0);

        public AtomicInteger getFailNum() {
            return failNum;
        }

        public AtomicInteger getReqNum() {
            return reqNum;
        }

        public AtomicInteger getCountFailNum() {
            return countFailNum;
        }
    }


    private HttpProxy(final String hostname, final int port, final String scheme) {
        super();
        Assert.notNull(hostname,"host name must not be null");
        this.hostname = hostname;
        this.lcHostname = hostname.toLowerCase(Locale.ROOT);
        if (scheme != null) {
            this.schemeName = scheme.toLowerCase(Locale.ROOT);
        } else {
            this.schemeName = DEFAULT_SCHEME_NAME;
        }
        this.port = port;
        this.proxy = new Proxy();
    }

    public static HttpProxy create(final String s) {
        Assert.notNull(s,"http host must not be null");
        String text = s;
        String scheme = null;
        final int schemeIdx = text.indexOf("://");
        if (schemeIdx > 0) {
            scheme = text.substring(0, schemeIdx);
            text = text.substring(schemeIdx + 3);
        }
        int port = -1;
        final int portIdx = text.lastIndexOf(":");
        if (portIdx > 0) {
            try {
                port = Integer.parseInt(text.substring(portIdx + 1));
            } catch (final NumberFormatException ex) {
                throw new IllegalArgumentException("Invalid HTTP host: " + text);
            }
            text = text.substring(0, portIdx);
        }
        return new HttpProxy(text, port, scheme);
    }

    public void fail() {
        this.proxy.getFailNum().addAndGet(1);
        this.proxy.getCountFailNum().addAndGet(1);
        this.proxy.getReqNum().addAndGet(1);
    }

    public void success() {
        this.proxy.getFailNum().getAndSet(0);
        this.proxy.getReqNum().addAndGet(1);
    }

    public String getHostName() {
        return this.hostname;
    }


    public int getPort() {
        return this.port;
    }


    public String getSchemeName() {
        return this.schemeName;
    }


    public String toURI() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(this.schemeName);
        buffer.append("://");
        buffer.append(this.hostname);
        if (this.port != -1) {
            buffer.append(':');
            buffer.append(Integer.toString(this.port));
        }
        return buffer.toString();
    }



    public String toHostString() {
        if (this.port != -1) {
            final StringBuilder buffer = new StringBuilder(this.hostname.length() + 6);
            buffer.append(this.hostname);
            buffer.append(":");
            buffer.append(Integer.toString(this.port));
            return buffer.toString();
        } else {
            return this.hostname;
        }
    }


    @Override
    public String toString() {
        return toURI();
    }


    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof HttpProxy) {
            final HttpProxy that = (HttpProxy) obj;
            return this.lcHostname.equals(that.lcHostname)
                    && this.port == that.port
                    && this.schemeName.equals(that.schemeName);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = lcHostname.hashCode();
        result = 31 * result + port;
        result = 31 * result + schemeName.hashCode();
        return result;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }



}
