package com.game.converter;

import com.game.dto.PlayerDto;
import com.game.entity.Player;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlayerConverter {

    @Autowired
    private ModelMapper modelMapper;

    public Player convertToPlayer(PlayerDto playerDTO) {
        return modelMapper.map(playerDTO, Player.class);
    }

    public PlayerDto convertToPlayerDto(Player player){
        return modelMapper.map(player, PlayerDto.class);
    }
}
