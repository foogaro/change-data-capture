package com.foogaro.cdc.infinispan.prometheus;

public class Util {

    public static String removeQuote (String label) {
        return label.replace("\"","");
    }
}
