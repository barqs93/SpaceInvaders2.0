package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;

public class Plane extends Thing{
	int health;


	Plane(double x, double y, double w, double h, double vx, double vy, double ax, double ay, Color c, File pic) {
		super(x, y, w, h, vx, vy, ax, ay, c, pic, true);
		health = 100;

	}

	void update(Graphics g, Thing other){
		this.draw(g);
		this.move();
		this.borders();
		this.collision(other);
	}

	public boolean collision(Enemy other){
		if(this.r.intersects(other.r)  && other.visible == true) {
			health--;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		}
		else {
			return false;
		}
	}
}





