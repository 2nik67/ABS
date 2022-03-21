package loan.category;

import java.util.ArrayList;
import java.util.List;

public abstract class Categories {
    private static List<Category> categoryList=new ArrayList<>();

    public static void addCategory(Category category){
        categoryList.add(category);
    }

    public static void printCategories(){
        for (int i = 0; i < categoryList.size(); i++) {
            System.out.println(categoryList.get(i).getCategory());
        }
    }

}
