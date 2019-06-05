package hgv.smash.resources;

public class AvatarLoader {
    private static final AvatarLoader instance=new AvatarLoader();

    private AvatarLoader(){

    }

    public AvatarLoader getInstance(){
        return instance;
    }
}
