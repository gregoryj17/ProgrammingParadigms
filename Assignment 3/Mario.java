import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;

public class Mario {

    final int width=60, height=95;

    int x=200;
    int y=0;
    double vert_vel=0;
    int frame;
    int lastGrounded;
    int pic=0;
    boolean moving=false;
    int scrollPos;
    int dropDistance, jumpDistance;
    static BufferedImage[] images = new BufferedImage[5];

    void update()
    {
        vert_vel += 1.2;
        if(vert_vel>0&&dropDistance<vert_vel){
            y+=dropDistance;
            vert_vel=0.0;
            lastGrounded=frame;
        }
        else if(vert_vel<0&&jumpDistance>vert_vel){
            y+=jumpDistance;
            vert_vel=0.0;
        }
        else{
            y += vert_vel;
        }
        if(y > 500)
        {
            vert_vel = 0.0;
            y = 500; // snap back to the ground
            lastGrounded=frame;
        }
        frame++;
        if(moving)pic=(pic+1)%5;
    }

    void jump(){
        if(frame-lastGrounded<=5) {
            vert_vel -= 5;
        }
    }

    void moving(boolean left, boolean right){
        moving = ((left^right)||vert_vel!=0);
    }

    boolean collidesWith(int x, int y, int w, int h, int scrollAmount, int scrollPos){
        this.scrollPos=scrollPos;
        if((x-scrollPos)>=(this.x+scrollAmount+width))return false;
        if((this.x+scrollAmount)>=(x-scrollPos+w))return false;
        if(y>=(this.y+height))return false;
        if(this.y>=(y+h))return false;
        return true;
    }

    boolean collidesX(int x, int w){
        if((x-scrollPos)>=(this.x+width))return false;
        if((this.x)>=(x-scrollPos+w))return false;
        return true;
    }

    void setDropDistance(int dd){
        dropDistance=dd;
    }

    void setJumpDistance(int jd){
        jumpDistance=jd;
    }

    BufferedImage getImage(){
        try{
            if(images[pic]==null){
                images[pic] = ImageIO.read(new File("mario"+(pic+1)+".png"));
            }
        }catch(Exception e){
            e.printStackTrace(System.err);
            System.exit(1);
        }

        return images[pic];
    }

}
