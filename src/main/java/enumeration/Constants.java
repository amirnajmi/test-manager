package enumeration;

public enum Constants {
    JMETER_HOME("/home/videolan/Downloads/jmeter"),
    GATLING_HOME("/home/videolan/Downloads/gatling"),
    GATLING_TEST_DIR("/user-files"),
    GATLING_RESULT_DIR("/gatling-result"),
    ZIP_FILE_EXTENSION(".zip"),
    SCALA_FILE_EXTENSION(".scala"),
    JMETER_FILE_EXTENSION(".jmx"),
    JMETER_RESULT_FILE_EXTENSION(".jtl");

    private String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
