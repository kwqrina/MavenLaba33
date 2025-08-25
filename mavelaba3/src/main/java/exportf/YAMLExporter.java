package exportf;

import objects.Creature;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import objects.Recipe;

public class YAMLExporter extends CreatureExporter {
    @Override
    public boolean exportCreatures(Map<String, List<Creature>> creatures) {
        var yamlCreatures = creatures.get("yaml");
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndent(2);

        Yaml yaml = new Yaml(options);

        try (FileWriter writer = new FileWriter("result.yaml")) {
            Map<String, Object> yamlData = new HashMap<>();
            Map<String, Object> creaturesMap = new HashMap<>();

            for (Creature creature : yamlCreatures) {
                creaturesMap.put(creature.getName(), createCreatureMap(creature));
            }

            yamlData.put("creatures", creaturesMap);

            yaml.dump(yamlData, writer);
            if (this.hasNext()) return next.exportCreatures(creatures);
            else return true;
        } catch (IOException e) {
            return false;
        }
    }

    private Map<String, Object> createCreatureMap(Creature creature) {
        Map<String, Object> creatureMap = new HashMap<>();

        creatureMap.put("description", creature.getDescription());
        creatureMap.put("dangerLevel", creature.getDangerLevel());
        creatureMap.put("areas", creature.getAreas());
        creatureMap.put("vulnerability", creature.getVulnerabilities());
        creatureMap.put("height", creature.getHeight());
        creatureMap.put("weight", creature.getWeight());
        creatureMap.put("activityTime", creature.getActivityTime());
        creatureMap.put("recipe", createRecipeMap(creature.getRecipe()));

        return creatureMap;
    }

    private Map<String, Object> createRecipeMap(Recipe recipe) {
        Map<String, Object> recipeMap = new HashMap<>();

        recipeMap.put("name", recipe.getName());
        recipeMap.put("ingredients", recipe.getIngredients());
        recipeMap.put("cookingTime", recipe.getCookingTime());
        recipeMap.put("effectiveness", recipe.getEffectiveness());

        return recipeMap;
    }

}