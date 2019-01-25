package it.polimi.middleware.processingengine.message;

import java.io.Serializable;
import java.util.List;

public class ResultMessage implements Serializable {

    private final List<String> results;

    public ResultMessage(List<String> results) {
        this.results = results;
    }

    public List<String> getResults() {
        return results;
    }
}
