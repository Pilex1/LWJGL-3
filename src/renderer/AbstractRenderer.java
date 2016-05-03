package renderer;

import models.*;

public abstract class AbstractRenderer {
	
	abstract void start();
	abstract void render();
	abstract void loadLight(Light light);
	abstract void stop();

}
