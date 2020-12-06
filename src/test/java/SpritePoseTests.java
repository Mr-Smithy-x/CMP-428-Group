import groupproject.spritesheeteditor.models.FileFormat;
import org.junit.jupiter.api.Test;

class SpritePoseTests {

    @Test
    void testIsValidPose() {
        String filename = "link_final_spritesheet.pose";
        String imagename = "link_final_spritesheet.png";
        FileFormat load = FileFormat.Companion.load(filename);
        assert load != null;
        assert load.getImage().equals(imagename);
        assert !load.getPoses().isEmpty();
        assert load.getPoses().get(0).getPose().equals("UP");
    }

}
