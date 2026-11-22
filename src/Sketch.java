import processing.core.PApplet;

public class Sketch extends PApplet {
  public static void main(String[] args) {
    PApplet.main("Sketch");
  }

  TelaMenu menu;
  static int corFundo;

  @Override
  public void settings() {
    size(800, 600);
  }

  @Override
  public void setup() {
    menu = new TelaMenu(this);
  }

  @Override
  public void draw() {
    background(corFundo); 

    if(!menu.getProximaEtapa()) {
      menu.atualiza();
      corFundo = 0xFF664a32; // #664a32
      return;
    }
    
  }
}

