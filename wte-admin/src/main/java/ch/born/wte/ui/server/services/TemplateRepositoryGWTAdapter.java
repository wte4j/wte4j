package ch.born.wte.ui.server.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ch.born.wte.LockingException;
import ch.born.wte.Template;
import ch.born.wte.TemplateRepository;
import ch.born.wte.User;
import ch.born.wte.ui.shared.TemplateDto;
import ch.born.wte.ui.shared.TemplateService;
import ch.born.wte.ui.shared.TemplateServiceException;
import ch.born.wte.ui.shared.UserDto;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TemplateRepositoryGWTAdapter extends RemoteServiceServlet implements TemplateService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ServiceContext serviceContext;

	@Autowired
	private MessageFactory messageFactory;

	@Autowired
	private TemplateRepository templateRepository;

	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public List<TemplateDto> getTemplates() {
		List<Template<Object>> templates = templateRepository.queryTemplates()
				.list();
		List<TemplateDto> templateDtos = new ArrayList<TemplateDto>();
		for (Template<Object> template : templates) {
			TemplateDto templateDto = createTemplateDto(template);
			templateDtos.add(templateDto);
		}
		return templateDtos;
	}

	@Override
	public TemplateDto lockTemplate(TemplateDto templateDto) {
		Template<?> template = lookup(templateDto);
		try {
			template = templateRepository.lockForEdit(template, serviceContext.getCurrentUser());
			return createTemplateDto(template);
		} catch (LockingException e) {
			logger.debug("template {}_{} is locked by {}",
					template.getDocumentName(),
					template.getLanguage(),
					template.getLockingUser().getDisplayName(),
					e);
			throw createServiceException(MessageKeys.LOCKED_TEMPLATE);
		}
	}

	@Override
	public TemplateDto unlockTemplate(TemplateDto templateDto) {
		Template<?> template = lookup(templateDto);
		template = templateRepository.unlock(template);
		return createTemplateDto(template);
	}

	@Override
	public void deleteTemplate(TemplateDto templateDto) {
		Template<?> template = lookup(templateDto);
		try {
			templateRepository.delete(template);

		} catch (LockingException e) {
			logger.debug("template {}_{} is locked by {}",
					template.getDocumentName(),
					template.getLanguage(),
					template.getLockingUser().getDisplayName(),
					e);
			throw createServiceException(MessageKeys.LOCKED_TEMPLATE);
		}

	}

	Template<?> lookup(TemplateDto dto) {
		Template<?> template = templateRepository.getTemplate(dto.getDocumentName(), dto.getLanguage());
		if (template == null) {
			throw createServiceException(MessageKeys.TEMPLATE_NOT_FOUND);
		}
		return template;
	}

	TemplateDto createTemplateDto(Template<?> template) {
		TemplateDto dto = new TemplateDto();
		dto.setDocumentName(template.getDocumentName());
		dto.setLanguage(template.getLanguage());

		if (template.getEditedAt() != null) {
			dto.setUpdateAt(new Date(template.getEditedAt().getTime()));
		}

		dto.setEditor(createUserDto(template.getEditor()));
		if (template.getLockingUser() != null) {
			dto.setLockingUser(createUserDto(template.getLockingUser()));
		}

		return dto;
	}

	UserDto createUserDto(User user) {
		UserDto dto = new UserDto();
		dto.setUserId(user.getUserId());
		dto.setDisplayName(user.getDisplayName());
		return dto;
	}

	TemplateServiceException createServiceException(String key) {
		String message = messageFactory.createMessage(key);
		return new TemplateServiceException(message);
	}

}