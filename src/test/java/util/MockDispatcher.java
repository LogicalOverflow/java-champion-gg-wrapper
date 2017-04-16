package util;

import com.google.common.util.concurrent.Uninterruptibles;
import com.lvack.championggwrapper.data.staticdata.Role;
import com.lvack.championggwrapper.data.staticdata.RoleStatOrder;
import com.lvack.championggwrapper.data.staticdata.StatOrder;
import lombok.Getter;
import lombok.Setter;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Assert;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class MockDispatcher extends Dispatcher {
	@Getter private MockResponse lastResponse;
	@Setter private long delay = 0;
	@Getter private List<Long> callTimes = new ArrayList<>();

	@Override public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
		lastResponse = getResponse(request);
		if (lastResponse == null) Assert.fail("failed to open file " + getPath(request, getUrl(request)));
		return lastResponse;
	}

	private MockResponse getResponse(RecordedRequest request) {
		String path = request.getPath();
		if (!request.getPath().startsWith("/")) path = "/" + path;

		callTimes.add(System.currentTimeMillis());

		HttpUrl url = HttpUrl.parse("http://localhost" + path);

		String apiKey = url.queryParameter("api_key");
		if (!Objects.equals(Constants.API_KEY, apiKey)) {
			return new MockResponse().setBody("\n" +
				"<html>\n" +
				"<head><title>403 Forbidden</title></head>\n" +
				"<body bgcolor=\"white\">\n" +
				"<center><h1>403 Forbidden</h1></center>\n" +
				"<hr><center>openresty/1.9.3.2</center>\n" +
				"</body>\n" +
				"</html>\n" +
				"<!-- a padding to disable MSIE and Chrome friendly error page -->\n" +
				"<!-- a padding to disable MSIE and Chrome friendly error page -->\n" +
				"<!-- a padding to disable MSIE and Chrome friendly error page -->\n" +
				"<!-- a padding to disable MSIE and Chrome friendly error page -->\n" +
				"<!-- a padding to disable MSIE and Chrome friendly error page -->\n" +
				"<!-- a padding to disable MSIE and Chrome friendly error page -->\n")
				.setResponseCode(403);
		}

		path = getPath(request, url);
		InputStream jsonStream = getClass().getClassLoader().getResourceAsStream(path);

		if (jsonStream == null) {
			return null;
		}

		Uninterruptibles.sleepUninterruptibly(delay, TimeUnit.MILLISECONDS);

		return Constants.GSON.fromJson(new InputStreamReader(jsonStream), MockResponse.class);
	}

	private String getPath(RecordedRequest request, HttpUrl url) {
		HttpUrl targetUrl;
		if (isPaged(url)) {
			HttpUrl.Builder builder = url.newBuilder();
			if (url.queryParameter("page") == null) builder.addQueryParameter("page", "1");
			if (url.queryParameter("limit") == null) builder.addQueryParameter("limit", "10");
			targetUrl = builder.build();
		} else targetUrl = url;

		HttpUrl finalUrl = targetUrl;
		return "[" + request.getMethod() + "]" +
			finalUrl.pathSegments().stream().collect(Collectors.joining("-")) +
			finalUrl.queryParameterNames().stream().sorted()
				.filter(n -> !"api_key".equals(n))
				.map(n -> n + "=" + finalUrl.queryParameter(n))
				.collect(Collectors.joining(";", "[", "]")) + ".json";
	}

	private boolean isPaged(HttpUrl url) {
		String path = url.pathSegments().stream().collect(Collectors.joining("/"));
		for (StatOrder statOrder : StatOrder.values()) {
			if (Objects.equals("stats/champs/" + statOrder, path)) return true;
		}

		for (Role role : Role.values()) {
			if (Objects.equals("stats/role/" + role, path)) return true;
			for (RoleStatOrder roleStatOrder : RoleStatOrder.values()) {
				if (Objects.equals("stats/role/" + role + "/" + roleStatOrder, path)) return true;
			}
		}
		return false;
	}

	private HttpUrl getUrl(RecordedRequest request) {
		String path = request.getPath();
		if (!request.getPath().startsWith("/")) path = "/" + path;
		return HttpUrl.parse("http://localhost" + path);
	}
}
