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
    Robo descendente = new Robo(dna.clone());

    if(p.random(0, 1) < TAXA_MUTACAO) {
      float posGene = p.random(0, dna.length);

      // TODO: Modificar gene
    }

    return null;
  }

  public Robo(int[] dna) {
    this.dna = dna;
  }
}
