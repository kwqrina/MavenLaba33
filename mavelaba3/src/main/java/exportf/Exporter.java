package exportf;

import objects.Creature;

import java.util.List;
import java.util.Map;


public interface Exporter {
    void setNext(CreatureExporter next);

    boolean exportCreatures(Map<String, List<Creature>> creatures);
}
