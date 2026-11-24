import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeradorCaverna {

  private static final int QUANTIDADE_MINIMA_CAMARAS = 15;
  private static final int QUANTIDADE_MAXIMA_CAMARAS = 25;

  private static final double CHANCE_TUNEL_EXTRA = 0.25;
  private static final double CHANCE_CAMARA_COM_RECURSO = 0.75;

  private static final int CUSTO_MINIMO_TUNEL = 5;
  private static final int CUSTO_MAXIMO_TUNEL = 20;
  private static final int QUANTIDADE_MINIMA_RECURSOS = 1;
  private static final int QUANTIDADE_MAXIMA_RECURSOS = 4;
  private static final int QUANTIDADE_MINIMA_UNIDADES = 1;
  private static final int QUANTIDADE_MAXIMA_UNIDADES = 5;

  /** Gera uma caverna aleatória e conectada, com recursos e túneis. */
  public static Caverna gerar() {
    Random aleatorio = new Random();

    int quantidadeCamaras = aleatorio.nextInt(
      QUANTIDADE_MAXIMA_CAMARAS - QUANTIDADE_MINIMA_CAMARAS + 1
    ) + QUANTIDADE_MINIMA_CAMARAS;

    Caverna.Camara[] camaras = new Caverna.Camara[quantidadeCamaras];

    // Cria e inicializa as câmaras.
    for (int i = 0; i < quantidadeCamaras; i++) {
      camaras[i] = new Caverna.Camara();
      inicializarRecursos(camaras[i], aleatorio);
    }

    // Primeiro cria uma árvore de conexões, garantindo que nenhuma
    // câmara fique isolada e que todas possam ser alcançadas pela entrada.
    for (int i = 1; i < quantidadeCamaras; i++) {
      int outraCamara = aleatorio.nextInt(i);
      conectar(camaras, i, outraCamara, aleatorio);
    }

    // Depois adiciona túneis extras para criar caminhos alternativos.
    for (int i = 0; i < quantidadeCamaras; i++) {
      for (int j = i + 1; j < quantidadeCamaras; j++) {
        if (!possuiConexao(camaras[i], camaras[j])
            && aleatorio.nextDouble() < CHANCE_TUNEL_EXTRA) {
          conectar(camaras, i, j, aleatorio);
        }
      }
    }

    // A câmara de índice zero é a entrada da caverna.
    return new Caverna(camaras, 0);
  }

  private static void inicializarRecursos(Caverna.Camara camara,
                                           Random aleatorio) {
    if (aleatorio.nextDouble() >= CHANCE_CAMARA_COM_RECURSO) {
      return;
    }

    // Embaralha os tipos para selecionar recursos sem repetição.
    List<Recurso> recursosDisponiveis = new ArrayList<>();
    Collections.addAll(recursosDisponiveis, Recurso.values());
    Collections.shuffle(recursosDisponiveis, aleatorio);

    int quantidadeTipos = aleatorio.nextInt(
      Math.min(QUANTIDADE_MAXIMA_RECURSOS, recursosDisponiveis.size())
        - QUANTIDADE_MINIMA_RECURSOS + 1
    ) + QUANTIDADE_MINIMA_RECURSOS;

    for (int i = 0; i < quantidadeTipos; i++) {
      int quantidade = aleatorio.nextInt(
        QUANTIDADE_MAXIMA_UNIDADES - QUANTIDADE_MINIMA_UNIDADES + 1
      ) + QUANTIDADE_MINIMA_UNIDADES;

      camara.adicionarRecurso(recursosDisponiveis.get(i), quantidade);
    }
  }

  private static void conectar(Caverna.Camara[] camaras, int indiceA,
                               int indiceB, Random aleatorio) {
    int custo = aleatorio.nextInt(
      CUSTO_MAXIMO_TUNEL - CUSTO_MINIMO_TUNEL + 1
    ) + CUSTO_MINIMO_TUNEL;

    // As conexões são bidirecionais e possuem o mesmo custo nos dois sentidos.
    camaras[indiceA].adicionarConexao(camaras[indiceB], custo);
    camaras[indiceB].adicionarConexao(camaras[indiceA], custo);
  }

  private static boolean possuiConexao(Caverna.Camara origem,
                                        Caverna.Camara destino) {
    for (Caverna.Camara conexao : origem.getConexoes()) {
      if (conexao == destino) {
        return true;
      }
    }
    return false;
  }

  private GeradorCaverna() {}
}
