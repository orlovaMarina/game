package com.game.service;

import com.game.dto.PlayerDTO;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exception_handling.NoSuchIDException;
import com.game.exception_handling.NoSuchPlayerException;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Player getPlayer(Long id) {
        Player player = null;

        if (id <= 0 || id % 1 != 0) throw new NoSuchIDException("ID" + id + " is not valid");

        Optional<Player> optional = playerRepository.findById(id);
        if (optional.isPresent()) {
            player = optional.get();
        } else throw new NoSuchPlayerException("There is no player with ID = " + id +
                " in Database");
        return player;
    }

    @Override
    public Player updatePlayer(Long id, Player newPlayer) {
        Player player = getPlayer(id);

        if (newPlayer.getName() != null) player.setName(newPlayer.getName());
        if (newPlayer.getTitle() != null) player.setTitle(newPlayer.getTitle());
        if (newPlayer.getRace() != null) player.setRace(newPlayer.getRace());
        if (newPlayer.getProfession() != null) player.setProfession(newPlayer.getProfession());
        if (newPlayer.getBirthday() != null) player.setBirthday(newPlayer.getBirthday());
        if (newPlayer.getBanned() != null) player.setBanned(newPlayer.getBanned());
        if (newPlayer.getExperience() != null) {
            player.setExperience(newPlayer.getExperience());
            enrichPlayer(player);
        }
        return player;
    }

    public Player enrichPlayer(Player newPlayer) {
        Integer level = ((int) Math.sqrt(200 * newPlayer.getExperience() + 2500) - 50) / 100;
        newPlayer.setLevel(level);

        Integer untilNextLevel = 50 * (level + 1) * (level + 2) - newPlayer.getExperience();
        newPlayer.setUntilNextLevel(untilNextLevel);

        return newPlayer;
    }
    @Override
    public void savePlayer(Player player) {
        playerRepository.save(player);
    }

    @Override
    public void deletePlayer(Player player) {
        playerRepository.delete(player);
    }


    public Player convertToPlayer(PlayerDTO playerDTO) {
        Player player = new Player();
        player.setName(playerDTO.getName());
        player.setBirthday(playerDTO.getBirthday());
        player.setProfession(playerDTO.getProfession());
        player.setRace(playerDTO.getRace());
        player.setTitle(playerDTO.getTitle());
        player.setExperience(playerDTO.getExperience());
        player.setBanned(playerDTO.getBanned());
        return player;
    }

    public List<Player> findAllPlayers(String name,
                                       String title,
                                       Race race,
                                       Profession profession,
                                       Long after,
                                       Long before,
                                       Integer minExperience,
                                       Integer maxExperience,
                                       Integer minLevel,
                                       Integer maxLevel,
                                       Boolean banned,
                                       Pageable pageable) {

        String strRace = (race == null) ? "" : race.name();
        String strProfession = (profession == null) ? "" : profession.name();
        if (name == null) name = "";
        if (title == null) title = "";
        Date dateAfter = (after == null) ? new Date(0L) : new Date(after);
        Date dateBefore = (before == null) ? new Date() : new Date(before);
        int minExp = (minExperience == null) ? 0 : minExperience;
        int maxExp = (maxExperience == null) ? Integer.MAX_VALUE : maxExperience;
        int minLvl = (minLevel == null) ? 0 : minLevel;
        int maxLvl = (maxLevel == null) ? Integer.MAX_VALUE : maxLevel;
        Collection<Boolean> banneds = new ArrayList<>();
        if (banned == null) {
            banneds.add(true);
            banneds.add(false);
        } else {
            banneds.add(banned);
        }
        return playerRepository.findPlayersByNameContainingAndTitleContainingAndRaceContainingAndProfessionContainingAndBirthdayBetweenAndExperienceBetweenAndLevelBetweenAndBannedInAllIgnoreCase(
                name,
                title,
                strRace,
                strProfession,
                dateAfter,
                dateBefore,
                minExp,
                maxExp,
                minLvl,
                maxLvl,
                banneds,
                pageable);
    }

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
    ) {
        String strRace = (race == null) ? "" : race.name();
        String strProfession = (profession == null) ? "" : profession.name();
        if (name == null) name = "";
        if (title == null) title = "";
        Date dateAfter = (after == null) ? new Date(0L) : new Date(after);
        Date dateBefore = (before == null) ? new Date() : new Date(before);
        int minExp = (minExperience == null) ? 0 : minExperience;
        int maxExp = (maxExperience == null) ? Integer.MAX_VALUE : maxExperience;
        int minLvl = (minLevel == null) ? 0 : minLevel;
        int maxLvl = (maxLevel == null) ? Integer.MAX_VALUE : maxLevel;
        Collection<Boolean> banneds = new ArrayList<>();
        if (banned == null) {
            banneds.add(true);
            banneds.add(false);
        } else {
            banneds.add(banned);
        }

        return (long) playerRepository.findPlayersByNameContainingAndTitleContainingAndRaceContainingAndProfessionContainingAndBirthdayBetweenAndExperienceBetweenAndLevelBetweenAndBannedInAllIgnoreCase(
                name,
                title,
                strRace,
                strProfession,
                dateAfter,
                dateBefore,
                minExp,
                maxExp,
                minLvl,
                maxLvl,
                banneds,
                Pageable.unpaged()
        ).size();
    }
}
