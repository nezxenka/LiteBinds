package org.nezxenka.litebinds.util;

import java.util.Locale;

public class NumberFormatter {

    public String format(int number) {
        return String.format(Locale.US, "%,d", number);
    }
}
