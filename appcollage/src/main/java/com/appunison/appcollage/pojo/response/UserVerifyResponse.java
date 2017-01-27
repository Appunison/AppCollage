package com.appunison.appcollage.model.pojo.response;
/**
 * 
 * @author appunison
 *
 */
public class UserVerifyResponse extends BaseResponse{
	private VerificationObject result;
	public VerificationObject getResult() {
		return result;
	}
	public void setResult(VerificationObject result) {
		this.result = result;
	}
	
	public class VerificationObject
	{
		
		private Boolean user_verification;

		public Boolean getUser_verification() {
			return user_verification;
		}

		public void setUser_verification(Boolean user_verification) {
			this.user_verification = user_verification;
		}
		
	}
}
