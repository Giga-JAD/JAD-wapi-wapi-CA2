package com.Giga_JAD.Wapi_Wapi.model.blueprint;

public class User {
	private int recipient_id;
	private String Key;
	private boolean is_valid_role;
	private boolean is_verified;
//	private int is_admin;
//	private int is_blocked;

	public User(int recipient_id, String Key, boolean is_valid_role, boolean is_verified) {
		this.recipient_id = recipient_id;
		this.Key = Key;
		this.is_valid_role = is_valid_role;
		this.is_verified = is_verified;
//		this.is_admin = is_admin;
//		this.is_blocked = is_blocked;
	}

	public int getRecipient_id() {
		return recipient_id;
	}

	public void setRecipient_id(int recipient_id) {
		this.recipient_id = recipient_id;
	}

	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		Key = key;
	}

	public boolean isIs_valid_role() {
		return is_valid_role;
	}

	public void setIs_valid_role(boolean is_valid_role) {
		this.is_valid_role = is_valid_role;
	}

	public boolean isIs_verified() {
		return is_verified;
	}

	public void setIs_verified(boolean is_verified) {
		this.is_verified = is_verified;
	}

	public User() {
//		this.user_id = 1;
//		this.user_name = "John Doe";
//		this.is_admin = false;
//		this.is_blocked = false;
	}

//	public int getUserId() {
//		return user_id;
//	}
//
//	public void setUserId(int user_id) {
//		this.user_id = user_id;
//	}
//
//	public String getUsername() {
//		return user_name;
//	}
//
//	public void setUsername(String user_name) {
//		this.user_name = user_name;
//	}
//
//	public boolean isIsAdmin() {
//		return is_admin == 1;
//	}
//
//	public void setIsAdmin(int is_admin) {
//		this.is_admin = is_admin;
//	}
//
//	public boolean isIsBlocked() {
//		return is_blocked == 1;
//	}
//
//	public void setIsBlocked(int is_blocked) {
//		this.is_blocked = is_blocked;
//	}
}