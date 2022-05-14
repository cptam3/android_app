package scm.cptam3.witch;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.ux.TransformationSystem;

import java.util.Random;



public class Witch2node extends TransformableNode {

    private Vector3 spawn;
    private Random rand = new Random();

    private double speedx = 0;
    private double speedz = 0;

    private float rotation = 0;

    public Witch2node(TransformationSystem transformationSystem) {
        super(transformationSystem);

        getInitialPosi();
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);

        fly();
    }

    public void getInitialPosi(){
        spawn = getLocalPosition();
    }

    public void resetToSpawn(){
        setLocalPosition(spawn);
        speedx = 0;
        speedz = 0;
    }

    public void fly() {
        Vector3 posi = getLocalPosition();
        posi.x += speedx;
        posi.z += speedz;

        setLocalPosition(posi);

        updateFacingDirection();
    }

    private void updateFacingDirection() {
        rotation = (float)(-(Math.atan2(speedz, speedx) / Math.PI * 180.0f - 90));

        Quaternion r = Quaternion.axisAngle(
                new Vector3(0.0f, 1.0f, 0.0f), rotation);

        this.setLocalRotation(r);
    }

    public void fly_forward(){
        speedz = -0.0025;
        speedx = 0;
    }

    public void fly_backward(){
        speedz = 0.0025;
        speedx = 0;
    }

    public void fly_leftward(){
        speedz = 0;
        speedx = -0.0025;
    }

    public void fly_rightward(){
        speedz = 0;
        speedx = 0.0025;
    }

}
