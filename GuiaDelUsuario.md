**21/10/2012 -- Nos mudamos a `GitHub`, queremos formar una mejor comunidad donde todos puedan participar. Visítanos!**

**Nueva guía del usuario: https://github.com/bigdata-mx/factura-electronica/wiki/Guia-del-usuario**

# Librería de componentes #

La librería presenta una interfaz muy simple centrada en el Comprobante Fiscal Digital (CFD), las clases principales son `CFDv3` y `CFDv2` que tienen la lógica correspondiente a las versiones 3.0 y 2.0 del CFD respectivamente.

## Breve introducción ##

A continuación se presentan a manera de introducción algunos ejemplos de como utilizar la librería. Más adelante se explican a detalle los pasos necesarios para descargar e instalar la librería.

### Creación de un CFDv3 ###

Se puede crear un `CFDv3` de tres formas:

  * A partir de un `InputStream`, que puede ser un archivo previamente  generado o el contenido de un request HTTP, etc.:
```
    CFDv3 cfd = new CFDv3(new FileInputStream(file));
```
  * A partir de un `Comprobante` nuevo:
```
    ObjectFactory of = new ObjectFactory();
    Comprobante comp = of.createComprobante();
    comp.setVersion("3.0");
    comp.setFecha(new Date());
    ...
    CFDv3 cfd = new CFDv3(comp);
```
  * Cargar un `Comprobante` a partir de un archivo y posteriormente utilizarlo para crear un CFDv3:
```
    Comprobante comp = CFDv3.newComprobante(new FileInputStream(file));
    ...
    CFDv3 cfd = new CFDv3(comp);
```

### Creación de un CFDv2 ###

De igual forma se puede crear un `CFDv2`:

  * A partir de un `InputStream`, que puede ser un archivo previamente  generado o el contenido de un request HTTP, etc.:
```
    CFDv2 cfd = new CFDv2(new FileInputStream(file));
```
  * A partir de un `Comprobante`, que es el modelo que representa al XSD en la aplicación:
```
    ObjectFactory of = new ObjectFactory();
    Comprobante comp = of.createComprobante();
    comp.setVersion("2.0");
    comp.setFecha(new Date());
    ...
    CFDv2 cfd = new CFDv2(comp);
```
  * Cargar un `Comprobante` a partir de un archivo y posteriormente utilizarlo para crear un CFDv2:
```
    Comprobante comp = CFDv2.newComprobante(new FileInputStream(file));
    ...
    CFDv2 cfd = new CFDv2(comp);
```

### Principales operaciones ###

Las clases CFDv3 y el CFDv2, tienen cuatro métodos principales `sellar()`, `validar()`,  `verificar()` y `guardar()`.

El flujo típico para _firmar_ un CFDI versión 3.0 se ve así:
```
    CFDv3 cfd = new CFDv3(new FileInputStream(file)); // Crea el CFD a partir de un archivo
    Key key = KeyLoader.loadPKCS8PrivateKey(new FileInputStream(keyfile),  password); // Carga la llave privada
    Certificate cert = KeyLoader.loadX509Certificate(new FileInputStream(certFile)); // Carga el certificado
    Comprobante sellado = cfd.sellarComprobante(key, cert); // Sellar CFD y obtener un Comprobante sellado
    cfd.guardar(new FileOutputStream(outFile)); // Serializa el CFD ya firmado
    String cadena = cfd.getCadenaOriginal(); // Obtener cadena original
    String sello = sellado.getSello(); // Obtener sello
```

El flujo típico para _verificar_ un CFDI versión 3.0, se ve así:

```
    CFDv3 cfd = new CFDv3(new FileInputStream(file)); // Crea el CFD a partir de un archivo
    cfd.validar(); // Valida el XML, que todos los elementos estén presentes
    cfd.verificar(); // Verifica un CFD ya firmado
```

Además de estas operaciones el CFDv2 permite verificar el CFD utilizando un certificado externo:

