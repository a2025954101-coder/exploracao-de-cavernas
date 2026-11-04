// O robo explora a caverna em busca de recursos. O caminho percorrido é
// definido pelo seu DNA.

import processing.core.PApplet;

public class Robo {
  public static final int ENERGIA_INICIAL = 100;
  private static final float TAXA_MUTACAO = 0.5f; // Testar diferentes valores

  private int energia = ENERGIA_INICIAL;

  private int[] dna;

  public int getEnergia() {
    return energia;
  }

  public Robo reproduzir(PApplet p) {
    var novoDNA = dna.clone();

    if(p.random(0, 1) < TAXA_MUTACAO) {
      for(int k = 0; k < novoDNA.length; k++) {
        if(p.random(1) < 0.001) {
          novoDNA[k] = (int)PApplet.constrain(
            (float)novoDNA[k] +
            p.random(-0.1f, 0.1f), 0.0f, 1.0f
          );
        }
      }
    }

    return new Robo(novoDNA);
  }

  public Robo(int[] dna) {
    this.dna = dna;
  }
}
