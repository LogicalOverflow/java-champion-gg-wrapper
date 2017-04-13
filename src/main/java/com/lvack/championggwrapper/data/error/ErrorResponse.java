package com.lvack.championggwrapper.data.error;

/**
 * ErrorResponseClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

public interface ErrorResponse {
	String getError();
	String getMessage();
	String getStack();
}
