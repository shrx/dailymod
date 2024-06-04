import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import basemod.BaseMod;
import basemod.interfaces.PostUpdateSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.daily.DailyScreen;
import com.megacrit.cardcrawl.helpers.ModHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class DailyMod implements PostUpdateSubscriber {
    private static final Logger logger = LogManager.getLogger(DailyMod.class);

    public DailyMod() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new DailyMod();
    }

    @Override
    public synchronized void receivePostUpdate() {
        DailyScreen dailyScreen = new DailyScreen();
        dailyScreen.open();
        if (!ModHelper.enabledMods.isEmpty()) {
            logger.info("XXXXXX dailypatch ok");
            logger.info("XXXXXX dailypatch mods={}", ModHelper.enabledMods);
            List<String> modLines = ModHelper.enabledMods.stream().map(m -> String.format(Locale.getDefault(),
                            "{\"name\": \"%s\", \"description\": \"%s\", \"positive\": %s}", m.name, m.description, m.positive))
                    .collect(Collectors.toList());
            List<String> line = Arrays.asList(String.format(Locale.getDefault(), "[%s]", String.join(", ", modLines)));
            Path file = Paths.get("daily-modifiers.json");
            try {
                Files.write(file, line, StandardCharsets.UTF_8);
            } catch (IOException e) {
                logger.error("Unable to save daily modifiers to file!", e);
                throw new RuntimeException(e);
            }
            System.exit(0);
        }
    }
}
