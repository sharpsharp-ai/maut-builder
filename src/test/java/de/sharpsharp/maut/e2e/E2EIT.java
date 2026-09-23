package de.sharpsharp.maut.e2e;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/** Dieselben Features, durch die echte GUI geklickt. Läuft nur bei mvn verify (Failsafe, *IT). */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features",
        glue = "de.sharpsharp.maut.e2e",
        plugin = {"pretty", "summary"})
public class E2EIT {
}
