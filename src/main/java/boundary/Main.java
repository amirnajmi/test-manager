package boundary;

import controller.TestController;
import controller.TestControllerBuilder;

public class Main {

    public static void main(String[] args) {
        String fileName = "template.zip" ;
        TestController testController = null;
        try {
            testController = TestControllerBuilder.build("loadlogic.dynalias.com"
                    , 63321,"ccservice", "capcal2011", fileName);
            testController.runTest(fileName, "123");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
