package com.lvack.championggwrapper.retrofit;

import com.google.gson.JsonSyntaxException;
import com.lvack.championggwrapper.data.error.ErrorResponse;
import okhttp3.ResponseBody;
import retrofit2.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public class APIResponseAdapterFactory extends CallAdapter.Factory {
	private APIResponseAdapterFactory() {}

	public static APIResponseAdapterFactory create() {
		return new APIResponseAdapterFactory();
	}

	public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
		if (getRawType(returnType) != APIResponse.class) return null;

		Converter<ResponseBody, ErrorResponse> converter = retrofit.
				responseBodyConverter(ErrorResponse.class, new Annotation[0]);

		if (!(returnType instanceof ParameterizedType))
			throw new IllegalStateException("APIResponse return type must be parameterized"
					+ " as APIResponse<Foo> or APIResponse<? extends Foo>");

		Type innerType = getParameterUpperBound(0, (ParameterizedType) returnType);

		if (getRawType(innerType) != Response.class) {
			return new APIResponseAdapter<>(converter, innerType);
		}

		if (!(innerType instanceof ParameterizedType)) {
			throw new IllegalStateException("Response must be parameterized"
					+ " as Response<Foo> or Response<? extends Foo>");
		}

		Type responseType = getParameterUpperBound(0, (ParameterizedType) innerType);
		return new APIResponseAdapter<>(converter, responseType);
	}

	private static final class APIResponseAdapter<R> implements CallAdapter<R, APIResponse<R>> {
		private final Converter<ResponseBody, ErrorResponse> converter;
		private final Type responseType;

		private APIResponseAdapter(Converter<ResponseBody, ErrorResponse> converter, Type responseType) {
			this.converter = converter;
			this.responseType = responseType;
		}

		public Type responseType() {
			return responseType;
		}

		public APIResponse<R> adapt(Call<R> call) {
			APIResponse<R> apiResponse = new APIResponse<>();
			call.enqueue(new Callback<R>() {
				@Override public void onResponse(Call<R> call, Response<R> response) {
					apiResponse.setResponseCode(response.code());
					if (response.isSuccessful()) {
						apiResponse.setContent(response.body());
						apiResponse.setState(APIResponse.State.SUCCESS);
					} else {
						ErrorResponse errorResponse;
						try {
							errorResponse = converter.convert(response.errorBody());
						} catch (Throwable t) {
							onFailure(call, t);
							return;
						}
						apiResponse.setErrorResponse(errorResponse);
						apiResponse.setState(APIResponse.State.API_ERROR);
					}
				}

				@Override public void onFailure(Call<R> call, Throwable t) {
					apiResponse.setError(t);
					if (JsonSyntaxException.class.isAssignableFrom(t.getClass()))
						apiResponse.setState(APIResponse.State.INVALID_API_KEY);
					else apiResponse.setState(APIResponse.State.FAILURE);
				}
			});
			return apiResponse;
		}
	}
}
