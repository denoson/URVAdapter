package com.sas.urvadapter;

public class URVConst {

    public static final class Logic {

        public static final int UNKNOWN = 0;
        public static final int TITLE = 1;
        public static final int DESCR = 2;
        public static final int COUNTER_VALUE = 3;
        public static final int COUNTER_UNITS = 4;

        public static final int COL1_VALUE = 10;
        public static final int COL1_LABEL = 11;

        public static final int COL2_VALUE = 12;
        public static final int COL2_LABEL = 13;

        public static final int COL3_VALUE = 14;
        public static final int COL3_LABEL = 15;

        public static final int COL4_VALUE = 16;
        public static final int COL4_LABEL = 17;

        public static final int ICON_A = 18;
        public static final int ICON_B = 19;
        public static final int ICON_C = 20;

        public static final int BUTTON1_LABEL = 21;
        public static final int BUTTON2_LABEL = 22;
        public static final int BUTTON3_LABEL = 23;
    }


    public static final class ElementType {
        public static final int TEXT_LABEL = 0;
        public static final int TEXT_ICON = 1;
    }

    public static final class ValueType {

        public static final int STRING = 0;
        public static final int INTEGER = 1;
        public static final int FLOAT = 2;
        public static final int BOOLEAN = 3;
        public static final int COLOR = 4;

    }

    public static final class LabelAction {
        public static final int CUSTOM = 0;
        public static final int VALUE_INT = 1;
        public static final int VALUE_FLOAT = 2;
    }

    public static final class ClickerAction {
        public static final int CUSTOM = 0;
        public static final int VALUE_INCREASE = 1;
        public static final int VALUE_DECREASE = 2;
    }
}
