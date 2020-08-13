package com.github.dloiacono.quarkus.docx4j;

import static javax.ws.rs.core.HttpHeaders.CONTENT_DISPOSITION;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static org.jboss.resteasy.spi.CorsHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;

import io.smallrye.mutiny.Uni;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/generate")
public class GeneratorResource {

  protected static final String ATTACHMENT_FILENAME = "attachment; filename=";

  @Inject
  GeneratorService generatorService;

  @POST
  @Operation(summary = "Generates the document")
  @APIResponse(responseCode = "201", description = "Document generated")
  @Consumes(MULTIPART_FORM_DATA)
  public Uni<Response> generateDocument(
      @RequestBody(
          required = true,
          content = @Content(
              mediaType = MULTIPART_FORM_DATA,
              schema = @Schema(implementation = MultipartFormDataInput.class)))
      @MultipartForm GeneratorMultipartForm form) {

    return Uni.createFrom().voidItem()
        .onItem().produceUni(item -> generatorService
            .generatePDF(form.template, form.content))
        .map(stream -> (StreamingOutput) stream::writeTo)
        .map(out -> Response.ok(out)
            .header("Content-Type", "application/pdf")
            .header(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION)
            .header(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + form.fileName)
            .build());
  }
}
