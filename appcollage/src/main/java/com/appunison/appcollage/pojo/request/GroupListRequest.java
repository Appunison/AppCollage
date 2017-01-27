package com.appunison.appcollage.model.pojo.request;
/**
 * this model is used for
 * fetch list of group
 * @author appunison
 *
 */
public class GroupListRequest extends BaseRequest{
	
	private String id/*, page_no*/;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/*public String getPage_no() {
		return page_no;
	}

	public void setPage_no(String page_no) {
		this.page_no = page_no;
	}*/

}
