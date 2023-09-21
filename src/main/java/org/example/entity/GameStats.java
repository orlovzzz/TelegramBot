package org.example.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameStats {

    @SerializedName("match_id")
    private long matchId;

    @SerializedName("player_slot")
    private int playerSlot;

    @SerializedName("radiant_win")
    private boolean radiantWin;

    private CurrentGame currentGame = new CurrentGame();
}
