package ch.born.wte;

import javax.annotation.Generated;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;
@Embeddable
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
