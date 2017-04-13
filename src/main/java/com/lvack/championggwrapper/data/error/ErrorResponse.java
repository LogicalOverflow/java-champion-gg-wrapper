package com.lvack.championggwrapper.data.error;


public interface ErrorResponse {
	String getError();

	String getMessage();

	String getStack();
}
