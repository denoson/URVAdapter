package com.sas.urvadapter;

public class URVNumberDialogConfig {

    public float defaultValue = 0;

    public float minValue = 0;
    public float maxValue = 0;

    public boolean useNegative = true;
    public boolean useFloatNumber = true;

    public int colorBackground = 0xFF101010;
    public int colorDigits = 0xFF444444;
    public int colorOperation = 0xFF666666;
    public int colorFunction = 0xFFf09c00;
    public int colorOk = 0xFF145925;
    public int colorCancel = 0xFF242424;
    public int colorButtonText = 0xFFFFFFFF;

    public String textTitle = "Input a number";
    public String textValidateError = "The number must be between %.2f and %.2f";
    public String textUnknownFormat = "Invalid number format";

}
