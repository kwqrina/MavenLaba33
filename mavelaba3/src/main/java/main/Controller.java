/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import exportf.CreatureExporter;
import objects.Creature;
import importf.CreatureImporter;
import importf.*;
import exportf.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {
    public Map<String, List<Creature>> importData(File[] files) {
        CreatureImporter jsonImporter = new JSONImporter();
        CreatureImporter xmlImporter = new XMLImporter();
        CreatureImporter yamlImporter = new YAMLImporter();

        jsonImporter.setNext(xmlImporter);
        xmlImporter.setNext(yamlImporter);
        Map<String, List<Creature>> creatures = new HashMap<>();
        for (File file : files) {
            List<Creature> importedCreatures = jsonImporter.importCreatures(file);
            if (importedCreatures != null) creatures.put(importedCreatures.getFirst().getSource(), importedCreatures);
        }
        return creatures;
    }

    public boolean exportData(Map<String, List<Creature>> creatures) {
        CreatureExporter jsonExporter = new JSONExporter();
        CreatureExporter xmlExporter = new XMLExporter();
        CreatureExporter yamlExporter = new YAMLExporter();

        jsonExporter.setNext(xmlExporter);
        xmlExporter.setNext(yamlExporter);

        return jsonExporter.exportCreatures(creatures);
    }
}
