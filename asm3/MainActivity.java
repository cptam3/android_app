package scm.cptam3.witch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.ar.core.Anchor;
import com.google.ar.core.Camera;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.collision.Ray;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity{
    private ArFragment fragment;
    private Scene scene;

    private int gameMode = 0;

    private int size = 0; // 0 is small, 1 is big

    private TextView sight;
    private int[] screenPt = new int[2];
    private int numofPortal;
    private int[] startpt = new int[2];
    private int[] endpt = new int[2];

    private int callWinDialog = 0;
    private int calltime = 0;

    private Button btn_teleport;
    private Button btn_add;
    private Button btn_reset;
    private Button btn_clear;
    private Button btn_guide;
    private Button btn_changeMode;
    private Button btn_size;

    private ImageButton btn_forward;
    private ImageButton btn_backward;
    private ImageButton btn_leftward;
    private ImageButton btn_rightward;

    private final int WITCH_INDEX = 0, WALL_INDEX = 1, PORTAL_INDEX = 2, HOUSE_INDEX = 3;
    private int[] models = {R.raw.witch, R.raw.wall, R.raw.portal, R.raw.house};
    private String[] modelNames = {"witch","wall","portal","house"};
    private ModelRenderable[] renderables = new ModelRenderable[models.length];


    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        sight = findViewById(R.id.sight);

        sight.getLocationOnScreen(screenPt);
        screenPt[0]+=20;
        screenPt[1]-=70;

    }

    private void loadModels(){
        for(int i =0; i < models.length; i++){
            int finalIndex = i;
            ModelRenderable.builder()
                    .setSource(this, models[finalIndex]) // exercise 1
                    .setIsFilamentGltf(true)
                    .build()
                    .thenAccept(renderable -> renderables[finalIndex] = renderable)
                    .exceptionally(
                            throwable -> {
                                Log.e("loadModels", "Unable to load Renderable.", throwable);
                                return null;
                            });
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        loadModels();

        btn_add = findViewById(R.id.btn_add);
        btn_reset = findViewById(R.id.btn_reset);
        btn_teleport = findViewById(R.id.btn_teleport);
        btn_clear = findViewById(R.id.btn_clear);
        btn_guide = findViewById(R.id.btn_guide);
        btn_changeMode = findViewById(R.id.btn_changeMode);

        btn_forward = findViewById(R.id.btn_forward);
        btn_backward = findViewById(R.id.btn_backward);
        btn_leftward = findViewById(R.id.btn_leftward);
        btn_rightward = findViewById(R.id.btn_rightward);

        btn_size = findViewById(R.id.btn_mapsize);

        numofPortal = 8;

        //fragment = (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        fragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        scene = fragment.getArSceneView().getScene();

        btn_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAll();
                if(size == 0){
                    size = 1;
                    btn_size.setText("big");
                    if(gameMode == 1) {
                        numofPortal = 15;
                    }else if(gameMode == 0) {
                        numofPortal = 4;
                    }

                } else if(size == 1){
                    size = 0;
                    btn_size.setText("small");
                    if(gameMode == 1) {
                        numofPortal = 20;
                    }else if(gameMode == 0) {
                        numofPortal = 8;
                    }

                }
            }
        });

        btn_changeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAll();
                if(gameMode == 0){
                    gameMode = 1;
                    if(size == 0) {
                        numofPortal = 20;
                    } else if(size == 1) {
                        numofPortal = 15;
                    }
                    btn_changeMode.setText("Mode 2");
                    btn_add.setText("");
                    btn_clear.setText("");
                } else if(gameMode == 1){
                    gameMode = 0;
                    if(size == 0) {
                        numofPortal = 8;
                    } else if(size == 1) {
                        numofPortal = 4;
                    }
                    btn_changeMode.setText("Mode 1");
                    btn_add.setText("ADD/REMOVE");
                    btn_clear.setText("CLEAR");
                }
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameMode == 0) {
                    addRemoveWall();
                }
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calltime = 0;
                removeAll();
                setupSpawn();
                addPortal();
                callWinDialog = 0;
            }
        });

        btn_teleport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                respawn();
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameMode == 0)
                    removeWalls();
            }
        });

        btn_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameMode == 0)
                    howToPlay();
                if(gameMode == 1)
                    howToPlay2();
            }
        });

        btn_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameMode == 1){
                    flyForward();
                }
            }
        });

        btn_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameMode == 1){
                    flyBackward();
                }
            }
        });

        btn_leftward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameMode == 1){
                    flyLeftward();
                }
            }
        });

        btn_rightward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameMode == 1){
                    flyRightward();
                }
            }
        });

        scene.addOnUpdateListener(new Scene.OnUpdateListener() {
            @Override
            public void onUpdate(FrameTime frameTime) {
                witchWallCollision();
                witchPortalCollision();
                witchHouseCollision();
            }
        });
    }



    private void removeNode(Node node) {                                    //removeNode(node) are referenced from ARShooting - Topic 10
        AnchorNode parent = (AnchorNode)node.getParent();
        parent.getAnchor().detach();

        parent.removeChild(node);
        scene.removeChild(parent);
    }

    private HitTestResult sceneHitTest(float x, float y) {                  //sceneHitTest(x,y) are referenced from ARShooting - Topic 10
        Ray ray = scene.getCamera().screenPointToRay(x, y);

        return scene.hitTest(ray);
    }

    private void addRemoveWall(){
        Frame frame = fragment.getArSceneView().getArFrame();
        Camera camera = frame.getCamera();
        if (camera.getTrackingState() != TrackingState.TRACKING) return;

        HitTestResult sceneHit = sceneHitTest(screenPt[0], screenPt[1]);

        if (sceneHit != null && sceneHit.getNode() != null) {
            Node node = sceneHit.getNode();
            if (node.getName().equals(modelNames[WALL_INDEX])) {
                removeNode(node);
            }
            return;
        }

        List<HitResult> hits = frame.hitTest(screenPt[0], screenPt[1]);
        for (HitResult hit : hits) {
            Trackable trackable = hit.getTrackable();
            if (trackable instanceof Plane &&
                    ((Plane)trackable).isPoseInPolygon(hit.getHitPose()))
            {
                Anchor anchor = hit.createAnchor();
                AnchorNode anchorNode = new AnchorNode(anchor);
                anchorNode.setParent(scene);

                TransformableNode wall = new TransformableNode(fragment.getTransformationSystem());
                wall.setParent(anchorNode);
                wall.setName(modelNames[WALL_INDEX]);
                wall.setRenderable(renderables[WALL_INDEX]);
                Quaternion r = Quaternion.axisAngle(
                        new Vector3(0.0f, 1.0f, 0.0f), 60);
                wall.setLocalRotation(r);
                if(size == 0) {
                    wall.getScaleController().setMinScale(0.01f);
                    wall.getScaleController().setMaxScale(0.03f);
                } else if(size == 1) {
                    wall.getScaleController().setMinScale(0.05f);
                    wall.getScaleController().setMaxScale(0.1f);
                }
                break;
            }
        }
    }

    private void addPortal() {
        Frame frame = fragment.getArSceneView().getArFrame();
        Camera camera = frame.getCamera();
        if (camera.getTrackingState() != TrackingState.TRACKING) return;
        if (gameMode == 0){
            for (int i = 0; i < numofPortal; i++) {
                Random rand = new Random();
                int[] temp = new int[2];
                if(size == 0) {
                    temp[0] = screenPt[0] + (rand.nextInt(80) - 40) * 13;
                    temp[1] = screenPt[1] + (rand.nextInt(80) - 40) * 13;
                } else if( size == 1) {
                    temp[0] = screenPt[0] + (rand.nextInt(80) - 40) * 20;
                    temp[1] = screenPt[1] + (rand.nextInt(80) - 40) * 20;
                }

                List<HitResult> hits = frame.hitTest(temp[0], temp[1]);
                for (HitResult hit : hits) {

                    Anchor anchor = hit.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(scene);
                    Trackable trackable = hit.getTrackable();
                    if (trackable instanceof Plane &&
                            ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                        TransformableNode portal = new TransformableNode(fragment.getTransformationSystem());
                        portal.setParent(anchorNode);
                        portal.setName(modelNames[PORTAL_INDEX]);
                        portal.setRenderable(renderables[PORTAL_INDEX]);
                        //Quaternion r = Quaternion.axisAngle(
                        //        new Vector3(0.0f, 1.0f, 0.0f), 60);
                        //portal.setLocalRotation(r);
                        if(size == 0) {
                            portal.getScaleController().setMinScale(0.025f);
                            portal.getScaleController().setMaxScale(0.035f);
                        } else if(size == 1) {
                            portal.getScaleController().setMinScale(0.08f);
                            portal.getScaleController().setMaxScale(0.1f);
                        }
                        break;
                    }
                }
            }
        }
        if (gameMode == 1){
            for (int i = 0; i < numofPortal; i++) {
                Random rand = new Random();
                int[] temp = new int[2];
                if(size == 0) {
                    temp[0] = screenPt[0] + (rand.nextInt(80) - 40) * 13;
                    temp[1] = screenPt[1] + (rand.nextInt(80) - 40) * 13;
                } else if(size == 1) {
                    temp[0] = screenPt[0] + (rand.nextInt(80) - 40) * 20;
                    temp[1] = screenPt[1] + (rand.nextInt(80) - 40) * 20;
                }

                List<HitResult> hits = frame.hitTest(temp[0], temp[1]);
                for (HitResult hit : hits) {

                    Anchor anchor = hit.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(scene);
                    Trackable trackable = hit.getTrackable();
                    if (trackable instanceof Plane &&
                            ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                        TransformableNode portal = new movingNode(fragment.getTransformationSystem());
                        portal.setParent(anchorNode);
                        portal.setName(modelNames[PORTAL_INDEX]);
                        portal.setRenderable(renderables[PORTAL_INDEX]);
                        //Quaternion r = Quaternion.axisAngle(
                        //        new Vector3(0.0f, 1.0f, 0.0f), 60);
                        //portal.setLocalRotation(r);
                        if(size == 0) {
                            portal.getScaleController().setMinScale(0.025f);
                            portal.getScaleController().setMaxScale(0.035f);
                        } else if(size == 1) {
                            portal.getScaleController().setMinScale(0.08f);
                            portal.getScaleController().setMaxScale(0.1f);
                        }
                        break;
                    }
                }
            }
        }
    }

    private void setupSpawn(){
        Frame frame = fragment.getArSceneView().getArFrame();
        Camera camera = frame.getCamera();
        if (camera.getTrackingState() != TrackingState.TRACKING) return;

        Random rand = new Random();
        startpt[0] = screenPt[0];
        startpt[1] = screenPt[1];

        if(size == 0) {
            endpt[0] = screenPt[0]+(rand.nextInt(10)+300)*(rand.nextInt(2)-1);
            endpt[1] = screenPt[1]+(rand.nextInt(10)+300)*(rand.nextInt(2)-1);
        } else if(size == 1) {
            endpt[0] = screenPt[0] + (rand.nextInt(10) + 700) * (rand.nextInt(2) - 1);
            endpt[1] = screenPt[1] + (rand.nextInt(10) + 700) * (rand.nextInt(2) - 1);
        }
     /*   HitTestResult startHit = sceneHitTest(startpt[0], startpt[1]);
        while (startHit != null && startHit.getNode() != null){
            startpt[0] = screenPt[0]+ rand.nextInt(50)* (rand.nextInt(2)-1);
            startpt[1] = screenPt[1]+ rand.nextInt(50)* (rand.nextInt(2)-1);
            startHit = sceneHitTest(startpt[0], startpt[1]);
        }*/

        List<HitResult> startHits = frame.hitTest(startpt[0], startpt[1]);
        for (HitResult hit : startHits) {

            Anchor anchor = hit.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(scene);
            Trackable trackable = hit.getTrackable();
            if(gameMode == 0) {
                if (trackable instanceof Plane &&
                        ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                    TransformableNode WITCH = new Witchnode(fragment.getTransformationSystem());
                    WITCH.setParent(anchorNode);
                    WITCH.setName(modelNames[WITCH_INDEX]);
                    WITCH.setRenderable(renderables[WITCH_INDEX]);
                    if(size == 0) {
                        WITCH.getScaleController().setMinScale(0.0001f);
                        WITCH.getScaleController().setMaxScale(0.0002f);
                    } else if (size == 1) {
                        WITCH.getScaleController().setMinScale(0.0002f);
                        WITCH.getScaleController().setMaxScale(0.0004f);
                    }
                    break;
                }
            } else if(gameMode == 1){
                if (trackable instanceof Plane &&
                        ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                    TransformableNode WITCH = new Witch2node(fragment.getTransformationSystem());
                    WITCH.setParent(anchorNode);
                    WITCH.setName(modelNames[WITCH_INDEX]);
                    WITCH.setRenderable(renderables[WITCH_INDEX]);
                    if(size == 0) {
                        WITCH.getScaleController().setMinScale(0.0001f);
                        WITCH.getScaleController().setMaxScale(0.0002f);
                    } else if (size == 1) {
                        WITCH.getScaleController().setMinScale(0.0002f);
                        WITCH.getScaleController().setMaxScale(0.0004f);
                    }
                    break;
                }
            }
        }

    /*    HitTestResult endHit = sceneHitTest(endpt[0], endpt[1]);
        while (endHit != null && endHit.getNode() != null){
            endpt[0] = screenPt[0]+(rand.nextInt(10)+300)*(rand.nextInt(2)-1);
            endpt[1] = screenPt[1]+(rand.nextInt(10)+300)*(rand.nextInt(2)-1);
            endHit = sceneHitTest(endpt[0], endpt[1]);
        }*/

        List<HitResult> endHits = frame.hitTest(endpt[0], endpt[1]);
        for (HitResult hit : endHits) {

            Anchor anchor = hit.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(scene);
            Trackable trackable = hit.getTrackable();

            if (trackable instanceof Plane &&
                    ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                TransformableNode HOUSE = new TransformableNode(fragment.getTransformationSystem());
                HOUSE.setParent(anchorNode);
                HOUSE.setName(modelNames[HOUSE_INDEX]);
                HOUSE.setRenderable(renderables[HOUSE_INDEX]);
                if(size == 0) {
                    HOUSE.getScaleController().setMinScale(0.01f);
                    HOUSE.getScaleController().setMaxScale(0.03f);
                } else if(size == 1) {
                    HOUSE.getScaleController().setMinScale(0.05f);
                    HOUSE.getScaleController().setMaxScale(0.1f);
                }
                break;
            }
        }
    }

    private void removeAll(){
        scene.callOnHierarchy(
                node -> {
                    if(node.getName().equals(modelNames[HOUSE_INDEX]))
                        removeNode(node);
                    if(node.getName().equals(modelNames[WALL_INDEX]))
                        removeNode(node);
                    if(node.getName().equals(modelNames[WITCH_INDEX]))
                        removeNode(node);
                    if(node.getName().equals(modelNames[PORTAL_INDEX]))
                        removeNode(node);
                }
        );
    }

    private void removeWalls(){
        scene.callOnHierarchy(
                node -> {
                    if(node.getName().equals(modelNames[WALL_INDEX]))
                        removeNode(node);
                }
        );
    }

    private void respawn(){
        scene.callOnHierarchy(
                node -> {
                    if (node.getName().equals(modelNames[WITCH_INDEX])) {
                        if(gameMode == 0) {
                            ((Witchnode) node).resetToSpawn();
                        } else {
                            ((Witch2node) node).resetToSpawn();
                        }
                    }
                }
        );
    }

    private void witchWallCollision(){                          //the structure of checking collision are from ARShooting - Topic 10

        ArrayList<Node> Witch = new ArrayList<>();
        scene.callOnHierarchy(
                node -> {
                    if (node.getName().equals(modelNames[WITCH_INDEX]))
                        Witch.add(node);
                }
        );

        for (Node witch: Witch) {
            ArrayList<Node> overlapNodes = scene.overlapTestAll(witch);

            for (Node overlapNode : overlapNodes) {
                if (overlapNode.getName().equals(modelNames[WALL_INDEX])) {

                     ((Witchnode)witch).changeMovingDirection();

                      boolean stillOverlapped = true;
                      do {
                          stillOverlapped = scene.overlapTest(witch) != null;
                          if (stillOverlapped) ((Witchnode)witch).fly();
                      } while (stillOverlapped);

                    break;
                }
            }
        }
    }
    private void witchPortalCollision(){                                        //the structure of checking collision are from ARShooting - Topic 10
        ArrayList<Node> Witch = new ArrayList<>();
        scene.callOnHierarchy(
                node -> {
                    if (node.getName().equals(modelNames[WITCH_INDEX]))
                        Witch.add(node);
                }
        );

        for (Node witch: Witch) {
            ArrayList<Node> overlapNodes = scene.overlapTestAll(witch);

            for (Node overlapNode : overlapNodes) {
                if (overlapNode.getName().equals(modelNames[PORTAL_INDEX])) {
                    if(gameMode == 0) {
                        ((Witchnode) witch).resetToSpawn();
                    } else {
                        ((Witch2node) witch).resetToSpawn();
                    }

                    break;
                }
            }
        }
    }

    private void witchHouseCollision(){                                     //the structure of checking collision are from ARShooting - Topic 10
        ArrayList<Node> Witch = new ArrayList<>();
        scene.callOnHierarchy(
                node -> {
                    if (node.getName().equals(modelNames[WITCH_INDEX]))
                        Witch.add(node);
                }
        );

        for (Node witch: Witch) {
            ArrayList<Node> overlapNodes = scene.overlapTestAll(witch);

            for (Node overlapNode : overlapNodes) {
                if (overlapNode.getName().equals(modelNames[HOUSE_INDEX])) {

                    calltime++;
                    if(gameMode == 0) {
                        ((Witchnode) witch).resetToSpawn();
                    } else {
                        ((Witch2node) witch).resetToSpawn();
                    }

                    if(callWinDialog ==0 && calltime==2) {
                        windialog windialog = new windialog();
                        windialog.show(getSupportFragmentManager(), "win dialog");
                        callWinDialog++;
                    }
                    break;
                }
            }
        }


    }

    private void howToPlay(){
        howToPlayDialog howToPlay = new howToPlayDialog();
        howToPlay.show(getSupportFragmentManager(),"how to play");

    }

    private void howToPlay2(){
        howToPlay2Dialog howToPlay2 = new howToPlay2Dialog();
        howToPlay2.show(getSupportFragmentManager(),"how to play 2");
    }

    private void flyForward(){
        scene.callOnHierarchy(
                node -> {
                    if (node.getName().equals(modelNames[WITCH_INDEX])) {
                            ((Witch2node) node).fly_forward();
                    }
                }
        );
    }

    private void flyBackward(){
        scene.callOnHierarchy(
                node -> {
                    if (node.getName().equals(modelNames[WITCH_INDEX])) {
                        ((Witch2node) node).fly_backward();
                    }
                }
        );
    }

    private void flyLeftward(){
        scene.callOnHierarchy(
                node -> {
                    if (node.getName().equals(modelNames[WITCH_INDEX])) {
                        ((Witch2node) node).fly_leftward();
                    }
                }
        );
    }

    private void flyRightward(){
        scene.callOnHierarchy(
                node -> {
                    if (node.getName().equals(modelNames[WITCH_INDEX])) {
                        ((Witch2node) node).fly_rightward();
                    }
                }
        );
    }

}