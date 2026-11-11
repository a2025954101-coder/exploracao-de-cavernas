import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import processing.core.PApplet;

// Faz a avaliação e evolução de um conjunto de robôs.
public class Populacao {
  private static final long PENALIDADE_SEM_RETORNO = 100_000L;

  private final PApplet p;
  private final Caverna caverna;
  private final int tamanho;
  private final int quantidadePassos;
  private List<Robo> robos;
  private List<Long> aptidoes;
  private Robo melhor;
  private long melhorAptidao;
  private int geracao;

  public Populacao(PApplet p, Caverna caverna, int tamanho,
                   int quantidadePassos) {
    this.p = p;
    this.caverna = caverna;
    this.tamanho = tamanho;
    this.quantidadePassos = quantidadePassos;
    this.robos = new ArrayList<>();
    this.aptidoes = new ArrayList<>();

    for (int i = 0; i < tamanho; i++) {
      robos.add(new Robo(p, caverna, quantidadePassos));
    }

    avaliar();
  }

  public void avaliar() {
    aptidoes = new ArrayList<>(robos.size());
    melhor = null;
    melhorAptidao = Long.MIN_VALUE;

    for (Robo robo : robos) {
      Robo.ResultadoAvaliacao resultado = robo.avaliar(caverna);
      long aptidao = resultado.valorTotal - resultado.energiaConsumida;
      if (!resultado.chegouBase) {
        aptidao -= PENALIDADE_SEM_RETORNO;
      }
      aptidoes.add(aptidao);

      if (melhor == null || aptidao > melhorAptidao) {
        melhor = robo;
        melhorAptidao = aptidao;
      }
    }
  }

  public void proximaGeracao() {
    avaliar();
    Robo elite = melhor;
    long menorAptidao = Collections.min(aptidoes);

    List<Robo> novosRobos = new ArrayList<>(tamanho);
    novosRobos.add(elite);
    while (novosRobos.size() < tamanho) {
      Robo selecionado = selecionarPorRoleta(menorAptidao);
      novosRobos.add(selecionado.reproduzir(p, caverna));
    }

    robos = novosRobos;
    geracao++;
    avaliar();
  }

  private Robo selecionarPorRoleta(long menorAptidao) {
    double pesoTotal = 0;
    for (long aptidao : aptidoes) {
      pesoTotal += (double) (aptidao - menorAptidao) + 1.0;
    }

    double sorteio = p.random((float) pesoTotal);
    for (int i = 0; i < robos.size(); i++) {
      sorteio -= (double) (aptidoes.get(i) - menorAptidao) + 1.0;
      if (sorteio < 0) {
        return robos.get(i);
      }
    }

    return robos.get(robos.size() - 1);
  }

  public int getGeracao() {
    return geracao;
  }

  public Robo getMelhor() {
    return melhor;
  }
  
  public long getMelhorAptidao() {
    return melhorAptidao;
  }

  public List<Robo> getRobos() {
    return robos;
  }
}
