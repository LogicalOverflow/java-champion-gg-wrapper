package com.lvack.championggwrapper.data.base.item;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * ItemDataClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

@Data
public class ItemData {
	@SerializedName("id")
	private int id;
	@SerializedName("name")
	private String name;
}
