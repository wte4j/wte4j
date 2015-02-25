package ch.born.wte.ui.shared;

import java.io.Serializable;

public class UserDto implements Serializable {

	private String userId;
	private String displayName;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
