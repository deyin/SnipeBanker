package io.dylan.snipebanker.utils;

import android.text.Html;
import android.text.Spanned;

public final class HtmlUtils {

    public static Spanned showHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}
