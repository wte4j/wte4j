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
package org.wte4j;

import javax.annotation.Generated;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.StringUtils;

@Embeddable
/**
 * A User has a id and a display name. The id is used to identify the user an must be unique
 */
public class User {

	private String userId;

	private String displayName;

	public User() {
	}

	public User(String userName, String displayName) {
		this.userId = userName;
		if (StringUtils.isEmpty(displayName)) {
			this.displayName = userName;
		} else {
			this.displayName = displayName;
		}
	}

	public String getUserId() {
		return userId;
	}

	public String getDisplayName() {
		return displayName;
	}

	void setUserId(String userName) {
		this.userId = userName;
	}

	void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	@Generated("eclipse")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	@Generated("eclipse")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
