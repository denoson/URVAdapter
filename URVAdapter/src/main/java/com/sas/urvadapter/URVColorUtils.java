package com.sas.urvadapter;

import android.graphics.Color;

/**
 * Утилитарный класс для работы с цветом в Android.
 * Все методы статические, не требуют создания экземпляра.
 */
public class URVColorUtils {

    // Стандарты контрастности WCAG 2.1
    public static final double WCAG_AA_RATIO = 4.5;
    public static final double WCAG_AAA_RATIO = 7.0;
    public static final double WCAG_AA_LARGE_RATIO = 3.0;
    public static final double WCAG_AAA_LARGE_RATIO = 4.5;

    /**
     * Преобразует строку с HEX-представлением цвета в целочисленное значение ARGB.
     * Поддерживаются форматы: #RGB, #ARGB, #RRGGBB, #AARRGGBB (без # тоже допускается).
     * @param hex строка с цветом (например "#FF8800" или "FF8800")
     * @return цвет в формате 0xAARRGGBB
     * @throws IllegalArgumentException если строка некорректна
     */
    public static int hexToInt(String hex) {
        if (hex == null) throw new IllegalArgumentException("Hex string cannot be null");
        String clean = hex.trim();
        if (clean.startsWith("#")) clean = clean.substring(1);

        int len = clean.length();
        if (len == 3) { // RGB -> RRGGBB
            String r = clean.substring(0, 1);
            String g = clean.substring(1, 2);
            String b = clean.substring(2, 3);
            clean = r + r + g + g + b + b;
        } else if (len == 4) { // ARGB -> AARRGGBB
            String a = clean.substring(0, 1);
            String r = clean.substring(1, 2);
            String g = clean.substring(2, 3);
            String b = clean.substring(3, 4);
            clean = a + a + r + r + g + g + b + b;
        }

        if (clean.length() == 6) { // RRGGBB, альфа = FF
            return 0xFF000000 | Integer.parseInt(clean, 16);
        } else if (clean.length() == 8) { // AARRGGBB
            return (int) Long.parseLong(clean, 16);
        } else {
            throw new IllegalArgumentException("Invalid hex color: " + hex);
        }
    }

    /**
     * Преобразует целочисленный цвет в HEX-строку.
     * @param color цвет в формате 0xAARRGGBB
     * @param includeAlpha включать ли альфа-канал в строку
     * @return строка вида "#RRGGBB" или "#AARRGGBB"
     */
    public static String intToHex(int color, boolean includeAlpha) {
        return includeAlpha
                ? String.format("#%08X", color)
                : String.format("#%06X", color & 0xFFFFFF);
    }

    // ---------- Компоненты цвета ----------
    public static int alpha(int color) { return (color >> 24) & 0xFF; }
    public static int red(int color)   { return (color >> 16) & 0xFF; }
    public static int green(int color) { return (color >> 8) & 0xFF; }
    public static int blue(int color)  { return color & 0xFF; }

