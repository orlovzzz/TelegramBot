package org.example.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {
    @SerializedName("player_slot")
    private int playerSlot;

    private int assists;

    private int kills;

    private int deaths;

    @SerializedName("net_worth")
    private int netWorth;

    @SerializedName("hero_damage")
    private int heroDamage;

    @SerializedName("hero_id")
    private int heroId;

    @SerializedName("tower_damage")
    private int towerDamage;

    private boolean isRadiant;
}
