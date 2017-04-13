package com.lvack.championggwrapper.data.error;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * AdvancedAPIErrorClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

@Data
public class AdvancedErrorResponse implements ErrorResponse {
	@SerializedName("error")
	private ErrorResponse errorResponse;

	@Override public String getError() {
		return errorResponse.getError();
	}

	@Override public String getMessage() {
		return errorResponse.getMessage();
	}

	@Override public String getStack() {
		return errorResponse.getStack();
	}
}
