package com.lvack.championggwrapper.data.base.rune;

import com.lvack.championggwrapper.data.base.MostGamesHighestWinPair;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * RuneSetDataClass for champion-gg-wrapper
 *
 * @author Leon Vack - TWENTY |20
 */

@Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
public class RuneSetData extends MostGamesHighestWinPair<RuneSet> {
}
