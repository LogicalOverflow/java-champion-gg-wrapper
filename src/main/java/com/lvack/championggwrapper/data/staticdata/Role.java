package com.lvack.championggwrapper.data.staticdata;

import com.google.gson.annotations.SerializedName;

/**
 * RoleClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

public enum Role {
	@SerializedName("Top")TOP,
	@SerializedName("Jungle")JUNGLE,
	@SerializedName("Middle")MIDDLE,
	@SerializedName("ADC")ADC,
	@SerializedName("Support")SUPPORT,
	UNKNOWN;

	@Override public String toString() {
		try {
			SerializedName annotation = getClass().getField(name()).getAnnotation(SerializedName.class);
			if (annotation != null) return annotation.value();
		} catch (NoSuchFieldException ignored) {
		}
		return name();
	}
}
