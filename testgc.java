import java.util.ArrayList ; 
// only useed for java jdk 1.6


public class testgc {
  private String largeString = new String(new byte[100000]);
  String getString() {
    return largeString.substring(0, 2);
  }
  public static void main(String[] argvs) {
    ArrayList<String> tmp = new ArrayList<String>();
    for (int i= 0 ; i < 100000 ; i++) {
      tmp.add(new testgc().getString());
    }
  }
}
