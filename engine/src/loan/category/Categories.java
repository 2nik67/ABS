package loan.category;

import java.util.ArrayList;
import java.util.List;

public abstract class Categories {
    private static List<Category> categoryList=new ArrayList<>();

    public static void setCategoryList(ArrayList<Category> categoryList) {
       Categories.categoryList=new ArrayList<>(categoryList);
    }

    /*public static void addCategory(Category category){
        categoryList.add(category);
    }*/

    public static void printCategories(){
        for (Category category : categoryList) {
            System.out.println(category.getCategory());
        }
    }





    /*public static void resetCategories(){
        categoryList.clear();
    }*/
}
