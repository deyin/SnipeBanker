package io.dylan.snipebanker.parsers;

import android.support.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.dylan.snipebanker.models.Odds;

public class OddsParser {

    public  static List<Odds> parse(@NonNull String response, @NonNull String matchId) {

        Document doc = Jsoup.parse(response);

        return parse(doc, matchId);
    }

    private static List<Odds> parse(@NonNull Document doc, @NonNull String matchId) {
        List<Odds> oddsList = new ArrayList<>();

        Elements elements = doc.select("label.sxinput");

        for(Element element : elements) {
            oddsList.add(parse(element, matchId));
        }

        return oddsList;
    }

    private static Odds parse(Element label, @NonNull String matchId) {

        Odds odds = new Odds();

        odds.setMatchId(matchId);

        // providerId
        String providerId = label.selectFirst("input.numclass").attr("value");
        odds.setProviderId(providerId);

        // providerName
        odds.setProviderName(label.nextElementSibling().text());

        return odds;
    }
}
