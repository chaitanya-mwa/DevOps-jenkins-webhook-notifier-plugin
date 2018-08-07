package org.kaorimatz.jenkins.plugins.webhooknotifier;

import hudson.model.Describable;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

public class Webhook implements Describable<Webhook> {

    private String url;

    private Format format = Format.JSON;

    private Integer connectTimeout;

    private Integer socketTimeout;

    private ProxyConfigurationSource proxyConfigurationSource = ProxyConfigurationSource.NONE;

    private String proxyHost;

    private Integer proxyPort;

    private String proxyUsername;

    private String proxyPassword;

    @DataBoundConstructor
    public Webhook() {
    }

    @Override
    public Descriptor<Webhook> getDescriptor() {
        return Jenkins.getInstance().getDescriptorOrDie(Webhook.class);
    }

    public String getUrl() {
        return url;
    }

    @DataBoundSetter
    public void setUrl(String url) {
        this.url = url;
    }

    public Format getFormat() {
        return format;
    }

    @DataBoundSetter
    public void setFormat(Format format) {
        this.format = format;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    @DataBoundSetter
    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    @DataBoundSetter
    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public ProxyConfigurationSource getProxyConfigurationSource() {
        return proxyConfigurationSource;
    }

    @DataBoundSetter
    public void setProxyConfigurationSource(ProxyConfigurationSource proxyConfigurationSource) {
        this.proxyConfigurationSource = proxyConfigurationSource;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    @DataBoundSetter
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    @DataBoundSetter
    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    @DataBoundSetter
    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    @DataBoundSetter
    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }
}
