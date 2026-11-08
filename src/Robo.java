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

  public Robo reproduzir(PApplet p, Caverna caverna) {
    var novoDNA = dna.clone();

    if(p.random(0, 1) < TAXA_MUTACAO) {
      for(int k = 1; k < novoDNA.length - 1; k++) {
        if(p.random(1) < 0.001) {
          int quantidadeOpcoes = 0;
          for (int candidato = 0; candidato < caverna.getQuantidadeCamaras(); candidato++) {
            if (caverna.getCustoTravessia(novoDNA[k - 1], candidato) >= 0
                && caverna.getCustoTravessia(candidato, novoDNA[k + 1]) >= 0) {
              quantidadeOpcoes++;
            }
          }

          if (quantidadeOpcoes > 0) {
            int opcaoEscolhida = (int) p.random(quantidadeOpcoes);
            for (int candidato = 0; candidato < caverna.getQuantidadeCamaras(); candidato++) {
              if (caverna.getCustoTravessia(novoDNA[k - 1], candidato) >= 0
                  && caverna.getCustoTravessia(candidato, novoDNA[k + 1]) >= 0) {
                if (opcaoEscolhida == 0) {
                  novoDNA[k] = candidato;
                  break;
                }
                opcaoEscolhida--;
              }
            }
          }
        }
      }
    }

    return new Robo(caverna, novoDNA);
  }

  public Robo(PApplet p, Caverna caverna, int quantidadePassos) {
    this(caverna, gerarDNA(p, caverna, quantidadePassos));
  }

  public Robo(Caverna caverna, int[] dna) {
    if (caverna == null || dna == null || dna.length < 3) {
      throw new IllegalArgumentException("A rota precisa começar e terminar na base, com ao menos um túnel.");
    }

    int indiceBase = caverna.getIndiceInicio();
    if (indiceBase < 0 || indiceBase >= caverna.getQuantidadeCamaras()) {
      throw new IllegalArgumentException("O índice da base não corresponde a uma câmara.");
    }
    if (dna[0] != indiceBase || dna[dna.length - 1] != indiceBase) {
      throw new IllegalArgumentException("A rota precisa começar e terminar na base.");
    }

    for (int i = 1; i < dna.length; i++) {
      if (dna[i - 1] < 0 || dna[i - 1] >= caverna.getQuantidadeCamaras()
          || dna[i] < 0 || dna[i] >= caverna.getQuantidadeCamaras()) {
        throw new IllegalArgumentException("A rota contém o índice de uma câmara inexistente.");
      }
      if (caverna.getCustoTravessia(dna[i - 1], dna[i]) < 0) {
        throw new IllegalArgumentException("A rota contém duas câmaras sem túnel entre elas.");
      }
    }

    this.dna = dna.clone();
  }

  private static int[] gerarDNA(PApplet p, Caverna caverna, int quantidadePassos) {
    if (p == null || caverna == null || quantidadePassos < 1) {
      throw new IllegalArgumentException("É necessário informar a caverna e ao menos um passo.");
    }

    int[] ida = new int[quantidadePassos + 1];
    ida[0] = caverna.getIndiceInicio();
    int passosRealizados = 0;

    for (int passo = 0; passo < quantidadePassos; passo++) {
      int origem = ida[passosRealizados];
      int quantidadeVizinhos = 0;

      for (int candidato = 0; candidato < caverna.getQuantidadeCamaras(); candidato++) {
        if (caverna.getCustoTravessia(origem, candidato) >= 0
            && caverna.getCustoTravessia(candidato, origem) >= 0) {
          quantidadeVizinhos++;
        }
      }

      if (quantidadeVizinhos == 0) {
        break;
      }

      int vizinhoEscolhido = (int) p.random(quantidadeVizinhos);
      for (int candidato = 0; candidato < caverna.getQuantidadeCamaras(); candidato++) {
        if (caverna.getCustoTravessia(origem, candidato) >= 0
            && caverna.getCustoTravessia(candidato, origem) >= 0) {
          if (vizinhoEscolhido == 0) {
            ida[++passosRealizados] = candidato;
            break;
          }
          vizinhoEscolhido--;
        }
      }
    }

    if (passosRealizados == 0) {
      throw new IllegalArgumentException("A câmara-base não possui túneis para iniciar uma rota.");
    }

    // O caminho de volta repete os passos da ida na ordem inversa.
    int[] dna = new int[passosRealizados * 2 + 1];
    int indice = 0;
    for (int i = 0; i <= passosRealizados; i++) {
      dna[indice++] = ida[i];
    }
    for (int i = passosRealizados - 1; i >= 0; i--) {
      dna[indice++] = ida[i];
    }

    return dna;
  }
}
