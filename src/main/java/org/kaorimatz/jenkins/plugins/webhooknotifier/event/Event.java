package org.kaorimatz.jenkins.plugins.webhooknotifier.event;

import com.google.common.base.CaseFormat;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.kaorimatz.jenkins.plugins.webhooknotifier.Format;
import org.kohsuke.stapler.export.ClassAttributeBehaviour;
import org.kohsuke.stapler.export.DataWriter;
import org.kohsuke.stapler.export.ExportConfig;
import org.kohsuke.stapler.export.ExportedBean;
import org.kohsuke.stapler.export.Model;
import org.kohsuke.stapler.export.ModelBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

@ExportedBean
public abstract class Event {

    public HttpEntity toEntity(Format format) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        ExportConfig config = new ExportConfig().withClassAttribute(ClassAttributeBehaviour.NONE);
        DataWriter dataWriter = format.flavor.createDataWriter(this, writer, config);
        Model model = new ModelBuilder().get(getClass());
        model.writeTo(this, dataWriter);
        writer.flush();
        return new ByteArrayEntity(outputStream.toByteArray(), format.contentType);
    }

    public String getName() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, getClass().getSimpleName());
    }
}
