package scm.cptam3.witch;

import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.ux.TransformationSystem;

import java.util.Random;

public class Witchnode extends TransformableNode {

    private Vector3 spawn;
    private double spawn_speed = 0;
    private double spawn_angle = 0;
    private double spawn_speedx = 0;
    private double spawn_speedz = 0;
    private double speedx = 0;
    private double speedz = 0;
    private Random rand = new Random();


    private float rotation = 0;
    public Witchnode(TransformationSystem transformationSystem) {
        super(transformationSystem);

        resetInitialMovement();
        getInitialPosi();
    }

    public void resetInitialMovement(){
        spawn_speed = 0.003f;
        spawn_angle = Math.toRadians(rand.nextInt(360));
        spawn_speedx = spawn_speed * Math.cos(spawn_angle);
        spawn_speedz = -spawn_speed * Math.sin(spawn_angle);
        speedx = spawn_speedx;
        speedz = spawn_speedz;

    }

    public void getInitialPosi(){
        spawn = getLocalPosition();
    }


    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);

        fly();
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

    public void changeMovingDirection() {

        if(speedx>0&&speedz<0){
            speedz = -speedz;
        } else if(speedx>0&&speedz>0){
            speedx = -speedx;
        } else if(speedx<0&&speedz>0){
            speedz = -speedz;
        } else if(speedx<0&&speedz<0){
            speedx = -speedx;
        } else{
            speedx = - speedx;
            speedz = -speedz;
        }
        updateFacingDirection();
    }

    public void resetToSpawn(){
        setLocalPosition(spawn);
        speedx = spawn_speedx;
        speedz = spawn_speedz;
    }


}
