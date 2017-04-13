package com.lvack.championggwrapper.data.champion;

import com.google.gson.annotations.SerializedName;
import lombok.Data;


@Data
public class DamageComposition {
	@SerializedName("trueDmg")
	private double trueDmg;
	@SerializedName("magicDmg")
	private double magicDmg;
	@SerializedName("physicalDmg")
	private double physicalDmg;
}
