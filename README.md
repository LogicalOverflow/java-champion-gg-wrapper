[![Build Status](https://travis-ci.org/LogicalOverflow/java-champion-gg-wrapper.svg?branch=master)](https://travis-ci.org/LogicalOverflow/java-champion-gg-wrapper)

# java-champion-gg-wrapper
A simple Java wrapper for the ChampionGG API (http://api.champion.gg/docs/)

## Features
* Automatic rate limiting: All API calles can easily be rate limited. All API instances created with the same factory share one rate limiter. The number of requests per second can freely be set as the second parameter of the factory constructor. A value of 0 or a negative value disables the rate limiting. The default is no rate limiting.
* Asyncronous calls: Calls return immediately and other calculation can be done while waiting for the result (or more API calls can be started). The response provides a `waitForResponse` method to easily wait for an result once its required.
* Fully tested: Maybe not strictly a feature but all API methods get tested with mocked data (which was previously directly pulled from the API)
* Complete: Maybe also not strictly a feature but all API methods provided by the ChampionGG API are currently implemented.

## Usage
First you need add it as a depencency to your pom. To do that add the repository:
```
<repositories>
	<repository>
		<id>java-champion-gg-wrapper-mvn-repo</id>
		<url>https://raw.github.com/LogicalOverflow/java-champion-gg-wrapper/mvn-repo/</url>
		<snapshots>
			<enabled>true</enabled>
			<updatePolicy>always</updatePolicy>
		</snapshots>
	</repository>
</repositories>
```
And the the depenency:
```
<dependencies>
	<dependency>
		<groupId>com.lvack.champion-gg-wrapper</groupId>
		<artifactId>champion-gg-wrapper</artifactId>
		<version>1.0</version>
	</dependency>
</dependencies>
```

If you do not use maven, the jars can be found in the `mvn-repo` branch. If you use these directly be aware that they do not contain libraries this project depends on. You might need to also add them:
* retrofit2 (version 2.2.0; with converter-gson and adapter-guava)
* gson (version 2.8.0)
* guava (version 21.0)
* commons-io (version 2.5)
* commons-lang3 (version 3.5)

Then the usage is pretty straight forward:
```java
ChampionGGAPIFactory factory = new ChampionGGAPIFactory("API_KEY", 10); // do at most 10 requests per second
ChampionGGAPI api = factory.buildChampionGGAPI();

APIResponse<List<HighLevelChampionData>> response = api.getHighLevelChampionData();
response.waitForResponse();

if (response.isSuccess()) {
	List<HighLevelChampionData> content = response.getContent();
	HighLevelChampionData data = content.get(0);

	System.out.println("Champion: " + data.getName());
	for (RoleData roleData : data.getRoles()) {
		System.out.println("- Position: " + roleData.getRole());
		System.out.println(String.format("  - Played %04.1f%% (%d games) of the time in this role",
			roleData.getPercentPlayed(), roleData.getGameCount()));
	}
} else {
	// something went wrong

	// the api returned an error
	if (response.isAPIError()) System.out.println(response.getErrorResponse());

	// an exception was thrown somewhere in the process
	if (response.isFailure()) response.getError().printStackTrace();

	if (response.isInvalidAPIKey()) System.out.println("Invalid API key!");
}
```

