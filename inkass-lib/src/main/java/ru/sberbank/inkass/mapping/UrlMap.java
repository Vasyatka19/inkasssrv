package ru.sberbank.inkass.mapping;

public enum UrlMap {

    GRAPH_GET_NEW_GRAPH("/graph/getNewGraph");

    private String url;

    UrlMap(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
