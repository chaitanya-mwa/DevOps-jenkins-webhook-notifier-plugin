package org.kaorimatz.jenkins.plugins.webhooknotifier;

import hudson.ProxyConfiguration;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.List;

public class JenkinsProxySelector extends ProxySelector {

    private final ProxyConfiguration proxyConfiguration;

    public JenkinsProxySelector(ProxyConfiguration proxyConfiguration) {
        this.proxyConfiguration = proxyConfiguration;
    }

    @Override
    public List<Proxy> select(URI uri) {
        return Collections.singletonList(proxyConfiguration.createProxy(uri.getHost()));
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
    }
}
