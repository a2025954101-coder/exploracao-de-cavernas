import java.util.HashMap;
import java.util.Collections;
import java.util.Map;

public class Caverna {
  public static class Camara {
    private final HashMap<Recurso, Integer> quantRecursos = new HashMap<>();

    private Camara[] conexoes = new Camara[0];
    private int[] custoConexoes = new int[0];

    public Map<Recurso, Integer> getQuantRecursos() {
      return Collections.unmodifiableMap(quantRecursos);
    }

    public Camara[] getConexoes() {
      return conexoes.clone();
    }

    public int getCustoConexao(Camara destino) {
      for (int i = 0; i < conexoes.length; i++) {
        if (conexoes[i] == destino) return custoConexoes[i];
      }
      return -1;
    }

    void adicionarRecurso(Recurso recurso, int quantidade) {
      quantRecursos.put(recurso, quantidade);
    }

    void adicionarConexao(Camara destino, int custo) {
      int tamanho = conexoes.length;
      Camara[] novasConexoes = new Camara[tamanho + 1];
      int[] novosCustos = new int[tamanho + 1];
      System.arraycopy(conexoes, 0, novasConexoes, 0, tamanho);
      System.arraycopy(custoConexoes, 0, novosCustos, 0, tamanho);
      novasConexoes[tamanho] = destino;
      novosCustos[tamanho] = custo;
      conexoes = novasConexoes;
      custoConexoes = novosCustos;
    }
  }

  private Camara[] camaras;
  private int indiceInicio;
  private int[][] camarasAjdacentes;

  private int[][] aplicarAdjacencia() {
    int[][] adjacencias = new int[camaras.length][camaras.length];

    for(int i = 0; i < camaras.length; i++) {
      for(int j = 0; j < camaras.length; j++) {
        adjacencias[i][j] = getCustoTravessia(i, j);
      }
    }

    return adjacencias;
  }


  public int getIndiceInicio() {  
    return indiceInicio;
  }                             
                              
  public Camara getInicio() {     
    return camaras[indiceInicio]; 
  }                             

  public int getQuantidadeCamaras() {
    return camaras.length;
  }

  public Camara getCamara(int indice) {
    return camaras[indice];
  }

  public Camara[] getCamaras() {
    return camaras.clone();
  }

  public int[][] getCamarasAjdacentes() {
    int[][] copia = new int[camarasAjdacentes.length][];
    for (int i = 0; i < camarasAjdacentes.length; i++) {
      copia[i] = camarasAjdacentes[i].clone();
    }
    return copia;
  }

  public int getCustoTravessia(int indiceOrigem, int indiceDestino) {
    assert indiceOrigem >= 0 && indiceOrigem < camaras.length;
    assert indiceDestino >= 0 && indiceDestino < camaras.length;

    Camara origem = camaras[indiceOrigem];
    Camara destino = camaras[indiceDestino];
    return origem.getCustoConexao(destino);
  }


  public Caverna(Camara[] camaras, int indiceInicio) {
    assert indiceInicio >= 0 && indiceInicio < camaras.length;
    
    this.camaras = camaras.clone();
    this.indiceInicio = indiceInicio;

    camarasAjdacentes = aplicarAdjacencia();
  } 
}
