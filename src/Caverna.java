import java.util.HashMap;

public class Caverna {
  public class Camara {
    public HashMap<Recurso, Integer> quantRecursos;

    public Camara[] conexoes;
    public int[] custoConexoes;
  }

  private Camara[] caramas;

  public Caverna() {
    // TODO: Decidir se essa classe vai iniciar o grafo aleatóriamente ou se
    //       vai receber o grafo pronto
  }
}
