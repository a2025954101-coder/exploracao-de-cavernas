// O robo explora a caverna em busca de recursos. O caminho percorrido é
// definido pelo seu DNA.

import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;

public class Robo {
  public static final int ENERGIA_INICIAL = 100;
  private static final float TAXA_MUTACAO = 0.5f; // Testar diferentes valores

  private int energia = ENERGIA_INICIAL;

  private int[] dna;
  private ResultadoAvaliacao resultadoAvaliacao;

  public static class ResultadoAvaliacao {
    public Map<Recurso, Integer> recursosColetados;
    public int energiaConsumida;
    public boolean chegouBase;
    public long valorTotal;

    private ResultadoAvaliacao(HashMap<Recurso, Integer> recursosColetados,
                                int energiaConsumida, boolean chegouBase,
                                long valorTotal) {
      this.recursosColetados = recursosColetados;
      this.energiaConsumida = energiaConsumida;
      this.chegouBase = chegouBase;
      this.valorTotal = valorTotal;
    }
  }

  public int getEnergia() {
    return energia;
  }

  public ResultadoAvaliacao getResultadoAvaliacao() {
    return resultadoAvaliacao;
  }

  public ResultadoAvaliacao avaliar(Caverna caverna) {
    // Cada avaliação começa na base com a energia cheia.
    Map<Recurso, Integer> recursosColetados = new HashMap<>();
    boolean[] camarasVisitadas = new boolean[caverna.getQuantidadeCamaras()];
    int indiceBase = caverna.getIndiceInicio();
    int indiceAtual = indiceBase;

    int energiaRestante = ENERGIA_INICIAL;
    long valorTotal = 0;
    int movimentosRealizados = 0;

    // A rota só é executada se o primeiro gene indicar a base.
    boolean rotaIniciadaNaBase = dna.length > 0 && dna[0] == indiceBase;
    if (rotaIniciadaNaBase) {
      valorTotal = coletarRecursos(caverna.getCamara(indiceAtual), recursosColetados);
      camarasVisitadas[indiceAtual] = true;

      for (int i = 1; i < dna.length; i++) {
        int proximoIndice = dna[i];
        int custo = caverna.getCustoTravessia(indiceAtual, proximoIndice);
        // A simulação para se não tiver um túnel ou energia suficiente.
        if (custo < 0 || custo > energiaRestante) {
          break;
        }

        energiaRestante -= custo;
        indiceAtual = proximoIndice;
        movimentosRealizados++;

        if (!camarasVisitadas[indiceAtual]) {
          valorTotal += coletarRecursos(caverna.getCamara(indiceAtual), recursosColetados);
          camarasVisitadas[indiceAtual] = true;
        }
      }
    }

    energia = energiaRestante;
    // Chegar à base exige percorrer toda a rota e depois terminar nela.
    boolean chegouBase =
      rotaIniciadaNaBase
      && movimentosRealizados == dna.length - 1
      && indiceAtual == indiceBase;

    resultadoAvaliacao = new ResultadoAvaliacao(
      new HashMap<>(recursosColetados),
      ENERGIA_INICIAL - energiaRestante,
      chegouBase,
      valorTotal
    );
    return resultadoAvaliacao;
  }

  private long coletarRecursos(Caverna.Camara camara, Map<Recurso, Integer> coletados) {
    long valor = 0;
    for (Map.Entry<Recurso, Integer> recurso : camara.quantRecursos.entrySet()) {
      Recurso tipo = recurso.getKey();
      int quantidade = recurso.getValue();
      valor += (long) quantidade * tipo.getValor();

      if (coletados.containsKey(tipo)) {
        quantidade += coletados.get(tipo);
      }

      coletados.put(tipo, quantidade);
    }

    return valor;
  }

  public Robo reproduzir(PApplet p, int quantCamaras) {
    var novoDNA = dna.clone();

    if(p.random(0, 1) < TAXA_MUTACAO) {
      for(int k = 0; k < novoDNA.length; k++) {
        if(p.random(1) < 0.001) {
          int v = (int)p.random(-1.0f, 1.0f+1); // v entre [-1, 1]

          novoDNA[k] = (int)PApplet.constrain(
            (float)novoDNA[k] + v,
            0, quantCamaras-1
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
