package com.appunison.appcollage.model.pojo.response;

import java.io.Serializable;
import java.util.List;
/**
 * details of gallery
 * @author appunison
 *
 */

public class Gallery implements Serializable{

	private String group_id, group_name, time, collage_id, collage_image;
	private List<GalleryUser> images;
	public String getGroup_name() {
		return group_name;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getCollage_id() {
		return collage_id;
	}
	public void setCollage_id(String collage_id) {
		this.collage_id = collage_id;
	}
	public String getCollage_image() {
		return collage_image;
	}
	public void setCollage_image(String collage_image) {
		this.collage_image = collage_image;
	}
	public List<GalleryUser> getImages() {
		return images;
	}
	public void setImages(List<GalleryUser> images) {
		this.images = images;
	}
	
}
