import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;
import processing.core.PVector;

public class DesenhadorCaverna {

    private final float RAIO_CAMARA;
    
    private PApplet pApplet;
    private Caverna caverna;
    private Map<Caverna.Camara, PVector> pontoPorCamara;


    public DesenhadorCaverna(PApplet pApplet, Caverna caverna) {
        this.pApplet = pApplet;
        this.caverna = caverna;
        pontoPorCamara = new HashMap<>();
        RAIO_CAMARA = 50;

        coordenadaCamara();
        pApplet.rectMode(PApplet.CENTER);
    }

    public void atualiza() {
        desenha();
    }

    private void coordenadaCamara() {
        Caverna.Camara[] camaras = caverna.getCamaras();
        if (camaras.length == 0) return;

        int maxTentativas = Math.max(100, camaras.length * 100);
        float distanciaMinima = 2 * RAIO_CAMARA + 12;

        for (Caverna.Camara camara : camaras) {
            PVector coordenada = null;

            for (int tentativa = 0; tentativa < maxTentativas; tentativa++) {
                PVector candidata = new PVector(
                    pApplet.random(RAIO_CAMARA, pApplet.width - RAIO_CAMARA),
                    pApplet.random(RAIO_CAMARA, pApplet.height - RAIO_CAMARA));

                if (verificaCoordenada(candidata, distanciaMinima)) {
                    coordenada = candidata;
                    break;
                }
            }

            if (coordenada == null) {
                distribuirEmGrade(camaras);
                return;
            }

            pontoPorCamara.put(camara, coordenada);
        }
    }

    private boolean verificaCoordenada(PVector coordenada, float distanciaMinima) {
        if (coordenada == null || pApplet.width < 2 * RAIO_CAMARA
            || pApplet.height < 2 * RAIO_CAMARA)
            return false;

        for(PVector ponto : pontoPorCamara.values()) {
            if(PVector.dist(ponto, coordenada) < distanciaMinima)
                return false;
        }

        return true;
    }

    // Distribuição alternativa finita, em linhas e colunas, caso o sorteio
    // não encontre espaço. O espaçamento se adapta à quantidade de câmaras.
    private void distribuirEmGrade(Caverna.Camara[] camaras) {
        pontoPorCamara.clear();

        int colunas = (int) Math.ceil(Math.sqrt(camaras.length
            * (double) pApplet.width / Math.max(1, pApplet.height)));

        colunas = Math.max(1, colunas);
        int linhas = (int) Math.ceil(camaras.length / (double) colunas);
        float larguraCelula = pApplet.width / (float) colunas;
        float alturaCelula = pApplet.height / (float) linhas;

        for (int i = 0; i < camaras.length; i++) {
            int coluna = i % colunas;
            int linha = i / colunas;

            pontoPorCamara.put(camaras[i], new PVector(
                (coluna + 0.5f) * larguraCelula,
                (linha + 0.5f) * alturaCelula));
        }
    }

    public void desenha() {
        Caverna.Camara[] camaras = caverna.getCamaras();

        for(Caverna.Camara camara : camaras) {
            Caverna.Camara[] conexoes = camara.conexoes;

            for(Caverna.Camara conexao : conexoes)
                desenhaTunel(camara, conexao);

            desenhaCamara(pontoPorCamara.get(camara));
        }
    }

    private void desenhaCamara(PVector coordenada) {
        pApplet.circle(coordenada.x, coordenada.y, 2*RAIO_CAMARA);
    }

    private void desenhaTunel(Caverna.Camara origem, Caverna.Camara destino) {
        PVector pontoO = pontoPorCamara.get(origem);
        PVector pontoD = pontoPorCamara.get(destino);

        float dx = pontoD.x - pontoO.x;
        float dy = pontoD.y - pontoO.y;
        float angulo = PApplet.atan2(dy, dx);

        float meioX = (pontoO.x + pontoD.x) / 2; 
        float meioY = (pontoO.y + pontoD.y) / 2; 

        pApplet.pushMatrix();
        
        pApplet.translate(meioX, meioY);

        pApplet.rotate(angulo);
        pApplet.rect(0, 0, PVector.dist(pontoO, pontoD), 10);

        pApplet.popMatrix();
    }

    public Caverna getCaverna() {
        return caverna;
    }
}
