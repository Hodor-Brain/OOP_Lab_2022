package ua.university.filters;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XssFilterUtil {

    private static final Logger log = LoggerFactory.getLogger(XssFilterUtil.class);

    private static List<Pattern> patterns = null;

    private static List<Object[]> getXssPatternList() {

        List<Object[]> ret = new ArrayList<Object[]>();

        ret.add(new Object[]{"<(no)?script[^>]*>.*?</(no)?script>", Pattern.CASE_INSENSITIVE});
        ret.add(new Object[]{"</script>", Pattern.CASE_INSENSITIVE});
        ret.add(new Object[]{"<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        ret.add(new Object[]{"eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        ret.add(new Object[]{"expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        ret.add(new Object[]{"(javascript:|vbscript:|view-source:)*", Pattern.CASE_INSENSITIVE});
        ret.add(new Object[]{"<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        ret.add(new Object[]{"(window\\.location|window\\.|\\.location|document\\.cookie|document\\.|alert\\(.*?\\)|window\\.open\\()*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        ret.add(new Object[]{"<+\\s*\\w*\\s*(oncontrolselect|oncopy|oncut|ondataavailable|ondatasetchanged|ondatasetcomplete|ondblclick|ondeactivate|ondrag|ondragend|ondragenter|ondragleave|ondragover|ondragstart|ondrop|onerror=|onerroupdate|onfilterchange|onfinish|onfocus|onfocusin|onfocusout|onhelp|onkeydown|onkeypress|onkeyup|onlayoutcomplete|onload|onlosecapture|onmousedown|onmouseenter|onmouseleave|onmousemove|onmousout|onmouseover|onmouseup|onmousewheel|onmove|onmoveend|onmovestart|onabort|onactivate|onafterprint|onafterupdate|onbefore|onbeforeactivate|onbeforecopy|onbeforecut|onbeforedeactivate|onbeforeeditocus|onbeforepaste|onbeforeprint|onbeforeunload|onbeforeupdate|onblur|onbounce|oncellchange|onchange|onclick|oncontextmenu|onpaste|onpropertychange|onreadystatechange|onreset|onresize|onresizend|onresizestart|onrowenter|onrowexit|onrowsdelete|onrowsinserted|onscroll|onselect|onselectionchange|onselectstart|onstart|onstop|onsubmit|onunload)+\\s*=+", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL});
        return ret;
    }

    private static List<Pattern> getPatterns() {


        if (patterns == null) {

            List<Pattern> list = new ArrayList<Pattern>();

            String regex = null;
            Integer flag = null;
            int arrLength = 0;

            for (Object[] arr : getXssPatternList()) {
                arrLength = arr.length;
                for (int i = 0; i < arrLength; i++) {
                    regex = (String) arr[0];
                    flag = (Integer) arr[1];
                    list.add(Pattern.compile(regex, flag));
                }
            }

            patterns = list;
        }

        return patterns;
    }

    public static String stripXss(String value) {

        if (StringUtils.isNotBlank(value)) {

//            log.info("【XSS Attack defense 】, The receiving character is ：{}", value);

            //
            Matcher matcher = null;

            for (Pattern pattern : getPatterns()) {
                matcher = pattern.matcher(value);
                //  matching
                if (matcher.find()) {
                    //  Delete the relevant string
                    value = matcher.replaceAll("");
                }
            }

//            log.info("【XSS Attack defense 】, The matching rule is ：{}, After treatment is ：{}", matcher, value);

            /**
             *  Replace with the transfer character , similar HtmlUtils.htmlEscape
             */
            //value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
            // Delete special symbols
            //String specialStr = "%20|=|!=|-|--|;|'|\"|%|#|+|//|/| |\\|<|>|(|)|{|}";

            if (StringUtils.isNotBlank(value)) {
                String specialStr = "%20|=|!=|-|--|;|'|\"|%|#|[+]|//|/|\\|<|>|(|)|{|}";
                for (String str : specialStr.split("\\|")) {
                    if (value.indexOf(str) > -1) {
                        value = value.replaceAll(str, "");

                    }
                }
                log.info("【XSS Attack defense 】, After special symbol processing, it is ：{}", value);
            }


        }


        return value;
    }


}