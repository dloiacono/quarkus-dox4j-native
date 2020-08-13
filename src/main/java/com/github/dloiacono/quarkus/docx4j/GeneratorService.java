package com.github.dloiacono.quarkus.docx4j;

import io.smallrye.mutiny.Uni;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

@Slf4j
@ApplicationScoped
public class GeneratorService {

  public Uni<ByteArrayOutputStream> generatePDF(
      @NotNull InputStream templateStream,
      @NotNull InputStream contentStream) {

    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      WordprocessingMLPackage wordMLPackage = Docx4J.load(templateStream);

      Docx4J.bind(
          wordMLPackage,
          contentStream,
          Docx4J.FLAG_BIND_INSERT_XML | Docx4J.FLAG_BIND_BIND_XML | Docx4J.FLAG_BIND_REMOVE_SDT);

      Docx4J.pdfViaFO();
      Docx4J.toPDF(wordMLPackage, outputStream);

      return Uni.createFrom().item(outputStream);

    } catch (Throwable ex) {
      log.error("Error generating the PDF", ex);
      return Uni.createFrom().failure(ex);
    }
  }
}
