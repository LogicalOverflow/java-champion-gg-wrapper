package com.lvack.championggwrapper.retrofit;

import com.lvack.championggwrapper.data.error.ErrorResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
public class APIResponse<R> implements Waitable {
	private transient final Object lock;
	@Getter @Setter private R content;
	@Getter @Setter private Throwable error;
	@Getter @Setter private ErrorResponse errorResponse;
	@Getter @Setter private int responseCode;
	@Getter private State state = State.IN_PROGRESS;

	public APIResponse() {
		lock = new Object();
	}

	protected APIResponse(Object lock) {
		this.lock = lock;
	}

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
		return getState() != State.IN_PROGRESS;
	}

	public boolean isSuccess() {
		return getState() == State.SUCCESS;
	}

	public boolean isAPIError() {
		return getState() == State.API_ERROR;
	}

	public boolean isFailure() {
		return getState() == State.FAILURE;
	}

	public boolean isInvalidAPIKey() {
		return getState() == State.INVALID_API_KEY;
	}

	public void setState(State state) {
		this.state = state;
		notifyWaitingClients();
	}

	public enum State {
		IN_PROGRESS, SUCCESS, API_ERROR, FAILURE, INVALID_API_KEY
	}

	public void notifyWaitingClients() {
		synchronized (lock) {
			lock.notifyAll();
		}
	}
}
