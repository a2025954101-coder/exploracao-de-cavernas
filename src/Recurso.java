public enum Recurso {
  DIAMANTE(200),
  ESMERALDA(150),
  OURO(100),
  FERRO(50),
  CARVAO(10);

  private int valor;

  public int getValor() { return valor; }

  private Recurso(int valor) {
    this.valor = valor;
  }
}
