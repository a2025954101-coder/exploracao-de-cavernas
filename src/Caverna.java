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

  public int getQuantidadeCamaras() {
    return camaras.length;
  }

  public Camara getCamara(int indice) {
    return camaras[indice];
  }

  public int getCustoTravessia(int indiceOrigem, int indiceDestino) {
    assert indiceOrigem >= 0 && indiceOrigem < camaras.length;
    assert indiceDestino >= 0 && indiceDestino < camaras.length;

    Camara origem = camaras[indiceOrigem];
    Camara destino = camaras[indiceDestino];

    for (int i = 0; i < origem.conexoes.length; i++) {
      if (origem.conexoes[i] == destino) {
        return origem.custoConexoes[i];
      }
    }

    return -1;
  }


  public Caverna(Camara[] camaras, int indiceInicio) {
    assert indiceInicio >= 0 && indiceInicio < camaras.length;
    
    this.camaras = camaras.clone();
    this.indiceInicio = indiceInicio;
  } 
}
