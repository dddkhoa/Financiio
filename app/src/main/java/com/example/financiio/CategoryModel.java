package com.example.financiio;

public class CategoryModel {
    String categoryName;
    int categoryImage;

    public CategoryModel(String categoryName, int categoryImage) {
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getCategoryImage() {
        return categoryImage;
    }
}
