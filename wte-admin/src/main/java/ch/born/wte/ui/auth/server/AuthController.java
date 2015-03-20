/**
 * Copyright (C) 2015 Born Informatik AG (www.born.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
