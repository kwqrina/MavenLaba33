package exportf;

import objects.Creature;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.List;
import java.util.Map;

import objects.Recipe;

public class XMLExporter extends CreatureExporter {
    @Override
    public boolean exportCreatures(Map<String, List<Creature>> creatures) {
        var xmlCreatures = creatures.get("xml");
        XMLOutputFactory factory = XMLOutputFactory.newInstance();

        try (FileOutputStream fos = new FileOutputStream("result.xml");
             BufferedWriter writer = new BufferedWriter(
                     new OutputStreamWriter(fos, "UTF-8"))) {

            XMLStreamWriter xmlWriter = factory.createXMLStreamWriter(writer);

            xmlWriter.writeStartDocument("UTF-8", "1.0");
            xmlWriter.writeCharacters("\n");
            xmlWriter.writeStartElement("bestiary");
            xmlWriter.writeCharacters("\n");

            for (Creature creature : xmlCreatures) {
                writeCreature(xmlWriter, creature);
            }

            xmlWriter.writeEndElement();
            xmlWriter.writeCharacters("\n");
            xmlWriter.writeEndDocument();

            xmlWriter.flush();
            xmlWriter.close();
            if (this.hasNext()) return next.exportCreatures(creatures);
            else return true;
        } catch (IOException | XMLStreamException e) {
            return false;
        }
    }

    private void writeCreature(XMLStreamWriter writer, Creature creature)
            throws XMLStreamException {
        writer.writeCharacters("    ");
        writer.writeStartElement("creature");
        writer.writeAttribute("name", creature.getName());
        writer.writeCharacters("\n");

        writeElement(writer, "description", creature.getDescription());
        writeElement(writer, "dangerLevel", String.valueOf(creature.getDangerLevel()));

        writer.writeCharacters("        ");
        writer.writeStartElement("areas");
        writer.writeCharacters("\n");
        for (String area : creature.getAreas()) {
            writeElement(writer, "area", area, "            ");
        }
        writer.writeCharacters("        ");
        writer.writeEndElement();
        writer.writeCharacters("\n");

        writer.writeCharacters("        ");
        writer.writeStartElement("vulnerabilities");
        writer.writeCharacters("\n");
        for (Map.Entry<String, Boolean> entry : creature.getVulnerabilities().entrySet()) {
            writeVulnerability(writer, entry.getKey(), entry.getValue());
        }
        writer.writeCharacters("        ");
        writer.writeEndElement();
        writer.writeCharacters("\n");

        writeRecipe(writer, creature.getRecipe());

        writer.writeCharacters("    ");
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

    private void writeElement(XMLStreamWriter writer, String tag, String content)
            throws XMLStreamException {
        writeElement(writer, tag, content, "        ");
    }

    private void writeElement(XMLStreamWriter writer, String tag,
                              String content, String indent) throws XMLStreamException {
        writer.writeCharacters(indent);
        writer.writeStartElement(tag);
        writer.writeCharacters(content);
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

    private void writeVulnerability(XMLStreamWriter writer,
                                    String type, Boolean isVulnerable) throws XMLStreamException {
        writer.writeCharacters("            ");
        writer.writeStartElement("vulnerability");
        writer.writeAttribute("type", type);
        writer.writeCharacters(String.valueOf(isVulnerable));
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

    private void writeRecipe(XMLStreamWriter writer, Recipe recipe)
            throws XMLStreamException {
        writer.writeCharacters("        ");
        writer.writeStartElement("recipe");
        writer.writeCharacters("\n");

        writeElement(writer, "name", recipe.getName());

        writer.writeCharacters("            ");
        writer.writeStartElement("ingredients");
        writer.writeCharacters("\n");
        for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
            writer.writeCharacters("                ");
            writer.writeStartElement("ingredient");
            writer.writeAttribute("name", entry.getKey());
            writer.writeAttribute("quantity", String.valueOf(entry.getValue()));
            writer.writeEndElement();
            writer.writeCharacters("\n");
        }
        writer.writeCharacters("            ");
        writer.writeEndElement();
        writer.writeCharacters("\n");

        writeElement(writer, "cookingTime", String.valueOf(recipe.getCookingTime()));
        writeElement(writer, "effectiveness", recipe.getEffectiveness());

        writer.writeCharacters("        ");
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }
}