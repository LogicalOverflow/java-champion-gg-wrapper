package com.lvack.championggwrapper.data.base.skill;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * SkillNameClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

@Data
public class SkillName {
	@SerializedName("name")
	private String name;
}
