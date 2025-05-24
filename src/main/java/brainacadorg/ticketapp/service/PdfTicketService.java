package brainacadorg.ticketapp.service;

import brainacadorg.ticketapp.model.Ticket;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class PdfTicketService {

    public byte[] generateTicketPdf(Ticket ticket) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Електронний квиток", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Подія: " + ticket.getEvent().getName()));
            document.add(new Paragraph("Дата: " + ticket.getEvent().getEventDate()));
            document.add(new Paragraph("Місце: " + ticket.getNumber()));
            document.add(new Paragraph("Ціна: " + ticket.getCost() + " грн"));
            document.add(new Paragraph("\n"));

            String qrCodeText = "Квиток #" + ticket.getId() + " - " + ticket.getEvent().getName();
            byte[] qrCodeImage = generateQrCode(qrCodeText, 150, 150);

            Image qrImage = Image.getInstance(qrCodeImage);
            qrImage.setAlignment(Element.ALIGN_CENTER);
            document.add(qrImage);

            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Помилка генерації PDF-квитка", e);
        }
    }

    private byte[] generateQrCode(String text, int width, int height) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            BitMatrix bitMatrix = new MultiFormatWriter()
                    .encode(text, BarcodeFormat.QR_CODE, width, height);
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Помилка генерації QR-коду", e);
        }
    }
}
