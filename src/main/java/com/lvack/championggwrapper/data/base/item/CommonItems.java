package com.lvack.championggwrapper.data.base.item;

import com.google.gson.annotations.SerializedName;
import com.lvack.championggwrapper.data.base.WinRateGameCountRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;


@Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
public class CommonItems extends WinRateGameCountRole {
	@SerializedName("items")
	private List<Integer> itemIds;
}
