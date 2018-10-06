package io.dylan.snipebanker.parsers;

import android.support.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.dylan.snipebanker.models.Match;
import io.dylan.snipebanker.models.Odds;
import io.dylan.snipebanker.models.Result;
import io.dylan.snipebanker.models.Status;
import io.dylan.snipebanker.persist.converters.DateConverter;
import io.dylan.snipebanker.utils.StringUtils;

public class MatchParser {

    private static final Pattern DATE_PATTERN = Pattern.compile("星期[一二三四五六日]\\s*(.*)");
    private static final Pattern MATCH_URL_PATTERN = Pattern.compile("http://www.okooo.com/soccer/match/(.*)/trends/");
    private static final Pattern LEAGUE_URL_PATTERN = Pattern.compile("http://www.okooo.com/soccer/league/(.*)/");

    public static List<Match> parse(String response) {

        List<Match> matchList = new ArrayList<>();

        Document doc = Jsoup.parse(response);
        Elements tables = doc.select("table.jcmaintable");
        for (Element table : tables) {
            matchList.addAll(parse(table));
        }

        return matchList;
    }

    private static List<Match> parse(@NonNull Element table) {

        Element tbody = table.child(0);
        Element tr = tbody.child(0);
        Date date = parseDate(tr);
        return parseDateMatchList(tr, date);
    }

    @NonNull
    private static Date parseDate(@NonNull Element tr) {
        Element td = tr.child(0);
        String strDate = td.select("div > span > span").iterator().next().text();
        Matcher matcher = DATE_PATTERN.matcher(strDate);
        if (matcher.find()) {
            String dateGroup = matcher.group(1);
            return DateConverter.dateFromYmdString(dateGroup);
        }
        throw new NoSuchElementException("date not found in elements");
    }

    private static List<Match> parseDateMatchList(@NonNull Element tr, @NonNull final Date date) {

        List<Match> matchList = new ArrayList<>();

        Elements siblingElements = tr.siblingElements();
        for (Element siblingElement : siblingElements) {
            Match match = parseMatch(siblingElement, date);
            matchList.add(match);
        }

        return matchList;
    }

    private static Match parseMatch(@NonNull Element tr, @NonNull final Date date) {

        Match match = new Match();
        match.setMatchDate(date);

        {
            // matchNo
            Element td = tr.selectFirst("td.tdsx");
            String matchNo = td.selectFirst("span.xh").text();
            match.setMatchNo(matchNo);

            // league
            Match.League league = parseLeague(td);
            match.setLeague(league);
        }

        {
            // matchTime
            Element td = tr.selectFirst("td.switchtime");
            String matchTime = td.attr("title").split("：")[1];
            match.setMatchTime(DateConverter.dateFromYmdHmsString(matchTime));
        }

        {
            // home &away
            parseIdVs(tr, match);
        }

        // odds
        {

            Element td = tr.selectFirst("td.zqmixztbox");

            parseOddsOfSporttery(td, match);

            parseHandicapOddsOfSporttery(td, match);
        }

        return match;

    }

    private static void parseHandicapOddsOfSporttery(@NonNull Element td, @NonNull Match match) {
        Element handicapOddsOfSportteryElement = td.selectFirst("div.rqBetObj");
        Odds.OddsChange handicapOddsOfSporttery = new Odds.OddsChange();
        handicapOddsOfSporttery.setTime(new Date());

        int handicap = StringUtils.parseToInt(handicapOddsOfSportteryElement.selectFirst("div.handicapObj").text());
        match.setHandicaps(handicap);

        Iterator<Element> iterator = handicapOddsOfSportteryElement.select("div.mixspf > a.rqbetObj").iterator();
        if (iterator.hasNext()) {
            Element next = iterator.next();
            handicapOddsOfSporttery.setOddsOfWin(Double.valueOf(next.text()));
            if ("1".equals(next.attr("isresult"))) {
                handicapOddsOfSporttery.setActualResult(Result.HANDICAP_WIN);
            }
            next = next.nextElementSibling();
            handicapOddsOfSporttery.setOddsOfDraw(Double.valueOf(next.text()));
            if ("1".equals(next.attr("isresult"))) {
                handicapOddsOfSporttery.setActualResult(Result.HANDICAP_DRAW);
            }
            next = next.nextElementSibling();
            handicapOddsOfSporttery.setOddsOfLose(Double.valueOf(next.text()));
            if ("1".equals(next.attr("isresult"))) {
                handicapOddsOfSporttery.setActualResult(Result.HANDICAP_LOSE);
            }
        }
        match.setHandicapOddsOfSporttery(handicapOddsOfSporttery);
    }

