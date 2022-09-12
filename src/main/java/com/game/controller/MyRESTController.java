package com.game.controller;

import com.game.converter.PlayerConverter;
import com.game.dto.PlayerDto;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import com.game.util.PlayerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.*;
import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/players")
public class MyRESTController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerValidator playerValidator;

    @Autowired
    private PlayerConverter playerConverter;

    @GetMapping("")
    List<PlayerDto> all(@RequestParam(required = false, defaultValue = "") String name,
                        @RequestParam(required = false, defaultValue = "") String title,
                        @RequestParam(required = false) Race race,
                        @RequestParam(required = false) Profession profession,
                        @RequestParam(required = false) Long after,
                        @RequestParam(required = false) Long before,
                        @RequestParam(required = false) Boolean banned,
                        @RequestParam(required = false, defaultValue = "0") Integer minExperience,
                        @RequestParam(required = false) Integer maxExperience,
                        @RequestParam(required = false, defaultValue = "0") Integer minLevel,
                        @RequestParam(required = false) Integer maxLevel,
                        @RequestParam(required = false) PlayerOrder order,
                        @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                        @RequestParam(required = false, defaultValue = "3") Integer pageSize) {

        Pageable sorted = PageRequest.of(pageNumber, pageSize, Sort.by((order == null) ? "id" : order.getFieldName()));

        return playerService.getAllPlayers( name,
                                            title,
                                            race,
                                            profession,
                                            after,
                                            before,
                                            banned,
                                            minExperience,
                                            maxExperience,
                                            minLevel,
                                            maxLevel,
                                            sorted)
                                    .stream().map(playerConverter::convertToPlayerDto).collect(Collectors.toList());
    }

    @GetMapping("/count")
    Long count(@RequestParam(required = false, defaultValue = "") String name,
               @RequestParam(required = false, defaultValue = "") String title,
               @RequestParam(required = false) Race race,
               @RequestParam(required = false) Profession profession,
               @RequestParam(required = false) Long after,
               @RequestParam(required = false) Long before,
               @RequestParam(required = false) Boolean banned,
               @RequestParam(required = false) Integer minExperience,
               @RequestParam(required = false) Integer maxExperience,
               @RequestParam(required = false) Integer minLevel,
               @RequestParam(required = false) Integer maxLevel) {

        return playerService.count( name,
                                    title,
                                    race,
                                    profession,
                                    after,
                                    before,
                                    minExperience,
                                    maxExperience,
                                    minLevel,
                                    maxLevel,
                                    banned);
    }

    @GetMapping("/{id}")
    public PlayerDto getPlayer(@PathVariable("id")Long id){
       return playerConverter.convertToPlayerDto(playerService.getPlayer(id));
    }

    @PostMapping("")
    public ResponseEntity<Player> createPlayer(@Valid @RequestBody PlayerDto playerDTO, BindingResult bindingResult) throws Exception {
        Player newPlayer = playerConverter.convertToPlayer(playerDTO);
        playerValidator.validate(newPlayer, bindingResult);
        if(bindingResult.hasErrors()){
            throw new ValidationException("Some of the fields are incorrect");
        }
        playerService.enrichPlayer(newPlayer);
        playerService.savePlayer(newPlayer);
        return new ResponseEntity<>(newPlayer, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable("id") Long id, @Valid @RequestBody PlayerDto newPlayerDto
            , BindingResult bindingResult ) throws ValidationException {
        Player newPlayer = playerConverter.convertToPlayer(newPlayerDto);
        if(     newPlayer.getName()==null&&newPlayer.getTitle()==null&&
                newPlayer.getRace()==null&&newPlayer.getProfession()==null&&
                newPlayer.getBanned()==null&&newPlayer.getBirthday()==null&&
                newPlayer.getExperience()==null) {
            return new ResponseEntity<>(playerService.getPlayer(id), HttpStatus.OK);
        }
        else{
            Player player = playerService.updatePlayer(id, newPlayer);
            playerValidator.validate(player, bindingResult);
            if(bindingResult.hasErrors()){
                throw new ValidationException("Some of the fields are incorrect");
            }
        playerService.savePlayer(player);
        return new ResponseEntity<>(player, HttpStatus.OK);}
    }

    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable("id") Long id){
        Player player = playerService.getPlayer(id);
        playerService.deletePlayer(player);
        return "Player with id " + id + " was deleted";
    }
}
