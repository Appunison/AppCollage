package com.appunison.appcollage.model.pojo.response;

import java.io.Serializable;
import java.util.List;
/**
 * this class get Response
 * list of gallery
 * @author appunison
 *
 */

public class GalleryResponse extends BaseResponse implements Serializable{

	private List<Gallery> result;

	public List<Gallery> getResult() {
		return result;
	}

	public void setResult(List<Gallery> result) {
		this.result = result;
	}
}
