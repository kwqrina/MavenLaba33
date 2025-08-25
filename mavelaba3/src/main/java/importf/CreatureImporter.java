/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package importf;

import objects.Creature;

import java.io.File;
import java.util.List;

public abstract class CreatureImporter implements Importer{
    protected CreatureImporter next;

    public void setNext(CreatureImporter next) {
        this.next = next;
    }

    public abstract List<Creature> importCreatures(File file);
}
