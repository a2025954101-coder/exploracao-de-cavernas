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
        RAIO_CAMARA =  (pApplet.width * pApplet.height) / (2 * caverna.getQuantidadeCamaras());

        coordenadaCamara();
        pApplet.rectMode(PApplet.CENTER);
    }

    public void atualiza() {
        desenha();
    }

    private void coordenadaCamara() {
        Caverna.Camara[] camaras = caverna.getCamaras();

        for(Caverna.Camara camara : camaras) {
            PVector coordenada;

            do {
                coordenada = new PVector(pApplet.random(0 + RAIO_CAMARA, pApplet.width - RAIO_CAMARA), 
                                        pApplet.random(0 + RAIO_CAMARA, pApplet.height - RAIO_CAMARA));
            } while(!verificaCoordenada(coordenada));

            pontoPorCamara.put(camara, coordenada);
        }
    }

    private boolean verificaCoordenada(PVector coordenada) {
        if(coordenada == null || 
            coordenada.x - RAIO_CAMARA < 0  || coordenada.x + RAIO_CAMARA > pApplet.width
            || coordenada.y - RAIO_CAMARA < 0  || coordenada.y + RAIO_CAMARA > pApplet.height)
            return false;

        for(PVector ponto : pontoPorCamara.values()) {
            if(PVector.dist(ponto, coordenada) <= RAIO_CAMARA) 
                return false;
        }

        return true;
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

        float x = Math.abs(pontoO.x - pontoD.x);
        float y = Math.abs(pontoO.y - pontoD.y);

        float angulo = PApplet.atan2(x, y);

        float meioX = (pontoO.x + pontoD.x) / 2; 
        float meioY = (pontoO.y + pontoO.y) / 2;

        pApplet.pushMatrix();
        
        pApplet.translate(meioX, meioY);

        pApplet.rotate(angulo);
        pApplet.rect(0, 0, PVector.dist(pontoO, pontoD), 20);

        pApplet.popMatrix();
    }

    public Caverna getCaverna() {
        return caverna;
    }
}
