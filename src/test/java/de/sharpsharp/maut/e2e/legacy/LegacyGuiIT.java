package de.sharpsharp.maut.e2e.legacy;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Legacy-Form des GUI-Akzeptanztests: jeder Schritt ist genau eine Handlung oder Beobachtung
 * auf einer der drei Oberflächen. Läuft nur bei mvn verify (Failsafe, *IT).
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features/legacy",
        glue = "de.sharpsharp.maut.e2e.legacy",
        plugin = {"pretty", "summary"})
public class LegacyGuiIT {
}