    /**
     * Собрать цвет из компонентов ARGB.
     */
    public static int argb(int alpha, int red, int green, int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    /**
     * Собрать непрозрачный цвет из RGB.
     */
    public static int rgb(int red, int green, int blue) {
        return argb(0xFF, red, green, blue);
    }

    // ---------- Изменение яркости ----------
    /**
     * Затемнить цвет умножением яркости (Value в HSV) на factor.
     * @param factor множитель (0..1). 1 – без изменений, меньше – темнее.
     */
    public static int darken(int color, float factor) {
        factor = Math.max(0, Math.min(1, factor));
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= factor;
        return Color.HSVToColor(alpha(color), hsv);
    }

    /**
     * Осветлить цвет увеличением яркости (Value) к 1.
     * @param factor степень осветления (0..1). 0 – без изменений, 1 – белый.
     */
    public static int brighten(int color, float factor) {
        factor = Math.max(0, Math.min(1, factor));
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = hsv[2] + (1 - hsv[2]) * factor;
        return Color.HSVToColor(alpha(color), hsv);
    }

    // ---------- Яркость и контрастность ----------
    /**
     * Вычисляет относительную яркость (luminance) по формуле WCAG 2.1.
     * Значение от 0 (чёрный) до 1 (белый).
     */
    public static double calculateLuminance(int color) {
        double r = linearize(red(color) / 255.0);
        double g = linearize(green(color) / 255.0);
        double b = linearize(blue(color) / 255.0);
        return 0.2126 * r + 0.7152 * g + 0.0722 * b;
    }

    private static double linearize(double channel) {
        return channel <= 0.03928
                ? channel / 12.92
                : Math.pow((channel + 0.055) / 1.055, 2.4);
    }

    /**
     * Упрощённая яркость для быстрой оценки (воспринимаемая яркость).
     */
    public static double getBrightness(int color) {
        return (0.299 * red(color) + 0.587 * green(color) + 0.114 * blue(color)) / 255.0;
    }

    /**
     * Светлый ли цвет (по упрощённой яркости).
     */
    public static boolean isLight(int color) {
        return getBrightness(color) > 0.5;
    }

    /**
     * Коэффициент контрастности между двумя цветами по WCAG.
     * Результат от 1 (одинаковые) до 21 (чёрный/белый).
     */
    public static double calculateContrastRatio(int color1, int color2) {
        double lum1 = calculateLuminance(color1);
        double lum2 = calculateLuminance(color2);
        double bright = Math.max(lum1, lum2);
        double dark = Math.min(lum1, lum2);
        return (bright + 0.05) / (dark + 0.05);
    }

    // ---------- Подбор цвета текста ----------
    /**
     * Возвращает чёрный или белый в зависимости от яркости фона.
     */
    public static int getContrastTextColor(int backgroundColor) {
        return isLight(backgroundColor) ? Color.BLACK : Color.WHITE;
    }

    /**
     * Выбирает цвет текста из двух вариантов, обеспечивающий максимальный контраст,
     * и при этом соответствующий WCAG AA (4.5:1), если это возможно.
     * @param backgroundColor цвет фона
     * @param lightText светлый вариант текста
     * @param darkText тёмный вариант текста
     * @return цвет текста
     */
    public static int getReadableTextColor(int backgroundColor, int lightText, int darkText) {
        double contrastLight = calculateContrastRatio(backgroundColor, lightText);
        double contrastDark = calculateContrastRatio(backgroundColor, darkText);
        boolean lightOk = contrastLight >= WCAG_AA_RATIO;
        boolean darkOk = contrastDark >= WCAG_AA_RATIO;

        if (lightOk && !darkOk) return lightText;
        if (darkOk && !lightOk) return darkText;
        // оба подходят или оба не подходят – выбираем с большим контрастом
        return contrastLight >= contrastDark ? lightText : darkText;
    }

    /**
     * Вариант с чёрным/белым по умолчанию.
     */
    public static int getReadableTextColor(int backgroundColor) {
        return getReadableTextColor(backgroundColor, Color.WHITE, Color.BLACK);
    }

    // ---------- Смешивание и альфа ----------
    /**
     * Смешивает два цвета с заданной пропорцией.
     * @param color1 первый цвет
     * @param color2 второй цвет
     * @param ratio доля второго цвета (0 = все color1, 1 = все color2)
     */
    public static int mixColors(int color1, int color2, float ratio) {
        ratio = Math.max(0, Math.min(1, ratio));
        int a = (int) (alpha(color1) * (1 - ratio) + alpha(color2) * ratio);
        int r = (int) (red(color1)   * (1 - ratio) + red(color2)   * ratio);
        int g = (int) (green(color1) * (1 - ratio) + green(color2) * ratio);
        int b = (int) (blue(color1)  * (1 - ratio) + blue(color2)  * ratio);
        return argb(a, r, g, b);
    }

    /**
     * Изменяет прозрачность (альфа-канал) цвета.
     * @param alpha значение от 0 до 255
     */
    public static int setAlpha(int color, int alpha) {
        alpha = Math.max(0, Math.min(255, alpha));
        return (color & 0x00FFFFFF) | (alpha << 24);
    }

    /**
     * Инвертирует цвет (RGB), альфа сохраняется.
     */
    public static int invert(int color) {
        return argb(alpha(color),
                0xFF - red(color),
                0xFF - green(color),
                0xFF - blue(color));
    }
}