    private static void parseOddsOfSporttery(@NonNull Element td, Match match) {
        Odds.OddsChange oddsOfSporttery = new Odds.OddsChange();
        oddsOfSporttery.setTime(new Date());
        Element oddsOfSportteryElement = td.selectFirst("div.frqBetObj");
        Iterator<Element> iterator = oddsOfSportteryElement.select("a.betObj").iterator();
        if (iterator.hasNext()) {
            Element next = iterator.next();
            oddsOfSporttery.setOddsOfWin(Double.valueOf(next.text()));
            if ("1".equals(next.attr("isresult"))) {
                oddsOfSporttery.setActualResult(Result.WIN);
            }
            next = next.nextElementSibling();
            oddsOfSporttery.setOddsOfDraw(Double.valueOf(next.text()));
            if ("1".equals(next.attr("isresult"))) {
                oddsOfSporttery.setActualResult(Result.DRAW);
            }
            next = next.nextElementSibling();
            oddsOfSporttery.setOddsOfLose(Double.valueOf(next.text()));
            if ("1".equals(next.attr("isresult"))) {
                oddsOfSporttery.setActualResult(Result.LOSE);
            }
        }
        match.setOddsOfSporttery(oddsOfSporttery);
    }

    private static void parseIdVs(@NonNull Element tr, Match match) {
        Match.Team home = new Match.Team();

        Element td = tr.selectFirst("td.huihname");

        // home ranking &leagueName
        Element em = td.selectFirst("em");
        if (em.childNodeSize() > 0) {
            String strHomeRanking = em.child(0).text();
            if (strHomeRanking != null && !strHomeRanking.isEmpty()) {
                strHomeRanking = strHomeRanking.replace("[", "").replace("]", "");
                int ranking = StringUtils.parseToInt(strHomeRanking);
                home.setRanking(ranking);
            }
        }
        if (em.childNodeSize() > 1) {
            String strHomeLeague = em.child(1).text();
            if (strHomeLeague != null) {
                home.setLeague(strHomeLeague);
            }
        }

        // homeName
        Element homeElement = td.selectFirst("a.tar");
        String homeName = homeElement.text();

        String teamUrl = homeElement.attr("href"); // http://www.okooo.com/soccer/match/1044895/trends/
        Matcher matcher = MATCH_URL_PATTERN.matcher(teamUrl);
        if (matcher.find()) {
            match.setId(matcher.group(1));
        }

        home.setName(homeName);
        match.setHome(home);

        // VS|score
        Element vsOrScoreElement = td.selectFirst("b.bftext");
        if (vsOrScoreElement != null) {
            String vsOrScore = vsOrScoreElement.text();
            if (vsOrScore != null && !vsOrScore.equals("VS")) {
                match.setStatus(Status.FINISHED);
                match.setFinishedScore(vsOrScore);
            }
        }

        Match.Team away = new Match.Team();

        // awayName
        Element awayElement = td.selectFirst("a.tal");
        String awayName = awayElement.text();
        away.setName(awayName);

        // away ranking &leagueName
        em = awayElement.nextElementSibling();
        if (em.childNodeSize() > 0) {
            String strAwayRanking = em.child(0).text();
            if (strAwayRanking != null && !strAwayRanking.isEmpty()) {
                strAwayRanking = strAwayRanking.replace("[", "").replace("]", "");
                int ranking = StringUtils.parseToInt(strAwayRanking);
                away.setRanking(ranking);
            }
        }
        if (em.childNodeSize() > 1) {
            String strHomeLeague = em.child(1).text();
            if (strHomeLeague != null) {
                away.setLeague(strHomeLeague);
            }
        }
        match.setAway(away);
    }

    @NonNull
    private static Match.League parseLeague(Element td) {
        Match.League league = new Match.League();
        Element aElement = td.selectFirst("a.ls");
        String href = aElement.attr("href");
        if (href != null) {
            Matcher matcher = LEAGUE_URL_PATTERN.matcher(href);
            if (matcher.find()) {
                league.setId(StringUtils.parseToInt(matcher.group(1)));
                String name = aElement.text();
                league.setName(name);
            }
        }
        return league;
    }
}
