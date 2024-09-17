public class Item {

    private Image texture;
    private double[] coords;
    private int ID;
    private Hitbox hitbox;

    public Item(int x, int y, int ID) {
        this.coords = new double[] {x, y};
        this.ID = ID;
        this.hitbox = new Hitbox(25, 25, this.coords, false);
        switch(ID){
            case 0:
            this.texture = new Image("textures/items/heart.png");
            break;
            case 1:
            this.texture = new Image("textures/items/heart_half.png");
            break;
            case 2:
            this.texture = new Image("textures/items/power_up.png");
            break;
        }
        this.texture.changePosition(this.coords);
    }

    public int id(){
        return this.ID;
    }

    public void show(){
        this.texture.show();
    }

    public void close(){
        this.texture.hide();
    }

    public Hitbox hitbox(){
        return this.hitbox;
    }
}
