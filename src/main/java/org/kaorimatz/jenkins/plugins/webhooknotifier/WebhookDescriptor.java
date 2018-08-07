package org.kaorimatz.jenkins.plugins.webhooknotifier;

import hudson.Extension;
import hudson.Util;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.QueryParameter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Extension
public class WebhookDescriptor extends Descriptor<Webhook> {

    @SuppressWarnings("unused")
    public WebhookDescriptor() {
        super(Webhook.class);
    }

    @SuppressWarnings("unused")
    public FormValidation doCheckUrl(@QueryParameter String value) {
        if (StringUtils.isBlank(value)) {
            return FormValidation.error("URL is required.");
        }

        try {
            URI uri = new URI(value);
            if (!"http".equals(uri.getScheme()) && !"https".equals(uri.getScheme())) {
                return FormValidation.error("scheme must be http or https.");
            }
            return FormValidation.ok();
        } catch (URISyntaxException e) {
            return FormValidation.error("URL is invalid.");
        }
    }

    @SuppressWarnings("unused")
    public FormValidation doTest(@QueryParameter String url,
                                 @QueryParameter Format format,
                                 @QueryParameter Integer connectTimeout,
                                 @QueryParameter Integer socketTimeout,
                                 @QueryParameter ProxyConfigurationSource proxyConfigurationSource,
                                 @QueryParameter String proxyHost,
                                 @QueryParameter Integer proxyPort,
                                 @QueryParameter String proxyUsername,
                                 @QueryParameter String proxyPassword) {

        WebhookClient client = new WebhookClient(
                url,
                format,
                connectTimeout,
                socketTimeout,
                proxyConfigurationSource,
                Util.fixEmptyAndTrim(proxyHost),
                proxyPort,
                Util.fixEmptyAndTrim(proxyUsername),
                Util.fixEmptyAndTrim(proxyPassword));

        try {
            client.ping();
        } catch (IOException | WebhookHttpException e) {
            return FormValidation.error(e, "Sending a ping event didn't succeed.");
        }
        return FormValidation.ok("Sending a ping event succeeded.");
    }
}
