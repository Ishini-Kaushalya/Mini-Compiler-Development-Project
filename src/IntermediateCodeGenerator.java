public class IntermediateCodeGenerator {
    private int tempCount = 1;

    public String newTemp() {
        return "t" + tempCount++;
    }

    public void emit(String code) {
        System.out.println(code);
    }
}
