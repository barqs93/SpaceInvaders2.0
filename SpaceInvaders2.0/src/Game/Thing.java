package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;


import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Thing {
	volatile double x;
	volatile double y;
	volatile double w;
	volatile double h;
	volatile double vx;
	volatile double vy;
	volatile double vel;
	volatile double ang;
	volatile double acc;
	volatile double ax;
	volatile double ay;
	Color c;
	Rectangle r;
	Image img;
	static double limit = 5;
	boolean visible;
	

	Thing(double x, double y, double w, double h, double vx, double vy, double ax, double ay, Color c, File pic, boolean visible) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.vx = vx;
		this.vy = vy;
		this.ax = ax;
		this.ay = ay;
		this.c = c;
		this.vel = 0;
		this.visible = visible;
		r = new Rectangle((int)x,(int) y,(int) w,(int) h);
		if(pic == null) {

		}
		else {
			try {
				this.img = ImageIO.read(pic);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	void update(Graphics g, Thing other){
		this.draw(g);
		this.move();
		this.borders();
		this.collision(other);
	}

	void move() {
		

	
		
		
		if(vy == 0.0) {
			if(vx<0) 
				ang = 180;
			if(vx>0) 
				ang = 0;
		
		}
		else if(vx!=0) {
			ang = Math.toDegrees(Math.atan(vy/vx));
			if(vx < 0 && vy < 0)
				ang= - 90 - (90 - ang);
			if(vx < 0 && vy > 0)
				ang= 90 + (90 + ang);
		}
		else if (vy > 0)
			ang = 90;
		else if (vy < 0)
			ang = -90;	
		
		
		
		x = x + vx;
		y = y + vy;
	
		
		r = new Rectangle((int)x,(int) y,(int) w,(int) h);

		if(vel > limit) 
			vel = limit;
		
		
	
		vx = vel*Math.cos(Math.toRadians(ang)) + ax;
		
		
	
		vy = vel*Math.sin(Math.toRadians(ang)) + ay;
	
		
		
		vel = Math.sqrt(vx*vx + vy*vy);
		
		//slowing down
		
	
		
	}

	void borders() {
		if(x > Window.w)
			x = 0;
		if(x < 0)
			x = Window.w;
		if(y > Window.h)
			y =22;
		if(y < 22)
			y = Window.h;
	}

	public void draw(Graphics g){
		if(visible==true) {
			if(img!=null) {
				g.drawImage(img, (int) x,(int) y,(int) w,(int) h, null);
			}
			else{
				g.setColor(c);
				g.fillRect((int) x,(int) y,(int) w,(int) h);
			}
		}
		
	}

	boolean collision(Thing other) {
		if(this.r.intersects(other.r)) {
			return true;
		}
		else {
			return false;
		}
	}
}
