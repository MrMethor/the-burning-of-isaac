import java.util.ArrayList;

public class HUD {

    private Map map;
    private ArrayList<Image> hearts;
    private Image halfHeart;
    private Image[][] rooms;
    private Image upgrade;
    private int upgradeTimer;

    public HUD(Map map) {
        this.map = map;
        this.hearts = new ArrayList<Image>();
        this.rooms = new Image[5][5];
        this.upgradeTimer = 0;
    }

    // Updates the HUD
    public void healthUpdate(int health){
        healthReset();
        int fullHearts = health / 2;
        int halfHearts = health % 2;
        int i;
        for(i = 0; i < fullHearts; i++){
            this.hearts.add(new Image("textures/heart_full.png"));
            this.hearts.get(i).changePosition(new double[] {-350 + (i * 60), 250});
            this.hearts.get(i).show();
        }
        if(halfHearts != 0){
            this.halfHeart = new Image("textures/heart_half.png");
            this.halfHeart.changePosition(new double[] {-350 + (i * 60), 250});
            this.halfHeart.show();
        }
    }

    private void healthReset(){
        for(int i = 0; i < this.hearts.size(); i++){
            this.hearts.get(0).hide();
            this.hearts.remove(0);
        }
        if(this.halfHeart != null)
            this.halfHeart.hide();
        this.halfHeart = null;
    }

    public void minimapUpdate(){
        minimapReset();
        for(int i = 0; i < this.map.rooms().length; i++){
            for(int j = 0; j < this.map.rooms()[0].length; j++){
                if(this.map.rooms()[i][j] != null){
                    if(this.map.rooms()[i][j] == this.map.currentRoom())
                        this.rooms[i][j] = new Image("textures/minimap_current.png");
                    else if(this.map.rooms()[i][j].isCleared())
                        this.rooms[i][j] = new Image("textures/minimap_explored.png");
                    else
                        this.rooms[i][j] = new Image("textures/minimap_unexplored.png");
                    this.rooms[i][j].changePosition(new double[] {(j * 30) + 240, (-i * 15) + 280});
                    this.rooms[i][j].show();
                }
            }
        }
    }

    private void minimapReset(){
        for(int i = 0; i < this.rooms.length; i++){
            for(int j = 0; j < this.rooms[0].length; j++){
                if(this.rooms[i][j] != null)
                    this.rooms[i][j].hide();
                this.rooms[i][j] = null;
            }
        }
    }
    
    public void upgradeUpdate(String type){
        if(this.upgrade != null){
            this.upgrade.hide();
            this.upgrade = null;
        }
        this.upgradeTimer = 180;
        this.upgrade = new Image("textures/upgrades/" + type + ".png");
        this.upgrade.changePosition(new double[] {-320, 215});
        this.upgrade.show();
    }
    
    public void upgradeTimer(){
        if(this.upgradeTimer > 0)
            this.upgradeTimer--;
        else{
            if(this.upgrade != null){
                this.upgrade.hide();
                this.upgrade = null;
            }
        }
    }
    
    public void newMap(Map map){
        this.map = map;
    }

    public void close(){
        healthReset();
        minimapReset();
    }
}
