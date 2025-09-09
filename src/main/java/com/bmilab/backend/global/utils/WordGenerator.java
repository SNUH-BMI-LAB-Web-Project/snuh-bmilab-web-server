package com.bmilab.backend.global.utils;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHyperlinkRun;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class WordGenerator {

    public static final String WORD_MEDIA_TYPE =
            "application/vnd.openxmlformats-officedocument.wordprocessingml" + ".document";

    public ByteArrayInputStream generateBy(List<ExcelRow> rows) throws IOException {

        try (
                XWPFDocument doc = generateDocument(rows); ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {
            doc.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private XWPFDocument generateDocument(List<ExcelRow> rows) {

        XWPFDocument doc = new XWPFDocument();

        for (int i = 0; i < rows.size(); i++) {
            List<String> d = rows.get(i).data();

            // 1) 날짜 / 이름 / 이메일 / 연구제목
            XWPFParagraph info = createParagraphWithSpacing(doc,80, 40);
            XWPFRun mr = info.createRun();
            applyStyle(mr, Style.INFO);
            joinWith(mr, safe(d, 0), INFO_DELIM, safe(d, 1), INFO_DELIM, safe(d, 2), INFO_DELIM, safe(d, 3));

            // 2) 연구내용
            String body = d.size() > 4 ? safe(d, 4) : "";
            XWPFParagraph content = createParagraphWithSpacing(doc, 0, 60);
            XWPFRun cr = content.createRun();
            applyStyle(cr, Style.BODY);
            writeWithLineBreaks(cr, body);

            // 3) 첨부파일 URL (하이퍼링크)
            if (d.size() > 5 && notBlank(d.get(5))) {
                String[] urls = d.get(5).split("\\R", -1);
                for (String raw : urls) {
                    String url = raw == null ? "" : raw.trim();
                    if (url.isEmpty()) {
                        continue;
                    }

                    XWPFParagraph attach = createParagraphWithSpacing(doc,0, 0);

                    XWPFRun label = attach.createRun();
                    applyStyle(label, Style.ATTACHMENT_LABEL);
                    label.setText(ATTACHMENT_LABEL);

                    // 하이퍼링크
                    try {
                        XWPFHyperlinkRun link = attach.createHyperlinkRun(url);
                        applyStyle(link, Style.ATTACHMENT_URL_LINK);
                        link.setText(url);
                    } catch (Throwable t) {
                        XWPFRun plain = attach.createRun();
                        applyStyle(plain, Style.ATTACHMENT_URL_PLAIN);
                        plain.setText(url);
                    }
                }
            }

            // 4) 구분선
            XWPFParagraph sep = createParagraphWithSpacing(doc,80, 0);
            XWPFRun sr = sep.createRun();
            sr.setText(SEPARATOR);
            if (i < rows.size() - 1) {
                sr.addBreak();
            }
        }

        return doc;
    }

    private static final String INFO_DELIM = " / ";
    private static final String ATTACHMENT_LABEL = "첨부: ";
    private static final String SEPARATOR = "=====";
    private static final int INFO_FONT_SIZE = 11;
    private static final int BODY_FONT_SIZE = 10;
    private static final int ATT_FONT_SIZE = 10;
    private static final String LINK_COLOR = "0563C1";

    private enum Style {
        INFO,
        BODY,
        ATTACHMENT_LABEL,
        ATTACHMENT_URL_LINK,
        ATTACHMENT_URL_PLAIN
    }

    private static void applyStyle(XWPFRun run, Style style) {
        switch (style) {
            case INFO -> {
                run.setBold(true);
                run.setFontSize(INFO_FONT_SIZE);
            }
            case BODY -> {
                run.setFontSize(BODY_FONT_SIZE);
            }
            case ATTACHMENT_LABEL, ATTACHMENT_URL_PLAIN -> {
                run.setItalic(true);
                run.setFontSize(ATT_FONT_SIZE);
            }
            case ATTACHMENT_URL_LINK -> {
                run.setItalic(true);
                run.setFontSize(ATT_FONT_SIZE);
                run.setUnderline(UnderlinePatterns.SINGLE);
                run.setColor(LINK_COLOR);
            }
        }
    }

    private static XWPFParagraph createParagraphWithSpacing(XWPFDocument doc, int spacingBefore, int spacingAfter) {

        XWPFParagraph p = doc.createParagraph();
        p.setAlignment(ParagraphAlignment.LEFT);
        p.setSpacingBefore(spacingBefore);
        p.setSpacingAfter(spacingAfter);
        return p;
    }

    private static void writeWithLineBreaks(XWPFRun run, String text) {

        if (text == null || text.isEmpty()) {
            run.setText("");
            return;
        }
        String[] lines = text.split("\\R", -1); // \r\n, \n, \r 모두 대응
        for (int i = 0; i < lines.length; i++) {
            if (i > 0) {
                run.addBreak();
            }
            run.setText(lines[i]);
        }
    }

    private static boolean notBlank(String s) {

        return s != null && !s.trim().isEmpty();
    }

    private static String safe(List<String> d, int idx) {

        return (idx < d.size() && d.get(idx) != null) ? d.get(idx) : "";
    }

    private static void joinWith(XWPFRun run, String... parts) {

        for (String part : parts) {
            run.setText(part);
        }
    }

}