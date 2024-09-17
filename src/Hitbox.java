public class Hitbox {

    private double[] hitbox;
    private double[] coords;
    private double[] size;
    private boolean player;

    public Hitbox(double width, double height, double[] coords, boolean player) {
        this.coords = coords;
        this.player = player;
        this.size = new double[] {width, height};
        double[] newHitbox;
        if(this.player)
            newHitbox = new double[] {this.coords[0] + (this.size[0] / 2), this.coords[0] - (this.size[0] / 2), 
                this.coords[1], this.coords[1] - (this.size[1] / 2)};
        else
            newHitbox = new double[] {this.coords[0] + (this.size[0] / 2), this.coords[0] - (this.size[0] / 2), 
                this.coords[1] + (this.size[1] / 2), this.coords[1] - (this.size[1] / 2)};
        this.hitbox = newHitbox;
    }

    // Updates a hitbox
    public void update(double[] coords){
        if(this.player)
            this.hitbox = new double[] {this.coords[0] + (this.size[0] / 2), this.coords[0] - (this.size[0] / 2), 
                this.coords[1], this.coords[1] - (this.size[1] / 2)};
        else
            this.hitbox = new double[] {this.coords[0] + (this.size[0] / 2), this.coords[0] - (this.size[0] / 2), 
                this.coords[1] + (this.size[1] / 2), this.coords[1] - (this.size[1] / 2)};
    }

    // Checks if hitbox colided with other hitbox
    public char hit(Hitbox hitbox){
        if(((this.hitbox[0] > hitbox.get()[1] && this.hitbox[0] < hitbox.get()[0]) || (this.hitbox[1] < hitbox.get()[0] && this.hitbox[1] > hitbox.get()[1]))
        && ((this.hitbox[2] > hitbox.get()[3] && this.hitbox[2] < hitbox.get()[2]) || (this.hitbox[3] < hitbox.get()[2] && this.hitbox[3] > hitbox.get()[3]))){
            char min;
            double R = hitbox.get()[0] - this.hitbox[1];
            double L = this.hitbox[0] - hitbox.get()[1];
            double U = hitbox.get()[2] - this.hitbox[3];
            double D = this.hitbox[2] - hitbox.get()[3];
            double subtotal;
            if(R < L){
                min = 'R';
                subtotal = R;
            }
            else{
                min = 'L';
                subtotal = L;
            }
            if(subtotal > U){
                min = 'U';
                subtotal = U;
            }
            if(subtotal > D){
                min = 'D';
                subtotal = D;
            }
            return min;
        }
        else
            return 'n';
    }

    public void changeCoords(double[] coords){
        this.coords = coords;
    }

    // Getters
    public double[] get(){
        return this.hitbox;
    }

    public double height(){
        return this.size[1];
    }

    public double side(int side){
        return this.hitbox[side];
    }
}