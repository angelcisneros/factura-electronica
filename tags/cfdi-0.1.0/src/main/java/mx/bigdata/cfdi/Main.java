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

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import mx.bigdata.cfdi.schema.ObjectFactory;
import mx.bigdata.cfdi.schema.Comprobante;
import mx.bigdata.cfdi.schema.Comprobante.Conceptos;
import mx.bigdata.cfdi.schema.Comprobante.Conceptos.Concepto;
import mx.bigdata.cfdi.schema.Comprobante.Emisor;
import mx.bigdata.cfdi.schema.Comprobante.Impuestos;
import mx.bigdata.cfdi.schema.Comprobante.Impuestos.Traslados;
import mx.bigdata.cfdi.schema.Comprobante.Impuestos.Traslados.Traslado;
import mx.bigdata.cfdi.schema.Comprobante.Receptor;
import mx.bigdata.cfdi.schema.TUbicacionFiscal;
import mx.bigdata.cfdi.schema.TUbicacion;
import mx.bigdata.cfdi.security.KeyLoader;

public final class Main {
    
  public static void main(String[] args) throws Exception {
    CFDv3 cfd = new CFDv3(createComprobante());
    cfd.validate();
    System.err.printf("Cadena original: %s\n", cfd.getOriginalString());
    CFDv3.dump("Digestion", cfd.getDigest(), System.err);
    PrivateKey key = KeyLoader.loadPKCS8PrivateKey(new FileInputStream(args[1]),
                                            args[2]);
    System.err.printf("Sello: %s\n", cfd.getSignature(key));
    Certificate cert = KeyLoader
      .loadX509Certificate(new FileInputStream(args[3]));
    cfd.sign(key, cert);
    cfd.verify();
    cfd.marshal(System.err);
  }

  private static Comprobante createComprobante() throws Exception {
    ObjectFactory of = new ObjectFactory();
    Comprobante comp = of.createComprobante();
    comp.setVersion("3.0");
    Date date = new GregorianCalendar(2010, 02, 06, 20, 38, 12).getTime();
    comp.setFecha(date);
    comp.setSello("");
    comp.setFormaDePago("PAGO EN UNA SOLA EXHIBICION");
    comp.setNoCertificado("30001000000100000800");
    comp.setCertificado("");
    comp.setSubTotal(new BigDecimal("488.50"));
    comp.setTotal(new BigDecimal("488.50"));
    comp.setTipoDeComprobante("ingreso");
    comp.setEmisor(createEmisor(of));
    comp.setReceptor(createReceptor(of));
    comp.setConceptos(createConceptos(of));
    comp.setImpuestos(createImpuestos(of));
    return comp;
  }
    
  private static Emisor createEmisor(ObjectFactory of) {
    Emisor emisor = of.createComprobanteEmisor();
    emisor.setNombre("PHARMA PLUS SA DE CV");
    emisor.setRfc("PPL961114GZ1");
    TUbicacionFiscal uf = of.createTUbicacionFiscal();
    uf.setCalle("AV. RIO MIXCOAC");
    uf.setCodigoPostal("03240");
    uf.setColonia("ACACIAS"); 
    uf.setEstado("MEXICO, D.F."); 
    uf.setMunicipio("BENITO JUAREZ"); 
    uf.setNoExterior("No. 140"); 
    uf.setPais("Mexico"); 
    emisor.setDomicilioFiscal(uf);
    TUbicacion u = of.createTUbicacion();
    u.setCalle("AV. UNIVERSIDAD");
    u.setCodigoPostal("03910");
    u.setColonia("OXTOPULCO"); 
    u.setEstado("DISTRITO FEDERAL");
    u.setNoExterior("1858");
    u.setPais("Mexico"); 
    emisor.setExpedidoEn(u); 
    return emisor;
  }

  private static Receptor createReceptor(ObjectFactory of) {
    Receptor receptor = of.createComprobanteReceptor();
    receptor.setNombre("JUAN PEREZ PEREZ");
    receptor.setRfc("PEPJ8001019Q8");
    TUbicacion uf = of.createTUbicacion();
    uf.setCalle("AV UNIVERSIDAD");
    uf.setCodigoPostal("04360");
    uf.setColonia("COPILCO UNIVERSIDAD"); 
    uf.setEstado("DISTRITO FEDERAL"); 
    uf.setMunicipio("COYOACAN"); 
    uf.setNoExterior("16 EDF 3"); 
    uf.setNoInterior("DPTO 101"); 
    uf.setPais("Mexico"); 
    receptor.setDomicilio(uf);
    return receptor;
  }

  private static Conceptos createConceptos(ObjectFactory of) {
    Conceptos cps = of.createComprobanteConceptos();
    List<Concepto> list = cps.getConcepto(); 
    Concepto c1 = of.createComprobanteConceptosConcepto();
    c1.setUnidad("CAPSULAS");
    c1.setImporte(new BigDecimal("244.00"));
    c1.setCantidad(new BigDecimal("1.0"));
    c1.setDescripcion("VIBRAMICINA 100MG 10");
    c1.setValorUnitario(new BigDecimal("244.00"));
    list.add(c1);
    Concepto c2 = of.createComprobanteConceptosConcepto();
    c2.setUnidad("BOTELLA");
    c2.setImporte(new BigDecimal("137.93"));
    c2.setCantidad(new BigDecimal("1.0"));
    c2.setDescripcion("CLORUTO 500M");
    c2.setValorUnitario(new BigDecimal("137.93"));
    list.add(c2);
    Concepto c3 = of.createComprobanteConceptosConcepto();    
    c3.setUnidad("TABLETAS");
    c3.setImporte(new BigDecimal("84.50"));
    c3.setCantidad(new BigDecimal("1.0"));
    c3.setDescripcion("SEDEPRON 250MG 10");
    c3.setValorUnitario(new BigDecimal("84.50"));
    list.add(c3);
    return cps;
  }

  private static Impuestos createImpuestos(ObjectFactory of) {
    Impuestos imps = of.createComprobanteImpuestos();
    Traslados trs = of.createComprobanteImpuestosTraslados();
    List<Traslado> list = trs.getTraslado(); 
    Traslado t1 = of.createComprobanteImpuestosTrasladosTraslado();
    t1.setImporte(new BigDecimal("0.00"));
    t1.setImpuesto("IVA");
    t1.setTasa(new BigDecimal("0.00"));
    list.add(t1);
    Traslado t2 = of.createComprobanteImpuestosTrasladosTraslado();
    t2.setImporte(new BigDecimal("22.07"));
    t2.setImpuesto("IVA");
    t2.setTasa(new BigDecimal("16.00"));
    list.add(t2);
    imps.setTraslados(trs);
    return imps;
  }


}