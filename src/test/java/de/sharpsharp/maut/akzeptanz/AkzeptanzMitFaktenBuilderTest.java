package de.sharpsharp.maut.akzeptanz;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/** Alle Features, Ausgangslage über Variante 1 (Fakten-Builder). Läuft bei mvn test. */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features",
        glue = {"de.sharpsharp.maut.akzeptanz.gemeinsam", "de.sharpsharp.maut.akzeptanz.fakten"},
        plugin = {"pretty", "summary"})
public class AkzeptanzMitFaktenBuilderTest {
}
