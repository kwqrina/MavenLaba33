package importf;

import objects.Creature;
import objects.Recipe;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLImporter extends CreatureImporter {
    @Override
    public List<Creature> importCreatures(File file) {
        if (!file.getName().endsWith(".xml")) {
            return next != null ? next.importCreatures(file) : null;
        }

        List<Creature> creatures = new ArrayList<>();

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(new FileReader(file));

            Creature currentCreature = null;
            ArrayList<String> currentAreas = new ArrayList<>();
            Recipe currentRecipe = null;
            String currentName = null;
            Map<String, Integer> currentIngredients = new HashMap<>();

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();
                    currentName = qName;

                    switch (qName) {
                        case "creature":
                            String name = startElement.getAttributeByName(
                                    new QName("name")).getValue();
                            currentCreature = new Creature(name, "xml");
                            break;
                        case "vulnerability":
                            String vulnName = startElement.getAttributeByName(
                                    new QName("name")).getValue();
                            String quantity = startElement.getAttributeByName(
                                    new QName("quantity")).getValue();
                            assert currentCreature != null;
                            currentCreature.addVulnerability(vulnName, Boolean.parseBoolean(quantity));
                            break;
                        case "recipe":
                            currentRecipe = new Recipe();
                            currentIngredients = new HashMap<>();
                            break;
                        case "ingredient":
                            String ingName = startElement.getAttributeByName(
                                    new QName("name")).getValue();
                            String ingQuantity = startElement.getAttributeByName(
                                    new QName("quantity")).getValue();

                            currentIngredients.put(ingName, Integer.parseInt(ingQuantity));
                            break;
                    }
                } else if (event.isCharacters()) {
                    String data = event.asCharacters().getData().trim();

                    if (currentCreature != null && !data.isEmpty()) {
                        switch (currentName) {
                            case "area":
                                currentAreas.add(data);
                                break;
                            case "description":
                                currentCreature.setDescription(data);
                                break;
                            case "dangerLevel":
                                currentCreature.setDangerLevel(Integer.parseInt(data));
                                break;
                            case "firstMention":
                                currentCreature.setFirstMention(data);
                                break;
                            case "height":
                                currentCreature.setHeight(Double.parseDouble(data));
                                break;
                            case "weight":
                                currentCreature.setWeight(Integer.parseInt(data));
                                break;
                            case "activityTime":
                                currentCreature.setActivityTime(data);
                                break;
                            case "name":
                                assert currentRecipe != null;
                                currentRecipe.setName(data);
                                break;
                            case "cookingTime":
                                assert currentRecipe != null;
                                currentRecipe.setCookingTime(Integer.parseInt(data));
                                break;
                            case "effectiveness":
                                assert currentRecipe != null;
                                currentRecipe.setEffectiveness(data);
                                break;
                            case null:
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + currentName);
                        }
                    }
                } else if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    String qName = endElement.getName().getLocalPart();

                    if (qName.equals("recipe")) {
                        if (currentRecipe != null && currentCreature != null) {
                            currentRecipe.setIngredients(currentIngredients);
                            currentCreature.setRecipe(currentRecipe);
                            currentIngredients = new HashMap<>();
                        }
                    } else if (qName.equals("creature")) {
                        assert currentCreature != null;
                        currentCreature.setAreas(currentAreas);
                        currentAreas = new ArrayList<>();
                        creatures.add(currentCreature);
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return creatures;
    }
}