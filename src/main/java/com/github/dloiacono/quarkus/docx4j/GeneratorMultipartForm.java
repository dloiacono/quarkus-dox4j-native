package com.github.dloiacono.quarkus.docx4j;

import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.io.InputStream;
import javax.ws.rs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

@RegisterForReflection
public class GeneratorMultipartForm {

  @FormParam("template")
  @PartType(APPLICATION_OCTET_STREAM)
  public InputStream template;

  @FormParam("content")
  @PartType(APPLICATION_OCTET_STREAM)
  public InputStream content;

  @FormParam("fileName")
  @PartType(TEXT_PLAIN)
  public String fileName;
}