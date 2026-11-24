import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class DesenhadorCaverna {

    private final float RAIO_CAMARA;
    
    private PApplet pApplet;
    private Caverna caverna;
    private PImage imagemRobo;
    private PImage imagemEntrada;
    private Map<Caverna.Camara, PVector> pontoPorCamara;


    public DesenhadorCaverna(PApplet pApplet, Caverna caverna) {
        this.pApplet = pApplet;
        this.caverna = caverna;
        imagemRobo = pApplet.loadImage("assets/Robo.png");
        imagemEntrada = pApplet.loadImage("assets/entrada_caverna.png");
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

        pApplet.pushStyle();
        pApplet.rectMode(PApplet.CENTER);
        pApplet.textAlign(PApplet.CENTER, PApplet.CENTER);

        for (int i = 0; i < camaras.length; i++) {
            for (Caverna.Camara conexao : camaras[i].conexoes) {
                int destino = indiceDaCamara(camaras, conexao);

                if (destino > i) {
                    desenhaTunel(camaras[i], conexao,
                        caverna.getCustoTravessia(i, destino));
                }
            }
        }

        for (int i = 0; i < camaras.length; i++) {
            desenhaCamara(pontoPorCamara.get(camaras[i]), i,
                i == caverna.getIndiceInicio());
        }
        pApplet.popStyle();
    }

    private int indiceDaCamara(Caverna.Camara[] camaras, Caverna.Camara alvo) {
        for (int i = 0; i < camaras.length; i++) {
            if (camaras[i] == alvo) return i;
        }

        return -1;
    }

    private void desenhaCamara(PVector coordenada, int indice, boolean base) {
        pApplet.stroke(35, 29, 21);
        pApplet.strokeWeight(2);
        pApplet.fill(base ? 0xFFFFD166 : 0xFFE8DCC8);
        pApplet.circle(coordenada.x, coordenada.y, 2*RAIO_CAMARA);
        pApplet.fill(30, 25, 20);
        if (base && imagemEntrada != null) {
            pApplet.imageMode(PApplet.CENTER);
            pApplet.image(imagemEntrada, coordenada.x, coordenada.y - 5, 52, 52);
            pApplet.textSize(12);
            pApplet.text("BASE / C" + indice, coordenada.x, coordenada.y + 36);
        } else {
            pApplet.textSize(18);
            pApplet.text("C" + indice, coordenada.x, coordenada.y);
        }
    }

    private void desenhaTunel(Caverna.Camara origem, Caverna.Camara destino,
                              int custo) {
        PVector pontoO = pontoPorCamara.get(origem);
        PVector pontoD = pontoPorCamara.get(destino);

        float dx = pontoD.x - pontoO.x;
        float dy = pontoD.y - pontoO.y;
        float angulo = PApplet.atan2(dy, dx);

        float distancia = PVector.dist(pontoO, pontoD);
        float meioX = (pontoO.x + pontoD.x) / 2;
        float meioY = (pontoO.y + pontoD.y) / 2;

        pApplet.pushMatrix();
        pApplet.translate(meioX, meioY);
        pApplet.rotate(angulo);
        pApplet.stroke(65, 58, 48);
        pApplet.strokeWeight(2);
        pApplet.fill(115, 101, 78);
        pApplet.rect(0, 0, Math.max(0, distancia - 2 * RAIO_CAMARA), 12);
        pApplet.popMatrix();

        pApplet.noStroke();
        pApplet.fill(255, 248, 230);
        pApplet.rect(meioX, meioY - 12, 38, 22, 5);
        pApplet.fill(35, 29, 21);
        pApplet.textSize(12);
        pApplet.text("E" + custo, meioX, meioY - 12);
    }

    public void desenhaRobo(int indiceCamara, int indicePopulacao,
                            int tamanhoPopulacao, boolean melhor) {
        Caverna.Camara[] camaras = caverna.getCamaras();
        if (indiceCamara < 0 || indiceCamara >= camaras.length) return;
        PVector ponto = pontoPorCamara.get(camaras[indiceCamara]);
        if (ponto == null) return;

        pApplet.pushStyle();
        float x = ponto.x;
        float y = ponto.y;
        float diametro = 9;

        if (melhor) {
            // O melhor fica maior e acima do centro da câmara.
            y -= 28;
            pApplet.stroke(255, 220, 90);
            pApplet.strokeWeight(4);
            pApplet.noFill();
            pApplet.circle(x, y, 48);
            if (imagemRobo != null) {
                pApplet.imageMode(PApplet.CENTER);
                pApplet.image(imagemRobo, x, y, 40, 40);
            } else {
                diametro = 25;
                pApplet.fill(55, 130, 220);
            }
        } else {
            // Robôs da população ficam distribuídos em volta do centro,
            // evitando que os que visitam a mesma câmara virem um único ponto.
            float angulo = PApplet.TWO_PI * indicePopulacao
                / Math.max(1, tamanhoPopulacao);
            x += PApplet.cos(angulo) * 31;
            y += PApplet.sin(angulo) * 31;
            pApplet.noStroke();
            pApplet.fill(120, 190, 235, 190);
        }

        if (!melhor || imagemRobo == null) {
            pApplet.circle(x, y, diametro);
        }
        if (melhor && imagemRobo == null) {
            pApplet.fill(255);
            pApplet.noStroke();
            pApplet.textAlign(PApplet.CENTER, PApplet.CENTER);
            pApplet.textSize(13);
            pApplet.text("M", x, y);
        }
        pApplet.popStyle();
    }

    public Caverna getCaverna() {
        return caverna;
    }
}
