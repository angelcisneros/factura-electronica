**21/10/2012 -- Nos mudamos a `GitHub`, queremos formar una mejor comunidad donde todos puedan participar. Visítanos!**

**Preguntas Frecuentes: https://github.com/bigdata-mx/factura-electronica/wiki/Preguntas-frecuentes**

1.  **¿Puedo utilizar los componentes en aplicaciones comerciales?**

Sí, siempre y cuando se atribuya adecuadamente según los términos de la [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0). Para más información consulta la página de  [preguntas frecuentes](http://www.apache.org/foundation/licence-FAQ.html) de la Licencia.

2. **¿Cuáles características implementa?**

La librería implementa el sellado de los CFDI con SHA1 y RSA, así como la verificación del sello. Por otro lado implementa el timbrado de los CFDI así como la verificación del timbre.

Adicionalmente se ha agregado soporte para el  sellado de los CFD v2.0 con SHA1 y RSA, así como la verificación del sello.

3. **¿Qué debo hacer para reportar un problema o sugerir una mejora?**

Busca la respuesta en la sección de [preguntas frecuentes](PreguntasFrecuentes.md) o en la sección de [seguimiento](http://code.google.com/p/factura-electronica/issues/list). Si no encuentras la respuesta, crea una nueva entrada utilizando la liga de _New Issue_ y haremos todo lo posible por solucionarlo.

4. **¿Cómo puedo descargar el código?**

Puedes descargarlo en http://code.google.com/p/factura-electronica/source/checkout.

En general puedes utilizar el .jar de la versión más reciente que esté disponible en http://code.google.com/p/factura-electronica/downloads/list, para realizar las funciones principales de los CFD.

5. **¿Cuáles son las dependencias de los componentes?**

Actualmente los componentes dependen de las siguientes librerías: [guava-libraries](http://code.google.com/p/guava-libraries/), [Commons Codec](http://commons.apache.org/codec/) y [not-yet-commons-ssl](http://juliusdavies.ca/commons-ssl/), todas ellas disponibles bajo la Licencia Apache, Version 2.0 y las librerías de [JAXB](https://jaxb.dev.java.net/) disponibles bajo la licencia [CDDL](http://www.sun.com/cddl/). Adicionalmente se utiliza la librería [Bouncy Castle](http://www.bouncycastle.org/java.html) para depurar la aplicación.

6.  **¿Cómo agrego el código del SVN a NetBeans o Eclipse para compilar las librerías?**

**_NetBeans_**

Para agregar el código a NetBeans es muy importante tener instalada al menos la versión 3.0.1 de [Maven](http://maven.apache.org/download.html).  Para configurar la versión de Maven en NetBeans:

  1. "Tools > Options > Miscellaneous  > Maven"
  1. En External Maven Home, agrega el PATH a la versión 3.0.1 de Maven

A continuación importa el proyecto de la siguiente manera: “File > New Project > Maven > Maven Project with Existing POM” y  en el siguiente diálogo selecciona el directorio con el código descargado del svn.

Finalmente construye el proyecto con el comando Build. Esto compila el código generado y corrige los errores de compilación que aparecen al importar el proyecto.

Para más información consulta: http://wiki.netbeans.org/MavenBestPractices

**_Eclipse_**

Para agregar el código a Eclipse es necesario instalar el plugin [m2eclipse](http://m2eclipse.sonatype.org/installing-m2eclipse.html) e importar el proyecto utilizando el menú: "File > Import > Maven > Existing Maven Projects". Finalmente sobre el proyecto de ecplise "Maven > Update Project Configuration" para generar los esquemas y actualizar la configuración del proyecto.

7. **¿Se pueden utilizar las librerías en Google App Engine?**

Sí, las librerías están diseñadas para funcionar sobre [Google App Engine](http://code.google.com/appengine/). Para hacerlo es necesario utilizar el TransofrmerFactory de [Xalan](http://xml.apache.org/xalan-j/), ya que la implementación de `javax.xml.transform.TransformerFactory` no es compatible con las restricciones de la plataforma en App Engine.

Puedes cambiar el TransformerFactory de la siguiente manera:

```
      CFDv3 cfd = new CFDv3(...);
      TransformerFactory factory = TransformerFactory.newInstance(
                        "org.apache.xalan.processor.TransformerFactoryImpl",
                         Thread.currentThread().getContextClassLoader()); 
      cfd.setTransformerFactory(factory);
```

En esta liga hay un ejemplo de un CFD generado en GAE utilizando las librerías: http://factura-electronica.appspot.com/ejemplos/cfdv3

8. **¿Cómo utilizo caracteres Unicode (caracteres acentuados y otros caracteres especiales) dentro del código de mi programa?**

La forma recomendada para incluir caracteres Unicode en el código fuente es en su forma escapada, de esta forma los caracteres siempre corresponderán a su representación Unicode independientemente de como esté codificado el archivo. Por ejemplo, en lugar de utilizar:

> `comp.setFormaDePago("UNA SOLA EXHIBICIÓN");`

es conveniente utilizar:

> `comp.setFormaDePago("UNA SOLA EXHIBICI\u00D3N");`

Ya que la segunda forma es independiente de la codificación de la plataforma donde se compile el programa. Esto es importante ya que la codificación del CFD se hace en UTF-8 y una codificación distinta puede resultar en errores al verificar el certificado.

9.  **¿Cuál es el objetivo de escribir esta librería?**

La librería busca tener una base de código común para el desarrollo de aplicaciones de factura electrónica escritas en Java, a fin de que los desarrolladores  dirigan sus esfuerzos a generar aplicaciones con mayor valor agregado. Al mismo tiempo que las librerías se benefician de su uso generalizado y las mejoras provenientes de la comunidad.

10. **¿Cuáles son las siguientes metas del proyecto?**

Las siguientes metas son:
  * Mejorar los mensajes de error del programa de línea de comandos
  * Agregar los esquemas de los complementos

11. **¿Cómo me entero de las actualizaciones y mejoras a las librerías?**

Entérate de las mejoras y actualizaciones a las librerías a través de nuestra [cuenta de twitter](http://www.twitter.com/bigdata_mx)  [![](http://twitter-badges.s3.amazonaws.com/t_mini-a.png)](http://www.twitter.com/bigdata_mx)