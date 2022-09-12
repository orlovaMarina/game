package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exception_handling.NoSuchIDException;
import com.game.exception_handling.NoSuchPlayerException;
import com.game.repository.PlayerRepository;
import com.game.util.PlayerSpecification;
import com.game.util.SearchCriteria;
import com.game.util.SearchOperation;
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
        if ( id <= 0 || id % 1 != 0) throw new NoSuchIDException("ID" + id + " is not valid");
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
                                         Pageable sorted) {

        PlayerSpecification spec = new PlayerSpecification();
        if(!name.equals("")) spec.add(new SearchCriteria("name", name, SearchOperation.MATCH));
        if(!Objects.equals(title, "")) spec.add(new SearchCriteria("title", title, SearchOperation.MATCH));
        if(race!=null) spec.add(new SearchCriteria("race", race, SearchOperation.EQUAL));
        if(profession!=null) spec.add(new SearchCriteria("profession", profession, SearchOperation.EQUAL));
        if(after!=null) spec.add(new SearchCriteria("birthday", after, SearchOperation.DATE_GREATER_THAN));
        if(before!=null) spec.add(new SearchCriteria("birthday", before, SearchOperation.DATE_LESS_THAN));
        if(banned!=null) spec.add(new SearchCriteria("banned", banned, SearchOperation.EQUAL));
        if(maxLevel!=null) spec.add(new SearchCriteria("level", maxLevel, SearchOperation.LESS_THAN_EQUAL));
        if(minLevel!=null) spec.add(new SearchCriteria("level", minLevel, SearchOperation.GREATER_THAN_EQUAL));
        if(minExperience!=null) spec.add(new SearchCriteria("experience", minExperience, SearchOperation.GREATER_THAN_EQUAL));
        if(maxExperience!=null) spec.add(new SearchCriteria("experience", maxExperience, SearchOperation.LESS_THAN_EQUAL));
        return playerRepository.findAll(spec, sorted).getContent();
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
                      Boolean banned) {
        PlayerSpecification spec = new PlayerSpecification();
        if(!name.equals("")) spec.add(new SearchCriteria("name", name, SearchOperation.MATCH));
        if(!Objects.equals(title, "")) spec.add(new SearchCriteria("title", title, SearchOperation.MATCH));
        if(race!=null) spec.add(new SearchCriteria("race", race, SearchOperation.EQUAL));
        if(profession!=null) spec.add(new SearchCriteria("profession", profession, SearchOperation.EQUAL));
        if(after!=null) spec.add(new SearchCriteria("birthday", after, SearchOperation.DATE_GREATER_THAN));
        if(before!=null) spec.add(new SearchCriteria("birthday", before, SearchOperation.DATE_LESS_THAN));
        if(banned!=null) spec.add(new SearchCriteria("banned", banned, SearchOperation.EQUAL));
        if(maxLevel!=null) spec.add(new SearchCriteria("level", maxLevel, SearchOperation.LESS_THAN_EQUAL));
        if(minLevel!=null) spec.add(new SearchCriteria("level", minLevel, SearchOperation.GREATER_THAN_EQUAL));
        if(minExperience!=null) spec.add(new SearchCriteria("experience", minExperience, SearchOperation.GREATER_THAN_EQUAL));
        if(maxExperience!=null) spec.add(new SearchCriteria("experience", maxExperience, SearchOperation.LESS_THAN_EQUAL));
        return playerRepository.count(spec);
    }
}
