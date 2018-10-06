package io.dylan.snipebanker.parsers;

import android.support.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.dylan.snipebanker.models.Odds;
import io.dylan.snipebanker.persist.converters.DateConverter;
import io.dylan.snipebanker.utils.StringUtils;

public class OddsChangeParser {

    public static List<Odds.OddsChange> parse(@NonNull String response, @NonNull String providerId) {

        Document doc = Jsoup.parse(response);

        return parse(doc, providerId);
    }

    private static List<Odds.OddsChange> parse(@NonNull Document doc, @NonNull String providerId) {

        List<Odds.OddsChange> oddsList = new ArrayList<>();

        Element tbody = doc.selectFirst("div.wrap > table > tbody");

        Elements trs = tbody.select("tr");

        Iterator<Element> iterator = trs.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Element next = iterator.next();
            if (++i <= 2) {
                continue; // skip the first 2 rows
            }
            Elements elements = next.select("td");
            if (elements.size() < 12) {
                continue;
            }
            oddsList.add(parse(elements, providerId));
        }

        return oddsList;
    }

    private static Odds.OddsChange parse(@NonNull Elements elements, @NonNull String providerId) {

        Odds.OddsChange odds = new Odds.OddsChange();

        odds.setProviderId(providerId);

        int i = 0;
        String text;

        // time
        text = elements.get(i).text();
        odds.setTime(DateConverter.dateFromslashYmdHmString(text));

        // hoursBefore
        ++i;
        text = elements.get(i).text();
        odds.setHoursBeforeMatch(text);

        // win
        ++i;
        text = elements.get(i).text();
        odds.setOddsOfWin(StringUtils.parseToDouble(escape(text)));

        // draw
        ++i;
        text = elements.get(i).text();
        odds.setOddsOfDraw(StringUtils.parseToDouble(escape(text)));


        // lose
        ++i;
        text = elements.get(i).text();
        odds.setOddsOfLose(StringUtils.parseToDouble(escape(text)));

        // probability of win
        ++i;
        text = elements.get(i).text();
        odds.setProbabilityOfWin(StringUtils.parseToDouble(escape(text)));

        // probability of win
        ++i;
        text = elements.get(i).text();
        odds.setProbabilityOfDraw(StringUtils.parseToDouble(escape(text)));


        // probability of win
        ++i;
        text = elements.get(i).text();
        odds.setProbabilityOfLose(StringUtils.parseToDouble(escape(text)));


        // kelly of win
        ++i;
        text = elements.get(i).text();
        odds.setKellyOfWin(StringUtils.parseToDouble(escape(text)));

        // kelly of win
        ++i;
        text = elements.get(i).text();
        odds.setKellyOfDraw(StringUtils.parseToDouble(escape(text)));


        // kelly of win
        ++i;
        text = elements.get(i).text();
        odds.setKellyOfLose(StringUtils.parseToDouble(escape(text)));

        // lossRatio
        ++i;
        text = elements.get(i).text();
        odds.setLossRatio(StringUtils.parseToDouble(escape(text)));

        return odds;
    }

    private static String escape(@NonNull final String text) {
        return text.replace("↓", "").replace("↑", "");
    }
}
