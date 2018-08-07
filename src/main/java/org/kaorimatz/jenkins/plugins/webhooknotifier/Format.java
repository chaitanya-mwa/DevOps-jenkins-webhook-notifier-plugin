package org.kaorimatz.jenkins.plugins.webhooknotifier;

import org.apache.http.entity.ContentType;
import org.kohsuke.stapler.export.Flavor;

public enum Format {

    @SuppressWarnings("unused")
    JSON(ContentType.APPLICATION_JSON, Flavor.JSON),

    @SuppressWarnings("unused")
    XML(ContentType.APPLICATION_XML, Flavor.XML);

    public final ContentType contentType;

    public final Flavor flavor;

    Format(ContentType contentType, Flavor flavor) {
        this.contentType = contentType;
        this.flavor = flavor;
    }

    @SuppressWarnings("unused")
    public String getMimeType() {
        return contentType.getMimeType();
    }
}
