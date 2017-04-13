package com.lvack.championggwrapper.retrofit;

import com.lvack.championggwrapper.data.error.ErrorResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
public class APIResponse<R> {
	private transient final Object lock = new Object();
	@Getter @Setter private R content;
	@Getter @Setter private Throwable error;
	@Getter @Setter private ErrorResponse errorResponse;
	@Getter @Setter private int responseCode;
	@Getter private State state = State.IN_PROGRESS;

	public void waitForResponse() {
		synchronized (lock) {
			while (!isComplete()) {
				try {
					lock.wait();
				} catch (InterruptedException ignored) {
				}
			}
		}
	}

	public boolean isComplete() {
		return state != State.IN_PROGRESS;
	}

	public boolean isSuccess() {
		return state == State.SUCCESS;
	}

	public boolean isAPIError() {
		return state == State.API_ERROR;
	}

	public boolean isFailure() {
		return state == State.FAILURE;
	}

	public boolean isInvalidAPIKey() {
		return state == State.INVALID_API_KEY;
	}

	void setState(State state) {
		this.state = state;
		synchronized (lock) {
			lock.notifyAll();
		}
	}

	public enum State {
		IN_PROGRESS, SUCCESS, API_ERROR, FAILURE, INVALID_API_KEY
	}
}
