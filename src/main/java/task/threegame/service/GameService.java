package task.threegame.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import task.threegame.domain.GameMove;
import task.threegame.exception.SecondPlayerNotActiveException;
import task.threegame.util.constants.GameMessages;
import task.threegame.util.helper.GameHelper;


@Service
public class GameService {

    private static final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameHelper gameHelper;

    private Integer number;

    private Integer addedNumber;

    private Integer newNumber;

    public GameService( GameHelper gameHelper) {
        this.gameHelper = gameHelper;
    }

    public void startGame(Integer initialNumber) {
        if (gameHelper.isSecondPlayerAvailable()) {
            log.info("the game is starting with initial number " + initialNumber);
            log.info("sending {} to the other player" , initialNumber);
            GameMove gameMove = new GameMove(initialNumber);
            gameHelper.sendGameMoveToOtherPlayer(gameMove);
        } else {
            throw new SecondPlayerNotActiveException(GameMessages.SECOND_PLAYER_NOT_AVAILABLE);
        }
    }

    public void play(GameMove otherPlayerMove) {
        number = otherPlayerMove.getNumber();
        if (number == 1) {
            log.info(GameMessages.SECOND_PLAYER_WINNER);
            return;
        }
        GameMove myMove;
        if (number % 3 == 0) {
            addedNumber = 0;
        } else {
            addedNumber = (number + 1) % 3 == 0 ? 1 : -1;
        }
        newNumber = gameHelper.getNewNumber(number , addedNumber);
        log.info(gameHelper.getSuitableUserMessage(number, addedNumber));
        if (newNumber == 1) {
            log.info(GameMessages.WINNER);
        }
        myMove = new GameMove(newNumber);
        if (gameHelper.isSecondPlayerAvailable()) {
            log.info("sending {} to the other player", myMove.getNumber());
            gameHelper.sendGameMoveToOtherPlayer(myMove);
        } else {
            throw new SecondPlayerNotActiveException(GameMessages.SECOND_PLAYER_NOT_AVAILABLE_ANYMORE);
        }
    }


}
