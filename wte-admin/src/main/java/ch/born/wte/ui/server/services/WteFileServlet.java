package ch.born.wte.ui.server.services;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ch.born.wte.Template;
import ch.born.wte.TemplateRepository;

@MultipartConfig(fileSizeThreshold = 50 *1024, // 2MB
maxFileSize = 50 *1024, // 50KB
maxRequestSize = 52 * 1024)// 50KB
public class WteFileServlet extends HttpServlet {
    @Autowired
    private TemplateRepository repository;
    @Autowired
    private ServiceContext serviceContext;
    @Autowired
    private MessageFactory messageFactory;

    @Override
    public void init() throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String templateName = request.getParameter("name");
        String language = request.getParameter("language");
        Template<Object> template = repository.getTemplate(templateName, language);
        try (InputStream in = request.getPart("file").getInputStream()) {
            template.update(in, serviceContext.getUser());
        }catch (IllegalStateException e) {
            response.getWriter().print(messageFactory.createMessage(MessageKey.UPLOADED_FILE_NOT_VALID.getValue()));
        } 
        
        catch (Exception e) {
            response.getWriter().print(messageFactory.createMessage(MessageKey.INTERNAL_SERVER_ERROR.getValue()));
        }
    }
}
