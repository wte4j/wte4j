package ch.born.wte.ui.auth.server;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ch.born.wte.ui.server.services.ServiceContext;

@RestController
@RequestMapping("/*")
public class AuthController {

	@Autowired
	private ServiceContext serviceContext;

	@RequestMapping(value="isLoggedIn",method = RequestMethod.GET, produces = "application/json")
	public boolean isLoggedIn(HttpServletRequest request)
			throws Exception {
		return (request.getRemoteUser() != null);
	}

	@RequestMapping(value="logout", method = RequestMethod.POST, produces = "application/json")
	public void logout(HttpServletRequest request)
			throws Exception {
		request.logout();
	}
}
