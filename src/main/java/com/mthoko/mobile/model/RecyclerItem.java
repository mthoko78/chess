package com.mthoko.mobile.model;

/**
 * Created by Mthoko on 03 May 2018.
 */
public class RecyclerItem {
    private Long id;
    private String title;
    private String description;

    public RecyclerItem(String title, String description, Long id) {
        this.title = title;
        this.description = description;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
