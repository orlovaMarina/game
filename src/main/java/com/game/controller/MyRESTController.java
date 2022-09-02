package com.game.controller;

import com.game.dto.PlayerDTO;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerServiceImpl;
import com.game.util.PlayerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.List;

@RestController
@Validated
@RequestMapping("/rest")
public class MyRESTController {

    @Autowired
    private PlayerServiceImpl playerService;

    @Autowired
    private PlayerValidator playerValidator;

    @GetMapping("/players")
    List<Player> all(@RequestParam(required = false) String name,
                     @RequestParam(required = false) String title,
                     @RequestParam(required = false) Race race,
                     @RequestParam(required = false) Profession profession,
                     @RequestParam(required = false) Long after,
                     @RequestParam(required = false) Long before,
                     @RequestParam(required = false) Boolean banned,
                     @RequestParam(required = false) Integer minExperience,
                     @RequestParam(required = false) Integer maxExperience,
                     @RequestParam(required = false) Integer minLevel,
                     @RequestParam(required = false) Integer maxLevel,
                     @RequestParam(required = false) PlayerOrder order,
                     @RequestParam(required = false) Integer pageNumber,
                     @RequestParam(required = false) Integer pageSize) {

        int pgN = (pageNumber == null) ? 0 : pageNumber;
        int pgS = (pageSize == null) ? 3 : pageSize;
        String ord = (order == null) ? "id" : order.getFieldName();

        Pageable sorted = PageRequest.of(pgN, pgS, Sort.by(ord));
        List<Player> result = playerService.findAllPlayers(
                name,
                title,
                race,
                profession,
                after,
                before,
                minExperience,
                maxExperience,
                minLevel,
                maxLevel,
                banned,
                sorted);
        return result;
    }

    @GetMapping("/players/count")
    Long count(@RequestParam(required = false) String name,
               @RequestParam(required = false) String title,
               @RequestParam(required = false) Race race,
               @RequestParam(required = false) Profession profession,
               @RequestParam(required = false) Long after,
               @RequestParam(required = false) Long before,
               @RequestParam(required = false) Boolean banned,
               @RequestParam(required = false) Integer minExperience,
               @RequestParam(required = false) Integer maxExperience,
               @RequestParam(required = false) Integer minLevel,
               @RequestParam(required = false) Integer maxLevel) {

        return playerService.count(name,
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

    @GetMapping("/players/{id}")
    public Player getPlayer(@PathVariable("id") Long id){
       return playerService.getPlayer(id);
    }

    @PostMapping("/players")
    public Player createPlayer(@RequestBody PlayerDTO playerDTO, BindingResult bindingResult) throws Exception {
        Player newPlayer = playerService.convertToPlayer(playerDTO);

        playerValidator.validate(newPlayer, bindingResult);

        if(bindingResult.hasErrors()){
            throw new ValidationException("Some of the fields are incorrect");
        }

        playerService.savePlayer(playerService.enrichPlayer(newPlayer));
        return newPlayer;
    }



    @PostMapping("players/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable("id") Long id, @RequestBody PlayerDTO newPlayerDTO
            , BindingResult bindingResult ) throws ValidationException {

        Player newPlayer = playerService.convertToPlayer(newPlayerDTO);

        if(newPlayer.getName()==null&&newPlayer.getTitle()==null&&
        newPlayer.getRace()==null&&newPlayer.getProfession()==null
        &&newPlayer.getBanned()==null&&newPlayer.getBirthday()==null&&
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

    @DeleteMapping("/players/{id}")
    public String deleteEmployee(@PathVariable("id") Long id){
        Player player = playerService.getPlayer(id);

        playerService.deletePlayer(player);
        return "Player with id " + id + " was deleted";
    }


}
