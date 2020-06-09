package boundary;

import controller.TestController;
import controller.TestControllerBuilder;
import util.MockFTP;

public class Main {

    public static void main(String[] args) {
//        String fileName = "template.zip" ;
//        TestController testController = null;
//        try {
//            testController = TestControllerBuilder.build("loadlogic.dynalias.com"
//                    , 63321,"ccservice", "capcal2011", fileName);
//            testController.runTest(fileName, "123");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
//            String testFileName = FileUtil.getTestFileName(TestFramework.GATLING, "HttpSimulation1.zip", false);
            new MockFTP().setup();
            TestController build = TestControllerBuilder.build("localhost"
                    , 4000, "user", "password", "template.zip");
            build.runTest("template.zip", "12313");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
