public class Mario {

    final int width=60, height=95;

    int x=200;
    int y=0;
    double vert_vel=0;
    int frame;
    int lastGrounded;
    int pic=0;
    boolean moving=false;

    void update()
    {
        vert_vel += 1.2;
        y += vert_vel;
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
        if((x-scrollPos)>(this.x+scrollAmount+width))return false;
        if((this.x+scrollAmount)>(x-scrollPos+w))return false;
        if(y>(this.y+height))return false;
        if(this.y>(y+h))return false;
        return true;
    }

}
