import java.util.HashMap;

public class Caverna {
  public class Camara {
    public HashMap<Recurso, Integer> quantRecursos;

    public Camara[] conexoes;
    public int[] custoConexoes;
  }

  private Camara[] camaras;

  public Caverna(Camara[] camaras) {
    this.camaras = camaras;
  }
}
