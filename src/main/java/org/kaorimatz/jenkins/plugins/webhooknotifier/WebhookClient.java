package org.kaorimatz.jenkins.plugins.webhooknotifier;

import hudson.PluginWrapper;
import hudson.model.AbstractBuild;
import jenkins.model.Jenkins;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.kaorimatz.jenkins.plugins.webhooknotifier.event.Build;
import org.kaorimatz.jenkins.plugins.webhooknotifier.event.Event;
import org.kaorimatz.jenkins.plugins.webhooknotifier.event.Ping;

import java.io.IOException;

public class WebhookClient {

    private final String url;

    private final Format format;

    private final Integer connectTimeout;

    private final Integer socketTimeout;

    private final ProxyConfigurationSource proxyConfigurationSource;

    private final String proxyHost;

    private final Integer proxyPort;

    private final String proxyUsername;

    private final String proxyPassword;

    public WebhookClient(String url,
                         Format format,
                         Integer connectTimeout,
                         Integer socketTimeout,
                         ProxyConfigurationSource proxyConfigurationSource,
                         String proxyHost,
                         Integer proxyPort,
                         String proxyUsername,
                         String proxyPassword) {

        this.url = url;
        this.format = format;
        this.connectTimeout = connectTimeout;
        this.socketTimeout = socketTimeout;
        this.proxyConfigurationSource = proxyConfigurationSource;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
    }

    public void send(AbstractBuild<?, ?> build) throws IOException, WebhookHttpException {
        execute(new Build<>(build));
    }

    public void ping() throws IOException, WebhookHttpException {
        execute(new Ping());
    }

    private void execute(Event event) throws IOException, WebhookHttpException {
        CloseableHttpClient client = HttpClients.custom()
                .setUserAgent(getUserAgent())
                .disableCookieManagement()
                .disableAutomaticRetries()
                .disableRedirectHandling()
                .setRoutePlanner(proxyConfigurationSource.createRoutePlanner(proxyHost, proxyPort))
                .build();

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(connectTimeout == null ? -1 : connectTimeout)
                .setSocketTimeout(socketTimeout == null ? -1 : socketTimeout)
                .build();

        HttpClientContext context = HttpClientContext.adapt(new BasicHttpContext());
        context.setRequestConfig(config);
        context.setCredentialsProvider(proxyConfigurationSource.createCredentialsProvider(proxyHost, proxyPort, proxyUsername, proxyPassword));

        HttpPost request = new HttpPost(url);
        request.setEntity(event.toEntity(format));
        request.setHeader("X-Jenkins-Webhook-Event", event.getName());

        try (CloseableHttpResponse response = client.execute(request, context)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode < 200 || statusCode > 299) {
                String body = EntityUtils.toString(response.getEntity());
                String message = String.format("Response status code is not 2xx. statusCode=%d, body=%s", statusCode, body);
                throw new WebhookHttpException(message);
            }
        }
    }

    private String getUserAgent() {
        PluginWrapper plugin = Jenkins.getInstance().getPlugin("webhook-notifier").getWrapper();
        return String.format("%s/%s (Jenkins %s)", plugin.getShortName(), plugin.getVersion(), Jenkins.getVersion());
    }
}
