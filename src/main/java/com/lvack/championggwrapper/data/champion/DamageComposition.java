package com.lvack.championggwrapper.data.champion;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * DamageCompositionClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

@Data
public class DamageComposition {
	@SerializedName("trueDmg")
	private double trueDmg;
	@SerializedName("magicDmg")
	private double magicDmg;
	@SerializedName("physicalDmg")
	private double physicalDmg;
}
