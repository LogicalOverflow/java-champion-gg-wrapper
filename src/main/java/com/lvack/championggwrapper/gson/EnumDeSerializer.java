package com.lvack.championggwrapper.gson;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * RoleDeserializerClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

public class EnumDeSerializer<T extends Enum> implements JsonDeserializer<T>, JsonSerializer<T> {
	private final Class<T> enumClass;

	public EnumDeSerializer(Class<T> enumClass) {
		this.enumClass = enumClass;
	}

	public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		if (json.isJsonNull()) return null;
		if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
			String tName = json.getAsString();
			for (T t : enumClass.getEnumConstants()) {
				if (Objects.equals(t.name(), tName)) return t;
				SerializedName serializedName = getSerializedName(t);
				if (serializedName != null) {
					if (Objects.equals(serializedName.value(), tName)) return t;
					for (String alt : serializedName.alternate()) if (Objects.equals(alt, tName)) return t;
				}
			}
			throw new JsonParseException("invalid " + enumClass.getSimpleName() + ": " + tName);
		}
		throw new JsonParseException("expected JsonNull or JsonPrimitive for string, got " + json);
	}

	@Override public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
		if (src == null) return JsonNull.INSTANCE;

		SerializedName annotation = getSerializedName(src);
		return new JsonPrimitive(annotation == null ? src.name() : annotation.value());
	}

	private SerializedName getSerializedName(T t) {
		try {
			return t.getClass().getField(t.name()).getAnnotation(SerializedName.class);
		} catch (NoSuchFieldException e) {
			return null;
		}
	}
}
