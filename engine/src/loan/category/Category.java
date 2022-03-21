package loan.category;

public class Category {
    private final String category;

    public Category(String category) {
        this.category = category;
        Categories.addCategory(this);
    }

    public String getCategory() {
        return category;
    }
}
