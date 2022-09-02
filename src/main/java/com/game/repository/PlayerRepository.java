package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findPlayersByNameContainingAndTitleContainingAndRaceContainingAndProfessionContainingAndBirthdayBetweenAndExperienceBetweenAndLevelBetweenAndBannedInAllIgnoreCase(
            String name,
            String title,
            String strRace,
            String strProfession,
            Date dateAfter,
            Date dateBefore,
            Integer minExperience,
            Integer maxExperience,
            Integer minLevel,
            Integer maxLevel,
            Collection<Boolean> banneds,
            Pageable page);

}
