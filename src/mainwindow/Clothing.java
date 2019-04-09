package mainwindow;

/**
 * The clothing class contains various types, gender, colors, size, ID, 
 * price, and quantity of the clothing item.
 *
 * @version 1.0
 * @author Jingwei Sun, John Chen, and Aziz Omar
 */
public class Clothing {

    /**
     * Types of clothing
     */
    public enum Type {
        Dress, Jeans, Outerwear, Pants, Shirts, Shorts, Skirts, Sleepwear, Sweaters;
    }

    /**
     * Genders of clothing
     */
    public enum Gender {
        Male, Female, Boys, Girls
    }

    /**
     * Colors of clothing
     */
    public enum Colors {
        Red, Orange, Yellow, Green, Blue, White, Black;
    }

    private int productId;
    private double price;
    private int quantity;

    private Colors color;
    private Gender gender;
    private Type type;
    private String size;
    private String url;

    public Clothing() {
    }

    /**  
    * Constructor of the clothing class that takes in the parameter and sets it to the variables.
    * @param productId Unique product ID of clothing item
    * @param type The type of clothing selected
    * @param gender The gender of clothing item
    * @param size The size of clothing item
    * @param color The color of clothing item
    * @param price The price of clothing item
    * @param quantity The quantity of clothing item
    */  
    public Clothing(int productId, Type type, Gender gender, String size, Colors color, double price, int quantity) {
        this.productId = productId;
        this.type = type;
        this.gender = gender;
        this.size = size;
        this.color = color;
        this.price = price;
        this.quantity = quantity;

    }

    /**
     * @return current URL of clothing item
     */
    public String getURL() {
        return url;
    }

    /**
     * @param url sets the URL as String
     */
    public void setURL(String url) {
        this.url = url;
    }

    /**
     * @return current color of the clothing item
     */
    public Colors getColor() {
        return color;
    }

    /**
     * @param color sets the color of clothing item
     */
    public void setColor(Colors color) {
        this.color = color;
    }

    /**
     * @param productId sets the unique ID of clothing item
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * @return current price of the clothing item
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price sets the price of the clothing item
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return current gender of the clothing item
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * @param gender sets the gender of the clothing item
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * @return current quantity of clothing items
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity sets the quantity of clothing items
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return current unique product ID of the clothing item
     */
    public int getProductId() {
        return productId;
    }

    /**
     * @return the type of clothing item
     */
    public Type getType() {
        return type;
    }

    /**
     * @param type sets the type of item
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * @return current size of the clothing item
     */
    public String getSize() {
        return size;
    }

    /**
     * @param size sets the size of the item
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * @return the toString output
     */
    @Override
    public String toString() {
        return "Product ID: " + productId + ", Type: " + type + ", Gender: " + gender
                + ", Quantity: " + quantity + ", Color: " + color + " , Size: "
                + size + ", Price: $" + String.format("%.2f", price) + "\n";
    }

}
