package task.threegame;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import task.threegame.util.constants.GameMessages;
import task.threegame.util.helper.GameHelper;

@AutoConfigureMockMvc
@SpringBootTest(classes = {ThreeGameApplication.class})
public class GameRestControllerIntTest {

    private final String apiUrl = "http://localhost:8080/threegame/";

    @Autowired
    private GameHelper gameHelper;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void startManualGameTest() throws Exception {
        if (gameHelper.isSecondPlayerAvailable()) {
            mockMvc.perform(get(apiUrl + "start-game/manual/10")).andExpect(status().isOk())
                .andExpect(content().string(GameMessages.GAME_HAS_STARTED));
        }
    }

    @Test
    public void startManualGameWithInvalidNumberTest() throws Exception {
        if (gameHelper.isSecondPlayerAvailable()) {
            mockMvc.perform(get(apiUrl + "start-game/manual/0")).andExpect(status().isBadRequest())
                .andExpect(content().string(GameMessages.INVALID_NUMBER));
        }
    }

    @Test
    public void startAutomaticGameTest() throws Exception {
        if (gameHelper.isSecondPlayerAvailable()) {
            mockMvc.perform(get(apiUrl + "start-game/automatic/false")).andExpect(status().isOk())
                .andExpect(content().string(GameMessages.GAME_HAS_STARTED));
        }
    }

    @Test
    public void startGameWhileOtherPlayerUnavailableTest() throws Exception {
        if (!gameHelper.isSecondPlayerAvailable()) {
            mockMvc.perform(get(apiUrl + "start-game/manual/10")).andExpect(status().isNotFound())
                .andExpect(content().string(GameMessages.SECOND_PLAYER_NOT_AVAILABLE));
        }
    }
}
