package task.threegame.util.helper;

import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import task.threegame.domain.GameMove;

@Component
public class GameHelper {

    private final String otherPlayerGameServiceUrl;

    private final String otherPlayerStartGameServiceUrl;

    private final RestTemplate restTemplate;

    private final Integer minimum;

    private final Integer maximum;

    private static final Logger log = LoggerFactory.getLogger(GameHelper.class);

    public GameHelper(@Value("${game.second-player.play-url}") String otherPlayerGameServiceUrl,
                      @Value("${game.second-player.start-url}") String otherPlayerStartGameServiceUrl, RestTemplateBuilder restTemplateBuilder,
                      @Value("${game.random-number-min}") Integer minimum,
                      @Value("${game.random-number-max}") Integer maximum) {
        this.otherPlayerGameServiceUrl = otherPlayerGameServiceUrl;
        this.otherPlayerStartGameServiceUrl = otherPlayerStartGameServiceUrl;
        this.restTemplate = restTemplateBuilder.build();
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public Integer getRandomNumber() {
        return ThreadLocalRandom.current().nextInt(minimum, maximum + 1);
    }

    public boolean isNumberValid(Integer number) {
        return number > 1 ;
    }

    public boolean isSecondPlayerAvailable() {
        try {
            restTemplate.getForObject(otherPlayerStartGameServiceUrl+"/automatic/true", String.class);
            return true;
        } catch (Exception ex) {
            log.error("second player service API is not reachable", ex);
            return false;
        }
    }

    public void sendGameMoveToOtherPlayer(GameMove gameMove) {
        restTemplate.postForObject(otherPlayerGameServiceUrl, gameMove, GameMove.class);
    }

    public String getSuitableUserMessage(Integer oldNumber, Integer valueAdded) {
        Integer newNumber = getNewNumber(oldNumber, valueAdded);
        String returnMsg = "";
        switch (valueAdded) {
            case 0:
                returnMsg += "just ";
                break;
            case 1:
                returnMsg += "added 1 => "+ (oldNumber+valueAdded) + " then ";
                break;
            case -1:
                returnMsg += "subtracted 1 => "+ (oldNumber+valueAdded) + " then ";
                break;
        }
        returnMsg += "divided by 3 to have the number " + newNumber;
        return returnMsg;
    }

    public Integer getNewNumber(Integer number, Integer addedNumber) {
        return (number + addedNumber) / 3;
    }
}
