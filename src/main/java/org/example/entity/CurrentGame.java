package org.example.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CurrentGame {

    @SerializedName("dire_score")
    private int direScore;

    @SerializedName("radiant_score")
    private int radiantScore;

    private int duration;

    private List<Player> players;

    private Player player = new Player();

    public String durationToString() {
        String time = "";
        int minutes = duration / 60;
        int seconds = duration % 60;
        if (minutes >= 60) {
            int hours = minutes / 60;
            minutes %= 60;
            time += hours + ":";
        }
        if (minutes / 10 == 0) {
            time += "0" + minutes + ":";
        } else {
            time += minutes + ":";
        }
        if (seconds / 10 == 0) {
            time += "0" + seconds;
        } else {
            time += seconds;
        }
        return time;
    }

}
