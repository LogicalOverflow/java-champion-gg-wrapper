package com.lvack.championggwrapper.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.lvack.championggwrapper.data.error.AdvancedErrorResponse;
import com.lvack.championggwrapper.data.error.ErrorResponse;
import com.lvack.championggwrapper.data.error.NormalErrorResponse;

import java.lang.reflect.Type;

/**
 * ErrorResponseDeserializerClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

public class ErrorResponseDeserializer implements JsonDeserializer<ErrorResponse> {
	@Override
	public ErrorResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		try {
			return context.deserialize(json, NormalErrorResponse.class);
		} catch (JsonParseException normal) {
			try {
				return context.deserialize(json, AdvancedErrorResponse.class);
			} catch (JsonParseException advanced) {
				throw new BothAttemptsFailedException(normal, advanced);
			}
		}
	}

	private class BothAttemptsFailedException extends JsonParseException {

		private BothAttemptsFailedException(JsonParseException normal, JsonParseException advanced) {
			super("failed to deserialize error response as both " +
					"normal error response as well as advanced error response");

			addSuppressed(normal);
			addSuppressed(advanced);
		}


	}
}
