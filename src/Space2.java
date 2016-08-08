import com.intellij.codeInsight.highlighting.HighlightManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.event.DocumentAdapter;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.JBColor;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fuzhipeng on 16/8/8.
 */
public class Space2 extends AnAction {

    private Project project;
    private Document document;

    //高亮
    private Editor editor;
    private HighlightManager highlightManager;
    private EditorColorsManager colorsManager;
    private TextAttributes attributes;


    @Override
    public void actionPerformed(AnActionEvent e) {
        hightInit(e);
        document.addDocumentListener(listener);
        check(document);
    }

    private DocumentListener listener = new DocumentAdapter() {
        @Override
        public void documentChanged(DocumentEvent documentEvent) {
            check(documentEvent.getDocument());
        }
    };

    int highLightNum;

    private void check(Document documentTemp) {
        String content = documentTemp.getText();

        Pattern p = Pattern.compile("\".*\"");
        Matcher m = p.matcher(content);
        highLightNum = 0;
        while (m.find()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                String item = m.group(i);
                spaceCheck(item, m.start(i));
                System.out.println(item + "\t 开始:" + m.start(i) + "\t 结束:" + m.end(i));
            }
        }
        if (highLightNum == 0) {
            Messages.showMessageDialog("Success!", "Information", Messages.getInformationIcon());
            document.removeDocumentListener(listener);
        }
    }

    private void spaceCheck(String item, int start) {
        Pattern pS = Pattern.compile("\\s+");
        Matcher mS = pS.matcher(item);
        while (mS.find()) {
            highLight(mS.start() + start, mS.end() + start);
            highLightNum++;
            System.out.println("空格---->" + "\t 开始:" + mS.start() + start + "\t 结束:" + mS.end() + start);
        }
    }

    //高亮的初始化
    private void hightInit(AnActionEvent e) {
        project = e.getProject();
        document = e.getData(PlatformDataKeys.EDITOR).getDocument();

        editor = e.getRequiredData(CommonDataKeys.EDITOR);
        highlightManager = HighlightManager.getInstance(project);
        colorsManager = EditorColorsManager.getInstance();
//        TextAttributesKey REFERENCE_HYPERLINK_COLOR = TextAttributesKey.createTextAttributesKey("CTRL_CLICKABLE", new TextAttributes(JBColor.blue, (Color)null, JBColor.blue, EffectType.LINE_UNDERSCORE, 0));
//        attributes = colorsManager.getGlobalScheme().getAttributes(EditorColors.SEARCH_RESULT_ATTRIBUTES);
//        attributes = colorsManager.getGlobalScheme().getAttributes(REFERENCE_HYPERLINK_COLOR);
//        attributes=new TextAttributes((Color)null,JBColor.red, (Color)null, null, 0);
//        new Color(0, 0, 0,0)
        attributes=new TextAttributes((Color)null,new JBColor( new Color(255,0,0,200), new Color(255, 255, 255,255)), (Color)null, null, 0);
    }

    //高亮的使用方法
    private void highLight(int start, int end) {
        highlightManager.addRangeHighlight(editor, start, end, attributes, true, null);
    }
}
