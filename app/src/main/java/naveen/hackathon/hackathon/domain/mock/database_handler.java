
package naveen.hackathon.hackathon.domain.mock;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import naveen.hackathon.hackathon.buyer_transport_price;
import naveen.hackathon.hackathon.model.CenterRepository;
import naveen.hackathon.hackathon.model.entities.Product;
import naveen.hackathon.hackathon.model.entities.ProductCategoryModel;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class database_handler {

    private static database_handler fakeServer;
    private ArrayList<String> Product_Name_raw = new ArrayList<String>();
    private ArrayList<String> Short_Description_raw = new ArrayList<String>();
    private ArrayList<String> Long_Description_raw = new ArrayList<String>();
    private ArrayList<String> Total_Price_raw = new ArrayList<String>();
    private ArrayList<String> Discount_raw = new ArrayList<String>();
    private ArrayList<String> Selling_Price_raw = new ArrayList<String>();
    private ArrayList<String> image_raw = new ArrayList<String>();



    private ArrayList<String> Product_Name_food = new ArrayList<String>();
    private ArrayList<String> Short_Description_food = new ArrayList<String>();
    private ArrayList<String> Long_Description_food = new ArrayList<String>();
    private ArrayList<String> Total_Price_food = new ArrayList<String>();
    private ArrayList<String> Discount_food = new ArrayList<String>();
    private ArrayList<String> Selling_Price_food = new ArrayList<String>();
    private ArrayList<String> image_food = new ArrayList<String>();


    private ArrayList<String> Product_Name_home = new ArrayList<String>();
    private ArrayList<String> Short_Description_home = new ArrayList<String>();
    private ArrayList<String> Long_Description_home = new ArrayList<String>();
    private ArrayList<String> Total_Price_home = new ArrayList<String>();
    private ArrayList<String> Discount_home = new ArrayList<String>();
    private ArrayList<String> Selling_Price_home = new ArrayList<String>();
    private ArrayList<String> image_home = new ArrayList<String>();


    private ArrayList<String> Product_Name_furniture = new ArrayList<String>();
    private ArrayList<String> Short_Description__furniture = new ArrayList<String>();
    private ArrayList<String> Long_Description__furniture = new ArrayList<String>();
    private ArrayList<String> Total_Price__furniture = new ArrayList<String>();
    private ArrayList<String> Discount__furniture = new ArrayList<String>();
    private ArrayList<String> Selling_Price__furniture = new ArrayList<String>();
    private ArrayList<String> image_furniture = new ArrayList<String>();

    public static database_handler getFakeWebServer() {

        if (null == fakeServer) {
            fakeServer = new database_handler();
        }
        return fakeServer;
    }

    void initiateFakeServer() {

        addCategory();

    }




    public void addCategory() {

        FirebaseFirestore Firebasefirestore;
        FirebaseAuth mAuth;

        mAuth = FirebaseAuth.getInstance();
        Firebasefirestore = FirebaseFirestore.getInstance();


        Product_Name_raw.clear();
        Product_Name_furniture.clear();
        Product_Name_home.clear();
        Product_Name_food.clear();

        Task<QuerySnapshot> doc1 = Firebasefirestore.collection("Raw Material")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e("Product_Result",document.get("Product Name")+" "+document.get("Short Description")+" "+document.get("Long Description")+" "+" "+document.get("Discount")+" " +document.get("Discount"));
                                Product_Name_raw.add(document.get("Product Name").toString());
                                Short_Description_raw.add(document.get("Short Description").toString());
                                Long_Description_raw.add(document.get("Long Description").toString());
                                Total_Price_raw.add(document.get("Total Price").toString());
                                Discount_raw.add(document.get("Discount").toString());
                                Selling_Price_raw.add(document.get("Selling Price").toString());
                                image_raw.add(document.get("image").toString());
                            }
                        } else {
                            Log.w("Result", "Error getting documents.", task.getException());
                        }
                    }
                });



        Task<QuerySnapshot> doc2 = Firebasefirestore.collection("Home-Made Items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e("Product_Result",document.get("Product Name")+" "+document.get("Short Description")+" "+document.get("Long Description")+" "+" "+document.get("Discount")+" " +document.get("Discount"));
                                Product_Name_home.add(document.get("Product Name").toString());
                                Short_Description_home.add(document.get("Short Description").toString());
                                Long_Description_home.add(document.get("Long Description").toString());
                                Total_Price_home.add(document.get("Total Price").toString());
                                Discount_home.add(document.get("Discount").toString());
                                Selling_Price_home.add(document.get("Selling Price").toString());
                                image_home.add(document.get("image").toString());
                            }
                        } else {
                            Log.w("Result", "Error getting documents.", task.getException());
                        }
                    }
                });




        Task<QuerySnapshot> doc3 = Firebasefirestore.collection("Furniture")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e("Product_Result",document.get("Product Name")+" "+document.get("Short Description")+" "+document.get("Long Description")+" "+" "+document.get("Discount")+" " +document.get("Discount"));
                                Product_Name_furniture.add(document.get("Product Name").toString());
                                Short_Description__furniture.add(document.get("Short Description").toString());
                                Long_Description__furniture.add(document.get("Long Description").toString());
                                Total_Price__furniture.add(document.get("Total Price").toString());
                                Discount__furniture.add(document.get("Discount").toString());
                                Selling_Price__furniture.add(document.get("Selling Price").toString());
                                image_furniture.add(document.get("image").toString());
                            }
                        } else {
                            Log.w("Result", "Error getting documents.", task.getException());
                        }
                    }
                });




        Task<QuerySnapshot> doc4 = Firebasefirestore.collection("Food Items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e("Product_Result",document.get("Product Name")+" "+document.get("Short Description")+" "+document.get("Long Description")+" "+" "+document.get("Discount")+" " +document.get("Discount"));
                                Product_Name_food.add(document.get("Product Name").toString());
                                Short_Description_food.add(document.get("Short Description").toString());
                                Long_Description_food.add(document.get("Long Description").toString());
                                Total_Price_food.add(document.get("Total Price").toString());
                                Discount_food.add(document.get("Discount").toString());
                                Selling_Price_food.add(document.get("Selling Price").toString());
                                image_food.add(document.get("image").toString());
                            }
                        } else {
                            Log.w("Result", "Error getting documents.", task.getException());
                        }
                    }
                });



        ArrayList<ProductCategoryModel> listOfCategory = new ArrayList<ProductCategoryModel>();


        listOfCategory
                .add(new ProductCategoryModel(
                        "Raw Materials",
                        "handloom industry,  and , small-scale industries, sericulture and the coir industry",
                        "10%",
                        "http://www.delimpex.ch/images/futtermuehle/futtermuehle_rohstoff_in_hand.jpg"));

        listOfCategory
                .add(new ProductCategoryModel(
                        "Home Made Items",
                        "General Houseware Items,Horticultural and Gardening Ceramics,Khadi,Village Industries, Handicrafts, Table Accessories and Houseware Decorative Items",
                        "15%",
                        "http://media.natgeotraveller.in/wp-content/uploads/2016/10/handmade-in-india.jpg"));

        listOfCategory
                .add(new ProductCategoryModel(
                        "Food Item",
                        "Sugar, Papadum, Rice, Oats, Wheat, Frozen Fruit",
                        "5%",
                        "https://www.laboratoriovalsusa.it/sites/default/files/Organic-Food.jpg"));

        listOfCategory
                .add(new ProductCategoryModel(
                        "Furnitures Items",
                        "Carpets,Wooden and Bamboo Arts,Reed Curtain and Reed Products,Glass Building Materials",
                        "5%",
                        "http://www.livspace.com/magazine/wp-content/uploads/2016/08/transitional-spaces-in-traditional-indian-style-interiors.jpg"));

        CenterRepository.getCenterRepository().setListOfCategory(listOfCategory);
    }

    public void getAllRawmaterial() {




        final ArrayList<Product> productlist = new ArrayList<Product>();

        final ArrayList<Product> finalProductlist = productlist;

        final ConcurrentHashMap<String, ArrayList<Product>> productMap = new ConcurrentHashMap<String, ArrayList<Product>>();


            for (int i=0;i<Product_Name_raw.size();i++) {
                productlist.add(new Product(
                        Product_Name_raw.get(i),
                        Short_Description_raw.get(i),
                        Long_Description_raw.get(i),
                        Total_Price_raw.get(i),
                        Discount_raw.get(i),
                        Selling_Price_raw.get(i),
                        "0",
                        image_raw.get(i),
                        Product_Name_raw.get(i)));
        }

        productMap.put("Raw Material", productlist);





        CenterRepository.getCenterRepository().setMapOfProductsInCategory(productMap);

    }

    public void getAllFurnitures() {

        ConcurrentHashMap<String, ArrayList<Product>> productMap = new ConcurrentHashMap<String, ArrayList<Product>>();

        ArrayList<Product> productlist = new ArrayList<Product>();


        for (int i=0;i<Product_Name_furniture.size();i++) {
            productlist.add(new Product(
                    Product_Name_furniture.get(i),
                    Short_Description__furniture.get(i),
                    Long_Description__furniture.get(i),
                    Total_Price__furniture.get(i),
                    Discount__furniture.get(i),
                    Selling_Price__furniture.get(i),
                    "0",
                    image_furniture.get(i),
                    Product_Name_furniture.get(i)));
        } productMap.put("FURNITURE", productlist);

        CenterRepository.getCenterRepository().setMapOfProductsInCategory(productMap);

    }

    public void getAllhomemade() {

        ConcurrentHashMap<String, ArrayList<Product>> productMap = new ConcurrentHashMap<String, ArrayList<Product>>();

        ArrayList<Product> productlist = new ArrayList<Product>();


        for (int i=0;i<Product_Name_home.size();i++) {
            productlist.add(new Product(
                    Product_Name_home.get(i),
                    Short_Description_home.get(i),
                    Long_Description_home.get(i),
                    Total_Price_home.get(i),
                    Discount_home.get(i),
                    Selling_Price_home.get(i),
                    "0",
                    image_home.get(i),
                    Product_Name_home.get(i)));
        } productMap.put("Home Made Items", productlist);

        CenterRepository.getCenterRepository().setMapOfProductsInCategory(productMap);

    }


    public void getAllfooditems() {

        ConcurrentHashMap<String, ArrayList<Product>> productMap = new ConcurrentHashMap<String, ArrayList<Product>>();

        ArrayList<Product> productlist = new ArrayList<Product>();


        for (int i=0;i<Product_Name_food.size();i++) {
            productlist.add(new Product(
                    Product_Name_food.get(i),
                    Short_Description_food.get(i),
                    Long_Description_food.get(i),
                    Total_Price_food.get(i),
                    Discount_food.get(i),
                    Selling_Price_food.get(i),
                    "0",
                    image_food.get(i),
                    Product_Name_food.get(i)));
        } productMap.put("Food", productlist);

        CenterRepository.getCenterRepository().setMapOfProductsInCategory(productMap);

    }

    public void getAllProducts(int productCategory) {

        if (productCategory == 0) {

            getAllRawmaterial();
        }
        else if(productCategory == 1){

            getAllhomemade();

        }
        else if(productCategory == 2){

            getAllfooditems();

        }
        else {

            getAllFurnitures();

        }

    }

}
