package com.appunison.appcollage.model.pojo.response;

import java.io.Serializable;

public class GalleryUser implements Serializable{

	private String image, name;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
