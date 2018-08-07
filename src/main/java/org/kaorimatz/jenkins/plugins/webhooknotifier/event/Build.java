package org.kaorimatz.jenkins.plugins.webhooknotifier.event;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import org.kohsuke.stapler.export.Exported;

public class Build<P extends AbstractProject<P, R>, R extends AbstractBuild<P, R>> extends Event {

    @Exported(merge = true)
    public final AbstractBuild<P, R> build;

    public Build(AbstractBuild<P, R> build) {
        this.build = build;
    }

    @SuppressWarnings("unused")
    @Exported
    public P getProject() {
        return build.getProject();
    }
}
