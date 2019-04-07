package mainwindow;

/**
 * This project is developed for a clothing retailer whose needs are to manage
 * inventory on a day-to-day basis. The required functionalities are adding,
 * editing, removing items while giving users the freedom to select clothing
 * types accordingly.
 *
 * April 5th, 2019
 *
 * @author Jingwei Sun, John Chen, Aziz Omar
 */

public class Clothing {
  
    public enum Type {
        Dress, Jeans, Outerwear, Pants, Shirts, Shorts, Skirts, Sleepwear, Sweaters;  
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
    private String url;

   
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
    
    public String getURL() {
    return url;
}
    
    public void setURL(String url) {
        this.url = url;
    }
    
    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {    
        this.color = color;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
    
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
