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
        for (int i = 0; i < categoryList.size(); i++) {
            System.out.println(i+1 + ") " + categoryList.get(i).getCategory());
        }
    }

    public static Category getCategoryByIndex(int index){
        return categoryList.get(index);
    }
    public static List<Category> getCategoryList() {
        return categoryList;
    }
    public static Integer getNumOfCategories() { return categoryList.size(); }
/*public static void resetCategories(){
        categoryList.clear();
    }*/
}