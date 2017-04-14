package com.lvack.championggwrapper.retrofit;

/**
 * WaitableClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

public interface Waitable {
	void waitForResponse();

	void notifyWaitingClients();
}
