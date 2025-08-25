package importf;

import objects.Creature;

import java.io.File;
import java.util.List;

public interface Importer {
    void setNext(CreatureImporter next);

    List<Creature> importCreatures(File file);
}
