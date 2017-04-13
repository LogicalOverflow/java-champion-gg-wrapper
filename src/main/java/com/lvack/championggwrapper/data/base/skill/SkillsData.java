package com.lvack.championggwrapper.data.base.skill;

import com.google.gson.annotations.SerializedName;
import com.lvack.championggwrapper.data.base.MostGamesHighestWinPair;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * SkillsDataClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

@Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
public class SkillsData extends MostGamesHighestWinPair<SkillOrderData> {
	@SerializedName("skillInfo")
	private List<SkillInfo> skillsInfo;
}
