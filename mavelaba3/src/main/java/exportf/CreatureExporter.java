package exportf;

import objects.Creature;

import java.util.List;
import java.util.Map;

public abstract class CreatureExporter implements Exporter {
    protected CreatureExporter next;

    public void setNext(CreatureExporter next) {
        this.next = next;
    }

    public abstract boolean exportCreatures(Map<String, List<Creature>> creatures);

    public boolean hasNext() {
        return next != null;
    }
}