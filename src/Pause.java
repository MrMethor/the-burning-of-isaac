public class Pause {

    private Game game;
    private Image background;
    private Button[] buttons;
    private Sound music; 

    public Pause(Game game) {
        this.game = game;
        this.background = new Image("textures/pause_background.png");
        this.background.changePosition(new double[]{0, 0});
        this.buttons = new Button[3];
    }

    public void show(){
        if(this.game.music() != null)
            this.game.music().changeFile("music/combat_menu.wav", true);
        this.game.changeState(2);
        this.background.show();
        newButton("textures/buttons/continue.png", 0);
        newButton("textures/buttons/save.png", 1);
        newButton("textures/buttons/exit.png", 2);
    }

    public void close(){
        for(int i = 0; i < this.buttons.length; i++){
            if(this.buttons[i] != null)
                this.buttons[i].hide();
            this.buttons[i] = null;
        }
        this.game.player().reset();
        this.game.changeState(1);
        this.background.hide();
        if(this.game.music() != null)
            this.game.music().changeFile("music/combat.wav", true);
    }

    public void click(int x, int y){
        if(this.buttons[0].clicked(x, y))
            close();
        else if(this.buttons[1].clicked(x, y)){}
        // Work in progress
        else if(this.buttons[2].clicked(x, y)){
            close();
            this.game.launcher().soundSetNull();
            if(this.game.music() != null)
                this.game.music().stop();
            this.game.changeState(0);
        }
    }

    private void newButton(String path, int slot){
        if(slot > -1 && slot < this.buttons.length)
            this.buttons[slot] = new Button(path, slot, 1, this.game.launcher());
    }
}
