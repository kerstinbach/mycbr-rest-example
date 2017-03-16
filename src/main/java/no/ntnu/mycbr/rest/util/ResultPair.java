package no.ntnu.mycbr.rest.util;

import java.util.HashMap;

public class ResultPair {
    public HashMap<String, String> content;
    public Double similarity;

    public ResultPair(HashMap<String, String> content, Double similarity) {
        this.content = content;
        this.similarity = similarity;
    }

    public HashMap<String, String> getContent() {
        return this.content;
    }
    public Double getSimilarity() {
        return this.similarity;
    }

}
