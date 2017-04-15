[![Build Status](https://img.shields.io/travis/LogicalOverflow/java-champion-gg-wrapper/master.svg?style=flat-square)](https://travis-ci.org/LogicalOverflow/java-champion-gg-wrapper)
[![Coveralls Coverage](https://img.shields.io/coveralls/LogicalOverflow/java-champion-gg-wrapper/master.svg?style=flat-square)](https://coveralls.io/github/LogicalOverflow/java-champion-gg-wrapper?branch=master)
[![Codacy Grade](https://img.shields.io/codacy/grade/d33215d6a3ca41d597ec28c3f06fbf88/master.svg?style=flat-square)](https://www.codacy.com/app/LogicalOverflow/java-champion-gg-wrapper/dashboard)
[![Codacy Coverage](https://img.shields.io/codacy/coverage/d33215d6a3ca41d597ec28c3f06fbf88/master.svg?style=flat-square)](https://www.codacy.com/app/LogicalOverflow/java-champion-gg-wrapper/files?filters=W3siaWQiOiJjb2x1bW5JbmRleCIsInZhbHVlcyI6WzVdfSx7ImlkIjoic29ydE9yZGVyIiwidmFsdWVzIjpbIkFzY2VuZGluZyJdfSx7ImlkIjoic2VhcmNoVGV4dCIsInZhbHVlcyI6W251bGxdfV0=)
[![GitHub Release Version](https://img.shields.io/github/release/LogicalOverflow/java-champion-gg-wrapper.svg?style=flat-square)](https://github.com/LogicalOverflow/java-champion-gg-wrapper/releases/latest)
[![Maven Version](https://img.shields.io/maven-central/v/com.lvack/java-champion-gg-wrapper.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/com.lvack/champion-gg-wrapper)
[![License](https://img.shields.io/github/license/LogicalOverflow/java-champion-gg-wrapper.svg?style=flat-square)](https://github.com/LogicalOverflow/java-champion-gg-wrapper/blob/master/LICENSE)

# java-champion-gg-wrapper
A simple Java wrapper for the ChampionGG API (http://api.champion.gg/docs/)

## Features
* Automatic rate limiting: All API calles can easily be rate limited. All API instances created with the same factory share one rate limiter. The number of requests per second can freely be set as the second parameter of the factory constructor. A value of 0 or a negative value disables the rate limiting. The default is no rate limiting.
* Asyncronous calls: Calls return immediately and other calculation can be done while waiting for the result (or more API calls can be started). The response provides a `waitForResponse` method to easily wait for an result once its required.
* Fully tested: Maybe not strictly a feature but all API methods get tested with mocked data (which was previously directly pulled from the API)
* Complete: Maybe also not strictly a feature but all API methods provided by the ChampionGG API are currently implemented.

## Usage
All you have to do is add the dependency to your maven project:
```xml
<dependencies>
    <dependency>
        <groupId>com.lvack</groupId>
        <artifactId>champion-gg-wrapper</artifactId>
        <version>{version}</version>
    </dependency>
</dependencies>
```

If you do not use maven, the jars can be found in the [releases](https://github.com/LogicalOverflow/java-champion-gg-wrapper/releases/latest). In case you want a jar without the included dependencies, you can download them from maven central ([latest version](https://search.maven.org/remote_content?g=com.lvack&a=champion-gg-wrapper&v=LATEST)).

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

