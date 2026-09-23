package de.sharpsharp.maut.akzeptanz;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/** Dieselben Features, Ausgangslage über Variante 3 (Endzustand-Builder). Läuft bei mvn test. */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features",
        glue = {"de.sharpsharp.maut.akzeptanz.gemeinsam", "de.sharpsharp.maut.akzeptanz.endzustand"},
        plugin = {"pretty", "summary"})
public class AkzeptanzMitEndzustandBuilderTest {
}
