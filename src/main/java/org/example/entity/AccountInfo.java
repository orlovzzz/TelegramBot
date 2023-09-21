package org.example.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class AccountInfo {

    private String accountId;

    private GameStats[] gameStats;

}
