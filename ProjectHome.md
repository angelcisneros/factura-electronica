**21/10/2012 -- Nos mudamos a `GitHub`, queremos formar una mejor comunidad donde todos puedan participar.**

**Nueva página https://github.com/bigdata-mx/factura-electronica !!**


La librería presenta una interfaz muy simple centrada en cada uno de los elementos enunciados por el SAT. Cada uno de estos elementos tiene la funcionalidad necesaria para: validar, firmar, verificar y serializar.

Comprobante Fiscal Digital por Internet (CFDv32):

```
    CFDv32 cfd = new CFDv32(new FileInputStream(file)); // Crea un CFD a partir de un InputStream
    Key key = KeyLoader.loadPKCS8PrivateKey(new FileInputStream(keyfile),  password);
    Certificate cert = KeyLoader.loadX509Certificate(new FileInputStream(certFile));
    Comprobante sellado = cfd.sellarComprobante(key, cert); // Firma el CFD y obtiene un Comprobante sellado
    cfd.validar(); // Valida el XML, que todos los elementos estén presentes
    cfd.verificar(); // Verifica un CFD ya firmado
    cfd.guardar(System.out); // Serializa el CFD a un OutputStream
```

Comprobante Fiscal Digital  (CFDv22):

```
    CFDv22 cfd = new CFDv22(new FileInputStream(file)); // Crea un CFD a partir de un InputStream
    Key key = KeyLoader.loadPKCS8PrivateKey(new FileInputStream(keyfile),  password);
    Certificate cert = KeyLoader.loadX509Certificate(new FileInputStream(certFile));
    Comprobante sellado = cfd.sellarComprobante(key, cert); // Firma el CFD y obtiene un Comprobante sellado
    cfd.validar(); // Valida el XML, que todos los elementos estén presentes
    cfd.verificar(); // Verifica un CFD ya firmado
    cfd.guardar(System.out); // Serializa el CFD a un OutputStream
```

Timbre Fiscal Digital (TFDv1):

```
    CFDv32 cfd = new CFDv32(new FileInputStream(file));// Crea un CFD a partir de un InputStream
    TFDv1 tfd = new TFDv1(cfd); // Crea un TDF a partir del CDF
    PrivateKey key = KeyLoader
        .loadPKCS8PrivateKey(new FileInputStream(keyfile), password);
    tfd.timbrar(key); // Timbra el CDF
    tfd.verificar(cert); // Verifica el TDF
    tfd.guardar(System.out); // Serializa el CFD timbrado a un OutputStream
```


Entérate de las mejoras y actualizaciones a las librerías a través de nuestra [cuenta de twitter](http://www.twitter.com/bigdata_mx)  [![](http://twitter-badges.s3.amazonaws.com/t_mini-a.png)](http://www.twitter.com/bigdata_mx)


**Para más información visítanos en github https://github.com/bigdata-mx/factura-electronica**
