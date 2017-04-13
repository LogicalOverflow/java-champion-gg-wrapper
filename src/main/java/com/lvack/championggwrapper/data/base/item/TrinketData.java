package com.lvack.championggwrapper.data.base.item;

import com.google.gson.annotations.SerializedName;
import com.lvack.championggwrapper.data.base.WinRateGameCount;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
public class TrinketData extends WinRateGameCount {
	@SerializedName("item")
	private ItemData item;
}
