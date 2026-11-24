import processing.core.PApplet;

public class Sketch extends PApplet {
  private static final int FRAMES_POR_PASSO = 60;

  public static void main(String[] args) {
    PApplet.main("Sketch");
  }

  int corFundo;

  // Estados do programa
  TelaMenu menu;
  boolean estaPausado;

  Caverna caverna;
  DesenhadorCaverna desenhador;
  Populacao populacao;
  int indicePassoRobo;
  int ultimoAvancoRobo;

  @Override
  public void settings() {
    size(1000, 800);
  }

  @Override
  public void setup() {
    menu = new TelaMenu(this);
    estaPausado = false;

    caverna = GeradorCaverna.gerar();
    desenhador = new DesenhadorCaverna(this, caverna);

    populacao = new Populacao(this, caverna, 40, 6);
    indicePassoRobo = 0;
    ultimoAvancoRobo = 0;
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
    atualizaRobo();
  }

  private void atualizaRobo() {
    Robo melhor = populacao.getMelhor();
    int[] rota = melhor.getDNA();
    if (rota.length == 0) return;

    if (frameCount - ultimoAvancoRobo >= FRAMES_POR_PASSO) {
      ultimoAvancoRobo = frameCount;
      indicePassoRobo++;

      if (indicePassoRobo >= rota.length) {
        populacao.proximaGeracao();
        indicePassoRobo = 0;
        rota = populacao.getMelhor().getDNA();
      }
    }

    int melhorAtual = populacao.getRobos().indexOf(populacao.getMelhor());

    for (int i = 0; i < populacao.getRobos().size(); i++) {
      Robo robo = populacao.getRobos().get(i);
      int[] rotaRobo = robo.getDNA();

      if (rotaRobo.length == 0) continue;

      int passoAtual = Math.min(indicePassoRobo, rotaRobo.length - 1);
      int cameraAtual = rotaRobo[passoAtual];
      
      desenhador.desenhaRobo(cameraAtual, i, populacao.getRobos().size(),
        i == melhorAtual);
    }

    fazTitulo(rota);
  }

  public void fazTitulo(int[] rota) {
    Robo melhor = populacao.getMelhor();
    int totalRecursos = 0;
    for (int quantidade : melhor.getResultadoAvaliacao().recursosColetados.values()) {
      totalRecursos += quantidade;
    }

    String resumo = "Melhor robô — recursos coletados: " + totalRecursos;
    windowTitle(resumo + " | Geração: " + populacao.getGeracao());
    pushStyle();
    fill(255);
    textAlign(LEFT, TOP);
    textSize(18);
    text(resumo, 12, 12);
    textSize(14);
    text("Geração: " + populacao.getGeracao()
      + "   Passo: " + (indicePassoRobo + 1) + "/" + rota.length
      + "   Robôs: " + populacao.getRobos().size(), 12, 36);
    popStyle();
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

