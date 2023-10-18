# Data

En esta capa se encontrarán las implementaciones para la comunicación con servicios externos a la apliocación. Ya sean
bases de datos, servicios web, etc.

Idealmente, debiera de haber un package por cada dominio[1] de negocio.

Dentro de este package nos encontraremos con otros y el número de estos variará según la complejidad de los recursos.

Hay dos packages que tendremos de base: DI y Repository. Ya hablaremos de ellos más adelante.

Hay otros packages que podrán existir de acuerdo a la necesidad. Si necesitamos implementar una conexión hacia una API
externa, podremos tener un package de nombre "network". Si necesitamos implementar acceso a una base de dato local,
podremos tener un package de nombre "local".

## Inyección de dependencias

DI: Este package es para definir la inyección de dependencias[2] mediante un modulo.

## Repositorio

Repository: Este package definirá la interfaz del repositorio para el dominio al que pertenece. También incluirá las
implementaciones que construyamos de esta. Hay algunas particularidades aquí: La interfaz que definimos aquí es de cara
al dominio.

Esto quiere decir que aquí expondremos al recurso al resto de la aplicación. Por ejemplo: si el dominio es
Auto, aquí tendremos un repositorio de autos. La interfaz de este repositorio será la que exponga los métodos de acceso
al recurso auto y la implementación será el modo en el que iremos a "buscar" a este recurso.

### Layer DATA en Android y Clean Architecture

En Android, la capa de datos es la que se encarga de la comunicación con servicios externos a la aplicación. Esto es
igual a lo que sucede en Clean Architecture. Tenemos una capa que define los adapters (serán local y network) de acceso
a estos servicios externos.

Pero hay una diferencia crítica entre Android y Clean. Clean propone que la dependencia sea "hacia el centro". Nuestro
centro es Domain. Pero en el caso de Android, Domain puede depender de Data para ir a obtener la información.

Es por ello que la interfaz de los repositorios vive aquí en Data y no como un puerto del dominio.

De todos modos, recomendamos que las firmas que se definen aquí respondan a modelos de negocio y no a modelos de Data.
Es decir,
devolver aquí modelos definidos en Domain y no los que hayamos definido en las implemnentaciones que veremos más
adelante.

### Implementación default

En este package también tendremos una implementación default del repositorio. Esta implementación funcionará como "
agregadora" de las diferentes implementaciones / adapters que existan en esta capa. Esto nos dará flexibilidad para ir a
buscar rcursos de diferente modo.

## Implementaciones, packages opcionales

Aquí estarán los adapters de los recursos externos. A diferencia de lo que se construye dentro de repository, las clases
aquí son de cara a los recursos externos. Es decir, tendremos una implementación por cada fuente de información. Puede
darse el caso de que sean coincidentes los recursos expuestos en el package repository y lo que vayamos a buscar como
no. Podemos llegar a necesitar ir a buscar a varias fuentes de información lo necesario para exponer un recurso.

### Network

Aquí estará la implementación del consumo de apis externas.

### Local

Aquí estará la implementación del consumo de bases de datos locales.
