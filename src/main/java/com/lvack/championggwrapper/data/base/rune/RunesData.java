package com.lvack.championggwrapper.data.base.rune;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * RuneDataClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

@Data
public class RunesData {
	@SerializedName("id")
	private int id;
	@SerializedName("number")
	private int number;
	@SerializedName("name")
	private String name;
	@SerializedName("description")
	private String description;
}
