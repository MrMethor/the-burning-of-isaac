public class DeathScreen {

    private Game game;
    private Image deathScreen;
    private Button[] buttons;
    private Sound music;

    public DeathScreen(Game game) {
        this.game = game;
        this.deathScreen = new Image("textures/death_screen.png");
        this.deathScreen.changePosition(new double[]{0, 0});
        this.buttons = new Button[2];
    }

    public void show(){
        if(this.game.music() != null)
            this.game.music().changeFile("music/dead.wav", false);
        this.game.changeState(3);
        this.deathScreen.show();
        newButton("textures/buttons/retry.png", 0);
        newButton("textures/buttons/exit.png", 1);
    }

    public void close(){
        for(int i = 0; i < this.buttons.length; i++){
            if(this.buttons[i] != null)
                this.buttons[i].hide();
            this.buttons[i] = null;
        }
        this.game.changeState(0);
        this.deathScreen.hide();
        if(this.game.music() != null)
            this.game.music().stop();
    }

    public void click(int x, int y){
        if(this.buttons[0].clicked(x, y)){
            close();
            this.game.newGame();
        }
        else if(this.buttons[1].clicked(x, y)){
            this.game.launcher().soundSetNull();
            close();
        }
    }

    private void newButton(String path, int slot){
        if(slot > -1 && slot < this.buttons.length)
            this.buttons[slot] = new Button(path, slot, 2, this.game.launcher());
    }
}
