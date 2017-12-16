package website.automate.shell.support;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.junit.Test;

public class CustomPromptProviderTest {

    private CustomPromptProvider provider = new CustomPromptProvider();
    
    @Test
    public void correctPromptReturned() {
        AttributedString result = provider.getPrompt();
        assertThat(result, is(new AttributedString("automate.website:>",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW))));
    }
}
