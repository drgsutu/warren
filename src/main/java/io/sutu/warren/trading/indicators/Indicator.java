package io.sutu.warren.trading.indicators;

import eu.verdelhan.ta4j.Rule;

public interface Indicator {

    Rule getEntryRule();

    Rule getExitRule();
}
