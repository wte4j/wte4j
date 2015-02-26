package ch.born.wte.ui.server;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.born.wte.Template;
import ch.born.wte.TemplateRepository;
import ch.born.wte.User;
import ch.born.wte.ui.shared.TemplateDto;
import ch.born.wte.ui.shared.TemplateService;
import ch.born.wte.ui.shared.UserDto;

@Service
public class TemplateServiceImpl implements TemplateService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TemplateRepository templateRepository;

	@Override
	public List<TemplateDto> getTemplates() {
		List<Template<Object>> templates = templateRepository.queryTemplates()
				.list();
		List<TemplateDto> templateDtos = new ArrayList<TemplateDto>();
		for (Template<Object> template : templates) {
			TemplateDto templateDto = createTemplateDto(template);
			templateDtos.add(templateDto);
		}
		UserDto user=new UserDto();
		user.setDisplayName("editor");
		
		TemplateDto templateDto=new TemplateDto();
		templateDto.setDocumentName("Test");
		templateDto.setLanguage("de");
		templateDto.setEditor(user);
		templateDto.setUpdateAt(new Date());
		templateDtos.add(templateDto);	
		return templateDtos;
	}

	@Override
	public TemplateDto lockTemplate(TemplateDto templateDto) {
		Template<Object> template = lookup(templateDto);
		template = templateRepository.lockForEdit(template, new User("admin",
				"admin"));
		return createTemplateDto(template);
	}

	@Override
	public TemplateDto unlockTemplate(TemplateDto templateDto) {
		Template<Object> template = lookup(templateDto);
		template = templateRepository.unlock(template);
		return createTemplateDto(template);
	}

	@Override
	public void deleteTemplate(TemplateDto templateDto) {
		Template<Object> template = lookup(templateDto);
		templateRepository.delete(template);

	}

	Template lookup(TemplateDto dto) {
		return templateRepository.getTemplate(dto.getDocumentName(),
				dto.getLanguage());
	}

	TemplateDto createTemplateDto(Template<Object> template) {
		TemplateDto dto = new TemplateDto();
		dto.setDocumentName(template.getDocumentName());
		dto.setLanguage(template.getLanguage());
		dto.setUpdateAt(template.getEditedAt());
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

	private String encode(String value) {
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("error on encoding string", e);
		}
		return value;

	}

}