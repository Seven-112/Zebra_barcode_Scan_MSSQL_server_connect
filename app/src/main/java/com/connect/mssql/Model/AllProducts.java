package com.connect.mssql.Model;

import java.util.ArrayList;
import java.util.List;

public class AllProducts {
    private static ArrayList<List> allproducts = new ArrayList<>();

    public AllProducts(){

    }
    public AllProducts(ArrayList<List> allproducts){

        this.allproducts = allproducts;

    }

    public ArrayList<List> getAllproducts() {
        return allproducts;
    }
    public void setAllproducts (ArrayList<List> allproducts) {

        this.allproducts = allproducts;

    }
}
