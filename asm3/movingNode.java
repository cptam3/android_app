package scm.cptam3.witch;

import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.ux.TransformationSystem;

import java.util.Random;

public class movingNode extends TransformableNode {
    private double spawn_speed = 0;
    private double spawn_angle = 0;
    private double spawn_speedx = 0;
    private double spawn_speedz = 0;
    private double speedx = 0;
    private double speedz = 0;
    private Random rand = new Random();
    private int i = 0;
    public movingNode(TransformationSystem transformationSystem) {
        super(transformationSystem);
        resetInitialMovement();
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);

        move();
        i+=2;
    }

    public void resetInitialMovement(){
        //spawn_speed = 0.001f;
        spawn_speed = 0.002f;
        spawn_angle = Math.toRadians(rand.nextInt(360));
        spawn_speedx = spawn_speed * Math.cos(spawn_angle);
        spawn_speedz = -spawn_speed * Math.sin(spawn_angle);

        speedx = spawn_speedx;
        speedz = spawn_speedz;
        //speedx = (rand.nextInt(2)-1)*0.002f;
        //speedz = (rand.nextInt(2)-1)*0.002f;
    }
    public void move() {
        double rad = Math.toRadians(i);
        double j = Math.sin(rad);
        Vector3 posi = getLocalPosition();
        if(j>=0) {
            posi.x += speedx;
            posi.z += speedz;
        } else{
            posi.x -= speedx;
            posi.z -= speedz;
        }
        setLocalPosition(posi);
    }
    
}
