# Domain

En esta capa se ubicará la lógida de negocio de nuestra aplicación.
Idealmente, debiera de haber un package por cada dominio[1] de negocio.

Dentro de cada dominio, se ubicarán los siguientes elementos: DI, Models, UseCases/Services.

## Inyección de dependencias

DI: Este package es para definir la inyección de dependencias[2] mediante un modulo.

## Modelos

Models: Este package tiene los modelos de negocio de este dominio. Idealmente debieran de ser
o Aggregates o al menos no anémicos.

### Aggregate Models

Los "aggregates" abarcan diversos modelos vinculados entre sí. Típicamente tendremos un modelo de, en términos DER[3]
una entidad fuerte y otras entidades débiles que dependen de la fuerte. Por ejemplo, en un sistema de facturación, el
modelo de factura es el agregado fuerte y los items de factura son los agregados débiles. Al modelo se accederá siempre
a través de esta entidad fuerte.

Nota: Estas entidades son de negocio, no de base de datos. Por lo tanto, no deben de tener anotaciones de persistencia.

### Anemic Models

Existe un anti patrón de diseño llamado "anemic model" que consiste en tener modelos que solo tienen atributos y
getters. Esto rompe con la idea de "Programación orientada a objetos" que combina datos y procesos (comportamiento)[4].

Idealmente, nuestros modelos de negocio debieran de poder tener comportamiento.

## Use Cases / Services

Estos objetos definirán firmas y comportamiento para los casos de uso del dominio. Exponen las acciones que un tercero
puede hacer en nuestra aplicación. Habitualmente se los conoce como "servicios" o "casos de uso" y abarcan varias
fuentes de información en caso de necesitar obtener estado de diferentes modelos del propio dominio.

Estas clases no debieran de depender de ninguna anotación o implementación ajena a lo que es el dominio. Aquí no
debieramos de encontrarnos con anotaciones de Hilt (Inyección de dependencias de android) quizá si de Dagger (inyección
de dependencias de Kotlin). Tampoco debieramos de encontrarnos con dependencias de acceso a bases de datos como Room o
de comunicación HTTP como retrofit. Finalmente, tampoco debieran de haber viewmodel o livedata que son componentes
Android.

[1]: https://martinfowler.com/bliki/DomainDrivenDesign.html

[2]: https://developer.android.com/training/dependency-injection/hilt-android

[3]: https://es.wikipedia.org/wiki/Diagrama_entidad-relaci%C3%B3n

[4]: https://martinfowler.com/bliki/AnemicDomainModel.html
