/*
 *  Copyright 2010 BigData.mx
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package mx.bigdata.cfdi;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.bind.util.JAXBSource;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import mx.bigdata.cfdi.schema.Comprobante;
import mx.bigdata.cfdi.security.KeyLoader;

public final class CFDv3 {

  private static final String XSLT = "/xslt/cadenaoriginal_3_0.xslt";
  
  private static final String XSD = "/xsd/cfdv3.xsd";
      
  private static final JAXBContext CONTEXT = createContext();
  
  private static final JAXBContext createContext() {
    try {
      return JAXBContext.newInstance("mx.bigdata.cfdi.schema");
    } catch (Exception e) {
      throw new Error(e);
    } 
  }

  final Comprobante document;

  public CFDv3(InputStream in) throws Exception {
    this.document = load(in);
  }

  public CFDv3(Comprobante comprobante) throws Exception {
    this.document = copy(comprobante);
  }

  public void sign(PrivateKey key, Certificate cert) throws Exception {
    String signature = getSignature(key);
    document.setSello(signature);
    byte[] bytes = cert.getEncoded();
    Base64 b64 = new Base64(-1);
    String certStr = b64.encodeToString(bytes);
    document.setCertificado(certStr);
  }

  public void validate() throws Exception {
    validate(null);
  }

  public void validate(ErrorHandler handler) throws Exception {
    SchemaFactory sf =
      SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Schema schema = sf.newSchema(getClass().getResource(XSD));
    Validator validator = schema.newValidator();
    if (handler != null) {
      validator.setErrorHandler(handler);
    }
    validator.validate(new JAXBSource(CONTEXT, document));
  }

  public void verify() throws Exception {
    byte[] digest = getDigest();
    String certStr = document.getCertificado();
    Base64 b64 = new Base64();
    byte[] cbs = b64.decode(certStr);
    X509Certificate cert = KeyLoader
      .loadX509Certificate(new ByteArrayInputStream(cbs)); 
    cert.checkValidity(); 
    String sigStr = document.getSello();
    byte[] signature = b64.decode(sigStr); 
    Signature sig = Signature.getInstance("SHA1withRSA");
    sig.initVerify(cert);
    sig.update(digest);
    boolean bool = sig.verify(signature);
    if (!bool) {
      throw new Exception("Invalid signature");
    }
  }

  public byte[] getOriginalBytes() throws Exception {
    JAXBSource in = new JAXBSource(CONTEXT, document);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Result out = new StreamResult(baos);
    TransformerFactory factory = TransformerFactory.newInstance();
    Transformer transformer = factory
      .newTransformer(new StreamSource(getClass().getResourceAsStream(XSLT)));
    transformer.transform(in, out);
    return baos.toByteArray();
  }
  
  public String getOriginalString() throws Exception {
    byte[] bytes = getOriginalBytes();
    return new String(bytes);
  }
  
  public byte[] getDigest() throws Exception {
    byte[] bytes = getOriginalBytes();
    return DigestUtils.sha(bytes);
  }
  
  public String getSignature(PrivateKey key) throws Exception {
    byte[] digest = getDigest();
    Signature sig = Signature.getInstance("SHA1withRSA");
    sig.initSign(key);
    sig.update(digest);
    byte[] ciphered = sig.sign();
    Base64 b64 = new Base64(-1);
    return b64.encodeToString(ciphered);
  }

  public void marshal(OutputStream out) throws Exception {
    Marshaller m = CONTEXT.createMarshaller();
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, 
                  "http://www.sat.gob.mx/cfd/3 cfdv3.xsd");
    m.marshal(document, out);
  }

  // Defensive deep-copy
  private Comprobante copy(Comprobante comprobante) throws Exception {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    DocumentBuilder db = dbf.newDocumentBuilder(); 
    Document doc = db.newDocument();
    Marshaller m = CONTEXT.createMarshaller();
    m.marshal(comprobante, doc);
    Unmarshaller u = CONTEXT.createUnmarshaller();
    return (Comprobante) u.unmarshal(doc);
  }

  private Comprobante load(InputStream source) throws Exception {
    try {
      Unmarshaller u = CONTEXT.createUnmarshaller();
      return (Comprobante) u.unmarshal(source);
    } finally {
      source.close();
    }
  }

  public static void dump(String title, byte[] bytes, PrintStream out) {
    out.printf("%s: ", title);
    for (byte b : bytes) {
      out.printf("%02x ", b & 0xff);
    }
    out.println();
  }

}