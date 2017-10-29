package com.lonecpp.core.base;

import com.lonecpp.core.pb.ResponseProbuf.PbResponse;

/**
 * @author seven sins
 * @date 2017年10月29日 下午10:38:08
 */
public class BaseController {

	protected PbResponse.Builder response(Integer status, String message) {
		PbResponse.Builder pbResponse = PbResponse.newBuilder();
		pbResponse.setStatus(status);
		pbResponse.setMessage(message);
		
		return pbResponse;
	}
	
	protected PbResponse.Builder createResponse(Integer status) {
		PbResponse.Builder pbResponse = PbResponse.newBuilder();
		pbResponse.setStatus(status);
		
		return pbResponse;
	}

}
