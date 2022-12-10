import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class The_Game_of_Life extends PApplet {

char resetkey='r';
char randomisekey='k';
char pausekey='p';
char stepkey='q';

boolean paused=true;

boolean mousedown=false;

int framerate=40;

int prevsquarechangedX=-1;
int prevsquarechangedY=-1;
float timeelapsedsinceclick;
float cellchangedelay=1*framerate;


int cols= 50;
int rows=50;
boolean[][] grid = new boolean [cols][rows];
boolean [][] oldgrid=new boolean[cols][rows];
int gridsize=1000;
int textcolumnwidth=220;
float spacing=gridsize/rows;


public void settings() {
  size(gridsize+textcolumnwidth, gridsize);
}

public void setup() {

  timeelapsedsinceclick=cellchangedelay;
  frameRate(framerate);

  resetgrid();
}

public void draw() {



  for (int j =0; j< rows; j+=1) {
    for (int i =0; i< cols; i+=1) {
      oldgrid[i][j]=grid[i][j];
    }
  }

  //oldgrid=grid;

  if (!paused) {
    updategrid();
  }

  int x=floor(PApplet.parseFloat(mouseX * cols/gridsize));
  int y=floor(PApplet.parseFloat(mouseY * rows/gridsize));

  if (mousedown && mouseX<gridsize && mouseY<gridsize ) {


    if (prevsquarechangedX!=x || prevsquarechangedY!=y || timeelapsedsinceclick>=cellchangedelay) {
      grid[x][y]=!grid[x][y];
      prevsquarechangedX=x;
      prevsquarechangedY=y;
      timeelapsedsinceclick=0;
    }
  }
  if (timeelapsedsinceclick<cellchangedelay) {
    timeelapsedsinceclick+=1;
  }

  drawgrid();
  drawtextcolumn();
}


public void updategrid() {

  for (int y =0; y< rows; y+=1) {
    for (int x =0; x< cols; x+=1) {
      grid[x][y]=updatecell(x, y, oldgrid[x][y]);
    }
  }
}

public boolean updatecell(int xpos, int ypos, boolean state) {
  //println("cell:   "  + xpos +"---"+ ypos );
  int livecells=0;
  for (int y =ypos-1; y<=ypos+1; y+=1) {
    for (int x =xpos-1; x<=xpos+1; x+=1) {

      if (x>=0 && x<cols && y>=0 && y<rows && !(x==xpos && y==ypos)) {
        // println("scanning ---" + "x:" + x +"  y:" +y + "-----" + oldgrid[x][y]);
        if (oldgrid[x][y]==true) {
          livecells+=1;
        }
      }
    }
  }
  //println("livecell  :"+livecells);
  if ((state==true && (livecells==2 || livecells==3)) || (state==false && livecells==3)) {
    return true;
  }
  return false;
}

public void drawgrid() {

  //float xspacing=(float)width/cols;
  //float yspacing=(float)height/rows;
  int colour=0;

  for (int y =0; y< rows; y+=1) {

    for (int x =0; x< cols; x+=1) {

      if (grid[y][x]==false) {
        colour=0   ;
      } else if (grid[y][x]==true )
      {
        colour=255;
      }
      fill(colour);
      stroke(110, 70, 70);
      rect(y*spacing, x*spacing, spacing, spacing );
    }
  }
}
public void resetgrid() {
  for (int y =0; y< rows; y+=1) {
    for (int x =0; x< cols; x+=1) {
      grid[y][x]=false;
    }
  }
}

public void randomisegrid() {
  for (int y =0; y< rows; y+=1) {
    for (int x =0; x< cols; x+=1) {
      grid[y][x]=randombool();
    }
  }
}

public void pause() {
  paused=!paused;
}

public void step() {
  updategrid();
}


public void keyPressed() {
  if (key==resetkey) {
    resetgrid();
  } else if (key==randomisekey) {
    randomisegrid();
  } else if (key==pausekey) {
    pause();
  } else if (key==stepkey) {
    step();
  }
}

public void mousePressed() {
  mousedown=true;
}
public void mouseReleased() {
  mousedown=false;
}

public boolean randombool() {
  return random(1)>0.5f;
}

public void drawtextcolumn() {
  stroke(100);
  textFont(createFont("Arial", 20, true), 20);
  fill(0);
  text("Press 'P' to Pause", gridsize+10, 100);
  text("Press 'R' to Reset", gridsize+10, 200);
  text("Press 'K' to Randomise", gridsize+10, 300);
  text("Press 'Q' to Step", gridsize+10, 400);
  text("Use Mouse to Draw", gridsize+10, 500);
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "The_Game_of_Life" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
