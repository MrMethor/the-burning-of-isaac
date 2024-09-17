import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;

public class Image {

    private boolean isVisible;
    private int leftUpperX;
    private int leftUpperY;
    private BufferedImage image;

    public Image(String imageFile) {      
        this.image = this.loadImage(imageFile);
        this.isVisible = false;
        this.leftUpperX = 0;
        this.leftUpperY = 0;
    }                   

    public void changeImage(String imageFile) {
        boolean drawn = this.isVisible;
        this.erase();
        this.image = this.loadImage(imageFile);
        if (drawn) {
            this.draw();
        }
    }    

    public void changePosition(double[] coords) {
        boolean drawn = this.isVisible;
        this.erase();
        this.leftUpperX = (int)coords[0] + 400 - this.width() / 2;
        this.leftUpperY = -(int)coords[1] + 300 - this.height() / 2;
        if (drawn) {
            this.draw();
        }
    }

    private BufferedImage loadImage(String file) {
        BufferedImage loadedImage = null;

        try {
            loadedImage = ImageIO.read(new File(file));
        } catch (IOException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "The file " + file + " couldn't be found.");
        }

        return loadedImage;
    }     

    private int width() {
        return this.image.getWidth();
    }

    private int height() {
        return this.image.getHeight();
    }    

    public void show() {      
        this.isVisible = true;
        this.draw();
    }

    public void hide() {       
        this.erase();
        this.isVisible = false;
    }  

    public void redraw (){
        Canvas canvas = Canvas.getCanvas();
        canvas.redraw();
    }

    public void eraseAll(){
        Canvas canvas = Canvas.getCanvas();
        canvas.erase();
        canvas.redraw();
    }

    private void draw() {
        if (this.isVisible) {
            Canvas canvas = Canvas.getCanvas();

            AffineTransform at = new AffineTransform();
            at.translate(this.leftUpperX + this.width() / 2, leftUpperY + this.height() / 2);
            at.translate(-this.width() / 2, -this.height() / 2);

            canvas.draw(this, image, at);
        }
    }

    private void erase() {
        if (this.isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}
