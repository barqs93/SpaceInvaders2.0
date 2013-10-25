package Game;

import java.awt.Color;
import java.awt.Graphics;


public class Laser extends Thing {

	boolean shot;
	int speed;


	Laser(double x, double y, double w, double h, double vx, double vy,
			double ax, double ay, Color c) {
		super(x, y, w, h, vx, vy, ax, ay, Color.green, null, false);
		shot = false;
	}

	void update(Graphics g, Enemy other){
		this.draw(g);
		this.move();
		this.borders();
		this.collision(other);
	}

	public void draw(Graphics g){
		if(visible == true) {
			if(img!=null) {
				g.drawImage(img, (int) x,(int) y,(int) w,(int) h, null);
			}
			else{
				g.setColor(c);
				g.fillRect((int) x,(int) y,(int) w,(int) h);
			}
		}
	}

	public void borders(){
		if(x > Window.w)
			visible = false;
		if(x < 0)
			visible = false;
		if(y > Window.h)
			visible = false;
		if(y < 0)
			visible = false;
	}

	public boolean collision(Enemy other){
		if(this.visible == true) {
			if(this.r.intersects(other.r)) {
				this.visible = false;
				other.visible = false;
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}


}
