package importf;

import objects.Creature;
import objects.Recipe;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YAMLImporter extends CreatureImporter {
    private final Yaml yaml = new Yaml();

    @Override
    public List<Creature> importCreatures(File file) {
        if (!file.getName().endsWith(".yaml") && !file.getName().endsWith(".yml")) {
            return next != null ? next.importCreatures(file) : null;
        }
        try {
            FileInputStream inputStream = new FileInputStream(file);
            Map<String, Object> yamlData = yaml.load(inputStream);
            List<Creature> creatures = new ArrayList<>();
            Map<String, Object> creaturesMap = (Map<String, Object>) yamlData.get("creatures");
            for (Map.Entry<String, Object> entry : creaturesMap.entrySet()) {
                Creature creature = parseCreature(entry.getKey(), (Map<String, Object>) entry.getValue());
                creatures.add(creature);
            }

            return creatures;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private Creature parseCreature(String name, Map<String, Object> creatureData) {
        Creature creature = new Creature(name, "yaml");
        creature.setDescription((String) creatureData.get("description"));
        creature.setDangerLevel(((Number) creatureData.get("dangerLevel")).intValue());
        ArrayList<String> areas = (ArrayList<String>) creatureData.get("areas");
        creature.setAreas(areas);
        creature.setFirstMention((String) creatureData.get("firstMention"));
        creature.setHeight(Double.parseDouble(creatureData.get("height").toString()));
        creature.setWeight(Integer.parseInt(creatureData.get("weight").toString()));

        creature.setActivityTime((String) creatureData.get("activityTime"));

        Map<String, Boolean> vulnerabilities = (Map<String, Boolean>) creatureData.get("vulnerability");
        for (String vulnerability : vulnerabilities.keySet()) {
            creature.getVulnerabilities().put(vulnerability, vulnerabilities.get(vulnerability));
        }


        Map<String, Object> recipeData = (Map<String, Object>) creatureData.get("recipe");
        creature.setRecipe(parseRecipe(recipeData));

        return creature;
    }

    private Recipe parseRecipe(Map<String, Object> recipeData) {
        Recipe recipe = new Recipe();
        recipe.setName((String) recipeData.get("name"));

        Map<String, Integer> ingredients = new HashMap<>();
        Map<String, Object> ingredientsData = (Map<String, Object>) recipeData.get("ingredients");
        ingredientsData.forEach((key, value) -> {
            ingredients.put(key, ((Number) value).intValue());
        });

        recipe.setIngredients(ingredients);

        recipe.setCookingTime(((Number) recipeData.get("cookingTime")).intValue());
        recipe.setEffectiveness((String) recipeData.get("effectiveness"));

        return recipe;
    }
}