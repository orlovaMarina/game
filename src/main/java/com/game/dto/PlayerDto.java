package com.game.dto;

import com.game.entity.Profession;
import com.game.entity.Race;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;

public class PlayerDto{

    @Id
    private Long id;

    @Size(max=12, message = "Name should not be greater than 12 symbols")
    @NotNull(message = "Name should not be empty")
    private String name;

    @Size(max=30, message = "Title should not be greater than 30 letters")
    private String title;

    @Enumerated(EnumType.STRING)
    private Race race;

    @Enumerated(EnumType.STRING)
    private Profession profession;

    private Integer experience;

    private Integer level;

    private Integer untilNextLevel;

    private Date birthday;

    private Boolean banned;

    public PlayerDto() {
    }

    public PlayerDto(String name, String title, Race race, Profession profession, Integer experience, Integer level, Integer untilNextLevel, Date birthday, Boolean banned) {
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = experience;
        this.level = level;
        this.untilNextLevel = untilNextLevel;
        this.birthday = birthday;
        this.banned = banned;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerDto playerDto = (PlayerDto) o;
        return Objects.equals(id, playerDto.id) && Objects.equals(name, playerDto.name) && Objects.equals(title, playerDto.title) && race == playerDto.race && profession == playerDto.profession && Objects.equals(experience, playerDto.experience) && Objects.equals(level, playerDto.level) && Objects.equals(untilNextLevel, playerDto.untilNextLevel) && Objects.equals(birthday, playerDto.birthday) && Objects.equals(banned, playerDto.banned);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, title, race, profession, experience, level, untilNextLevel, birthday, banned);
    }
}
