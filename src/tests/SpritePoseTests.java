package tests;

import groupproject.spritesheeteditor.models.PoseFileFormat;
import groupproject.spritesheeteditor.models.ProjectileFileFormat;
//import org.junit.jupiter.api.Test;

public class SpritePoseTests {

    //@Test
    public void testIsValidPose() {
        try {
            String filename = "link_final_spritesheet.pose";
            String imagename = "link_final_spritesheet.png";
            PoseFileFormat load = PoseFileFormat.Companion.load(filename);
            assert load != null;
            assert load.getImage().equals(imagename);
            assert !load.getPoses().isEmpty();
            assert load.getPoses().get(0).getPose().equals("UP");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void testIsValidProjectile() {
        try {
            String filename = "link_final_spritesheet";
            String imagename = "link_final_spritesheet.png";
            ProjectileFileFormat load = ProjectileFileFormat.Companion.load(filename);
            assert load != null;
            assert load.getImage().equals(imagename);
            assert !load.getSet().isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
