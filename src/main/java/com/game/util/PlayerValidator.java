package com.game.util;

import com.game.entity.Player;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class PlayerValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Player.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Player player = (Player) target;
        if(player.getExperience() < 0 || player.getExperience() > 10_000_000){
            errors.rejectValue("experience", "", "Experience should be between 0 and 10000000");
        }

        Date birthday = player.getBirthday();
        SimpleDateFormat f = new SimpleDateFormat("dd-mm-yyyy");
        long milliseconds = 0;
        try {
            Date d = f.parse(f.format(birthday));
            milliseconds = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(milliseconds < 0){
            errors.rejectValue("birthday", "", "Birthday should be a positive Long");
        }
        LocalDate birth = player.getBirthday().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate firstDate = LocalDate.of(2000, 1, 1);
        LocalDate secondDate = LocalDate.of(3000, 1, 1);
        if(birth.isBefore(firstDate) || birth.isAfter(secondDate)){
            errors.rejectValue("birthday", "", "Creation year should be between 2000 and 3000");
        }
    }
}
