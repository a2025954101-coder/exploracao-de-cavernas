import java.util.HashMap;

public class Caverna {
  public class Camara {
    public HashMap<Recurso, Integer> quantRecursos;

    public Camara[] conexoes;
    public int[] custoConexoes;
  }


  private Camara[] camaras;
  private int indiceInicio;


  public int getIndiceInicio() {  
    return indiceInicio;
  }                             
                              
  public Camara getInicio() {     
    return camaras[indiceInicio]; 
  }                             


  public Caverna(Camara[] camaras, int indiceInicio) {
    assert indiceInicio >= 0 && indiceInicio < camaras.length;
    
    this.camaras = camaras.clone();
    this.indiceInicio = indiceInicio;
  } 
}
