  
package frc.robot;

import org.team6083.lib.util.XBoxController;

public class xbox extends XBoxController {
    public xbox(int port) {
        super(port);
    }

    @Override
    public boolean toggleReverseButton(){
        return this.getBackButton();
    }
}