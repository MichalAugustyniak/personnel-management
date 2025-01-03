package com.pm.personnelmanagement.document.common.utils;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.*;

import javax.xml.bind.JAXBElement;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Docx4JUtils {
    public static P createParagraph(ObjectFactory factory, String text) {
        P p = factory.createP();
        R run = factory.createR();
        Text t = factory.createText();
        t.setValue(text);
        run.getContent().add(t);
        p.getContent().add(run);
        return p;
    }

    public static void applyBold(P paragraph) {
        for (Object obj : paragraph.getContent()) {
            if (obj instanceof R run) {
                RPr rPr = run.getRPr();
                if (rPr == null) {
                    rPr = new ObjectFactory().createRPr();
                    run.setRPr(rPr);
                }
                BooleanDefaultTrue b = new ObjectFactory().createBooleanDefaultTrue();
                rPr.setB(b);
            }
        }
    }

    public static void alignLeft(P paragraph) {
        setAlignment(paragraph, JcEnumeration.LEFT);
    }

    public static void alignCenter(P paragraph) {
        setAlignment(paragraph, JcEnumeration.CENTER);
    }

    public static void alignRight(P paragraph) {
        setAlignment(paragraph, JcEnumeration.RIGHT);
    }

    private static void setAlignment(P paragraph, JcEnumeration alignment) {
        PPr pPr = paragraph.getPPr();
        if (pPr == null) {
            pPr = new ObjectFactory().createPPr();
            paragraph.setPPr(pPr);
        }
        Jc jc = new ObjectFactory().createJc();
        jc.setVal(alignment);
        pPr.setJc(jc);
    }

    public static void applyItalic(P paragraph) {
        for (Object obj : paragraph.getContent()) {
            if (obj instanceof R run) {
                RPr rPr = run.getRPr();
                if (rPr == null) {
                    rPr = new ObjectFactory().createRPr();
                    run.setRPr(rPr);
                }
                BooleanDefaultTrue i = new ObjectFactory().createBooleanDefaultTrue();
                rPr.setI(i);
            }
        }
    }

    public static void applyUnderline(P paragraph) {
        for (Object obj : paragraph.getContent()) {
            if (obj instanceof R run) {
                RPr rPr = run.getRPr();
                if (rPr == null) {
                    rPr = new ObjectFactory().createRPr();
                    run.setRPr(rPr);
                }
                U u = new ObjectFactory().createU();
                u.setVal(UnderlineEnumeration.SINGLE);
                rPr.setU(u);
            }
        }
    }

    public static void applyColor(P paragraph, String colorHex) {
        for (Object obj : paragraph.getContent()) {
            if (obj instanceof R run) {
                RPr rPr = run.getRPr();
                if (rPr == null) {
                    rPr = new ObjectFactory().createRPr();
                    run.setRPr(rPr);
                }
                Color color = new ObjectFactory().createColor();
                color.setVal(colorHex);
                rPr.setColor(color);
            }
        }
    }

    public static void applyFontSize(P paragraph, int fontSize) {
        for (Object obj : paragraph.getContent()) {
            if (obj instanceof R run) {
                RPr rPr = run.getRPr();
                if (rPr == null) {
                    rPr = new ObjectFactory().createRPr();
                    run.setRPr(rPr);
                }

                HpsMeasure hps = new ObjectFactory().createHpsMeasure();
                hps.setVal(BigInteger.valueOf(fontSize * 2L));

                rPr.setSz(hps);
            }
        }
    }

    public static void replace(WordprocessingMLPackage wordMLPackage, Map<String, String> templateValue) {
        List<Object> content = wordMLPackage.getMainDocumentPart().getContent();
        List<P> paragraphs = new ArrayList<>();
        List<R> runs = new ArrayList<>();
        List<Text> texts = new ArrayList<>();
        for (Object obj : content) {
            if (obj instanceof P) {
                paragraphs.add((P) obj);
            }
        }
        for (P paragraph : paragraphs) {
            List<Object> paragraphContent = paragraph.getContent();
            for (Object paragraphContentElement : paragraphContent) {
                if (paragraphContentElement instanceof R) {
                    runs.add((R) paragraphContentElement);
                }
            }
        }
        for (R run : runs) {
            List<Object> runContent = run.getContent();
            for (Object runContentElement : runContent) {
                if (runContentElement instanceof JAXBElement<?>) {
                    Object element = ((JAXBElement<?>) runContentElement).getValue();
                    if (element instanceof Text) {
                        texts.add((Text) element);
                    }
                }
            }
        }
        for (Text text : texts) {
            for (Map.Entry<String, String> mapElement : templateValue.entrySet()) {
                if (text.getValue().contains(mapElement.getKey())) {
                    text.setValue(text.getValue().replace(mapElement.getKey(), mapElement.getValue()));
                }
            }
        }
    }
}
