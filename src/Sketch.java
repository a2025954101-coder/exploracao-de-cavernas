import processing.core.PApplet;

public class Sketch extends PApplet {
  public static void main(String[] args) {
    PApplet.main("Sketch");
  }

  int corFundo;

  // Estados do programa
  TelaMenu menu;
  boolean estaPausado;

  Caverna caverna;
  DesenhadorCaverna desenhador;

  @Override
  public void settings() {
    size(800, 600);
  }

  @Override
  public void setup() {
    menu = new TelaMenu(this);
    estaPausado = false;

    caverna = GeradorCaverna.gerar();
    desenhador = new DesenhadorCaverna(this, caverna);
  }

  @Override
  public void draw() {
    if(estaPausado) {
      desenhaPause();
      return;
    }

    background(corFundo);

    if(!menu.getProximaEtapa()) {
      desenhaMenu();
      return;
    }

    desenhador.atualiza();
  }

  @Override
  public void keyPressed() {
    switch (key) {
      case 'P': 
      case 'p':
        estaPausado = !estaPausado;
        loop();

        break;
    
      default:
        break;
    }
  }

  public void desenhaPause() {
    fill(0x8d, 0x8b, 0x8b, 0x55); // #8d8b8b
    rect(width/2, height/2, width, height);

    noLoop();
  }

  public void desenhaMenu() {
    menu.atualiza();
    corFundo = 0xFF725b39; // #725b39
  }
}

