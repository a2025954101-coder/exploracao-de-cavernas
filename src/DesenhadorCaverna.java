import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;
import processing.core.PVector;

public class DesenhadorCaverna {

    private final float RAIO_CAMARA = 60;
    
    private PApplet pApplet;
    private Caverna caverna;
    private Map<Caverna.Camara, PVector> pontoPorCamara;


    public DesenhadorCaverna(PApplet pApplet, Caverna caverna) {
        this.pApplet = pApplet;
        this.caverna = caverna;
        pontoPorCamara = new HashMap<>();

        coordenadaCamara();
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
            if(ponto == coordenada) continue;
            if(PVector.dist(ponto, coordenada) <= RAIO_CAMARA) 
                return false;
        }

        return true;
    }

    public void desenha() {
        Caverna.Camara[] camaras = caverna.getCamaras();

        for(Caverna.Camara camara : camaras) {
            desenhaCamara(pontoPorCamara.get(camara));
        }
    }

    private void desenhaCamara(PVector coordenada) {
        pApplet.circle(coordenada.x, coordenada.y, RAIO_CAMARA);
    }

    public Caverna getCaverna() {
        return caverna;
    }
}
