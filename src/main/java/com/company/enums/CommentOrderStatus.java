package com.company.enums;

public enum CommentOrderStatus {
    ID_ASC("id ASC"),
    ID_DESC("id DESC"),
    PROFILE_ID_ASC("profile.id ASC"),
    PROFILE_ID_DESC("profile.id DESC"),
    ARTICLE_ID_ASC("article.id ASC"),
    ARTICLE_ID_DESC("article.id DESC"),
    CREATED_DATE_ASC("createdDate ASC"),
    CREATED_DATE_DESC("createdDate DESC");

    private String query;

    CommentOrderStatus(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
