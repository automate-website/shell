package website.automate.shell.support;

import java.util.Collection;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

public class SystemMessageUtils {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    
    public static void log(String value){
        System.out.println(getLogMessage(value));
    }
    
    public static String format(Collection<String> values){
        return String.join(LINE_SEPARATOR, values);
    }
    
    public static AttributedString getLogMessage(String value){
        return new AttributedString(value,
                AttributedStyle.DEFAULT);
    }
    
      
    public static AttributedString getSuccessMessage(){
        return new AttributedString("ok.",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));
    }
}
