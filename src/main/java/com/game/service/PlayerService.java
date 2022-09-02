package com.game.service;

import com.game.entity.Player;

public interface PlayerService {
    //public List<Player> getAllPlayers();//Get players list

    public void savePlayer(Player player);//Create player Update player
//
    public Player getPlayer(Long id);//Get player

    public Player updatePlayer(Long id, Player newPlayer);

    public void deletePlayer(Player player);//Delete player

    //public Long getPlayersCount();//Get players count
}
