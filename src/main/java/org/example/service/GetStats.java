package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.entity.AccountInfo;
import org.example.entity.CurrentGame;
import org.example.entity.GameStats;
import org.example.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.math.BigInteger;

@Service
public class GetStats {

    private int n = 10;

    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();

    @Value("${steam_token}")
    private String steamToken;

    @Autowired
    private AccountInfo account;

    public String isWin(boolean radiantWin, boolean isRadiant) {
        if ((radiantWin && isRadiant) || (!radiantWin && !isRadiant)) {
            return "Win";
        } else {
            return "Lose";
        }
    }

    public void getStatsForPage(Model model) {
        model.addAttribute("countMatches", n);
        double win = 0;
        for (int i = 0; i < 10; i++) {
            if ((isWin(account.getGameStats()[i].isRadiantWin(), account.getGameStats()[i].getCurrentGame().getPlayer().isRadiant())).equals("Win")) {
                win++;
            }
        }
        model.addAttribute("winRate", String.format("%.2f", (win / n) * 100) + "%");
    }

    public String statsToString() {
        String stats = "";
        for (int i = 0; i < account.getGameStats().length; i++) {
            GameStats gameStats = account.getGameStats()[i];
            Player player = gameStats.getCurrentGame().getPlayer();
            stats += "Match ID: " +  gameStats.getMatchId() +  "\n" + "Duration: " + gameStats.getCurrentGame().durationToString() + "\n" +
            "Radian | " + gameStats.getCurrentGame().getRadiantScore() + " : " + gameStats.getCurrentGame().getDireScore() + " | Dire\n"
            + "Status: " + isWin(gameStats.isRadiantWin(), player.isRadiant()) +
            "\nHero ID: " + player.getHeroId() + "\n" + "Kills/Deaths/Assists - " + player.getKills() + "/" + player.getDeaths() + "/" + player.getAssists() + "\n" +
            "Hero Damage: " + player.getHeroDamage() + "\nTower Damage: " + player.getTowerDamage() + "\nNet Worth: " + player.getNetWorth() + "\n\n\n";
        }
        return stats;
    }

    public void getId(String id) {
        account.setAccountId(convertId(id));
        getGames();
    }

    public void getGames() {
        String url = "https://api.opendota.com/api/players/" + account.getAccountId() + "/matches";
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResponse = response.body().string();
                GameStats[] matchesStats = gson.fromJson(jsonResponse, GameStats[].class);
                if (matchesStats.length > n) {
                    GameStats[] temp = new GameStats[n];
                    System.arraycopy(matchesStats, 0, temp, 0, n);
                    account.setGameStats(temp);
                }
                getCurrentGameStats(account.getGameStats());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCurrentGameStats(GameStats[] mas) {
        OkHttpClient client = new OkHttpClient();
        for (int i = 0; i < mas.length; i++) {
            String url = "https://api.opendota.com/api/matches/" + mas[i].getMatchId();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    mas[i].setCurrentGame(gson.fromJson(jsonResponse, CurrentGame.class));
                    getCurrentPlayer(mas[i].getPlayerSlot(), mas[i].getCurrentGame());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getCurrentPlayer(int playerSlot, CurrentGame currentGame) {
        for (int i = 0; i < currentGame.getPlayers().size(); i++) {
            if (playerSlot == currentGame.getPlayers().get(i).getPlayerSlot()) {
                currentGame.setPlayer(currentGame.getPlayers().get(i));
            }
        }
    }

    public String convertId(String id) {
        BigInteger bigNumber1 = new BigInteger(id);
        BigInteger bigNumber2 = new BigInteger("76561197960265728");

        BigInteger result = bigNumber1.subtract(bigNumber2);
        return result.toString();
    }

    public AccountInfo getAccount() {
        return account;
    }
}