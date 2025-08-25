package importf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import objects.Creature;
import objects.Recipe;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JSONImporter extends CreatureImporter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Creature> importCreatures(File file) {
        if (!file.getName().endsWith(".json")) {
            return next != null ? next.importCreatures(file) : null;
        }

        try {
            JsonNode rootNode = objectMapper.readTree(file);
            List<Creature> creatures = new ArrayList<>();

            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String creatureName = entry.getKey();
                JsonNode creatureNode = entry.getValue();

                Creature creature = parseCreature(creatureName, creatureNode);
                creatures.add(creature);
            }
            return creatures;
        } catch (IOException e) {
            return null;
        }
    }

    private Creature parseCreature(String name, JsonNode creatureNode) {
        Creature creature = new Creature(name, "json");

        creature.setDescription(creatureNode.path("description").asText());
        creature.setDangerLevel(creatureNode.path("dangerLevel").asInt());
        JsonNode areasNode = creatureNode.get("areas");
        ArrayList<String> areas = new ArrayList<>();
        for (JsonNode areaNode : areasNode) {
            areas.add(areaNode.asText());
        }
        creature.setAreas(areas);

        creature.setFirstMention(creatureNode.path("firstMention").asText());
        creature.setHeight(Double.valueOf(creatureNode.path("height").asText()));
        creature.setWeight(Integer.parseInt(creatureNode.path("weight").asText()));
        creature.setActivityTime(creatureNode.path("activityTime").asText());

        JsonNode vulnerabilitiesNode = creatureNode.path("vulnerability");
        vulnerabilitiesNode.fields().forEachRemaining(entry -> {
            creature.addVulnerability(entry.getKey(), entry.getValue().asBoolean());
        });

        JsonNode recipeNode = creatureNode.path("recipe");
        creature.setRecipe(parseRecipe(recipeNode));

        return creature;
    }

    private Recipe parseRecipe(JsonNode recipeNode) {
        Recipe recipe = new Recipe();
        recipe.setName(recipeNode.path("name").asText());

        JsonNode ingredientsNode = recipeNode.path("ingredients");
        Map<String, Integer> ingredients = new HashMap<>();
        ingredientsNode.fields().forEachRemaining(entry -> {
            ingredients.put(entry.getKey(), entry.getValue().asInt());
        });
        recipe.setIngredients(ingredients);

        recipe.setCookingTime(recipeNode.path("cookingTime").asInt());
        recipe.setEffectiveness(recipeNode.path("effectiveness").asText());

        return recipe;
    }
}