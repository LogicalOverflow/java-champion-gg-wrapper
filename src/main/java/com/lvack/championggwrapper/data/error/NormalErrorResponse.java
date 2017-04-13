package com.lvack.championggwrapper.data.error;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * NormalErrorResponseClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

@Data
public class NormalErrorResponse implements ErrorResponse {
	@SerializedName("error")
	private String error;
	@SerializedName("message")
	private String message;
	@SerializedName("stack")
	private String stack;
}
