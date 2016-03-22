package es.mitc.redes.urbanismoenred.servicios.validacion;

import java.awt.Color;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class PDFGenerator extends PdfPageEventHelper {

	public static Color COLORBACKGROUNDTITULO = new Color(0x8e, 0xb2, 0xd2);
	public static Font COLORTEXTOTITULO = new Font(Font.NORMAL,12.0f,0,new Color(220,220,220));
	public static Font TEXTOTITULO = new Font(Font.BOLD,12.0f,0,new Color(0,0,0));

	/** Cabecera de página */
    private Image header;
    /** Imagen de pie de página */
    //private Image footer;
    /** Tabla de la cabecera */
    private PdfPTable table;
    /** Graphic state */
    private PdfGState gstate;
    /** template that will hold the total number of pages */
    private PdfTemplate tpl;
    /** font */
    private BaseFont helv;

    public PDFGenerator() {

	}

    public PDFGenerator(Image head) {
    	this.header = head;
    	//this.footer = foot;
	}


    /*
     * (non-Javadoc)
     * @see com.lowagie.text.pdf.PdfPageEventHelper#onOpenDocument(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
     */
    @Override
	public void onOpenDocument(PdfWriter writer, Document document) {
        try {
        	/** inicializacion de la tabla de cabecera */
        	table = new PdfPTable(1);
        	table.addCell(new PdfPCell(header,true));
        	/** inicializacion del Graphic State */
        	gstate = new PdfGState();
        	gstate.setFillOpacity(0.3f);
        	gstate.setStrokeOpacity(0.3f);
        	/** inicializacion de la template */
            tpl = writer.getDirectContent().createTemplate(100, 100);
            tpl.setBoundingBox(new Rectangle(-20, -20, 100, 100));
            /** inicializacion de la fuente */
            helv = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);
        }
        catch(Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.lowagie.text.pdf.PdfPageEventHelper#onEndPage(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
     */
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        cb.saveState();
        /** compone la headertable */
        table.setTotalWidth(document.right() - document.left());
        table.writeSelectedRows(0, -1, document.left(), document.getPageSize().getHeight() - 35, cb);
        /** compone el footer */
        String text = "" + writer.getPageNumber();
        String doc = "Documento de carácter informativo";

        Rectangle page = document.getPageSize();
        PdfPTable foot = new PdfPTable(3);
        PdfPCell cell = new PdfPCell ();
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);

        Phrase desc = new Phrase(doc);
        desc.getFont().setSize(9);
        cell.setPhrase(desc);
        foot.addCell(cell);

        Phrase num = new Phrase(text);
        num.getFont().setSize(9);
        cell.setPhrase(num);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        foot.addCell(cell);

        Calendar date = new GregorianCalendar();

        Phrase fecha = new Phrase(date.get(Calendar.DAY_OF_MONTH)+"/"+(date.get(Calendar.MONTH)+1)+"/"+date.get(Calendar.YEAR));
        fecha.getFont().setSize(9);
        cell.setPhrase(fecha);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        foot.addCell(cell);

        foot.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
        foot.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(),writer.getDirectContent());
        cb.restoreState();
    }



    /*
     * (non-Javadoc)
     * @see com.lowagie.text.pdf.PdfPageEventHelper#onCloseDocument(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
     */
    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
       tpl.beginText();
       tpl.setFontAndSize(helv, 9);
       tpl.setTextMatrix(0, 0);
       tpl.showText("" + (writer.getPageNumber() - 1));
       tpl.endText();
    }

	public Image getHeader() {
		return header;
	}
	public void setHeader(Image header) {
		this.header = header;
	}

}
