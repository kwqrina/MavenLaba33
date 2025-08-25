package exportf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import objects.Creature;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONExporter extends CreatureExporter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean exportCreatures(Map<String, List<Creature>> creatures) {
        var jsonCreatures = creatures.get("json");
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        Map<String, Object> outputData = new HashMap<>();

        for (Creature creature : jsonCreatures) {
            Map<String, Object> creatureData = new HashMap<>();

            creatureData.put("description", creature.getDescription());
            creatureData.put("dangerLevel", creature.getDangerLevel());
            creatureData.put("areas", creature.getAreas());
            creatureData.put("firstMention", creature.getFirstMention());
            creatureData.put("height", creature.getHeight());
            creatureData.put("weight", creature.getWeight());
            creatureData.put("activityTime", creature.getActivityTime());

            Map<String, Boolean> vulnerabilities = new HashMap<>(creature.getVulnerabilities());
            creatureData.put("vulnerability", vulnerabilities);

            if (creature.getRecipe() != null) {
                String recipeKey = "recipe";

                Map<String, Object> recipeData = new HashMap<>();
                recipeData.put("name", creature.getRecipe().getName());
                recipeData.put("ingredients", creature.getRecipe().getIngredients());
                recipeData.put("cookingTime", creature.getRecipe().getCookingTime());
                recipeData.put("effectiveness", creature.getRecipe().getEffectiveness());

                creatureData.put(recipeKey, recipeData);
            }

            outputData.put(creature.getName(), creatureData);
        }

        try {
            objectMapper.writeValue(new File("result.json"), outputData);
            if (this.hasNext()) return next.exportCreatures(creatures);
            else return true;
        } catch (IOException e) {
            return false;
        }
    }
}