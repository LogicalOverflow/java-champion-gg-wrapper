package com.lvack.championggwrapper.gson;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lvack.championggwrapper.data.error.ErrorResponse;
import com.lvack.championggwrapper.data.staticdata.ChampionRoleStat;
import com.lvack.championggwrapper.data.staticdata.ChampionStat;
import com.lvack.championggwrapper.data.staticdata.Role;
import com.lvack.championggwrapper.data.staticdata.RoleStat;

import java.util.Arrays;
import java.util.List;


public class GsonProvider {
	private static final List<Class<? extends Enum>> ENUMS = Arrays.asList(Role.class,
		ChampionRoleStat.class, ChampionStat.class, RoleStat.class);
	private static final Gson GSON = getGsonBuilder().create();

	@VisibleForTesting
	public GsonProvider() {
		throw new UnsupportedOperationException("gson provider can not be instantiated");
	}

	public static Gson getGson() {
		return GSON;
	}

	public static GsonBuilder getGsonBuilder() {
		GsonBuilder gsonBuilder = new GsonBuilder().enableComplexMapKeySerialization()
			.registerTypeAdapter(ErrorResponse.class, new ErrorResponseDeserializer());
		for (Class<? extends Enum> anEnum : ENUMS)
			gsonBuilder.registerTypeAdapter(anEnum, new EnumDeSerializer<>(anEnum));
		return gsonBuilder;
	}
}
