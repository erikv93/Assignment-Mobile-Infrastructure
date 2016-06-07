package erik.caa;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Erik on 7-6-2016.
 */
public class BackEndHelper {
    URL baseURL;
    URL createHighScoreURL;
    HttpClient

    public BackEndHelper() {
        try {
            baseURL = new URL("https://oege.ie.hva.nl/~leuvert001/php/mobdev/hiscoreCreate.php");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void setHighscore(String name, int score) {

    }
}
