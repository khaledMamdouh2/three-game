package task.threegame.api;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import task.threegame.domain.GameMove;
import task.threegame.exception.SecondPlayerNotActiveException;
import task.threegame.service.GameService;
import task.threegame.util.constants.GameMessages;
import task.threegame.util.helper.GameHelper;

@RestController
@RequestMapping("/threegame")
public class GameRestController {
    private final GameService gameService;

    private final GameHelper gameHelper;

    private static final Logger log = LoggerFactory.getLogger(GameRestController.class);

    public GameRestController(GameService gameService, GameHelper gameHelper) {
        this.gameService = gameService;
        this.gameHelper = gameHelper;
    }

    @GetMapping("/start-game/manual/{initialNumber}")
    public ResponseEntity<String> startManualGame(@PathVariable Integer initialNumber) {
        return gameHelper.isNumberValid(initialNumber) ? startGame(initialNumber) :
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GameMessages.INVALID_NUMBER);
    }

    @GetMapping("/start-game/automatic/{justCheckingAvailability}")
    public ResponseEntity<String> startAutomaticGame(@PathVariable boolean justCheckingAvailability) {
        if (justCheckingAvailability) {
            return ResponseEntity.ok("Hey! I am available");
        }
        return startGame(gameHelper.getRandomNumber());
    }

    private ResponseEntity<String> startGame(Integer initialNumber) {
        try {
            gameService.startGame(initialNumber);
            return ResponseEntity.ok(GameMessages.GAME_HAS_STARTED);
        } catch (SecondPlayerNotActiveException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/play")
    public ResponseEntity play(@RequestBody GameMove gameMove) {
        log.info("received number {} from the other player", gameMove.getNumber());
        try {
            gameService.play(gameMove);
            return ResponseEntity.ok().build();
        } catch (SecondPlayerNotActiveException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

}
