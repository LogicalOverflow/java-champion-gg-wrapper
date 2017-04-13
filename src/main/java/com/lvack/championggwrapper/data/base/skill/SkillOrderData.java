package com.lvack.championggwrapper.data.base.skill;

import com.google.gson.annotations.SerializedName;
import com.lvack.championggwrapper.data.base.WinRateGameCount;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;


@Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
public class SkillOrderData extends WinRateGameCount {
	@SerializedName("order")
	private List<String> order;
}
