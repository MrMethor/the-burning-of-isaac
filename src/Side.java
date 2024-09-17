public enum Side {
    RIGHT(0, "right"),
    LEFT(1, "left"),
    UP(2, "up"),
    DOWN(3, "down");
    
    private int number;
    private String name;
    
    private Side(int number, String name){
        this.number = number;
        this.name = name;
    }
    
    public int number(){
        return this.number;
    }
}
