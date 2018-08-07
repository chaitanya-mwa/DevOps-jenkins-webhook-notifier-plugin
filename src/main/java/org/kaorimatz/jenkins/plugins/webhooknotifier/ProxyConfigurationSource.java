package org.kaorimatz.jenkins.plugins.webhooknotifier;

import hudson.ProxyConfiguration;
import jenkins.model.Jenkins;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.SystemDefaultCredentialsProvider;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;

import java.net.ProxySelector;

public enum ProxyConfigurationSource {

    @SuppressWarnings("unused")
    NONE {
        @Override
        public CredentialsProvider createCredentialsProvider(String host, Integer port, String username, String password) {
            return null;
        }

        @Override
        public HttpRoutePlanner createRoutePlanner(String host, Integer port) {
            return null;
        }
    },

    @SuppressWarnings("unused")
    SYSTEM {
        @Override
        public CredentialsProvider createCredentialsProvider(String host, Integer port, String username, String password) {
            return new SystemDefaultCredentialsProvider();
        }

        @Override
        public HttpRoutePlanner createRoutePlanner(String host, Integer port) {
            return new SystemDefaultRoutePlanner(ProxySelector.getDefault());
        }
    },

    @SuppressWarnings("unused")
    JENKINS {
        @Override
        public CredentialsProvider createCredentialsProvider(String host, Integer port, String username, String password) {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            ProxyConfiguration proxyConfiguration = Jenkins.getInstance().proxy;
            if (proxyConfiguration == null) {
                return null;
            }
            AuthScope authScope = new AuthScope(proxyConfiguration.name, proxyConfiguration.port);
            Credentials credentials = new UsernamePasswordCredentials(proxyConfiguration.getUserName(), proxyConfiguration.getPassword());
            credentialsProvider.setCredentials(authScope, credentials);
            return credentialsProvider;
        }

        @Override
        public HttpRoutePlanner createRoutePlanner(String host, Integer port) {
            ProxyConfiguration proxyConfiguration = Jenkins.getInstance().proxy;
            if (proxyConfiguration == null) {
                return null;
            }
            return new SystemDefaultRoutePlanner(new JenkinsProxySelector(proxyConfiguration));
        }
    },

    @SuppressWarnings("unused")
    MANUAL {
        @Override
        public CredentialsProvider createCredentialsProvider(String host, Integer port, String username, String password) {
            if (username == null) {
                return null;
            }
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            AuthScope authScope = new AuthScope(host, port);
            Credentials credentials = new UsernamePasswordCredentials(username, password);
            credentialsProvider.setCredentials(authScope, credentials);
            return credentialsProvider;
        }

        @Override
        public HttpRoutePlanner createRoutePlanner(String host, Integer port) {
            if (host == null || port == null) {
                return null;
            }
            return new DefaultProxyRoutePlanner(new HttpHost(host, port));
        }
    };

    public abstract CredentialsProvider createCredentialsProvider(String host, Integer port, String username, String password);

    public abstract HttpRoutePlanner createRoutePlanner(String host, Integer port);
}
