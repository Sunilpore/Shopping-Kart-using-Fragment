package com.example.sunil.cartadd.Model;

import com.example.sunil.cartadd.Model.ProductModel;

public class CartModel {

     public int cartid,userid,prodid,cartquantity;
     public String cartProdname;
     ProductModel prodItem;

     public CartModel(int userid, int prodid, int cartquantity) {
          this.userid = userid;
          this.prodid = prodid;
//          this.cartProdname = cartProdname;
          this.cartquantity=cartquantity;
     }

     public CartModel(ProductModel prodItem, int cartquantity,int cartid) {
          this.prodItem = prodItem;
          this.cartquantity = cartquantity;
          this.cartid=cartid;
     }

    public CartModel(int prodid, ProductModel prodItem) {
        this.prodid = prodid;
        this.prodItem = prodItem;
    }

    public CartModel(int userid) {
        this.userid = userid;
    }

    public CartModel(ProductModel prodItem) {
        this.prodItem = prodItem;
    }

    public CartModel() {
     }

     public int getCartid() {
          return cartid;
     }

     public void setCartid(int cartid) {
          this.cartid = cartid;
     }

     public int getUserid() {
          return userid;
     }

     public void setUserid(int userid) {
          this.userid = userid;
     }

     public int getProdid() {
          return prodid;
     }

     public void setProdid(int prodid) {
          this.prodid = prodid;
     }

     public int getCartquantity() {
          return cartquantity;
     }

     public void setCartquantity(int cartquantity) {
          this.cartquantity = cartquantity;
     }

     public String getCartProdname() {
          return cartProdname;
     }

     public void setCartProdname(String cartProdname) {
          this.cartProdname = cartProdname;
     }

     public ProductModel getProdItem() {
          return prodItem;
     }

     public void setProdItem(ProductModel prodItem) {
          this.prodItem = prodItem;
     }
}