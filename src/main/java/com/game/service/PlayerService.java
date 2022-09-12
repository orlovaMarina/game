package com.game.service;

import com.game.dto.PlayerDto;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlayerService {
    public void savePlayer(Player player);

    public Player getPlayer(Long id);

    public Player enrichPlayer(Player newPlayer);

    public Player updatePlayer(Long id, Player newPlayer);

    public void deletePlayer(Player player);

    public List<Player> getAllPlayers(String name,
                                      String title,
                                      Race race,
                                      Profession profession,
                                      Long after,
                                      Long before,
                                      Boolean banned,
                                      Integer minExperience,
                                      Integer maxExperience,
                                      Integer minLevel,
                                      Integer maxLevel,
                                      Pageable sorted);

    public Long count(String name,
                      String title,
                      Race race,
                      Profession profession,
                      Long after,
                      Long before,
                      Integer minExperience,
                      Integer maxExperience,
                      Integer minLevel,
                      Integer maxLevel,
                      Boolean banned
    );
}
