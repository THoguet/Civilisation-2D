package projet.approche.objet.domain.valueObject;

import projet.approche.objet.domain.valueObject.resource.Resource;

public class Production extends Needs {
    public Production(int time, Resource... resources) {
        super(time, resources);
    }
}