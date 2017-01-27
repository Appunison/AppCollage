package com.appunison.appcollage.model.pojo.request;

public class AuthUser extends BaseRequest{
	
String password, user_id, id_type, time_zone, device_id, device_type;

public String getDevice_id() {
	return device_id;
}

public void setDevice_id(String device_id) {
	this.device_id = device_id;
}

public String getTime_zone() {
	return time_zone;
}

public void setTime_zone(String time_zone) {
	this.time_zone = time_zone;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

public String getUser_id() {
	return user_id;
}

public void setUser_id(String user_id) {
	this.user_id = user_id;
}

public String getId_type() {
	return id_type;
}

public void setId_type(String id_type) {
	this.id_type = id_type;
}

public String getDevice_type() {
	return device_type;
}

public void setDevice_type(String device_type) {
	this.device_type = device_type;
}



}
