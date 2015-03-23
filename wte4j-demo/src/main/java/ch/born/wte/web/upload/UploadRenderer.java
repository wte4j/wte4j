package ch.born.wte.web.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.el.ValueExpression;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;
import javax.faces.view.facelets.FaceletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileItem;

/**
 * Renderer for uploading files, see book 'Core Java Server Faces', David Geary
 * 
 */
@FacesRenderer(componentFamily = "javax.faces.Input", rendererType = "ch.born.wte.web.upload")
public final class UploadRenderer extends Renderer {

    private static final Logger LOGGER = Logger.getLogger(UploadRenderer.class);
    private FileItem uploadedFile;

    public UploadRenderer() {
        // default constructor, fulfill the jsf convention
    }

    @Override
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);

        writer.startElement("input", component);
        writer.writeAttribute("type", "file", "type");
        writer.writeAttribute("name", clientId, "clientId");
        writer.endElement("input");
        writer.flush();
    }

    @Override
    public void decode(final FacesContext context, final UIComponent component) {

        ExternalContext externalCtx = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalCtx.getRequest();
        String clientId = component.getClientId(context);
        FileItem fileItem = (FileItem) request.getAttribute(clientId);

        ValueExpression valueExpr = component.getValueExpression("value");

        Object newValue = null;
        if (null != valueExpr) {
            Class<?> valueType = valueExpr.getType(context.getELContext());
            if (valueType == byte[].class) {
                newValue = fileItem.get();
            } else if (valueType == InputStream.class) {
                try {
                    newValue = fileItem.getInputStream();

                } catch (IOException e) {
                    throw new FaceletException(e);
                }
            }

        } else {
            String encoding = request.getCharacterEncoding();
            if (null != encoding) {
                try {

                    newValue = fileItem.getString(encoding);
                } catch (UnsupportedEncodingException e) {
                    throw new FaceletException(e);
                }
            } else {
                newValue = fileItem.getString();
            }

        }

        EditableValueHolder editabelValueHolder = (EditableValueHolder) component;

        editabelValueHolder.setSubmittedValue(newValue);
        editabelValueHolder.setValid(true);

        uploadedFile = fileItem;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("stored uploaded file '" + fileItem.getName() + "' to upload renderer.");
        }

    }

    public FileItem getUploadedFile() {
        return uploadedFile;
    }

}
