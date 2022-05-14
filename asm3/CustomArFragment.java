package scm.cptam3.witch;

import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.ux.ArFragment;

public class CustomArFragment extends ArFragment {
    protected Config getSessionConfiguration(Session session) {
        //return super.getSessionConfiguration(session);
        Config config = new Config(session);
     //   config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
     //   config.setFocusMode(Config.FocusMode.AUTO);
        session.configure(config);

        return config;
    }
}
