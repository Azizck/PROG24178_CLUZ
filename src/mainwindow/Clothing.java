/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainwindow;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

/**
 *
 * @author CS
 */
public class Clothing {
  
    public enum Type {
        Dress, Jackets, Shirts, Shorts;     
    }
    
    public enum Gender {
        Male, Female, Boys, Girls
    }
   
    public enum Colors {
        Red, Orange, Yellow, Green, Blue, White, Black;
    }
     
    public enum Size {
        XS, S, M, L, XL;
    }
    
    private int productId;
    private double price;
    private int quantity;
    
    private Colors color;
    private Gender gender;
    private Type type;
    private Size s;
    private String size;
    private Image img;
   
  public Clothing() {}
        
        public Clothing(int productId, Type type, Gender gender, String size, Colors color, double price, int quantity) {
            this.productId = productId;
            this.type = type;
            this.gender = gender;
            this.size = size;
            this.color = color;
            this.price = price;
            this.quantity = quantity;
           
        }
        
    public Image getImage() {
        return img;
    }
    public void setImage() {
        this.img = img;
    }
    public Colors getColor() {
        return color;
    }

    //public abstract void setSize(Size size);
    //public abstract Size getSize();
    public void setColor(Colors color) {    
        this.color = color;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    /*
    public Type getProductType() {
        return type;
    }

    public void setProductType(Type type) {
        this.type = type;
    }
    */
    
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

   
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    
    public int getProductId() {
        return productId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
    
    

    @Override
    public String toString() {
        return "Product ID: "+ productId+ ", Type: " + type + ", Gender: " + gender + 
                ", Quantity: " + quantity + ", Color: " + color + " , Size: " + 
                size + ", Price: $" + String.format("%.2f", price) + "\n";
    }

  
   
    
}
