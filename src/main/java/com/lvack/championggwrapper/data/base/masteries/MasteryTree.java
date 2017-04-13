package com.lvack.championggwrapper.data.base.masteries;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;


@Data
public class MasteryTree {
	@SerializedName("tree")
	private String treeName;
	@SerializedName("total")
	private int pointsSpent;
	@SerializedName("data")
	private List<MasteryData> data;
}
