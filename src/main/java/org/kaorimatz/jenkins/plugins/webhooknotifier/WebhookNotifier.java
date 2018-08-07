package org.kaorimatz.jenkins.plugins.webhooknotifier;

import hudson.Launcher;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebhookNotifier extends Notifier {

    private static final Logger logger = Logger.getLogger(WebhookClient.class.getName());

    private final List<Webhook> webhooks;

    @DataBoundConstructor
    public WebhookNotifier(List<Webhook> webhooks) {
        this.webhooks = webhooks;
    }

    @Override
    public WebhookNotifierDescriptor getDescriptor() {
        return (WebhookNotifierDescriptor) super.getDescriptor();
    }

    @Override
    public boolean needsToRunAfterFinalized() {
        return true;
    }

    @Override
    public boolean prebuild(AbstractBuild<?, ?> build, BuildListener listener) {
        send(build);
        return true;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) {
        send(build);
        return true;
    }

    private void send(AbstractBuild<?, ?> build) {
        for (Webhook webhook : webhooks) {
            WebhookClient client = new WebhookClient(
                    webhook.getUrl(),
                    webhook.getFormat(),
                    webhook.getConnectTimeout(),
                    webhook.getSocketTimeout(),
                    webhook.getProxyConfigurationSource(),
                    Util.fixEmptyAndTrim(webhook.getProxyHost()),
                    webhook.getProxyPort(),
                    Util.fixEmptyAndTrim(webhook.getProxyUsername()),
                    Util.fixEmptyAndTrim(webhook.getProxyPassword()));

            try {
                client.send(build);
            } catch (IOException | WebhookHttpException e) {
                logger.log(Level.WARNING, "Error sending event to Webhook.", e);
            }
        }
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @SuppressWarnings("unused")
    public List<Webhook> getWebhooks() {
        return webhooks;
    }
}