```
    CFDv2 cfd = new CFDv2(new FileInputStream(file)); // Crea el CFD a partir de un archivo
    Certificate cert = KeyLoader.loadX509Certificate(new FileInputStream(certFile)); // Carga el certificado
    cfd.validar(); // Valida el XML, que todos los elementos estén presentes
    cfd.verificar(cert); // Verifica un CFD ya firmado
```

_Nota:  Puedes encontrar un ejemplo del uso de las librerías en las clases:_ [mx.bigdata.sat.cfdi.examples.Main](http://code.google.com/p/factura-electronica/source/browse/trunk/cfdi-base/src/main/java/mx/bigdata/sat/cfdi/examples/Main.java) y [mx.bigdata.sat.cfd.examples.Main](http://code.google.com/p/factura-electronica/source/browse/trunk/cfdi-base/src/main/java/mx/bigdata/sat/cfd/examples/Main.java).

### Creación de un `Comprobante` utilizando las librerías ###

Para crear un `Comprobante` de forma procedural es necesario utilizar los métodos de la clase [mx.bigdata.sat.cfdi.schema.ObjectFactory](http://factura-electronica.googlecode.com/svn/javadoc/mx/bigdata/sat/cfdi/schema/ObjectFactory.html) para la versión 3.0 y [mx.bigdata.sat.cfd.schema.ObjectFactory](http://factura-electronica.googlecode.com/svn/javadoc/mx/bigdata/sat/cfd/schema/ObjectFactory.html) para la versión 2.0.

```
    // CFDI versión 3.0
    ObjectFactory of = new ObjectFactory();
    Comprobante comp = of.createComprobante();
    comp.setVersion("3.0");
    comp.setFecha(new Date());
    comp.setFormaDePago("PAGO EN UNA SOLA EXHIBICION");
    comp.setSubTotal(new BigDecimal("488.50"));
    comp.setTotal(new BigDecimal("488.50"));
    comp.setTipoDeComprobante("ingreso");
    comp.setEmisor(createEmisor(of));
    comp.setReceptor(createReceptor(of));
    comp.setConceptos(createConceptos(of));
    comp.setImpuestos(createImpuestos(of));
    CFDv3 cfd = new CFDv3(comp); 
    ...
```

```
    // CFD versión 2.0
    ObjectFactory of = new ObjectFactory();
    Comprobante comp = of.createComprobante();
    comp.setVersion("2.0");
    comp.setFecha(new Date());
    comp.setSerie("ABCD");
    comp.setFolio("2");
    comp.setNoAprobacion(new BigInteger("49"));
    comp.setAnoAprobacion(new BigInteger("2008"));
    comp.setFormaDePago("UNA SOLA EXHIBICI\u00D3N");
    comp.setSubTotal(new BigDecimal("2000.00"));
    comp.setTotal(new BigDecimal("2320.00"));
    comp.setDescuento(new BigDecimal("0.00"));
    comp.setTipoDeComprobante("ingreso");
    comp.setEmisor(createEmisor(of));
    comp.setReceptor(createReceptor(of));
    comp.setConceptos(createConceptos(of));
    comp.setImpuestos(createImpuestos(of));
    CFDv2 cfd = new CFDv2(comp); 
    ...
```

_Nota:  Puedes encontrar un ejemplo de este código en las clases:_ [mx.bigdata.sat.cfdi.examples.ExampleCFDFactory](http://code.google.com/p/factura-electronica/source/browse/trunk/cfdi-base/src/main/java/mx/bigdata/sat/cfdi/examples/ExampleCFDFactory.java) y [mx.bigdata.sat.cfd.examples.ExampleCFDFactory](http://code.google.com/p/factura-electronica/source/browse/trunk/cfdi-base/src/main/java/mx/bigdata/sat/cfd/examples/ExampleCFDFactory.java).

# Uso de la librería #

## Instalación ##

  1. Descarga la [última versión](http://code.google.com/p/factura-electronica/downloads/list) de las librerías
  1. Descomprime el archivo cfdi-X.Y.Z-bin.zip o  cfdi-X.Y.Z-bin.tar.gz

## Herramientas de línea de comandos ##

La librería incluye dos programas que permiten trabajar con los CFD desde la linea de comandos: `cfd` para trabajar con la versión 2.0 y `cfdi` para trabajar con la versión 3.0. Para ejecutar estos comandos cambia al directorio donde se descomprimieron los archivos y ejecuta cualquiera de los siguientes ejemplos:

> _Nota: Se muestran los ejemplos para `*`nix, seguidos de los ejemplos para windows._

  * Para validar un CFD sellado contra el esquema:

```
# v3.0:
./bin/cfdi validar ejemplos/cfdv3.externo.xml
# v2.0:
./bin/cfd validar ejemplos/cfdv2.externo.xml
```

```
rem v3.0:
.\bin\cfdi validar ejemplos\cfdv3.externo.xml 
rem v2.0:
.\bin\cfd validar ejemplos\cfdv2.externo.xml 
```

  * Para verificar el sello del CFDI:

```
# v3.0
./bin/cfdi verificar ejemplos/cfdv3.externo.xml 
# v2.0
./bin/cfd verificar ejemplos/cfdv2.externo.xml 
```

```
rem v3.0:
.\bin\cfdi verificar ejemplos\cfdv3.externo.xml 
rem v2.0:
.\bin\cfd verificar ejemplos\cfdv2.externo.xml 
```

  * Para sellar el CFDI:

```
# v3.0:
./bin/cfdi sellar ejemplos/cfdv3.xml ejemplos/emisor.key a0123456789 ejemplos/emisor.cer cfdv3_sellado.xml 
# v2.0:
./bin/cfd sellar ejemplos/cfdv2.xml ejemplos/emisor.key a0123456789 ejemplos/emisor.cer cfdv2_sellado.xml 
```
```
rem v3.0:
.\bin\cfdi sellar ejemplos\cfdv3.xml ejemplos\emisor.key a0123456789 ejemplos\emisor.cer cfdv3_sellado.xml 
rem v2.0:
.\bin\cfd sellar ejemplos\cfdv2.xml ejemplos\emisor.key a0123456789 ejemplos\emisor.cer cfdv2_sellado.xml 
```


  * Para verificar un CFDI timbrado:

```
# v3.0:
./bin/cfdi verificar-timbrado ejemplos/cfdv3.externo.xml ejemplos/pac.cer
```
```
rem v3.0:
.\bin\cfdi verificar-timbrado ejemplos\cfdv3.externo.xml ejemplos\pac.cer
```

  * _Funcionalidad adicional, para timbrar el CFDI_:

```
# v3.0:
./bin/cfdi timbrar ejemplos/cfdv3_tfd.xml ejemplos/pac.key a0123456789 ejemplos/pac.cer cfdv3_timbrado.xml
```
```
rem v3.0:
.\bin\cfdi timbrar ejemplos\cfdv3_tfd.xml ejemplos\pac.key a0123456789 ejemplos\pac.cer cfdv3_timbrado.xml
```

> _Nota: Estos comandos no regresan ningún mensaje si la operación fue exitosa._


# Código fuente #

Los usuarios avanzados pueden estar interesados en descargar y compilar el código fuente, para ello es necesario seguir los siguientes pasos:

## Pre-requisitos ##

Para trabajar con el código fuente de la librería de factura electrónica debes tener instalados los siguientes sistemas:

  * [Java 6](http://www.java.com/en/download/manual.jsp)
  * [Maven](http://maven.apache.org/download.html)

## Obtener el código fuente ##

Puedes obtener el código fuente de las librerías en la siguiente dirección: http://code.google.com/p/factura-electronica/source/checkout

Revisa la información en las [preguntas frecuentes](PreguntasFrecuentes.md) para agregar el código fuente a un IDE (NetBeans o Eclipse)

## Compilar los componentes ##

Para compilar los componentes, utiliza el comando:

`mvn compile`

este comando preparará todas las dependencias y generará el código necesario para trabajar con el XSD de la versión 3.0 y 2.0 del CFD y la versión 1.0 del TFD.

## Ejecutar el programa de ejemplo ##

Para ejecutar el programa de ejemplo, utiliza el comando:

`mvn exec:java`