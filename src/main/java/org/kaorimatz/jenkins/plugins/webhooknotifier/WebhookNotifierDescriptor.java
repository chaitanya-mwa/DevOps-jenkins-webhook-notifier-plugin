package org.kaorimatz.jenkins.plugins.webhooknotifier;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;

import javax.annotation.Nonnull;

@Extension
public class WebhookNotifierDescriptor extends BuildStepDescriptor<Publisher> {

    public WebhookNotifierDescriptor() {
        super(WebhookNotifier.class);
    }

    @Override
    public boolean isApplicable(Class<? extends AbstractProject> jobType) {
        return true;
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Send build events to webhooks";
    }
}
